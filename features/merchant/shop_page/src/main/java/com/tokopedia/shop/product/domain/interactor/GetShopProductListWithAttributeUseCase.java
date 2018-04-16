package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductListWithAttributeUseCase extends GetShopProductAttributeUseCase<PagingList<ShopProductViewModel>> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final GetShopProductListUseCase getShopProductListUseCase;

    @Inject
    public GetShopProductListWithAttributeUseCase(GetShopProductListUseCase getShopProductListUseCase,
                                                  GetWishListUseCase getWishListUseCase,
                                                  GetProductCampaignsUseCase getProductCampaignsUseCase,
                                                  UserSession userSession,
                                                  ShopProductMapper shopProductMapper) {
        super(getWishListUseCase, getProductCampaignsUseCase, userSession, shopProductMapper);
        this.getShopProductListUseCase = getShopProductListUseCase;
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }

    @Override
    public Observable<PagingList<ShopProductViewModel>> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return getShopProductListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel)).flatMap(new Func1<PagingList<ShopProduct>, Observable<PagingList<ShopProductViewModel>>>() {
            @Override
            public Observable<PagingList<ShopProductViewModel>> call(final PagingList<ShopProduct> shopProductPagingList) {
                return getShopProductViewModelList(shopProductRequestModel, shopProductPagingList);
            }
        });
    }

    private Observable<PagingList<ShopProductViewModel>> getShopProductViewModelList(
            final ShopProductRequestModel shopProductRequestModel, final PagingList<ShopProduct> shopProductPagingList) {
        List<ShopProductViewModel> shopProductViewModelList = shopProductMapper.convertFromShopProduct(shopProductPagingList.getList());
        return getShopProductViewModelList(isShopOwner(shopProductRequestModel.getShopId()),
                shopProductRequestModel.isOfficialStore(), shopProductViewModelList)
                .flatMap(new Func1<List<ShopProductViewModel>, Observable<PagingList<ShopProductViewModel>>>() {
                    @Override
                    public Observable<PagingList<ShopProductViewModel>> call(List<ShopProductViewModel> shopProductViewModelList) {
                        PagingList<ShopProductViewModel> pagingList = new PagingList<>();
                        pagingList.setTotalData(shopProductPagingList.getTotalData());
                        pagingList.setPaging(shopProductPagingList.getPaging());
                        pagingList.setList(shopProductViewModelList);
                        return Observable.just(pagingList);
                    }
                });
    }
}
