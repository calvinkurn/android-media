package com.tokopedia.topads.dashboard.data.repository;

import android.content.Context;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.topads.dashboard.domain.model.ProductListDomain;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsSearchProductRepositoryImpl implements TopAdsSearchProductRepository {

    private CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource;
    private Context context;

    public TopAdsSearchProductRepositoryImpl(Context context,
            CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {
        this.context = context;
        this.cloudTopAdsSearchProductDataSource = cloudTopAdsSearchProductDataSource;
    }

    @Override
    public Observable<ProductListDomain> searchProduct(Map<String, String> param) {
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, new UserSession(context).getShopId());
        return cloudTopAdsSearchProductDataSource.searchProduct(param);
    }
}
