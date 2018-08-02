package com.tokopedia.shop.product.view.presenter;

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
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
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

public class ShopProductListPresenter extends BaseDaggerPresenter<ShopProductDedicatedListView> {

    private final GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;

    private final UserSession userSession;

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final DeleteShopProductUseCase deleteShopProductUseCase;

    @Inject
    public ShopProductListPresenter(GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase,
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

    public void getShopInfo(final String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId),
                new Subscriber<ShopInfo>() {
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
        shopProductRequestModel.setEtalaseId(etalaseId);

        if (!TextUtils.isEmpty(keyword)) {
            shopProductRequestModel.setKeyword(keyword);
        }

        if (wholesale > 0) {
            shopProductRequestModel.setWholesale(wholesale);
        }

        if (orderBy > 0) {
            shopProductRequestModel.setOrderBy(orderBy);
        }

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

    private void getShopProductWithEtalase(final ShopProductRequestModel shopProductRequestModel) {
        String selectedEtalaseId = shopProductRequestModel.getEtalaseId();
        // has etalase id
        // if there is no etalase list yet,
        //    1. From deeplink: etalaseID might contains as name instead of ID, so we need to loop the list to get the ID
        //    2. get the etalase list, merge and show the etalaselist to the view
        // if there is already etalase list, continue to get the product list
        ShopEtalaseRequestModel shopEtalaseRequestModel = new ShopEtalaseRequestModel(shopProductRequestModel.getShopId(),
                userSession.getUserId(), userSession.getDeviceId());

        List<ShopEtalaseViewModel> shopEtalaseViewModelList = getView().getShopEtalaseViewModelList();
        if (shopEtalaseViewModelList.size() > 0) {
            boolean isUseAce = isUseAce(shopEtalaseViewModelList, selectedEtalaseId);
            shopProductRequestModel.setUseAce(isUseAce);
            getShopProductWithWishList(shopProductRequestModel);
        } else {
            getShopEtalaseUseCase.unsubscribe();
            getShopEtalaseUseCase.execute(GetShopEtalaseUseCase.createParams(shopEtalaseRequestModel), new Subscriber<PagingListOther<EtalaseModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (isViewAttached()) {
                        getView().onErrorGetEtalaseList(e);
                        getShopProductWithWishList(shopProductRequestModel);
                    }
                }

                @Override
                public void onNext(PagingListOther<EtalaseModel> etalaseModelList) {
                    List<EtalaseModel> etalaseModelListTemp = new ArrayList<>();
                    etalaseModelListTemp.addAll(etalaseModelList.getListOther());
                    if (etalaseModelList.getList() != null && !etalaseModelList.getList().isEmpty()) {
                        etalaseModelListTemp.addAll(etalaseModelList.getList());
                    }

                    // id might come from deeplink
                    // So, if the selected name is empty, we know that the id might come from deeplink
                    // If the selectedName is not empty, we don't need to do find name anymore.
                    String selectedEtalaseName = getView().getSelectedEtalaseName();
                    if (TextUtils.isEmpty(selectedEtalaseName)) { // name is empty, id might be from deeplink, or from other activity
                        for (EtalaseModel etalaseModel : etalaseModelListTemp) {
                            if (selectedEtalaseId.equalsIgnoreCase(etalaseModel.getEtalaseId())) {
                                selectedEtalaseName = etalaseModel.getEtalaseName();
                                shopProductRequestModel.setUseAce((etalaseModel.isUseAce()));
                                break;
                            }
                        }
                        // If etalase Id not found, then we check the selectedEtalaseId with name.
                        if (TextUtils.isEmpty(selectedEtalaseName)) {
                            String cleanedSelectedEtalaseId = cleanString(selectedEtalaseId);
                            for (EtalaseModel etalaseModel : etalaseModelListTemp) {
                                String cleanedEtalaseName = cleanString(etalaseModel.getEtalaseName());
                                if (cleanedSelectedEtalaseId.equalsIgnoreCase(cleanedEtalaseName)) {
                                    shopProductRequestModel.setEtalaseId(etalaseModel.getEtalaseId());
                                    selectedEtalaseName = etalaseModel.getEtalaseName();
                                    shopProductRequestModel.setUseAce((etalaseModel.isUseAce()));
                                    break;
                                }
                            }
                            if (TextUtils.isEmpty(selectedEtalaseName)) {
                                shopProductRequestModel.setEtalaseId("");
                            }
                        }
                    }

                    // name is empty means etalase is deleted, so no need to add to chip, and make it to all etalase.
                    if (TextUtils.isEmpty(selectedEtalaseName)) {
                        shopProductRequestModel.setEtalaseId("");
                    } else {
                        getView().addNewEtalaseToChip(selectedEtalaseId, selectedEtalaseName);
                    }

                    ArrayList<ShopEtalaseViewModel> etalaseViewModelList = getView().getSelectedEtalaseViewModelList();

                    List<ShopEtalaseViewModel> shopEtalaseViewModelList = ShopProductMapper.mergeEtalaseList(
                            etalaseModelList, etalaseViewModelList, ShopPageConstant.ETALASE_TO_SHOW);
                    getView().onSuccessGetEtalaseList(shopEtalaseViewModelList, shopProductRequestModel.getEtalaseId(), selectedEtalaseName);
                    getShopProductWithWishList(shopProductRequestModel);
                }
            });
        }
    }

    private boolean isUseAce(List<ShopEtalaseViewModel> etalaseViewModelList, String selectedEtalaseId) {
        if (etalaseViewModelList != null) {
            for (ShopEtalaseViewModel shopEtalaseViewModel : etalaseViewModelList) {
                if (shopEtalaseViewModel.getEtalaseId().equalsIgnoreCase(selectedEtalaseId)) {
                    return shopEtalaseViewModel.isUseAce();
                }
            }
        }
        return true;
    }

    private String cleanString(String text) {
        return text.replaceAll("[\\W_]", "");
    }

    private void getShopProductWithWishList(ShopProductRequestModel shopProductRequestModel) {
        productListWithAttributeNewUseCase.unsubscribe();
        productListWithAttributeNewUseCase.execute(
                GetShopProductListWithAttributeUseCase.createRequestParam(shopProductRequestModel),
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
        getShopInfoUseCase.unsubscribe();
        deleteShopProductUseCase.unsubscribe();
    }
}