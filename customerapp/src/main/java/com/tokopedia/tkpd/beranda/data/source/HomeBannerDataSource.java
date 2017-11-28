package com.tokopedia.tkpd.beranda.data.source;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.beranda.data.mapper.HomeBannerMapper;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;

import rx.Observable;


/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeBannerDataSource {

    private final Context context;
    private final CategoryApi categoryApi;
    private final HomeBannerMapper homeBannerMapper;

    public HomeBannerDataSource(Context context, CategoryApi categoryApi, HomeBannerMapper homeBannerMapper) {
        this.context = context;
        this.categoryApi = categoryApi;
        this.homeBannerMapper = homeBannerMapper;
    }

    public Observable<HomeBannerResponseModel> getHomeBanner(RequestParams requestParams){
        return categoryApi.getBanners(SessionHandler.getLoginID(context), requestParams.getParameters())
                .map(homeBannerMapper);
    }
}
