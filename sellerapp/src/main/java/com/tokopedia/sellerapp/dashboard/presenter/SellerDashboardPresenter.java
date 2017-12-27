package com.tokopedia.sellerapp.dashboard.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.cache.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.common.ticker.usecase.GetTickerUseCase;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.shop.setting.constant.ShopCloseAction;
import com.tokopedia.seller.shop.setting.domain.interactor.UpdateShopScheduleUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.seller.shopscore.view.mapper.ShopScoreMapper;
import com.tokopedia.seller.shopscore.view.model.ShopScoreViewModel;
import com.tokopedia.sellerapp.dashboard.model.ShopModelWithScore;
import com.tokopedia.sellerapp.dashboard.presenter.listener.NotificationListener;
import com.tokopedia.sellerapp.dashboard.usecase.GetShopInfoWithScoreUseCase;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;

import javax.inject.Inject;

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

    @Inject
    public SellerDashboardPresenter(GetShopInfoWithScoreUseCase getShopInfoWithScoreUseCase,
                                    GetTickerUseCase getTickerUseCase,
                                    NewNotificationUseCase newNotificationUseCase,
                                    CacheApiClearAllUseCase cacheApiClearAllUseCase,
                                    UpdateShopScheduleUseCase updateShopScheduleUseCase) {
        this.getShopInfoWithScoreUseCase = getShopInfoWithScoreUseCase;
        this.getTickerUseCase = getTickerUseCase;
        this.newNotificationUseCase = newNotificationUseCase;
        this.cacheApiClearAllUseCase = cacheApiClearAllUseCase;
        this.updateShopScheduleUseCase = updateShopScheduleUseCase;
    }

    public void getShopInfoWithScore(){
        getShopInfoWithScoreUseCase.execute(
                RequestParams.EMPTY, getShopInfoAndScoreSubscriber());
    }

    private Subscriber<ShopModelWithScore> getShopInfoAndScoreSubscriber() {
        return new Subscriber<ShopModelWithScore>() {
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
            public void onNext(ShopModelWithScore ShopInfoWithScoreModel) {
                ShopScoreMainDomainModel shopScoreMainDomainModel =
                        ShopInfoWithScoreModel.getShopScoreMainDomainModel();
                ShopScoreViewModel shopScoreViewModel = ShopScoreMapper.map(shopScoreMainDomainModel);

                ShopModel shopModel = ShopInfoWithScoreModel.getShopModel();
                getView().onSuccessGetShopInfoAndScore(
                        shopModel,
                        shopScoreViewModel);
            }
        };
    }

    public void refreshShopInfo(){
        cacheApiClearAllUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
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

    public void getTicker(){
        getTickerUseCase.execute(RequestParams.EMPTY, getTickerSubscriber());
    }

    public void getNotification(){
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(true),getNotificationSubscriber());
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
                if(isSuccess){
                    getView().onSuccessOpenShop();
                }else{
                    getView().onErrorOpenShop();
                }
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
    }
}
