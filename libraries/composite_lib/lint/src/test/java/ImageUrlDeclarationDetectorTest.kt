import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.ImageUrlDeclarationDetector
import org.junit.Test

/**
 * Created by @ilhamsuaib on 26/05/23.
 */

@Suppress("UnstableAPIUsage")
class ImageUrlDeclarationDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ImageUrlDeclarationDetector()

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(ImageUrlDeclarationDetector.JAVA_ISSUE)
    }

    @Test
    fun `should show warning if image url declared out side of image_assets module`() {
        val stubFile = kotlin(
            "features/merchant/seller_home", """
            package com.tokopedia.sellerhome.analytic
            
            object TrackingHelper {
            
                const val EMPTY_DATA_URL = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"
            }
        """
        ).indented()

        lint().files(stubFile)
            .issues(ImageUrlDeclarationDetector.JAVA_ISSUE)
            .run()
    }
}