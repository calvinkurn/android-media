package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;
import android.test.mock.MockContext;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Response;
import rx.Observable;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * @author madi on 3/27/17.
 */
public class CloudTopAdsShopDataSourceTest {
    @Mock
    private MockContext context;
    @Mock
    private TopAdsService topAdsService;
    @Mock
    private RequestParams requestParams;

    private Gson gson;
    private CloudTopAdsShopDataSource cloudTopAdsShopDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gson = new Gson();
        given(requestParams.getParameters()).willReturn(new TKPDMapParam<String, Object>());
        cloudTopAdsShopDataSource = new CloudTopAdsShopDataSource(context, gson, topAdsService);
    }

    @Test
    public void getTopAdsShop() throws Exception {
        Observable<Response<String>> fakeResultObservable
                = Observable.just(Response.success(""));

        given(topAdsService.getShopTopAds(requestParams.getParameters()))
                .willReturn(fakeResultObservable);
        cloudTopAdsShopDataSource.getTopAdsShop(requestParams.getParameters());

        verify(topAdsService, atLeastOnce()).getShopTopAds(requestParams.getParameters());
    }

}