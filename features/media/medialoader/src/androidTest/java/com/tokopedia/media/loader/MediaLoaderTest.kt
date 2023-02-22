package com.tokopedia.media.loader

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat
import com.tokopedia.test.application.espresso_component.CommonActions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MediaLoaderTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(
        DebugMediaLoaderActivity::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        renderImageView {
            it.clearImage()
        }

        Intents.release()
    }

    @Test
    fun loadImage() {
        renderImageView {
            it.loadImage(publicImageUrl)
            it.takeScreenshot("loadImage")
        }
    }

    @Test
    fun loadImage_customPlaceHolder() {
        renderImageView {
            it.loadImage(publicImageUrl) {
                setPlaceHolder(R.drawable.mock_bg_placeholder)
            }
            it.takeEagerScreenshot("loadImage_customPlaceHolder")
        }
    }

    @Test
    fun loadImage_failed() {
        renderImageView {
            it.loadImage("")
            it.takeEagerScreenshot("loadImage_failed")
        }
    }

    @Test
    fun loadImage_invalidUrl() {
        renderImageView {
            it.loadImage(invalidUrl) {
                setErrorDrawable(R.drawable.mock_bg_placeholder)
            }
            it.takeEagerScreenshot("loadImage_invalidUrl")
        }
    }

    @Test
    fun loadImage_centerCrop() {
        renderImageView {
            it.loadImage(publicImageUrl) {
                centerCrop()
            }
            it.takeScreenshot("loadImage_centerCrop")
        }
    }

    @Test
    fun loadImage_overrideSize() {
        renderImageView {
            it.loadImage(publicImageUrl) {
                overrideSize(Resize(50, 50))
            }
            it.takeScreenshot("loadImage_overrideSize")
        }
    }

    @Test
    fun loadImage_decodeFormat_565() {
        renderImageView {
            it.loadImage(publicImageUrl) {
                decodeFormat(MediaDecodeFormat.PREFER_RGB_565)
            }
            it.takeScreenshot("loadImage_decodeFormat_565")
        }
    }

    @Test
    fun loadImage_fitCenter() {
        renderImageView {
            it.loadImageFitCenter(publicImageUrl)
            it.takeScreenshot("loadImage_fitCenter")
        }
    }

    @Test
    fun loadImage_circle() {
        renderImageView {
            it.loadImageCircle(publicImageUrl)
            it.takeScreenshot("loadImage_circle")
        }
    }

    @Test
    fun loadImage_rounded() {
        val rounded = 120f

        renderImageView {
            it.loadImageRounded(publicImageUrl, rounded)
            it.takeScreenshot("loadImage_rounded")
        }
    }

    @Test
    fun loadImage_centerCrop_and_roundedCorner() {
        val rounded = 120f

        renderImageView {
            it.loadImage(publicImageUrl) {
                centerCrop()
                setRoundedRadius(rounded)
            }
            it.takeScreenshot("loadImage_centerCrop_and_roundedCorner")
        }
    }

    @Test
    fun loadImage_withoutPlaceholder() {
        renderImageView {
            it.loadImageWithoutPlaceholder(publicImageUrl)
            it.takeScreenshot("loadImage_withoutPlaceholder")
        }
    }

    @Test
    fun loadIcon() {
        renderImageView {
            it.loadIcon(iconUrl)
            it.takeScreenshot("loadIcon")
        }
    }

    private fun View.takeScreenshot(caseName: String) {
        Thread.sleep(3000)
        takeEagerScreenshot(caseName)
    }

    private fun View.takeEagerScreenshot(caseName: String) {
        CommonActions.takeScreenShotVisibleViewInScreen(this, "media_loader", caseName)
    }

    private fun renderImageView(imgView: (ImageView) -> Unit) {
        onView(withId(R.id.img_sample)).check { view, _ -> imgView(view as ImageView) }
    }

    companion object {
        private const val secureImageUrl = "https://chat.tokopedia.com/tc/v1/download_secure/1844584535/2022-04-19/99f532b4-bfb0-11ec-9296-42010a2942a0"
        private const val publicImageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/11/13/819f37fd-cff6-4212-ae58-5618ac2b5e07.jpg"
        private const val iconUrl = "https://images.tokopedia.net/img/cache/100-square/iEWsxH/2021/10/5/75f2fb8a-a4ca-4cd6-a166-7279daef1d5b.png"
        private const val invalidUrl = "https://images.tokopedia.com/img/invalid_url.png"
    }
}
