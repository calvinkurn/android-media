package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.*

class GradleDetector : Detector(), Detector.GradleScanner {
    companion object {
        val interstingBlocks = listOf("dependencies")
        fun getStringDependency(value: String) = "\":" + value + "\""

        val IMPLEMENTATION = Implementation(
                GradleDetector::class.java,
                Scope.GRADLE_SCOPE
        )
    }

    protected fun isInterestingBlock(
            parent: String
    ): Boolean {
        return interstingBlocks.contains(parent)
    }

    override fun checkDslPropertyAssignment(
            context: GradleContext,
            property: String,
            value: String,
            parent: String,
            parentParent: String?,
            valueCookie: Any,
            statementCookie: Any
    ) {
        if (isInterestingBlock(parent)) {
            if (parent == "dependencies" && property == "implementation") {
                var dependency: String = value
                if (isInternalProjectDependency(value)) {
                    dependency = getInternalDependency(value)

                }
                checkDeprecatedDependencies(property, context, value, valueCookie, dependency)
                checkBannedDependencies(property, context, value, valueCookie, dependency)
            }
        }

    }


    private fun isInternalProjectDependency(value: String) =
            value.startsWith("project(") && value.endsWith(")")

    private fun getInternalDependency(value: String) =
            value.substring("project(".length, value.length - 1)

}

