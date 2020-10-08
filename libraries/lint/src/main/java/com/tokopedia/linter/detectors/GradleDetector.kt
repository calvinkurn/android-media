package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.*

class GradleDetector : Detector(), Detector.GradleScanner {
    companion object {


        val deprecatedList = mapOf<String, String>(getStringDependency("tkpddesign") to "", "rootProject.ext.libraries.tkpddesign" to "")
        val bannedList = mapOf<String, String>("rootProject.ext.supportLibDependencies.kotlinReflection" to "")

        fun getStringDependency(value: String) = "\":" + value + "\""
        private val IMPLEMENTATION = Implementation(
                GradleDetector::class.java,
                Scope.GRADLE_SCOPE
        )

        /** Deprecated Gradle constructs  */
        val DEPRECATED = Issue.create(
                "",  //$NON-NLS-1$
                "Deprecated Gradle Construct",
                "This detector looks for deprecated Gradle constructs which currently work but " +
                        "will likely stop working in a future update.",
                Category.CORRECTNESS,
                6,
                Severity.ERROR,
                IMPLEMENTATION
        )

        val BANNED = Issue.create(
                "GradleNotAllowed",  //$NON-NLS-1$
                "Not Allowed Gradle Construct",
                "This detector looks for Banned Gradle constructs which currently work but " +
                        "we are not allow them to use in app",
                Category.CORRECTNESS,
                6,
                Severity.ERROR,
                IMPLEMENTATION
        )




    }


    protected fun isInterestingBlock(
            parent: String,
            parentParent: String?
    ): Boolean {
        return parent == "dependencies"
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
        if (isInterestingBlock(parent, parentParent)) {
            if (parent == "dependencies" && property == "implementation") {
                checkDeprecatedDependencies(property, context, value, valueCookie)
            }
        }

    }
    private fun getBannedMessage(libName: String, solution: String): String {
        return "Usage to ${libName} is Banned Please remove this" + if (solution.isNotEmpty()) "Replace with ${solution}" else ""
    }

    fun getDeprecatedMessage(libName: String, solution: String): String {
        return "Usage to ${libName} is deprecated Please remove this" + if (solution.isNotEmpty()) "Replace with ${solution}" else ""
    }

    private fun checkDeprecatedDependencies(
            configuration: String,
            context: GradleContext,
            value: String,
            cookie: Any
    ) {
        var dependency: String = value
        if (isInternalProjectDependency(value)) {
            dependency = getInternalDependency(value)

        }
        if (deprecatedList.keys.contains(dependency)) {
            context.report(
                    DEPRECATED, context.getLocation(cookie), getDeprecatedMessage(
                    dependency,
                    deprecatedList[dependency] ?: ""
            )
            )
        }
        if (bannedList.keys.contains(dependency)) {
            context.report(
                    BANNED, context.getLocation(cookie), getBannedMessage(
                    dependency,
                    deprecatedList[dependency] ?: ""
            )
            )
        }
    }




    private fun isInternalProjectDependency(value: String) =
            value.startsWith("project(") && value.endsWith(")")

    private fun getInternalDependency(value: String) =
            value.substring("project(".length, value.length - 1)

}

