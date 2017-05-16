package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author madi on 3/23/17.
 */
public class GetWishlistUsecaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private RequestParams mockRequestParams;

    private GetWishlistUsecase getWishlistUsecase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getWishlistUsecase = new GetWishlistUsecase(
                threadExecutor, postExecutionThread, favoriteRepository);
    }

    @Test
    public void testCreateObservable() throws Exception {
        getWishlistUsecase.createObservable(mockRequestParams);
        verify(favoriteRepository).getWishlist(mockRequestParams.getParameters());

        verifyNoMoreInteractions(favoriteRepository);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

    @Test
    public void testGetDefaultParamsNotEmpty() throws Exception {
        RequestParams defaultParams = GetWishlistUsecase.getDefaultParams();
        assertTrue(defaultParams != null);

        assertTrue(defaultParams.getParameters() != null
                && defaultParams.getParameters().size() > 0);

        assertTrue(defaultParams.getParameters().containsKey(GetWishlistUsecase.KEY_COUNT));
        assertTrue(defaultParams.getParameters().containsKey(GetWishlistUsecase.KEY_PAGE));

        assertTrue(
                defaultParams.getParameters().get(GetWishlistUsecase.KEY_COUNT)
                == GetWishlistUsecase.DEFAULT_COUNT_VALUE
        );
        assertTrue(
                defaultParams.getParameters().get(GetWishlistUsecase.KEY_PAGE)
                        == GetWishlistUsecase.DEFAULT_PAGE_VALUE
        );
    }

}