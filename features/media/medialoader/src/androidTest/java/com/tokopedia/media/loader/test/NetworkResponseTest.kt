package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseTest
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.loadImage
import com.tokopedia.test.application.annotations.UiTest
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

                networkResponse { data, type ->
                    countingIdlingResource.decrement()

                    // Then
                    assert(data.isNotEmpty())
                    assert(type == null)
                }
            }
        }
    }

    @Test
    fun loadImage_getNotFoundFailureType() {
        onImageView {
            // When
            it.loadImage(notFoundUrl) {
                shouldTrackNetworkResponse(true)

                networkResponse { data, type ->
                    countingIdlingResource.decrement()

                    // Then
                    assert(data.isNotEmpty())
                    assert(type == FailureType.NotFound)
                }
            }
        }
    }
}
