package com.tokopedia.sellerapp.dashboard.presenter;

import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.common.ticker.usecase.GetTickerUseCase;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus;
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.shop.setting.constant.ShopCloseAction;
import com.tokopedia.seller.shop.setting.domain.interactor.UpdateShopScheduleUseCase;
import com.tokopedia.sellerapp.dashboard.presenter.listener.NotificationListener;
import com.tokopedia.sellerapp.dashboard.usecase.GetShopInfoWithScoreUseCase;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;
import com.tokopedia.user_identification_common.usecase.GetApprovalStatusUseCase;

import javax.inject.Inject;

import kotlin.Triple;
import rx.Subscriber;

/**
 * Created by hendry on 9/8/2017.
 */

public class SellerDashboardPresenter extends BaseDaggerPresenter<SellerDashboardView> {
    private final GetShopInfoWithScoreUseCase getShopInfoWithScoreUseCase;
    private final GetTickerUseCase getTickerUseCase;
    private final NewNotificationUseCase newNotificationUseCase;
    private final CacheApiClearAllUseCase cacheApiClearAllUseCase;
    private final UpdateShopScheduleUseCase updateShopScheduleUseCase;
    private final GetProductListSellingUseCase getProductListSellingUseCase;
    private final GetApprovalStatusUseCase getVerificationStatusUseCase;
    private final UserSessionInterface userSession;

    @Inject
    public SellerDashboardPresenter(GetShopInfoWithScoreUseCase getShopInfoWithScoreUseCase,
                                    GetTickerUseCase getTickerUseCase,
                                    NewNotificationUseCase newNotificationUseCase,
                                    CacheApiClearAllUseCase cacheApiClearAllUseCase,
                                    UpdateShopScheduleUseCase updateShopScheduleUseCase,
                                    GetProductListSellingUseCase getProductListSellingUseCase,
                                    GetApprovalStatusUseCase getVerificationStatusUseCase,
                                    UserSessionInterface userSession) {
        this.getShopInfoWithScoreUseCase = getShopInfoWithScoreUseCase;
        this.getTickerUseCase = getTickerUseCase;
        this.newNotificationUseCase = newNotificationUseCase;
        this.cacheApiClearAllUseCase = cacheApiClearAllUseCase;
        this.updateShopScheduleUseCase = updateShopScheduleUseCase;
        this.getProductListSellingUseCase = getProductListSellingUseCase;
        this.getVerificationStatusUseCase = getVerificationStatusUseCase;
        this.userSession = userSession;
    }

    public void getShopInfoWithScore() {
        getShopInfoWithScoreUseCase.execute(
                GetShopInfoWithScoreUseCase.createRequestParams(userSession.getShopId()), getShopInfoAndScoreSubscriber());
    }

    private Subscriber<Triple<ShopModel, GoldGetPmOsStatus, ShopScoreResult>> getShopInfoAndScoreSubscriber() {
        return new Subscriber<Triple<ShopModel, GoldGetPmOsStatus, ShopScoreResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorShopInfoAndScore(e);
                }
            }

            @Override
            public void onNext(Triple<ShopModel, GoldGetPmOsStatus, ShopScoreResult> resultTriple) {
                getView().onSuccessGetShopInfoAndScore(
                        resultTriple.getFirst(),
                        resultTriple.getSecond(),
                        resultTriple.getThird());
            }
        };
    }

    public void refreshShopInfo() {
        cacheApiClearAllUseCase.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                getShopInfoWithScore();
                getNotification();
            }
        });
    }

    public void getTicker() {
        getTickerUseCase.execute(RequestParams.EMPTY, getTickerSubscriber());
    }

    public void getVerificationStatus() {
        getVerificationStatusUseCase.execute(GetApprovalStatusUseCase.getRequestParam(),
                new GetApprovalStatusSubscriber(getView().getApprovalStatusListener()));
    }

    public void getNotification() {
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(true), getNotificationSubscriber());
    }

    private Subscriber<NotificationModel> getNotificationSubscriber() {
        return new NotificationSubscriber(new NotificationListener() {
            @Override
            public void onErrorGetNotificationDrawer(String errorMessage) {
                getView().onErrorGetNotifiction(errorMessage);
            }

            @Override
            public void onGetNotificationDrawer(DrawerNotification drawerNotification) {
                getView().onSuccessGetNotification(drawerNotification);
            }
        });
    }

    private Subscriber<Ticker.Tickers[]> getTickerSubscriber() {
        return new Subscriber<Ticker.Tickers[]>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetTickers(e);
            }

            @Override
            public void onNext(Ticker.Tickers[] tickers) {
                getView().onSuccessGetTickers(tickers);
            }
        };
    }

    public void openShop() {
        getView().showLoading();
        updateShopScheduleUseCase.execute(UpdateShopScheduleUseCase.cerateRequestParams("", "", "", ShopCloseAction.OPEN_SHOP), getSubscriberOpenShop());
    }

    private Subscriber<Boolean> getSubscriberOpenShop() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().onErrorOpenShop();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().hideLoading();
                if (isSuccess) {
                    getView().onSuccessOpenShop();
                } else {
                    getView().onErrorOpenShop();
                }
            }
        };
    }

    public void getProductList() {
        getProductListSellingUseCase.execute(GetProductListSellingUseCase.createRequestParamsManageProduct(0,
                "", CatalogProductOption.WITH_AND_WITHOUT, ConditionProductOption.ALL_CONDITION, "", 0,
                PictureStatusProductOption.WITH_AND_WITHOUT, SortProductOption.POSITION), getSubscriberGetListProduct());
    }

    private Subscriber<ProductListSellerModel> getSubscriberGetListProduct() {
        return new Subscriber<ProductListSellerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ProductListSellerModel productListSellerModel) {

            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoWithScoreUseCase.unsubscribe();
        getTickerUseCase.unsubscribe();
        updateShopScheduleUseCase.unsubscribe();
        cacheApiClearAllUseCase.unsubscribe();
        newNotificationUseCase.unsubscribe();
        getVerificationStatusUseCase.unsubscribe();
    }
}
