package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;

import java.util.List;

import rx.Observable;

/**
 * Created by normansyahputa on 3/14/18.
 */

public class GetProductCampaignsUseCase extends UseCase<List<ShopProductCampaign>> {
    private static final String PRODUCT_IDS = "PRODUCT_IDS";

    private ShopProductRepository shopProductRepository;

    public GetProductCampaignsUseCase(ShopProductRepository shopProductRepository) {
        this.shopProductRepository = shopProductRepository;
    }

    public static RequestParams createRequestParam(List<String> productIdList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PRODUCT_IDS, productIdList);
        return requestParams;
    }

    @Override
    public Observable<List<ShopProductCampaign>> createObservable(RequestParams requestParams) {
        List<String> productIdList = (List<String>) requestParams.getObject(PRODUCT_IDS);
        return shopProductRepository.getProductCampaigns(WishListProductListMapper.convertCommaValue(productIdList));
    }
}
