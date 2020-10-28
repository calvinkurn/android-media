package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.common.usecase.PostExecutionThread;
import com.tokopedia.seller.common.usecase.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author madi on 3/24/17.
 */
public class AddFavoriteShopUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private RequestParams mockRequestParams;
    @Mock
    private FavoriteRepository favoriteRepository;

    private AddFavoriteShopUseCase addFavoriteShopUseCase;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addFavoriteShopUseCase = new AddFavoriteShopUseCase(
                threadExecutor,
                postExecutionThread,
                favoriteRepository);

    }

    @Test
    public void testCreateObservable() throws Exception {
        addFavoriteShopUseCase.createObservable(mockRequestParams);
        verify(favoriteRepository).addFavoriteShop(mockRequestParams.getParamsAllValueInString());

        verifyNoMoreInteractions(favoriteRepository);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

}