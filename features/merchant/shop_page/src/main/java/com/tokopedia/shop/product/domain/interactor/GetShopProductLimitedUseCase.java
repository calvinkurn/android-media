package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
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

public class GetShopProductLimitedUseCase extends UseCase<PagingList<ShopProductBaseViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";
    private static final String GOLD_MERCHANT_STORE = "GOLD_MERCHANT";
    private static final String OFFICIAL_STORE = "OFFICIAL_STORE";
    private static final String PAGE = "PAGE";

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
        final ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setOfficialStore(officialStore);
        shopProductRequestModel.setPage(page);
        List<ShopProductLimitedFeaturedViewModel> defaultFeaturedProductList = new ArrayList<>();
        Observable<List<ShopProductLimitedFeaturedViewModel>> featuredProductObservable = Observable.just(defaultFeaturedProductList);
        if ((goldMerchantStore || officialStore) && page == 1) {
            featuredProductObservable = getShopProductFeaturedUseCase.createObservable(GetShopProductFeaturedUseCase.createRequestParam(shopId, officialStore))
                    .flatMap(new Func1<List<ShopProductViewModel>, Observable<List<ShopProductLimitedFeaturedViewModel>>>() {
                        @Override
                        public Observable<List<ShopProductLimitedFeaturedViewModel>> call(List<ShopProductViewModel> shopProductViewModels) {
                            return Observable.just(shopProductMapper.convertFromProductViewModelFeatured(shopProductViewModels));
                        }
                    });
        }
        Observable<PagingList<ShopProductHomeViewModel>> shopProductObservable =
                getShopProductListWithAttributeUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel))
                        .flatMap(new Func1<PagingList<ShopProductViewModel>, Observable<PagingList<ShopProductHomeViewModel>>>() {
                            @Override
                            public Observable<PagingList<ShopProductHomeViewModel>> call(PagingList<ShopProductViewModel> shopProductViewModelPagingList) {
                                return Observable.just(shopProductMapper.convertFromProductViewModel(shopProductViewModelPagingList));
                            }
                        });

        return Observable.zip(
                featuredProductObservable.subscribeOn(Schedulers.io()), shopProductObservable.subscribeOn(Schedulers.io()),
                new Func2<List<ShopProductLimitedFeaturedViewModel>, PagingList<ShopProductHomeViewModel>, PagingList<ShopProductBaseViewModel>>() {
                    @Override
                    public PagingList<ShopProductBaseViewModel> call(List<ShopProductLimitedFeaturedViewModel> shopProductFeatured, PagingList<ShopProductHomeViewModel> shopProductList) {
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

    public static RequestParams createRequestParam(String shopId, boolean goldMerchantStore, boolean officialStore, int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putBoolean(GOLD_MERCHANT_STORE, goldMerchantStore);
        requestParams.putBoolean(OFFICIAL_STORE, officialStore);
        requestParams.putInt(PAGE, page);
        return requestParams;
    }
}
