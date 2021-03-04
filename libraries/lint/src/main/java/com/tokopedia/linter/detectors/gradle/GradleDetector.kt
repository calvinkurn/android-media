package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.LinterConstants

@Suppress("UnstableApiUsage")
class GradleDetector : Detector(), Detector.GradleScanner {
    companion object {
        val intersting_blocks = listOf(LinterConstants.GradleConstructs.DEPENDENCIES)
        fun getStringDependency(value: String) = "\":" + value + "\""

        val IMPLEMENTATION = Implementation(
                GradleDetector::class.java,
                Scope.GRADLE_SCOPE
        )
    }

    private fun isInterestingBlock(
            parent: String
    ): Boolean {
        return intersting_blocks.contains(parent)
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
            if (parent == LinterConstants.GradleConstructs.DEPENDENCIES && property == LinterConstants.GradleConstructs.IMPLEMENTATION) {
                var dependency: String = value
                if (isInternalProjectDependency(value)) {
                    dependency = getInternalDependency(value)

                }
                DeprecatedDependencyDetector.checkDeprecatedDependencies(property, context, value, valueCookie, dependency)
                BannedDependencyDetector.checkBannedDependencies(property, context, value, valueCookie, dependency)
            }
        }

    }


    private fun isInternalProjectDependency(value: String) =
            value.startsWith("project(") && value.endsWith(")")

    private fun getInternalDependency(value: String) =
            value.substring("project(".length, value.length - 1)

}

