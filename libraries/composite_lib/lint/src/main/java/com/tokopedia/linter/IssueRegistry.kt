package com.tokopedia.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.tokopedia.linter.detectors.AndroidExportedDetector
import com.tokopedia.linter.detectors.AnimatedVectorDetector
import com.tokopedia.linter.detectors.DeprecatedMethodDetector
import com.tokopedia.linter.detectors.DeprecatedResourceDetector
import com.tokopedia.linter.detectors.DimenResourceValueDetector
import com.tokopedia.linter.detectors.DimenUsageDetector
import com.tokopedia.linter.detectors.GradientVectorDetector
import com.tokopedia.linter.detectors.ImageUrlDeclarationDetector
import com.tokopedia.linter.detectors.ResourceFragmentDetector
import com.tokopedia.linter.detectors.ResourcePackageDetector
import com.tokopedia.linter.detectors.SerializedNameAnnotationDetector
import com.tokopedia.linter.detectors.UnifyBackgroundDetector
import com.tokopedia.linter.detectors.UnsafeFragmentConstructorDetector
import com.tokopedia.linter.detectors.UnsupportedColorDetector
import com.tokopedia.linter.detectors.UnsupportedNestColorDetector
import com.tokopedia.linter.detectors.VectorDrawableDetector
import com.tokopedia.linter.detectors.gradle.BannedDependencyDetector.DEPENDENCY_BANNED
import com.tokopedia.linter.detectors.gradle.DeprecatedDependencyDetector.DEPENDENCY_DEPRECATED
import com.tokopedia.linter.detectors.gradle.HANSEL_REQUIRED
import com.tokopedia.linter.detectors.resources.FullyQualifiedResourceDetector
import com.tokopedia.linter.detectors.resources.MissingResourceImportAliasDetector
import com.tokopedia.linter.detectors.resources.WrongResourceImportAliasDetector
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
            UnsupportedColorDetector.XML_ISSUE,
            UnsupportedColorDetector.JAVA_ISSUE,
            ResourcePackageDetector.JAVA_ISSUE,
            DimenResourceValueDetector.ISSUE,
            DeprecatedResourceDetector.ISSUE,
            SerializedNameAnnotationDetector.RESPONSE_ISSUE,
            SerializedNameAnnotationDetector.ENTITY_ISSUE,
            SerializedNameAnnotationDetector.PARAM_ISSUE,
            AndroidExportedDetector.ISSUE,
            UnifyBackgroundDetector.ISSUE,
            DeprecatedMethodDetector.ISSUE,
            GradientVectorDetector.ISSUE,
            ResourceFragmentDetector.ISSUE,
            DimenUsageDetector.XML_ISSUE,
            UnsafeFragmentConstructorDetector.ISSUE,
            ImageUrlDeclarationDetector.JAVA_ISSUE,
            UnsupportedNestColorDetector.XML_ISSUE,
            UnsupportedNestColorDetector.JAVA_ISSUE,
            FullyQualifiedResourceDetector.ISSUE,
            MissingResourceImportAliasDetector.ISSUE,
            WrongResourceImportAliasDetector.ISSUE,
            HANSEL_REQUIRED
        )

    override val minApi: Int
        get() = 1

    override val api: Int = CURRENT_API
}
