package com.tokopedia.tkpd.home.favorite.data;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import rx.Observable;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author madi on 3/24/17.
 */
public class FavoriteDataRepositoryTest {

    @Mock
    private FavoriteFactory favoriteFactory;
    @Mock
    private RequestParams requestParams;

    private FavoriteDataRepository favoriteDataRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        favoriteDataRepository = new FavoriteDataRepository(favoriteFactory);
        TKPDMapParam<String, Object> stringObjectHashMap = new TKPDMapParam<>();
        given(requestParams.getParameters()).willReturn(stringObjectHashMap);
    }

    @Test
    public void testGetWishlist() throws Exception {

        Observable<DomainWishlist> mockDomainWishlistObservable
                = Observable.just(new DomainWishlist());

        given(favoriteDataRepository.getWishlist(requestParams.getParameters()))
                .willReturn(mockDomainWishlistObservable);

        favoriteDataRepository.getWishlist(requestParams.getParameters());

        verify(favoriteFactory).getWishlist(requestParams.getParameters());

    }

    @Test
    public void testGetWishlistWithoutForceRefresh() throws Exception {

    }

    @Test
    public void testGetFavoriteShop() throws Exception {

    }

    @Test
    public void testGetTopAdsShop() throws Exception {

    }

    @Test
    public void testAddFavoriteShop() throws Exception {

    }

}