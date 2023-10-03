import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.tokopedia.linter.detectors.ImageUrlDeclarationDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by @ilhamsuaib on 26/05/23.
 */

@Suppress("UnstableAPIUsage")
@RunWith(JUnit4::class)
class ImageUrlDeclarationDetectorTest {

    private val issue = ImageUrlDeclarationDetector.JAVA_ISSUE
    private val kotlinFile = """
        package com.tokopedia.sellerhome.analytic
            
        object TrackingHelper {
        
            const val EMPTY_DATA_URL = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"

            fun someMethod() {
                val localImageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"        
            }
        }
    """.trimIndent()

    @Test
    fun `should show warning if image url declared in a class and out side of image_assets module`() {
        lint().files(
            kotlin(
                """
            class ImageUrl {
                
                companion object {
                    const val EMPTY_DATA_URL = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"
                }

                fun someMethod() {
                    val localImageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"        
                }
            }
                """.trimIndent()
            )
        )
            .issues(issue)
            .allowMissingSdk()
            .run()
            .expectWarningCount(1)
    }

    @Test
    fun `should show warning if image url declared in a kotlin object out side of image_assets module`() {
        lint().files(kotlin(kotlinFile))
            .issues(issue)
            .allowMissingSdk()
            .run()
            .expectWarningCount(1)
    }

    @Test
    fun `don't show warning if image url declared inside of image_assets module`() {
        val stubFile = kotlin("src/main/java/com/tokopedia/imageassets/TokopediaImageUrl.kt", kotlinFile)
        lint().files(stubFile)
            .issues(issue)
            .allowMissingSdk()
            .run()
            .expectWarningCount(0)
    }
}
