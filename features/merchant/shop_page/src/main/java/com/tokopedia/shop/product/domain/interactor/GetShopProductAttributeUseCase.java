package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */
@Deprecated
public abstract class GetShopProductAttributeUseCase<T> extends UseCase<T> {

    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;
    protected final ShopProductMapper shopProductMapper;
    private final GetProductCampaignsUseCase getProductCampaignsUseCase;

    public GetShopProductAttributeUseCase(GetWishListUseCase getWishListUseCase, GetProductCampaignsUseCase getProductCampaignsUseCase,
                                          UserSession userSession, ShopProductMapper shopProductMapper) {
        this.getWishListUseCase = getWishListUseCase;
        this.getProductCampaignsUseCase = getProductCampaignsUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    protected Observable<List<ShopProductViewModelOld>> getShopProductViewModelList(
            final boolean isShopOwner, boolean officialStore, final List<ShopProductViewModelOld> shopProductViewModelOldList) {
        List<String> defaultWishList = new ArrayList<>();
        List<ShopProductCampaign> defaultShopProductCampaignList = new ArrayList<>();
        Observable<List<String>> wishlistObservable = Observable.just(defaultWishList);
        Observable<List<ShopProductCampaign>> campaignListObservable = Observable.just(defaultShopProductCampaignList);

        final List<String> productIdList = new ArrayList<>();
        for (ShopProductViewModelOld shopProductViewModelOld : shopProductViewModelOldList) {
            productIdList.add(shopProductViewModelOld.getId());
        }
        if (shopProductViewModelOldList.size() > 0 && !isShopOwner) {
            wishlistObservable = getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList));
        }
        if (shopProductViewModelOldList.size() > 0 && officialStore) {
            campaignListObservable = getProductCampaignsUseCase.createObservable(GetProductCampaignsUseCase.createRequestParam(productIdList));
        }

        return Observable.zip(wishlistObservable.subscribeOn(Schedulers.io()),
                campaignListObservable.subscribeOn(Schedulers.io()), new Func2<List<String>, List<ShopProductCampaign>, List<ShopProductViewModelOld>>() {
            @Override
            public List<ShopProductViewModelOld> call(List<String> wishList, List<ShopProductCampaign> shopProductCampaignList) {
                shopProductMapper.mergeShopProductViewModelWithWishList(shopProductViewModelOldList, wishList, !isShopOwner);
                shopProductMapper.mergeShopProductViewModelWithProductCampaigns(shopProductViewModelOldList, shopProductCampaignList);
                return shopProductViewModelOldList;
            }
        });
    }

    protected boolean isShopOwner(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}
