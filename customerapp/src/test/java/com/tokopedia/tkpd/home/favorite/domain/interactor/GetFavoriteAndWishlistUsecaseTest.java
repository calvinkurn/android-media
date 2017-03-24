package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.FuncN;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author madi on 3/23/17.
 */
public class GetFavoriteAndWishlistUsecaseTest {

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

    private GetFavoriteAndWishlistUsecase getFavoriteAndWishlistUsecase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        getFavoriteAndWishlistUsecase = new GetFavoriteAndWishlistUsecase(
                threadExecutor, postExecutionThread,getFavoriteShopUsecase, getWishlistUsecase);

    }

    @Test
    public void testCreateObservable() throws Exception {
        getFavoriteAndWishlistUsecase.createObservable(mockRequestParams);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

}