package com.tokopedia.sellerapp.dashboard.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.common.ticker.usecase.GetTickerUseCase;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.home.view.ReputationView;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreMainDataUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.sellerapp.dashboard.presenter.listener.NotificationListener;
import com.tokopedia.sellerapp.dashboard.usecase.GetShopInfoUseCase;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.sellerapp.home.view.mapper.ShopScoreMapper;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by User on 9/8/2017.
 */

public class SellerDashboardPresenter extends BaseDaggerPresenter<SellerDashboardView> {
    private GetShopInfoUseCase getShopInfoUseCase;
    private GetShopScoreMainDataUseCase getShopScoreMainDataUseCase;
    private GetTickerUseCase getTickerUseCase;
    private NotificationUseCase notificationUseCase;

    @Inject
    public SellerDashboardPresenter(GetShopInfoUseCase getShopInfoUseCase,
                                    GetShopScoreMainDataUseCase getShopScoreMainDataUseCase,
                                    GetTickerUseCase getTickerUseCase,
                                    NotificationUseCase notificationUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getShopScoreMainDataUseCase = getShopScoreMainDataUseCase;
        this.getTickerUseCase = getTickerUseCase;
        this.notificationUseCase = notificationUseCase;
    }

    public void getShopInfo() {
        getShopInfoUseCase.execute(RequestParams.EMPTY, getShopInfoSubscriber());
    }

    public void getShopScoreMainData() {
        getShopScoreMainDataUseCase.execute(
                RequestParams.EMPTY,
                getShopScoreSubscriber()
        );
    }

    public void getTicker(){
        getTickerUseCase.execute(RequestParams.EMPTY, getTickerSubscriber());
    }

    public void getNotification(){
        notificationUseCase.execute(NotificationUseCase.getRequestParam(true),getNotificationSubscriber());
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

    private Subscriber<ShopScoreMainDomainModel> getShopScoreSubscriber() {
        return new Subscriber<ShopScoreMainDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorShopScore(e);
                }
            }

            @Override
            public void onNext(ShopScoreMainDomainModel shopScoreMainDomainModel) {
                ShopScoreViewModel shopScoreViewModel = ShopScoreMapper.map(shopScoreMainDomainModel);
                getView().renderShopScore(shopScoreViewModel);
            }
        };
    }

    private Subscriber<ShopModel> getShopInfoSubscriber() {
        return new Subscriber<ShopModel>() {
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
            public void onNext(ShopModel shopModel) {
                getView().onSuccessGetShopInfo(shopModel.info);
                getView().onSuccessGetShopOpenInfo(shopModel.isOpen > 0);
                getView().onSuccessGetShopTransaction(shopModel.shopTxStats);

                ReputationView.ReputationViewModel reputationViewModel = new ReputationView.ReputationViewModel();
                reputationViewModel.typeMedal = shopModel.stats.shopBadgeLevel.set;
                reputationViewModel.levelMedal = shopModel.stats.shopBadgeLevel.level;
                reputationViewModel.reputationPoints = shopModel.stats.shopReputationScore;
                reputationViewModel.stats = shopModel.stats;
                getView().onSuccessGetReputation(reputationViewModel);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
        getShopScoreMainDataUseCase.unsubscribe();
        getTickerUseCase.unsubscribe();
    }
}
