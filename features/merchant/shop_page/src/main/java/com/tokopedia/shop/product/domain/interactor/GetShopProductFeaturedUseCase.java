package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductFeaturedUseCase extends GetShopProductAttributeUseCase<List<ShopProductViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";
    private static final String OFFICIAL_STORE = "OFFICIAL_STORE";

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;

    @Inject
    public GetShopProductFeaturedUseCase(GetFeatureProductListUseCase getFeatureProductListUseCase,
                                         GetWishListUseCase getWishListUseCase,
                                         GetProductCampaignsUseCase getProductCampaignsUseCase,
                                         UserSession userSession,
                                         ShopProductMapper shopProductMapper) {
        super(getWishListUseCase, getProductCampaignsUseCase, userSession, shopProductMapper);
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
    }

    @Override
    public Observable<List<ShopProductViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        final boolean officialStore = requestParams.getBoolean(OFFICIAL_STORE, false);
        return getFeatureProductListUseCase.createObservable(GetFeatureProductListUseCase.createRequestParam(shopId))
                .flatMap(new Func1<List<GMFeaturedProduct>, Observable<List<ShopProductViewModel>>>() {
            @Override
            public Observable<List<ShopProductViewModel>> call(final List<GMFeaturedProduct> gmFeaturedProductList) {
                List<ShopProductViewModel> shopProductViewModelList = shopProductMapper.convertFromProductFeatured(gmFeaturedProductList);
                return getShopProductViewModelList(isShopOwner(shopId), officialStore, shopProductViewModelList);
            }
        });
    }

    public static RequestParams createRequestParam(String shopId, boolean officialStore) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putBoolean(OFFICIAL_STORE, officialStore);
        return requestParams;
    }
}
