package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GetShopProductListWithAttributeUseCase extends BaseGetShopProductAttributeUseCase<PagingList<ShopProductViewModel>> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final GetShopProductListUseCase getShopProductListUseCase;
    private Subscription listSubscription;

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
        return getShopProductListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel))
                .flatMap(new Func1<PagingList<ShopProduct>, Observable<PagingList<ShopProductViewModel>>>() {
                    @Override
                    public Observable<PagingList<ShopProductViewModel>> call(final PagingList<ShopProduct> shopProductPagingList) {
                        return getShopProductViewModelList(shopProductRequestModel, shopProductPagingList);
                    }
                });
    }

    private Observable<PagingList<ShopProductViewModel>> getShopProductViewModelList(
            final ShopProductRequestModel shopProductRequestModel, final PagingList<ShopProduct> shopProductPagingList) {
        List<ShopProductViewModel> shopProductViewModelList = shopProductMapper.convertFromShopProduct(
                shopProductPagingList.getList(), shopProductRequestModel.getPage(), ShopPageConstant.DEFAULT_PER_PAGE);
        return super.getShopProductViewModelList(
                userSession.isLoggedIn(),
                isShopOwner(shopProductRequestModel.getShopId()),
                shopProductRequestModel.isOfficialStore(), shopProductViewModelList)
                .flatMap(new Func1<List<ShopProductViewModel>, Observable<PagingList<ShopProductViewModel>>>() {
                    @Override
                    public Observable<PagingList<ShopProductViewModel>> call(List<ShopProductViewModel> shopProductViewModelOldList) {
                        PagingList<ShopProductViewModel> pagingList = new PagingList<>();
                        pagingList.setTotalData(shopProductPagingList.getTotalData());
                        pagingList.setPaging(shopProductPagingList.getPaging());
                        pagingList.setList(shopProductViewModelOldList);
                        return Observable.just(pagingList);
                    }
                });
    }

    private Observable<List<List<ShopProductViewModel>>> createObservable(List<RequestParams> requestParams) {
        return Observable.from(requestParams)
                .concatMap(new Func1<RequestParams, Observable<? extends List<ShopProductViewModel>>>() {
                    @Override
                    public Observable<? extends List<ShopProductViewModel>> call(RequestParams requestParams) {
                        return createObservable(requestParams).map(new Func1<PagingList<ShopProductViewModel>, List<ShopProductViewModel>>() {
                            @Override
                            public List<ShopProductViewModel> call(PagingList<ShopProductViewModel> shopProductViewModelPagingList) {
                                return shopProductViewModelPagingList.getList();
                            }
                        });
                    }
                })
                .toList();
    }

    public void executeList(List<RequestParams> requestParamsList, Subscriber<List<List<ShopProductViewModel>>> subscriber) {
        this.listSubscription = createObservable(requestParamsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        getShopProductListUseCase.unsubscribe();
        if (listSubscription != null && !listSubscription.isUnsubscribed()) {
            listSubscription.unsubscribe();
        }
    }
}
