package com.tokopedia.shop.product.data.repository;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductRepositoryImpl implements ShopProductRepository {

    private final ShopProductCloudDataSource shopProductCloudDataSource;

    @Inject
    public ShopProductRepositoryImpl(ShopProductCloudDataSource shopProductCloudDataSource) {
        this.shopProductCloudDataSource = shopProductCloudDataSource;
    }

    @Override
    public Observable<PagingList<ShopProduct>> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        return shopProductCloudDataSource.getShopProductList(shopProductRequestModel).flatMap(new Func1<Response<DataResponse<PagingList<ShopProduct>>>, Observable<PagingList<ShopProduct>>>() {
            @Override
            public Observable<PagingList<ShopProduct>> call(Response<DataResponse<PagingList<ShopProduct>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    @Override
    public Observable<List<ShopProductCampaign>> getProductCampaigns(String ids) {
        return shopProductCloudDataSource.getProductCampaigns(ids);
    }
}
