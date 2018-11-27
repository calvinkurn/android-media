package com.tokopedia.gallery;

import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.gallery.viewmodel.ImageReviewListModel;

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

    private GetImageReviewSubscriber getImageReviewSubscriber;
    private GalleryView galleryView;

    @Before
    public void setUp() throws Exception {
        galleryView = Mockito.mock(GalleryView.class);
        getImageReviewSubscriber = new GetImageReviewSubscriber(galleryView);
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

        boolean isHasNextPage = true;
        ImageReviewListModel imageReviewListModel = new ImageReviewListModel(imageReviewItems, isHasNextPage);

        //when
        getImageReviewSubscriber.onNext(imageReviewListModel);

        //then
        Mockito.verify(galleryView).handleItemResult(imageReviewItems, isHasNextPage);
    }

    @Test
    public void onError_handleErrorResult()
    {
        //given
        Throwable e = new Throwable();

        //when
        getImageReviewSubscriber.onError(e);

        //then
        Mockito.verify(galleryView).handleErrorResult(e);
    }
}