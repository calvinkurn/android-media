package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zulfikarrahman on 5/23/17.
 */

public class TopAdsKeywordNewChooseGroupPresenterImpl extends TopAdsKeywordNewChooseGroupPresenter<TopAdsKeywordNewChooseGroupView> {

    public static final int TIMEOUT = 300;
    private final TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase;
    private final Subscription subscriptionSearchGroupName;
    private QueryListener listenerSearchGroupName;

    public TopAdsKeywordNewChooseGroupPresenterImpl(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase) {
        this.topAdsSearchGroupAdsNameUseCase = topAdsSearchGroupAdsNameUseCase;
        subscriptionSearchGroupName = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                listenerSearchGroupName = new QueryListener() {
                    @Override
                    public void getQueryString(String string) {
                        subscriber.onNext(string);
                    }
                };
            }
        }).debounce(TIMEOUT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceSearchGroupName());
    }

    @Override
    public void searchGroupName(String keyword) {
        if(listenerSearchGroupName != null){
            listenerSearchGroupName.getQueryString(keyword);
        }
    }


    public Subscriber<String> getSubscriberDebounceSearchGroupName() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                // remove checking cause param generator already handle it.
                topAdsSearchGroupAdsNameUseCase.unsubscribe();
                topAdsSearchGroupAdsNameUseCase.execute(TopAdsSearchGroupAdsNameUseCase.createRequestParams(s)
                        , getSubscriberSearchGroupName());
            }
        };
    }

    private Subscriber<List<GroupAd>> getSubscriberSearchGroupName() {
        return new Subscriber<List<GroupAd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onGetGroupAdListError(e);
            }

            @Override
            public void onNext(List<GroupAd> groupAds) {
                getView().onGetGroupAdList(groupAds);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSearchGroupAdsNameUseCase.unsubscribe();
        subscriptionSearchGroupName.unsubscribe();
    }

    private abstract class QueryListener {
        public abstract void getQueryString(String string);
    }
}
