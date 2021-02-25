package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.LinterConstants
import com.tokopedia.linter.LinterConstants.GradleConstructs.BUILD_SCRIPT
import com.tokopedia.linter.LinterConstants.GradleConstructs.DEPENDENCIES
import com.tokopedia.linter.LinterConstants.GradleConstructs.IMPLEMENTATION
import com.tokopedia.linter.LinterConstants.GradleConstructs.REPOSITORIES
import com.tokopedia.linter.detectors.gradle.BannedDependencyDetector.checkBannedDependencies
import com.tokopedia.linter.detectors.gradle.DeprecatedDependencyDetector.checkDeprecatedDependencies

@Suppress("UnstableApiUsage")
class GradleDetector : Detector(), Detector.GradleScanner {
    companion object {
        val intersting_blocks = listOf(LinterConstants.GradleConstructs.DEPENDENCIES,BUILD_SCRIPT)
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
        addGradleFileToMap(context.file.absolutePath)
        if (isInterestingBlock(parent)) {
            when(parent) {
                DEPENDENCIES->{
                    if (property == LinterConstants.GradleConstructs.IMPLEMENTATION) {
                        var dependency: String = value
                        if (isInternalProjectDependency(value)) {
                            dependency = getInternalDependency(value)

                        }
                        checkDeprecatedDependencies(property, context, value, valueCookie, dependency)
                        checkBannedDependencies(property, context, value, valueCookie, dependency)
                    }
                }
                BUILD_SCRIPT->{
                    when(property) {
                        REPOSITORIES -> checkHanselRepository(
                                context.file.absolutePath,
                                property,
                                parent,
                                value
                        )
                        DEPENDENCIES -> checkHanselClassPath(
                                context.file.absolutePath,
                                property,
                                parent,
                                value
                        )
                    }
                }
            }

        }

    }

    override fun checkMethodCall(
            context: GradleContext,
            statement: String,
            parent: String?,
            namedArguments: Map<String, String>,
            unnamedArguments: List<String>,
            cookie: Any
    ) {
        addGradleFileToMap(context.file.absolutePath)
        val plugin = namedArguments["plugin"]
        if (statement == "apply" && parent == null) {
            checkPlugin(context.file.absolutePath,plugin.toString(),context.project.dir.absolutePath)
        }
    }

    override fun afterCheckFile(context: Context) {
        checkHanselPresent(context.file.absolutePath,context)
    }


    private fun isInternalProjectDependency(value: String) =
            value.startsWith("project(") && value.endsWith(")")

    private fun getInternalDependency(value: String) =
            value.substring("project(".length, value.length - 1)

}

