package com.tokopedia.gallery

import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.gallery.viewmodel.ImageReviewListModel

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class GetImageReviewSubscriberTest {

    private var getImageReviewSubscriber: GetImageReviewSubscriber? = null
    private var galleryView: GalleryView? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        galleryView = Mockito.mock(GalleryView::class.java)
        getImageReviewSubscriber = GetImageReviewSubscriber(galleryView)
    }

    @Test
    fun onNext_validResult_handleItemResult() {
        //given
        val imageReviewItem1 = ImageReviewItem()
        val imageReviewItem2 = ImageReviewItem()

        val imageReviewItems = ArrayList<ImageReviewItem>()
        imageReviewItems.add(imageReviewItem1)
        imageReviewItems.add(imageReviewItem2)

        val isHasNextPage = true
        val imageReviewListModel = ImageReviewListModel(imageReviewItems, isHasNextPage)

        //when
        getImageReviewSubscriber!!.onNext(imageReviewListModel)

        //then
        Mockito.verify<GalleryView>(galleryView).handleItemResult(imageReviewItems, isHasNextPage)
    }

    @Test
    fun onError_handleErrorResult() {
        //given
        val e = Throwable()

        //when
        getImageReviewSubscriber!!.onError(e)

        //then
        Mockito.verify<GalleryView>(galleryView).handleErrorResult(e)
    }
}