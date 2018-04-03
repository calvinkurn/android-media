package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudLikeDislikeDataSource {

    private Context context;
    private ShopService shopService;
    private LikeDislikeDomainMapper likeDislikeDomainMapper;

    public CloudLikeDislikeDataSource(Context context,
                                      ShopService shopService,
                                      LikeDislikeDomainMapper likeDislikeDomainMapper) {
        this.context = context;
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
    }

    public Observable<LikeDislikeDomain> getLikeDislikeReviewCloudSource(Map<String, String> parameters) {
        return shopService.getApi().getLikeReview(AuthUtil.generateParams(MainApplication
                .getAppContext(), parameters))
                .map(likeDislikeDomainMapper);
    }
}
