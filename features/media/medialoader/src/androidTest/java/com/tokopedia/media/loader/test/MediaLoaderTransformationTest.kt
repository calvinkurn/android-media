package com.tokopedia.media.loader.test

import android.view.View
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.*
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.DEFAULT_ICON_SIZE
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.espresso_component.CommonActions
import org.junit.Assert.assertEquals
import org.junit.Test

@UiTest
class MediaLoaderTransformationTest : BaseTest() {

    override fun setUp() {
        super.setUp()
        countingIdlingResource.increment()
    }

    override fun tearDown() {
        onImageView {
            it.clearImage()
        }

        super.tearDown()
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

    private fun View.takeScreenshot(caseName: String) {
        takeEagerScreenshot(caseName)
    }

    private fun View.takeEagerScreenshot(caseName: String) {
        CommonActions.takeScreenShotVisibleViewInScreen(this, "media_loader", caseName)
    }
}
