package com.tokopedia.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.tokopedia.linter.detectors.AnimatedVectorDetector
import com.tokopedia.linter.detectors.VectorDrawableDetector
import com.tokopedia.linter.detectors.gradle.BannedDependencyDetector.DEPENDENCY_BANNED
import com.tokopedia.linter.detectors.gradle.DeprecatedDependencyDetector.DEPENDENCY_DEPRECATED
import com.tokopedia.linter.detectors.gradle.HANSEL_REQUIRED
import com.tokopedia.linter.detectors.sourcescanner.elements.ImportDetector.CLASS_IMPORT
import com.tokopedia.linter.detectors.sourcescanner.elements.annotation.ServerResponseDataTypeDetector.WRONG_DATA_TYPE
import com.tokopedia.linter.detectors.sourcescanner.method.MethodCallDetector
import com.tokopedia.linter.unify.UnifyDetector

class IssueRegistry : IssueRegistry() {
    override val issues
        get() = listOf(
                UnifyDetector.ISSUE,
                DEPENDENCY_BANNED,
                DEPENDENCY_DEPRECATED,
                CLASS_IMPORT,
                WRONG_DATA_TYPE,
                VectorDrawableDetector.ISSUE,
                MethodCallDetector.METHOD_CALL_PROHIBITED_ISSUE,
                AnimatedVectorDetector.JAVA_ISSUE,
                AnimatedVectorDetector.XML_ISSUE,
                HANSEL_REQUIRED
        )

    override val minApi: Int
        get() = 1

    override val api: Int = CURRENT_API
}
