import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.DimenUsageDetector
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @ilhamsuaib on 16/12/21.
 */

@RunWith(JUnit4::class)
class DimenUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return DimenUsageDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(DimenUsageDetector.XML_ISSUE)
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
                    
                        <View
                            android:layout_width="match_parent"
                            android:layout_height="@dimen/spacing_lvl1"
                            android:layout_below="@id/sahToolbar"
                            android:background="@drawable/sah_shadow" />
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(1)
    }

    @Test
    fun `given xml drawable that implemented dimen usage then show lint warning`() {
        lint().files(
            xml(
                "/res/drawable/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <shape
                        xmlns:android="http://schemas.android.com/apk/res/android"
                        android:shape="oval"
                        android:tileMode="disabled"
                        android:gravity="top" >
                        <size android:width="10dp" android:height="@dimen/layout_lvl0"/>
                        <solid
                            android:width="@dimen/layout_lvl0"
                            android:color="@color/Unify_G600"/>
                        <stroke
                            android:width="2dp"
                            android:color="@color/Unify_N0" />
                    </shape>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(2)
    }

    @Test
    fun `given a view component attribute with a dimen value then return regex matching result`() {
        val viewAttribute = "@dimen/spacing_lvl1"
        val expected = "@dimen/spacing_lvl1"
        val result = DimenUsageDetector.matchAttributeValue(viewAttribute)
        TestCase.assertEquals(expected, result?.value)
    }

    @Test
    fun `given a view component attribute with a actual value then return regex matching result null`() {
        val viewAttribute = "12dp"
        val expected = null
        val result = DimenUsageDetector.matchAttributeValue(viewAttribute)
        TestCase.assertEquals(expected, result)
    }
}