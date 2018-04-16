package com.tokopedia.shop.product.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    private final GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final DeleteShopProductUseCase deleteShopProductUseCase;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductListPresenter(GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase,
                                    AddToWishListUseCase addToWishListUseCase,
                                    RemoveFromWishListUseCase removeFromWishListUseCase,
                                    DeleteShopProductUseCase deleteShopProductUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    GetShopEtalaseUseCase getShopEtalaseUseCase,
                                    UserSession userSession) {
        this.getShopProductListWithAttributeUseCase = getShopProductListWithAttributeUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
        this.removeFromWishListUseCase = removeFromWishListUseCase;
        this.deleteShopProductUseCase = deleteShopProductUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    @NonNull
    private static ShopProductRequestModel getShopProductRequestModel(
            String shopId, String keyword, String etalaseId, int wholesale, int page, int orderBy) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setPage(page);
        if (etalaseId != null)
            shopProductRequestModel.setEtalaseId(etalaseId);

        if (keyword != null)
            shopProductRequestModel.setKeyword(keyword);

        if (wholesale > 0)
            shopProductRequestModel.setWholesale(wholesale);

        if (orderBy > 0)
            shopProductRequestModel.setOrderBy(orderBy);

        return shopProductRequestModel;
    }

    public void getShopPageList(final String shopId, final String keyword, final String etalaseId, final int wholesale, final int page, final int orderBy) {
        final ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(shopId, keyword, etalaseId, wholesale, page, orderBy);
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopName(shopInfo);
                shopProductRequestModel.setShopClosed((int) shopInfo.getInfo().getShopStatus() == ShopStatusDef.CLOSED);
                shopProductRequestModel.setOfficialStore(TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()));
                getShopProductWithEtalase(shopProductRequestModel);
            }
        });
    }

    private void getShopProductWithEtalase(final ShopProductRequestModel shopProductRequestModel) {
        if (TextUtils.isEmpty(shopProductRequestModel.getEtalaseId())) {
            getView().onSuccessGetEtalase("","");
            getShopProductWithWishList(shopProductRequestModel);
            return;
        }
        ShopEtalaseRequestModel shopEtalaseRequestModel = new ShopEtalaseRequestModel();
        shopEtalaseRequestModel.setShopId(shopProductRequestModel.getShopId());
        shopEtalaseRequestModel.setUserId(userSession.getUserId());
        shopEtalaseRequestModel.setDeviceId(userSession.getDeviceId());
        getShopEtalaseUseCase.execute(GetShopEtalaseUseCase.createParams(shopEtalaseRequestModel), new Subscriber<PagingListOther<EtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(PagingListOther<EtalaseModel> etalaseModelList) {
                List<EtalaseModel> etalaseModelListTemp = new ArrayList<>();
                etalaseModelListTemp.addAll(etalaseModelList.getListOther());
                etalaseModelListTemp.addAll(etalaseModelList.getList());
                String etalaseName = "";
                for (EtalaseModel etalaseModel : etalaseModelListTemp) {
                    if (shopProductRequestModel.getEtalaseId().equalsIgnoreCase(etalaseModel.getEtalaseId())) {
                        etalaseName = etalaseModel.getEtalaseName();
                    }
                }
                // If etalase Id not found, then reset etalaseId
                if (TextUtils.isEmpty(etalaseName)) {
                    shopProductRequestModel.setEtalaseId("");
                }
                getView().onSuccessGetEtalase(shopProductRequestModel.getEtalaseId(), etalaseName);
                getShopProductWithWishList(shopProductRequestModel);
            }
        });
    }

    private void getShopProductWithWishList(ShopProductRequestModel shopProductRequestModel) {
        getShopProductListWithAttributeUseCase.execute(GetShopProductListWithAttributeUseCase.createRequestParam(shopProductRequestModel), new Subscriber<PagingList<ShopProductViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(PagingList<ShopProductViewModel> shopProductList) {
                getView().renderList(shopProductList.getList(), PagingListUtils.checkNextPage(shopProductList));
            }
        });
    }

    public void addToWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorAddToWishList(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessAddToWishList(productId, aBoolean);
            }
        });
    }

    public void removeFromWishList(final String productId) {
        RequestParams requestParam = RemoveFromWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        removeFromWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorRemoveFromWishList(e);
                }
            }

            @Override
            public void onNext(Boolean value) {
                getView().onSuccessRemoveFromWishList(productId, value);
            }
        });
    }

    public void clearProductCache() {
        deleteShopProductUseCase.executeSync();
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductListWithAttributeUseCase.unsubscribe();
    }
}