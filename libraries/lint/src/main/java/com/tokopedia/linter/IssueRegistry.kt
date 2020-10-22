package com.tokopedia.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.tokopedia.linter.detectors.*

class IssueRegistry : IssueRegistry() {
    override val issues
        get() = listOf(
                BottomSheetUnifyDetector.ISSUE,
                CoreResourcesDetector.ISSUE,
                DatePickerUnifyDetector.ISSUE,
                DialogUnifyDetector.ISSUE,
                FloatingButtonUnifyDetector.ISSUE,
                LoaderUnifyDetector.ISSUE,
                SearchBarUnifyDetector.ISSUE,
                SetColourDetector.ISSUE,
                TabsUnifyDetector.ISSUE,
                TabsUnifyDetector.TAB_LAYOUT_ISSUE,
                ToasterDetector.ISSUE,
                TypographyDetector.ISSUE,
                UnifyButtonDetector.ISSUE,
                UnifyImageButtonDetector.ISSUE,
                VectorDrawableDetector.ISSUE,
                AnimatedVectorDetector.JAVA_ISSUE,
                AnimatedVectorDetector.XML_ISSUE,
                GradleDetector.DEPRECATED,
                GradleDetector.BANNED
        )

    override val minApi: Int
        get() = 1

    override val api: Int = CURRENT_API
}
