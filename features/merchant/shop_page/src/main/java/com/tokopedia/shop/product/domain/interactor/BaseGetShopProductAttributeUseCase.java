package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public abstract class BaseGetShopProductAttributeUseCase<T> extends UseCase<T> {

    private final GetWishListUseCase getWishListUseCase;
    private final UserSessionInterface userSession;
    protected final ShopProductMapper shopProductMapper;
    private final GetProductCampaignsUseCase getProductCampaignsUseCase;

    public BaseGetShopProductAttributeUseCase(GetWishListUseCase getWishListUseCase,
                                              GetProductCampaignsUseCase getProductCampaignsUseCase,
                                              UserSessionInterface userSession, ShopProductMapper shopProductMapper) {
        this.getWishListUseCase = getWishListUseCase;
        this.getProductCampaignsUseCase = getProductCampaignsUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    protected Observable<List<ShopProductViewModel>> getShopProductViewModelList(
            final boolean isShopOwner, boolean officialStore, final List<ShopProductViewModel> shopProductViewModelList) {
        List<String> defaultWishList = new ArrayList<>();
        List<ShopProductCampaign> defaultShopProductCampaignList = new ArrayList<>();
        Observable<List<String>> wishlistObservable;
        Observable<List<ShopProductCampaign>> campaignListObservable;

        final List<String> productIdList = new ArrayList<>();
        for (ShopProductViewModel shopProductViewModel : shopProductViewModelList) {
            productIdList.add(shopProductViewModel.getId());
        }
        if (shopProductViewModelList.size() > 0 && !isShopOwner) {
            wishlistObservable = getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList));
        } else {
            wishlistObservable = Observable.just(defaultWishList);
        }
        if (shopProductViewModelList.size() > 0 && officialStore) {
            campaignListObservable = getProductCampaignsUseCase.createObservable(GetProductCampaignsUseCase.createRequestParam(productIdList));
        } else {
            campaignListObservable = Observable.just(defaultShopProductCampaignList);
        }

        return Observable.zip(
                Observable.just(shopProductViewModelList),
                wishlistObservable.subscribeOn(Schedulers.io()),
                campaignListObservable.subscribeOn(Schedulers.io()),
                new Func3<List<ShopProductViewModel>, List<String>, List<ShopProductCampaign>, List<ShopProductViewModel>>() {
                    @Override
                    public List<ShopProductViewModel> call(List<ShopProductViewModel> shopProductViewModels, List<String> wishList,
                                                           List<ShopProductCampaign> shopProductCampaigns) {
                        shopProductMapper.mergeShopProductViewModel(
                                shopProductViewModels,
                                shopProductCampaigns,
                                wishList, !isShopOwner);
                        return shopProductViewModels;
                    }
                }
        );

    }

    protected boolean isShopOwner(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}
