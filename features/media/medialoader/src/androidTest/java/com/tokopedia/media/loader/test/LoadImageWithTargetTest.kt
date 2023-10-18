package com.tokopedia.media.loader.test

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.BaseTest
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.getBitmapImageUrlAsFlow
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.test.application.annotations.UiTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

@UiTest
class LoadImageWithTargetTest : BaseTest() {

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
    fun loadImage_getBitmapImageUrl_withTarget() {
        // When
        publicImageUrl.getBitmapImageUrl(
            context = applicationContext,
            target = MediaBitmapEmptyTarget(
                onReady = { bitmap ->
                    countingIdlingResource.decrement()
                    setImageViewContent(bitmap)

                    // Then
                    assert(bitmap.width.isMoreThanZero())
                }
            )
        )
    }

    @Test
    fun loadImage_getBitmapImageUrl_directOnReady() {
        // When
        publicImageUrl.getBitmapImageUrl(
            context = applicationContext,
            onReady = { bitmap ->
                countingIdlingResource.decrement()
                setImageViewContent(bitmap)

                // Then
                assert(bitmap.width.isMoreThanZero())
            }
        )
    }

    @Test
    fun loadImage_getBitmapImageUrl_directOnReady_withCircleCropTransform() {
        // When
        publicImageUrl.getBitmapImageUrl(
            context = applicationContext,
            properties = {
                isCircular(true)
            },
            onReady = { bitmap ->
                countingIdlingResource.decrement()
                setImageViewContent(bitmap)

                // Then
                assert(bitmap.width.isMoreThanZero())
            }
        )
    }

    @Test
    fun loadImage_getBitmapImageUrl_flowable() {
        // When
        runBlocking {
            val bitmap = publicImageUrl.getBitmapImageUrlAsFlow(
                context = applicationContext,
                properties = {
                    listener(
                        onSuccess = { bitmap, _ ->
                            if (bitmap == null) return@listener

                            countingIdlingResource.decrement()
                            setImageViewContent(bitmap)

                            // Then
                            assert(bitmap.width.isMoreThanZero())
                        }
                    )
                }
            ).first()

            assert(bitmap.width.isMoreThanZero())
        }
    }

    @Test
    fun loadImage_withEmptyTarget() {
        // When
        loadImageWithEmptyTarget(
            context = applicationContext,
            url = publicImageUrl,
            mediaTarget = MediaBitmapEmptyTarget(
                onReady = { bitmap ->
                    countingIdlingResource.decrement()
                    setImageViewContent(bitmap)

                    // Then
                    assert(bitmap.width.isMoreThanZero())
                }
            ))
    }

}
