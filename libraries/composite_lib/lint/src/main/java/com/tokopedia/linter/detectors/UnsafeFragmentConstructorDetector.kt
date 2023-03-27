package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.ClassScanner
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UClass

class UnsafeFragmentConstructorDetector : Detector(), SourceCodeScanner, ClassScanner {

    companion object {
        private const val ISSUE_EXPLANATION =
            "Declaring parameters in fragment constructor can cause crash when activity recreated. " +
                "Consider adding global variable inside fragment class or using android bundle."

        val ISSUE = Issue.create(
            id = "UnsafeFragmentConstructor",
            briefDescription = "Fragment has unsafe constructor.",
            explanation = ISSUE_EXPLANATION,
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.ERROR,
            implementation = Implementation(
                UnsafeFragmentConstructorDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val FRAGMENT_PACKAGE = "androidx.fragment.app.Fragment"
    }

    override fun applicableSuperClasses(): List<String> {
        return listOf(FRAGMENT_PACKAGE)
    }

    override fun visitClass(context: JavaContext, declaration: UClass) {
        val constructor = declaration.constructors
            .find { it.parameters.isNotEmpty() }

        if (constructor != null) {
            context.report(
                ISSUE,
                declaration,
                context.getLocation(constructor),
                ISSUE_EXPLANATION
            )
        }
    }
}
