package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author madi on 3/23/17.
 */
public class GetTopAdsShopUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private RequestParams mockRequestParams;

    private GetTopAdsShopUseCase getTopAdsShopUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        getTopAdsShopUseCase = new GetTopAdsShopUseCase(
                threadExecutor, postExecutionThread, favoriteRepository);
    }

    @Test
    public void testCreateObservable() throws Exception {
        getTopAdsShopUseCase.createObservable(mockRequestParams);
        verify(favoriteRepository).getTopAdsShop(mockRequestParams.getParameters(), false);
        verifyNoMoreInteractions(favoriteRepository);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

    @Test
    public void testGetDefaultParams() throws Exception {
        RequestParams defaultParams = GetTopAdsShopUseCase.defaultParams();
        assertTrue(defaultParams != null);

        assertTrue(defaultParams.getParameters() != null
                && defaultParams.getParameters().size() > 0);

        assertTrue(defaultParams.getParameters().containsKey(GetTopAdsShopUseCase.KEY_ITEM));
        assertTrue(defaultParams.getParameters().containsKey(GetTopAdsShopUseCase.KEY_PAGE));
        assertTrue(defaultParams.getParameters().containsKey(GetTopAdsShopUseCase.KEY_SRC));

        assertTrue(
                defaultParams.getParameters().get(GetTopAdsShopUseCase.KEY_PAGE)
                        == GetTopAdsShopUseCase.TOPADS_PAGE_DEFAULT_VALUE
        );

        assertTrue(
                defaultParams.getParameters().get(GetTopAdsShopUseCase.KEY_ITEM)
                        == GetTopAdsShopUseCase.TOPADS_ITEM_DEFAULT_VALUE
        );

        assertTrue(
                defaultParams.getParameters().get(GetTopAdsShopUseCase.KEY_SRC)
                        == GetTopAdsShopUseCase.SRC_FAV_SHOP_VALUE
        );
    }

}