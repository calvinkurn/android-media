package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */
@Deprecated
public class GetShopProductLimitedUseCase extends UseCase<PagingList<ShopProductBaseViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";
    private static final String GOLD_MERCHANT_STORE = "GOLD_MERCHANT";
    private static final String OFFICIAL_STORE = "OFFICIAL_STORE";
    private static final String PAGE = "PAGE";
    private static final String IS_SHOP_CLOSED = "IS_SHOP_CLOSED";
    private static final String PER_PAGE = "PER_PAGE";

    private final GetShopProductFeaturedUseCase getShopProductFeaturedUseCase;
    private final GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase;
    private ShopProductMapper shopProductMapper;

    @Inject
    public GetShopProductLimitedUseCase(GetShopProductFeaturedUseCase getShopProductFeaturedUseCase,
                                        GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase,
                                        ShopProductMapper shopProductMapper) {
        this.getShopProductFeaturedUseCase = getShopProductFeaturedUseCase;
        this.getShopProductListWithAttributeUseCase = getShopProductListWithAttributeUseCase;
        this.shopProductMapper = shopProductMapper;
    }

    @Override
    public Observable<PagingList<ShopProductBaseViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        final boolean goldMerchantStore = requestParams.getBoolean(GOLD_MERCHANT_STORE, false);
        final boolean officialStore = requestParams.getBoolean(OFFICIAL_STORE, false);
        final int page = requestParams.getInt(PAGE, 0);
        final int perPage = requestParams.getInt(PER_PAGE, ShopPageTrackingConstant.DEFAULT_PER_PAGE);
        final boolean isShopClosed = requestParams.getBoolean(IS_SHOP_CLOSED, false);
        final ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(
                shopId,isShopClosed, officialStore, page, true, perPage
        );
        List<ShopProductLimitedFeaturedViewModelOld> defaultFeaturedProductList = new ArrayList<>();
        Observable<List<ShopProductLimitedFeaturedViewModelOld>> featuredProductObservable = Observable.just(defaultFeaturedProductList);
//        if ((goldMerchantStore || officialStore) && page == 1) {
//            featuredProductObservable = getShopProductFeaturedUseCase.createObservable(GetShopProductFeaturedUseCase.createRequestParam(shopId, officialStore))
//                    .flatMap(new Func1<List<ShopProductViewModelOld>, Observable<List<ShopProductLimitedFeaturedViewModelOld>>>() {
//                        @Override
//                        public Observable<List<ShopProductLimitedFeaturedViewModelOld>> call(List<ShopProductViewModelOld> shopProductViewModelOlds) {
//                            return Observable.just(shopProductMapper.convertFromProductViewModelFeatured(shopProductViewModelOlds));
//                        }
//                    });
//        }
        Observable<PagingList<ShopProductHomeViewModelOld>> shopProductObservable =
                getShopProductListWithAttributeUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel))
                        .flatMap(new Func1<PagingList<ShopProductViewModelOld>, Observable<PagingList<ShopProductHomeViewModelOld>>>() {
                            @Override
                            public Observable<PagingList<ShopProductHomeViewModelOld>> call(PagingList<ShopProductViewModelOld> shopProductViewModelPagingList) {
                                return Observable.just(shopProductMapper.convertFromProductViewModel(shopProductViewModelPagingList, page, ShopPageTrackingConstant.DEFAULT_PER_PAGE));
                            }
                        });

        return Observable.zip(
                featuredProductObservable.subscribeOn(Schedulers.io()), shopProductObservable.subscribeOn(Schedulers.io()),
                new Func2<List<ShopProductLimitedFeaturedViewModelOld>, PagingList<ShopProductHomeViewModelOld>, PagingList<ShopProductBaseViewModel>>() {
                    @Override
                    public PagingList<ShopProductBaseViewModel> call(List<ShopProductLimitedFeaturedViewModelOld> shopProductFeatured, PagingList<ShopProductHomeViewModelOld> shopProductList) {
                        PagingList<ShopProductBaseViewModel> shopProductBaseViewModelPagingList = new PagingList<>();
                        shopProductBaseViewModelPagingList.setPaging(shopProductList.getPaging());
                        shopProductBaseViewModelPagingList.setTotalData(shopProductList.getTotalData());
                        List<ShopProductBaseViewModel> shopProductBaseViewModels = new ArrayList<>();
                        shopProductBaseViewModels.addAll(shopProductFeatured);
                        shopProductBaseViewModels.addAll(shopProductList.getList());
                        shopProductBaseViewModelPagingList.setList(shopProductBaseViewModels);
                        return shopProductBaseViewModelPagingList;
                    }
                }
        );
    }

    public static RequestParams createRequestParam(String shopId, boolean goldMerchantStore, boolean officialStore, int page,
                                                   boolean isShopClosed, int perPage) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putBoolean(GOLD_MERCHANT_STORE, goldMerchantStore);
        requestParams.putBoolean(OFFICIAL_STORE, officialStore);
        requestParams.putInt(PAGE, page);
        requestParams.putBoolean(IS_SHOP_CLOSED, isShopClosed);
        requestParams.putInt(PER_PAGE, perPage);
        return requestParams;
    }
}
