package com.tokopedia.media.loader

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.data.DEFAULT_ICON_SIZE
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.di.DaggerMediaLoaderComponent
import com.tokopedia.media.loader.di.MediaLoaderModule
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MediaLoaderTest {

    @get:Rule
    val activityTestRule = object : ActivityCustomRule(
        ActivityScenarioRule(
            DebugMediaLoaderActivity::class.java
        )
    ) {
        override fun before() {
            super.before()
            InstrumentationAuthHelper
                .loginInstrumentationTestUser1()
        }
    }

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation()
            .context
            .applicationContext

    private val interceptor = StubInterceptor()
    private val countingIdlingResource = CountingIdlingResource("media-loader")

    @Before
    fun setUp() {
        DaggerMediaLoaderComponent
            .builder()
            .mediaLoaderModule(
                MediaLoaderModule(applicationContext)
            )
            .build()
            .inject(interceptor)

        Intents.init()
        countingIdlingResource.increment()
    }

    @After
    fun tearDown() {
        onImageView {
            it.clearImage()
        }

        Intents.release()
    }

    @Test
    fun loadImage() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_secure() {
        onImageView {
            // When
            it.loadSecureImage(secureImageUrl, interceptor.userSession) {
                listener(onSuccess = { _, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_secure")
                })
            }
        }
    }

    @Test
    fun loadImage_customPlaceHolder() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                setPlaceHolder(R.drawable.mock_bg_placeholder)
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeEagerScreenshot("loadImage_customPlaceHolder")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_failed() {
        onImageView {
            // When
            it.loadImage("") {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeEagerScreenshot("loadImage_failed")
                    assertEquals(0, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_invalidUrl() {
        onImageView {
            // When
            it.loadImage(invalidUrl) {
                setErrorDrawable(R.drawable.mock_bg_placeholder)
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeEagerScreenshot("loadImage_invalidUrl")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_centerCrop() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                centerCrop()
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_centerCrop")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_overrideSize() {
        // Given
        val resize = 50

        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                overrideSize(Resize(resize, resize))
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_overrideSize")
                    assertEquals(resize, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_decodeFormat_565() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                decodeFormat(MediaDecodeFormat.PREFER_RGB_565)
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_decodeFormat_565")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_fitCenter() {
        onImageView {
            // When
            it.loadImageFitCenter(publicImageUrl) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_fitCenter")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_circle() {
        onImageView {
            // When
            it.loadImageCircle(publicImageUrl) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_circle")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_rounded() {
        // Given
        val rounded = 120f

        onImageView {
            // When
            it.loadImageRounded(publicImageUrl, rounded) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_rounded")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_centerCrop_and_roundedCorner() {
        // Given
        val rounded = 120f

        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                centerCrop()
                setRoundedRadius(rounded)
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_centerCrop_and_roundedCorner")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_withoutPlaceholder() {
        onImageView {
            // When
            it.loadImageWithoutPlaceholder(publicImageUrl) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadImage_withoutPlaceholder")
                    assertEquals(it.width, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadIcon() {
        onImageView {
            // When
            it.loadIcon(iconUrl) {
                listener(onSuccess = { bitmap, _ ->
                    countingIdlingResource.decrement()

                    // Then
                    it.takeScreenshot("loadIcon")
                    assertEquals(DEFAULT_ICON_SIZE, bitmap?.width)
                })
            }
        }
    }

    @Test
    fun loadImage_withEmptyTarget() {
        // When
        loadImageWithEmptyTarget(applicationContext, publicImageUrl, mediaTarget = MediaBitmapEmptyTarget(
            onReady = { bitmap ->
                countingIdlingResource.decrement()

                // Then
                assert(bitmap.width.isMoreThanZero())
            }
        ))
    }

    private fun View.takeScreenshot(caseName: String) {
        takeEagerScreenshot(caseName)
    }

    private fun View.takeEagerScreenshot(caseName: String) {
        CommonActions.takeScreenShotVisibleViewInScreen(this, "media_loader", caseName)
    }

    private fun onImageView(imgView: (ImageView) -> Unit) {
        onView(withId(R.id.img_sample)).check { view, _ -> imgView(view as ImageView) }
    }

    companion object {
        private const val secureImageUrl = "https://chat.tokopedia.com/tc/v1/download_secure/1844584535/2022-04-19/99f532b4-bfb0-11ec-9296-42010a2942a0"
        private const val publicImageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/11/13/819f37fd-cff6-4212-ae58-5618ac2b5e07.jpg"
        private const val iconUrl = "https://images.tokopedia.net/img/cache/100-square/iEWsxH/2021/10/5/75f2fb8a-a4ca-4cd6-a166-7279daef1d5b.png"
        private const val invalidUrl = "https://images.tokopedia.com/img/invalid_url.png"
    }
}
