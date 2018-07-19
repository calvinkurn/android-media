package com.tokopedia.shop.product.view.presenter.newpresenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeNewUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
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

public class ShopProductListPresenterNew extends BaseDaggerPresenter<ShopProductDedicatedListView> {

    private final GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;

    private final UserSession userSession;

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final DeleteShopProductUseCase deleteShopProductUseCase;
    private final static int USE_ACE = 1;

    @Inject
    public ShopProductListPresenterNew(GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase,
                                       AddToWishListUseCase addToWishListUseCase,
                                       RemoveFromWishListUseCase removeFromWishListUseCase,
                                       DeleteShopProductUseCase deleteShopProductUseCase,
                                       GetShopInfoUseCase getShopInfoUseCase,
                                       GetShopEtalaseUseCase getShopEtalaseUseCase,
                                       UserSession userSession) {
        this.productListWithAttributeNewUseCase = productListWithAttributeNewUseCase;
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
            String shopId,
            boolean isShopClosed, boolean isOfficialStore, boolean useAce, int itemPerPage,
            String keyword, String etalaseId, int wholesale, int page, int orderBy) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(
                shopId,
                isShopClosed,
                isOfficialStore,
                page,
                useAce,
                itemPerPage
        );
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

    public void getShopPageList(final ShopInfo shopInfo, final String keyword, final String etalaseId,
                                final int wholesale, final int page, final int orderBy) {
        ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(
                shopInfo.getInfo().getShopId(),
                !shopInfo.getInfo().isOpen(),
                shopInfo.getInfo().isShopOfficial(),
                true,
                ShopPageConstant.DEFAULT_PER_PAGE,
                keyword, etalaseId, wholesale, page, orderBy);
        getShopProductWithEtalase(shopProductRequestModel);
    }

    public void getShopInfo(final String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    private void getShopProductWithEtalase(final ShopProductRequestModel shopProductRequestModel) {
        if (TextUtils.isEmpty(shopProductRequestModel.getEtalaseId())) {
            getView().onSuccessGetEtalaseName("", "");
            getShopProductWithWishList(shopProductRequestModel);
            return;
        }
        ShopEtalaseRequestModel shopEtalaseRequestModel = new ShopEtalaseRequestModel(shopProductRequestModel.getShopId(),
                userSession.getUserId(), userSession.getDeviceId());
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
                if (etalaseModelList.getList() != null && !etalaseModelList.getList().isEmpty()) {
                    etalaseModelListTemp.addAll(etalaseModelList.getList());
                }
                String etalaseName = "";
                for (EtalaseModel etalaseModel : etalaseModelListTemp) {
                    if (shopProductRequestModel.getEtalaseId().equalsIgnoreCase(etalaseModel.getEtalaseId())) {
                        etalaseName = etalaseModel.getEtalaseName();
                        shopProductRequestModel.setUseAce((etalaseModel.getUseAce() == USE_ACE));
                    }
                }
                // If etalase Id not found, then reset etalaseId
                if (TextUtils.isEmpty(etalaseName)) {
                    for (EtalaseModel etalaseModel : etalaseModelListTemp) {
                        if (shopProductRequestModel.getEtalaseId().replaceAll("[\\W_]", "")
                                .equalsIgnoreCase(etalaseModel.getEtalaseName().replaceAll("[\\W_]", ""))) {
                            shopProductRequestModel.setEtalaseId(etalaseModel.getEtalaseId());
                            etalaseName = etalaseModel.getEtalaseName();
                            shopProductRequestModel.setUseAce((etalaseModel.getUseAce() == USE_ACE));
                        }
                    }
                    if (TextUtils.isEmpty(etalaseName)) {
                        shopProductRequestModel.setEtalaseId("");
                    }
                }
                getView().onSuccessGetEtalaseName(shopProductRequestModel.getEtalaseId(), etalaseName);
                getShopProductWithWishList(shopProductRequestModel);
            }
        });
    }

    private void getShopProductWithWishList(ShopProductRequestModel shopProductRequestModel) {
        productListWithAttributeNewUseCase.execute(
                GetShopProductListWithAttributeNewUseCase.createRequestParam(shopProductRequestModel),
                new Subscriber<PagingList<ShopProductViewModel>>() {
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
                getView().renderProductList(shopProductList.getList(),
                        PagingListUtils.checkNextPage(shopProductList));
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
        addToWishListUseCase.unsubscribe();
        removeFromWishListUseCase.unsubscribe();
        productListWithAttributeNewUseCase.unsubscribe();
        getShopEtalaseUseCase.unsubscribe();
    }
}