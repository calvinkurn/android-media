package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/23/18.
 */
@Deprecated
public class GetShopProductListWithAttributeUseCase extends GetShopProductAttributeUseCase<PagingList<ShopProductViewModelOld>> {

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
    public Observable<PagingList<ShopProductViewModelOld>> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return getShopProductListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel))
                .flatMap(new Func1<PagingList<ShopProduct>, Observable<PagingList<ShopProductViewModelOld>>>() {
            @Override
            public Observable<PagingList<ShopProductViewModelOld>> call(final PagingList<ShopProduct> shopProductPagingList) {
                return getShopProductViewModelList(shopProductRequestModel, shopProductPagingList);
            }
        });
    }

    private Observable<PagingList<ShopProductViewModelOld>> getShopProductViewModelList(
            final ShopProductRequestModel shopProductRequestModel, final PagingList<ShopProduct> shopProductPagingList) {
        List<ShopProductViewModelOld> shopProductViewModelOldList = shopProductMapper.convertFromShopProduct(shopProductPagingList.getList(), shopProductRequestModel.getPage(), ShopPageTrackingConstant.DEFAULT_PER_PAGE);
        return getShopProductViewModelList(isShopOwner(shopProductRequestModel.getShopId()),
                shopProductRequestModel.isOfficialStore(), shopProductViewModelOldList)
                .flatMap(new Func1<List<ShopProductViewModelOld>, Observable<PagingList<ShopProductViewModelOld>>>() {
                    @Override
                    public Observable<PagingList<ShopProductViewModelOld>> call(List<ShopProductViewModelOld> shopProductViewModelOldList) {
                        PagingList<ShopProductViewModelOld> pagingList = new PagingList<>();
                        pagingList.setTotalData(shopProductPagingList.getTotalData());
                        pagingList.setPaging(shopProductPagingList.getPaging());
                        pagingList.setList(shopProductViewModelOldList);
                        return Observable.just(pagingList);
                    }
                });
    }
}
