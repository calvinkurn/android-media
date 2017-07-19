package com.tokopedia.core.reputationproduct.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.apiservices.shop.apis.ShopApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudReputationProductDataSource {

    private Context context;
    private ShopService shopService;
    private LikeDislikeDomainMapper likeDislikeDomainMapper;

    public CloudReputationProductDataSource(Context context,
                                            ShopService shopService,
                                            LikeDislikeDomainMapper likeDislikeDomainMapper) {
        this.context = context;
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
    }

    public Observable<LikeDislikeDomain> getResCenterConversationMore(Map<String, String> parameters) {
        return shopService.getApi().getLikeReview(parameters)
                .map(likeDislikeDomainMapper);
    }
}
