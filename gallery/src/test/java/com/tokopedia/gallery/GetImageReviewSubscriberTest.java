package com.tokopedia.gallery;

import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GetImageReviewSubscriberTest {

    private static final int TEST_START_ROW = 5;

    private GetImageReviewSubscriber getImageReviewSubscriber;
    private GalleryView galleryView;

    @Before
    public void setUp() throws Exception {
        galleryView = Mockito.mock(GalleryView.class);
        getImageReviewSubscriber = new GetImageReviewSubscriber(galleryView, TEST_START_ROW);
    }

    @Test
    public void onNext_emptyResult_handleEmptyResult()
    {
        //given
        List<ImageReviewItem> imageReviewItems = new ArrayList<>();

        //when
        getImageReviewSubscriber.onNext(imageReviewItems);

        //then
        Mockito.verify(galleryView).handleEmptyResult();
    }

    @Test
    public void onNext_validResult_handleItemResult()
    {
        //given
        ImageReviewItem imageReviewItem1 = new ImageReviewItem();
        ImageReviewItem imageReviewItem2 = new ImageReviewItem();

        List<ImageReviewItem> imageReviewItems = new ArrayList<>();
        imageReviewItems.add(imageReviewItem1);
        imageReviewItems.add(imageReviewItem2);

        //when
        getImageReviewSubscriber.onNext(imageReviewItems);

        //then
        Mockito.verify(galleryView).handleItemResult(imageReviewItems);
    }

    @Test
    public void onError_handleErrorResult()
    {
        //given

        //when
        getImageReviewSubscriber.onError(new Throwable());

        //then
        Mockito.verify(galleryView).handleErrorResult(TEST_START_ROW);
    }
}