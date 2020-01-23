package com.tokopedia.shop.product.view.presenter;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<ShopProductDedicatedListView> {

    private final GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase;
    private final GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;

    private final UserSessionInterface userSession;

    private final GQLGetShopInfoUseCase gqlGetShopInfoUseCase;
    private final DeleteShopProductUseCase deleteShopProductUseCase;
    private WishListActionListener wishListActionListener;

    @Inject
    public ShopProductListPresenter(GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase,
                                    AddWishListUseCase addWishListUseCase,
                                    RemoveWishListUseCase removeWishListUseCase,
                                    DeleteShopProductUseCase deleteShopProductUseCase,
                                    GQLGetShopInfoUseCase gqlGetShopInfoUseCase,
                                    GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase,
                                    UserSessionInterface userSession) {
        this.productListWithAttributeNewUseCase = productListWithAttributeNewUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.deleteShopProductUseCase = deleteShopProductUseCase;
        this.gqlGetShopInfoUseCase = gqlGetShopInfoUseCase;
        this.getShopEtalaseByShopUseCase = getShopEtalaseByShopUseCase;
        this.userSession = userSession;
    }

    public void attachView(ShopProductDedicatedListView view, WishListActionListener wishlistListener) {
        this.wishListActionListener = wishlistListener;
        super.attachView(view);
    }

    public boolean isLogin() {
        return userSession.isLoggedIn();
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public void getShopInfo(final String shopId) {
        ArrayList<Integer> shopIds = new ArrayList<>();
        try {
            shopIds.add(Integer.parseInt(userSession.getShopId()));
        }
        catch (NumberFormatException exception) {
            Timber.d("Failed to convert shop ID to integer");
        }
        gqlGetShopInfoUseCase.setParams(GQLGetShopInfoUseCase.createParams(shopIds, null , GQLGetShopInfoUseCase.getDefaultShopFields()));
        gqlGetShopInfoUseCase.execute(
                shopInfo -> {
                    if (isViewAttached()) {
                        getView().onSuccessGetShopInfo(shopInfo);
                    }
                    return Unit.INSTANCE;
                },
                throwable -> {
                    if (isViewAttached()) {
                        getView().onErrorGetShopInfo(throwable);
                    }
                    return Unit.INSTANCE;
                }
        );
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
                                final int wholesale, final int page, final int orderBy, boolean isUseAce) {
        ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(
                shopInfo.getInfo().getShopId(),
                !shopInfo.getInfo().isOpen(),
                shopInfo.getInfo().isShopOfficial(),
                isUseAce,
                ShopPageConstant.DEFAULT_PER_PAGE,
                keyword, etalaseId, wholesale, page, orderBy);
        getShopProductWithWishList(shopProductRequestModel);
    }

    public void getShopEtalase(String shopId, boolean isOwner) {
        getShopEtalaseByShopUseCase.unsubscribe();
        getShopEtalaseByShopUseCase.execute(GetShopEtalaseByShopUseCase.createRequestParams(shopId, true, false, isOwner),
                new Subscriber<ArrayList<ShopEtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetEtalaseList(e);
                }
            }

            @Override
            public void onNext(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                List<ShopEtalaseViewModel> shopEtalaseViewModelList = ShopProductMapper.map(shopEtalaseModels);
                getView().onSuccessGetEtalaseList(shopEtalaseViewModelList);
            }
        });
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
        addWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);

    }

    public void removeFromWishList(final String productId) {
        removeWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);

    }

    public void clearCache() {
        deleteShopProductUseCase.executeSync();
        getShopEtalaseByShopUseCase.clearCache();
    }

    @Override
    public void detachView() {
        super.detachView();
        addWishListUseCase.unsubscribe();
        removeWishListUseCase.unsubscribe();
        productListWithAttributeNewUseCase.unsubscribe();
        getShopEtalaseByShopUseCase.unsubscribe();
        gqlGetShopInfoUseCase.cancelJobs();
        deleteShopProductUseCase.unsubscribe();
    }
}