import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.TkpdDesignAttributeDetector
import com.tokopedia.linter.detectors.TkpdDesignComponentDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @ilhamsuaib on 16/12/21.
 */

@RunWith(JUnit4::class)
class TkpdDesignResUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return TkpdDesignAttributeDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(TkpdDesignAttributeDetector.ISSUE)
    }

    @Test
    fun `given xml layout that implemented layout & icon tkpddesign, show error`() {
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

                      
                       <include layout="@layout/partial_shimmering_list"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>
                        
                       <LinearLayout 
                        android:drawable="@drawable/ic_switch_tumb_off"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectErrorCount(2)
    }


    @Test
    fun `given xml layout that implemented icon tkpddesign, show error`() {
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
                      
                       <include layout="@layout/partial_shimmering_list"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>

                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectErrorCount(1)
    }

    @Test
    fun `given xml layout that implemented layout tkpddesign, show error`() {
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

                      
                       <include layout="@layout/partial_shimmering_list"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>
                        
                    </RelativeLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectErrorCount(1)
    }

    @Test
    fun `given xml drawable that implemented drawable tkpddesign usage then show error`() {
        lint().files(
            xml(
                "/res/drawable/test.xml",
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
                        <item android:left="16dp" android:drawable="@drawable/bg_button_orange_enabled"/>
                    </layer-list>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectErrorCount(1)
    }

}
