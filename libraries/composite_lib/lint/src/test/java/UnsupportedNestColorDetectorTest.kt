import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.UnsupportedNestColorDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @yovi.putra on 06/04/23.
 */

@RunWith(JUnit4::class)
class UnsupportedNestColorDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return UnsupportedNestColorDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(
            UnsupportedNestColorDetector.XML_ISSUE
        )
    }

    @Test
    fun `given xml layout that implemented old color then show lint warning`() {
        lint().files(
            xml(
                "/res/layout/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        xmlns:tools="http://schemas.android.com/tools"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:background="@drawable/bg_border_pdp"
                        android:gravity="center_vertical"
                        android:orientation="horizontal">
                    
                        <com.tokopedia.unifyprinciples.Typography
                            android:id="@+id/socialProofChipTitle"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:textColor="@color/Unify_N700"
                            app:typographyType="display_3"
                            tools:text="Diskusi" />
                    
                    </LinearLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(0)
    }
}
