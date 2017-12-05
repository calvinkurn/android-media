package com.tokopedia.tkpd.beranda.presentation.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.errorhandle.WidgetRuntimeException;
import com.tokopedia.digital.widget.model.status.Status;
import com.tokopedia.tkpd.beranda.data.mapper.HomeDataMapper;
import com.tokopedia.tkpd.beranda.domain.interactor.GetBrandsOfficialStoreUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeBannerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeCategoryUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTickerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTopPicksUseCase;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractor;

import java.util.List;

import javax.inject.Inject;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    @Inject
    GetHomeBannerUseCase getHomeBannerUseCase;
    @Inject
    GetTickerUseCase getTickerUseCase;
    @Inject
    GetBrandsOfficialStoreUseCase getBrandsOfficialStoreUseCase;
    @Inject
    GetTopPicksUseCase getTopPicksUseCase;
    @Inject
    GetHomeCategoryUseCase getHomeCategoryUseCase;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    private final Context context;

    public HomePresenter(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getHomeData() {
        subscription = Observable.zip(getHomeBannerUseCase.getExecuteObservableAsync(getHomeBannerUseCase.getRequestParam()),
                getTickerUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getBrandsOfficialStoreUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getTopPicksUseCase.getExecuteObservableAsync(getTopPicksUseCase.getRequestParam()),
                getHomeCategoryUseCase.getExecuteObservableAsync(RequestParams.EMPTY), new HomeDataMapper(context))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "On Subscribe");
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        Log.w(TAG, "On Terminated");
                        if(isViewAttached()){
                            getView().hideLoading();
                        }
                    }
                }).subscribe(new HomeDataSubscriber());
        compositeSubscription.add(subscription);
    }

    private class HomeDataSubscriber extends Subscriber<List<Visitable>> {

        @Override
        public void onStart() {
            if (isViewAttached()) {
                getView().removeNetworkError();
                getView().showLoading();
            }
        }

        @Override
        public void onCompleted() {
            if (isViewAttached()) {
                getView().hideLoading();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showNetworkError();
            }
        }

        @Override
        public void onNext(List<Visitable> visitables) {
            Log.d(TAG, "onNext items " + visitables.size());
            if (isViewAttached()) {
                getView().setItems(visitables);
            }
        }
    }

}
