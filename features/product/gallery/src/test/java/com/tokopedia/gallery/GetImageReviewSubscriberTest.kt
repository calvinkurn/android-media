package com.tokopedia.gallery

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.gallery.viewmodel.ImageReviewListModel
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

import org.junit.Before
import org.junit.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito

import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(JUnitPlatform::class)
class GetImageReviewSubscriberTest : Spek ({

    given("getting response from server") {
        val galleryView: GalleryView = mock()
        val getImageReviewSubscriber = GetImageReviewSubscriber(galleryView)

        val imageReviewItem1 = ImageReviewItem()
        val imageReviewItem2 = ImageReviewItem()

        val imageReviewItems = ArrayList<ImageReviewItem>()
        imageReviewItems.add(imageReviewItem1)
        imageReviewItems.add(imageReviewItem2)

        val isHasNextPage = true
        val imageReviewListModel = ImageReviewListModel(imageReviewItems, isHasNextPage)

        val e = Throwable()

        on("success response from server") {
            getImageReviewSubscriber.onNext(imageReviewListModel)

            it("handle item result") {
                verify(galleryView).handleItemResult(imageReviewItems, isHasNextPage)
            }
        }

        on("error from server") {
            getImageReviewSubscriber.onError(e)

            it("handle error result") {
                verify(galleryView).handleErrorResult(e)
            }
        }
    }
})