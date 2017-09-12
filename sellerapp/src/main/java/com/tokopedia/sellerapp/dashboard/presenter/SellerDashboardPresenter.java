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
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.sellerapp.dashboard.model.ShopModelWithScore;
import com.tokopedia.sellerapp.dashboard.presenter.listener.NotificationListener;
import com.tokopedia.sellerapp.dashboard.usecase.GetShopInfoWithScoreUseCase;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.sellerapp.home.view.mapper.ShopScoreMapper;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 9/8/2017.
 */

public class SellerDashboardPresenter extends BaseDaggerPresenter<SellerDashboardView> {
    private GetShopInfoWithScoreUseCase getShopInfoWithScoreUseCase;
    private GetTickerUseCase getTickerUseCase;
    private NotificationUseCase notificationUseCase;

    @Inject
    public SellerDashboardPresenter(GetShopInfoWithScoreUseCase getShopInfoWithScoreUseCase,
                                    GetTickerUseCase getTickerUseCase,
                                    NotificationUseCase notificationUseCase) {
        this.getShopInfoWithScoreUseCase = getShopInfoWithScoreUseCase;
        this.getTickerUseCase = getTickerUseCase;
        this.notificationUseCase = notificationUseCase;
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

                ReputationView.ReputationViewModel reputationViewModel = new ReputationView.ReputationViewModel();
                reputationViewModel.typeMedal = shopModel.stats.shopBadgeLevel.set;
                reputationViewModel.levelMedal = shopModel.stats.shopBadgeLevel.level;
                reputationViewModel.reputationPoints = shopModel.stats.shopReputationScore;
                reputationViewModel.stats = shopModel.stats;
                getView().onSuccessGetShopInfoAndScore(
                        shopModel.info,
                        shopModel.isOpen > 0,
                        shopModel.shopTxStats,
                        reputationViewModel,
                        shopScoreViewModel);
            }
        };
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

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoWithScoreUseCase.unsubscribe();
        getTickerUseCase.unsubscribe();
    }
}
