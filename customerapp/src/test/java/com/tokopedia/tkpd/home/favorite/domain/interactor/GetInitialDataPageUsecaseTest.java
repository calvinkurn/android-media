package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author madi on 3/23/17.
 */
public class GetInitialDataPageUsecaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private GetFavoriteShopUsecase getFavoriteShopUsecase;
    @Mock
    private GetWishlistUsecase getWishlistUsecase;
    @Mock
    private RequestParams mockRequestParams;
    @Mock
    private GetTopAdsShopUseCase getTopAdsShopUseCase;

    private GetInitialDataPageUsecase getInitialDataPageUsecase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        getInitialDataPageUsecase = new GetInitialDataPageUsecase(
                threadExecutor,
                postExecutionThread,
                getFavoriteShopUsecase,
                getWishlistUsecase,
                getTopAdsShopUseCase);

    }

    @Test
    public void testCreateObservable() throws Exception {
        getInitialDataPageUsecase.createObservable(mockRequestParams);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

}