package com.tokopedia.tkpd.home.favorite.domain.interactor;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author madi on 3/24/17.
 */
public class GetAllDataFavoriteUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private RequestParams mockRequestParams;
    @Mock
    private GetFavoriteShopUsecase getFavoriteShopUsecase;
    @Mock
    private GetTopAdsShopUseCase getTopAdsShopUseCase;
    @Mock
    private Context context;

    private GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        getAllDataFavoriteUseCase = new GetAllDataFavoriteUseCase(
                context,
                threadExecutor,
                postExecutionThread,
                getFavoriteShopUsecase,
                getTopAdsShopUseCase);

    }

    @Test
    public void testCreateObservable() throws Exception {
        getAllDataFavoriteUseCase.createObservable(mockRequestParams);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

}