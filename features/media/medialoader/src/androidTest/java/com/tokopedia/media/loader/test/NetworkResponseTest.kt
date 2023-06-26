package com.tokopedia.media.loader.test

import android.widget.Toast
import com.tokopedia.media.loader.BaseTest
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Assert
import org.junit.Test

@UiTest
class NetworkResponseTest : BaseTest() {

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
    fun loadImage_getNetworkHeaderLog() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                shouldTrackNetworkResponse(true)

                networkResponse(
                    header = { data ->
                        countingIdlingResource.decrement()

                        // Then
                        Assert.assertTrue("the data should be not empty", data.isNotEmpty())
                    }
                )
            }
        }
    }

    @Test
    fun loadImage_getNetworkResponseLog() {
        onImageView {
            // When
            it.loadImage(publicImageUrl) {
                shouldTrackNetworkResponse(true)

                networkResponse(
                    response = { data ->
                        countingIdlingResource.decrement()

                        // Then
                        Assert.assertTrue("the data should be not empty", data.isNotEmpty())
                    }
                )
            }
        }
    }
}
