package com.tokopedia.shop.page.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.etalase.domain.interactor.DeleteShopEtalaseUseCase;
import com.tokopedia.shop.note.domain.interactor.DeleteShopNoteUseCase;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;
import com.tokopedia.shop.page.view.listener.ShopPageView;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/13/18.
 */

@Deprecated
public class ShopPagePresenter extends BaseDaggerPresenter<ShopPageView> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetShopInfoByDomainUseCase getShopInfoByDomainUseCase;
    private final GetReputationSpeedUseCase getReputationSpeedUseCase;
    private final ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase;
    private final DeleteShopInfoUseCase deleteShopInfoUseCase;
    private final DeleteShopProductUseCase deleteShopProductUseCase;
    private final DeleteShopEtalaseUseCase deleteShopEtalaseUseCase;
    private final DeleteShopNoteUseCase deleteShopNoteUseCase;
    private final UserSession userSession;

    @Inject
    public ShopPagePresenter(GetShopInfoUseCase getShopInfoUseCase,
                             GetShopInfoByDomainUseCase getShopInfoByDomainUseCase,
                             GetReputationSpeedUseCase getReputationSpeedUseCase,
                             ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase,
                             DeleteShopProductUseCase deleteShopProductUseCase,
                             DeleteShopInfoUseCase deleteShopInfoUseCase,
                             DeleteShopEtalaseUseCase deleteShopEtalaseUseCase,
                             DeleteShopNoteUseCase deleteShopNoteUseCase,
                             UserSession userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getShopInfoByDomainUseCase = getShopInfoByDomainUseCase;
        this.getReputationSpeedUseCase = getReputationSpeedUseCase;
        this.toggleFavouriteShopAndDeleteCacheUseCase = toggleFavouriteShopAndDeleteCacheUseCase;
        this.deleteShopInfoUseCase = deleteShopInfoUseCase;
        this.deleteShopProductUseCase = deleteShopProductUseCase;
        this.deleteShopEtalaseUseCase = deleteShopEtalaseUseCase;
        this.deleteShopNoteUseCase = deleteShopNoteUseCase;
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
        getShopReputationSpeed(shopId);
    }

    public void getShopInfoByDomain(String shopDomain) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), new Subscriber<ShopInfo>() {
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
                getShopReputationSpeed(shopInfo.getInfo().getShopId());
            }
        });
    }

    public void getShopReputationSpeed(String shopId) {
        getReputationSpeedUseCase.execute(GetReputationSpeedUseCase.createRequestParam(shopId), new Subscriber<ReputationSpeed>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetReputation(e);
                }
            }

            @Override
            public void onNext(ReputationSpeed reputationSpeed) {
                getView().onSuccessGetReputation(reputationSpeed);
            }
        });
    }

    public void toggleFavouriteShop(String shopId) {
        if (!userSession.isLoggedIn()) {
            if (isViewAttached()) {
                getView().onErrorToggleFavourite(new UserNotLoginException());
            }
            return;
        }
        toggleFavouriteShopAndDeleteCacheUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopId), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorToggleFavourite(e);
                }
            }

            @Override
            public void onNext(Boolean success) {
                getView().onSuccessToggleFavourite(success);
            }
        });
    }

    public void clearCache() {
        deleteShopInfoUseCase.executeSync();
        deleteShopProductUseCase.executeSync();
        deleteShopEtalaseUseCase.executeSync();
        deleteShopNoteUseCase.executeSync();
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
        getShopInfoByDomainUseCase.unsubscribe();
        getReputationSpeedUseCase.unsubscribe();
        toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe();
        deleteShopInfoUseCase.unsubscribe();
        deleteShopProductUseCase.unsubscribe();
        deleteShopEtalaseUseCase.unsubscribe();
        deleteShopNoteUseCase.unsubscribe();
    }
}
