package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.test.mock.MockContext;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Response;
import rx.Observable;

import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * @author madi on 3/27/17.
 */
public class CloudWishlistDataStoreTest {
    @Mock
    private MockContext context;
    @Mock
    private MojitoService mojitoService;
    @Mock
    private RequestParams requestParams;

    private CloudWishlistDataStore cloudWishlistDataStore;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Gson gson = new Gson();
        cloudWishlistDataStore = new CloudWishlistDataStore(context, gson, mojitoService);
        given(requestParams.getParameters()).willReturn(new TKPDMapParam<String, Object>());
    }

    @Test
    public void testGetWishlist() throws Exception {

        String fakeUserId = "1";
        Observable<Response<String>> fakeResultObservable
                = Observable.just(Response.success(""));

        given(mojitoService.getWishlist(fakeUserId, requestParams.getParameters()))
                .willReturn(fakeResultObservable);
        cloudWishlistDataStore.getWishlist(fakeUserId, requestParams.getParameters());

        verify(mojitoService, atLeastOnce()).getWishlist(fakeUserId,requestParams.getParameters());
    }

}