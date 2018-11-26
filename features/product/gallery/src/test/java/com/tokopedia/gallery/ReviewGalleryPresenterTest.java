package com.tokopedia.gallery;

import com.tokopedia.gallery.domain.GetImageReviewUseCase;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl;
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber;
import com.tokopedia.usecase.RequestParams;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReviewGalleryPresenterTest {

    private GetImageReviewUseCase getImageReviewUseCase;
    private GalleryView galleryView;
    private ReviewGalleryPresenter reviewGalleryPresenter;

    @Before
    public void setUp() throws Exception {
        getImageReviewUseCase = Mockito.mock(GetImageReviewUseCase.class);
        galleryView = Mockito.mock(GalleryView.class);
        reviewGalleryPresenter = new ReviewGalleryPresenterImpl(getImageReviewUseCase, galleryView);
    }

    @Test
    public void cancelLoadDataRequest_unsubscribeUseCase()
    {
        //when
        reviewGalleryPresenter.cancelLoadDataRequest();

        //then
        Mockito.verify(getImageReviewUseCase).unsubscribe();
    }

    @Test
    public void loadData_executeUseCase()
    {
        //given
        int productId = 1234;
        int page = 1;

        ArgumentCaptor<RequestParams> requestParams = ArgumentCaptor.forClass(RequestParams.class);
        ArgumentCaptor<GetImageReviewSubscriber> getImageReviewSubscriber
                = ArgumentCaptor.forClass(GetImageReviewSubscriber.class);

        //when
        reviewGalleryPresenter.loadData(productId, page);

        //then
        Mockito.verify(getImageReviewUseCase).execute(
                requestParams.capture(),
                getImageReviewSubscriber.capture()
        );
        assertEquals(productId, requestParams.getValue().getInt(GetImageReviewUseCase.KEY_PRODUCT_ID, 0));
        assertEquals(page, requestParams.getValue().getInt(GetImageReviewUseCase.KEY_PAGE, 0));
        assertEquals(ReviewGalleryPresenter.DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE, requestParams.getValue().getInt(GetImageReviewUseCase.KEY_TOTAL, 0));
        assertEquals(galleryView, getImageReviewSubscriber.getValue().galleryView);
    }
}