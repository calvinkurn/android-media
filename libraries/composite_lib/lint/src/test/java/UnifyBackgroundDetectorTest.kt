import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.UnifyBackgroundDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @ilhamsuaib on 17/12/21.
 */

@RunWith(JUnit4::class)
class UnifyBackgroundDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return UnifyBackgroundDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(UnifyBackgroundDetector.ISSUE)
    }

    @Test
    fun `given a xml layout with view group background = Unify_N0 then show the lint warning`() {
        lint().files(
            xml(
                "/res/layout/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                        android:id="@+id/sahRootLayout"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:fitsSystemWindows="true"
                        android:background="@color/Unify_N0">
                    
                        <View
                            android:layout_width="match_parent"
                            android:layout_height="@dimen/spacing_lvl1"
                            android:layout_below="@id/sahToolbar" />
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(1)
    }

    @Test
    fun `given a xml layout with view group background != Unify_N0 then the lint warning won't be shown`() {
        lint().files(
            xml(
                "/res/layout/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                        android:id="@+id/sahRootLayout"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:fitsSystemWindows="true"
                        android:background="@color/Unify_N75">
                    
                        <View
                            android:layout_width="match_parent"
                            android:layout_height="@dimen/spacing_lvl1"
                            android:layout_below="@id/sahToolbar" />
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(0)
    }
}