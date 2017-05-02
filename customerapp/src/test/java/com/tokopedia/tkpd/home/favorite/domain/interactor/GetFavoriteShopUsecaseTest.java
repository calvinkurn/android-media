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
public class GetFavoriteShopUsecaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private RequestParams mockRequestParams;

    private GetFavoriteShopUsecase getFavoriteShopUsecase;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        getFavoriteShopUsecase = new GetFavoriteShopUsecase(
                threadExecutor, postExecutionThread, favoriteRepository);

    }

    @Test
    public void testCreateObservable() throws Exception {
        getFavoriteShopUsecase.createObservable(mockRequestParams);

        verify(favoriteRepository)
                .getFavoriteShop(mockRequestParams.getParamsAllValueInString());

        verifyNoMoreInteractions(favoriteRepository);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

    @Test
    public void testGetDefaultParamsNotEmpty() throws Exception {
        RequestParams defaultParams = GetFavoriteShopUsecase.getDefaultParams();
        assertTrue(defaultParams != null);

        assertTrue(defaultParams.getParameters() != null
                && defaultParams.getParameters().size() > 0);

        assertTrue(defaultParams
                .getParameters().containsKey(GetFavoriteShopUsecase.KEY_PAGE));

        assertTrue(defaultParams.getParameters()
                .containsKey(GetFavoriteShopUsecase.KEY_OPTION_LOCATION));

        assertTrue(defaultParams.getParameters()
                .containsKey(GetFavoriteShopUsecase.KEY_OPTION_NAME));

        assertTrue(defaultParams.getParameters()
                .containsKey(GetFavoriteShopUsecase.KEY_PER_PAGE));


        assertTrue(
                defaultParams.getParameters().get(GetFavoriteShopUsecase.KEY_PER_PAGE)
                        == GetFavoriteShopUsecase.DEFAULT_PER_PAGE);
        assertTrue(
                defaultParams.getParameters().get(GetFavoriteShopUsecase.KEY_OPTION_NAME)
                        == GetFavoriteShopUsecase.DEFAULT_OPTION_NAME);

        assertTrue(
                defaultParams.getParameters().get(GetFavoriteShopUsecase.KEY_PAGE)
                        == GetFavoriteShopUsecase.INITIAL_VALUE);

        assertTrue(
                defaultParams.getParameters().get(GetFavoriteShopUsecase.KEY_OPTION_LOCATION)
                        == GetFavoriteShopUsecase.DEFAULT_OPTION_LOCATION);

    }
}