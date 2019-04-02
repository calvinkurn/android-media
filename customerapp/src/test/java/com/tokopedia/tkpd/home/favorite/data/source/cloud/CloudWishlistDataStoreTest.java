package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.test.mock.MockContext;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * @author madi on 3/27/17.
 */
public class CloudWishlistDataStoreTest {
    @Mock
    private MockContext context;
    @Mock
    private RequestParams requestParams;

    private CloudWishlistDataStore cloudWishlistDataStore;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cloudWishlistDataStore = new CloudWishlistDataStore(context);
        given(requestParams.getParameters()).willReturn(new TKPDMapParam<String, Object>());
    }

    @Test
    public void testGetWishlist() throws Exception {

        String fakeUserId = "1";
        Observable<DomainWishlist> fakeResultObservable
                = Observable.just(new DomainWishlist());

        given(cloudWishlistDataStore.getWishlist(fakeUserId, requestParams.getParameters()))
                .willReturn(fakeResultObservable);
        cloudWishlistDataStore.getWishlist(fakeUserId, requestParams.getParameters());

        verify(cloudWishlistDataStore, atLeastOnce()).getWishlist(fakeUserId, requestParams.getParameters());
    }

}