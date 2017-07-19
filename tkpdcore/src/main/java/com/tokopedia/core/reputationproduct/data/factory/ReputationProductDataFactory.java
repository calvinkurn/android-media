package com.tokopedia.core.reputationproduct.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.apiservices.shop.apis.ShopApi;
import com.tokopedia.core.reputationproduct.data.mapper.LikeDislikeDomainMapper;

/**
 * Created by yoasfs on 18/07/17.
 */

public class ReputationProductDataFactory {
    private Context context;
    private ShopService shopService;
    private LikeDislikeDomainMapper likeDislikeDomainMapper;

    public ReputationProductDataFactory(Context context,
                                        ShopService shopService,
                                        LikeDislikeDomainMapper likeDislikeDomainMapper) {
        this.context = context;
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
    }
    public CloudReputationProductDataSource getCloudReputationProductDataSource() {
        return new CloudReputationProductDataSource(context, shopService, likeDislikeDomainMapper);
    }
}
