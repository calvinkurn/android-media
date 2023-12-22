import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.TkpdDesignComponentDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @ilhamsuaib on 16/12/21.
 */

@RunWith(JUnit4::class)
class TkpdDesignUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return TkpdDesignComponentDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(TkpdDesignComponentDetector.ISSUE)
    }

    @Test
    fun `given xml layout that implemented dimen usage then show lint warning`() {
        lint().files(
            xml(
                "/res/layout/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                        android:id="@+id/sahRootLayout"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:fitsSystemWindows="true">
                        <com.tokopedia.design.component.EditTextCompat
                            android:id="@+id/filter_detail_search"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:tag="com.tokopedia.design"/>
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectErrorCount(1)
    }

}
