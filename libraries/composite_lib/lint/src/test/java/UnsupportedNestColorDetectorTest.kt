
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
            UnsupportedNestColorDetector.XML_ISSUE,
            UnsupportedNestColorDetector.JAVA_ISSUE
        )
    }

    @Test
    fun `regex validation`() {
        val regex = UnsupportedNestColorDetector.XML_REGEX_OLD_COLOR

        assertTrue(regex.containsMatchIn("Unify_N100_70"))
        assertTrue("Unify_N100_70".contains(regex))
        assertTrue("Unify_N50".contains(regex))
        assertTrue(
            """
            com.tokopedia.unifyprinciples.R.color.Unify_G500
            """.trimIndent().contains(regex)
        )
        assertTrue(
            """
            setTextColor(root.context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            """.trimIndent().contains(regex)
        )
        assertTrue(
            """
            android:textColor="@color/Unify_N700"
            """.trimMargin().contains(regex)
        )
        assertTrue(
            """
            android:color="@color/Unify_N700_10"
            """.trimMargin().contains(regex)
        )
        assertFalse("Unify_NN50".contains(regex))
        assertFalse(
            """
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
            """.trimIndent().contains(regex)
        )
    }

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
                        android:background="@color/Unify_N700"
                        android:color="@color/Unify_N700"
                        android:gravity="center_vertical"
                        android:orientation="horizontal">
                    
                        <com.tokopedia.unifyprinciples.Typography
                            android:id="@+id/socialProofChipTitle"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:textColor="@color/Unify_N700"
                            app:typographyType="display_3"
                            tools:text="Diskusi" />
                            
                        <com.tokopedia.unifyprinciples.Typography
                            android:id="@+id/socialProofChipTitle"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:color="@color/Unify_N700_50"
                            app:typographyType="display_3"
                            tools:text="Diskusi" />
                    
                    </LinearLayout>
                """.trimIndent()
            )
        ).allowMissingSdk()
            .run()
            .expectWarningCount(0)
    }

    fun testJava() {
        lint().files(
            kotlin(
                """
                package test.pkg;
                class TestClass1 {
                    
                    fun test() {
                        val test = "com.tokopedia.unifyprinciples.R.color.Unify_G500"
                    }
                }
                """.trimIndent()
            )
        )
            .issues(UnsupportedNestColorDetector.JAVA_ISSUE)
            .run()
            .expectWarningCount(1)
    }
}
