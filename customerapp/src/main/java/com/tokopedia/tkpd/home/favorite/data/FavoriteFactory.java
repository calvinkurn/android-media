package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudFavoriteShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudTopAdsShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudWishlistDataStore;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public class FavoriteFactory {
    private Context mContext;
    private Gson mGson;
    private final ServiceV4 mServiceVersion4;
    private final TopAdsService mTopAdsService;
    private final MojitoService mMojitoService;

    public FavoriteFactory(Context context, Gson gson,
                           ServiceV4 serviceVersion4,
                           TopAdsService topAdsService, MojitoService mojitoService) {

        mContext = context;
        mGson = gson;
        mServiceVersion4 = serviceVersion4;
        mTopAdsService = topAdsService;
        mMojitoService = mojitoService;
    }

    Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> param) {
        return new CloudWishlistDataStore(mContext, mGson, mMojitoService).getWishlist(param);
    }

    Observable<FavoriteShop> getFavoriteShop(TKPDMapParam<String, String> param) {
        return new CloudFavoriteShopDataSource(mContext, mGson, mServiceVersion4).getFavorite(param);
    }

    Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params) {
        CloudTopAdsShopDataSource topAdsShopDataSource
                = new CloudTopAdsShopDataSource(mContext, mGson, mTopAdsService);
        return topAdsShopDataSource.getTopAdsShop(params);
    }
}
