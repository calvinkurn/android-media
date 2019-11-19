package com.tokopedia.topads.dashboard.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.view.listener.TopAdsManageGroupPromoView;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zulfikarrahman on 2/16/17.
 */
public class TopAdsManageGroupPromoPresenterImpl<T extends TopAdsManageGroupPromoView> extends BaseDaggerPresenter<T> implements TopAdsManageGroupPromoPresenter<T> {

    public static final int TIME_DELAY = 300;
    private final TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase;
    private final TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase;
    private final TopAdsMinimumBidUseCase topAdsMinimumBidUseCase;
    private final Subscription subscriptionCheckGroupExist;
    private final Subscription subscriptionSearchGroupName;
    private QueryListener listenerCheckGroupExist;
    private QueryListener listenerSearchGroupName;
    private UserSessionInterface sessionInterface;

    public TopAdsManageGroupPromoPresenterImpl(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase,
                                               TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase,
                                               TopAdsMinimumBidUseCase topAdsMinimumBidUseCase,
                                               UserSessionInterface sessionInterface) {
        this.topAdsSearchGroupAdsNameUseCase = topAdsSearchGroupAdsNameUseCase;
        this.topAdsCheckExistGroupUseCase = topAdsCheckExistGroupUseCase;
        this.topAdsMinimumBidUseCase = topAdsMinimumBidUseCase;
        this.sessionInterface = sessionInterface;

        subscriptionCheckGroupExist = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                listenerCheckGroupExist = new QueryListener() {
                    @Override
                    public void getQueryString(String string) {
                        subscriber.onNext(string);
                    }
                };
            }
        }).debounce(TIME_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceCheckGroupExist());

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
        }).debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceSearchGroupName());

    }

    @Override
    public void checkIsGroupExist(String keyword) {
        if (listenerCheckGroupExist != null) {
            listenerCheckGroupExist.getQueryString(keyword);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptionCheckGroupExist.unsubscribe();
        subscriptionSearchGroupName.unsubscribe();
        topAdsSearchGroupAdsNameUseCase.unsubscribe();
        topAdsCheckExistGroupUseCase.unsubscribe();
    }

    @Override
    public void searchGroupName(String keyword) {
        if (listenerSearchGroupName != null) {
            listenerSearchGroupName.getQueryString(keyword);
        }
    }

    @Override
    public void checkIsGroupExistOnSubmitNewGroup(String keyword) {
        if (keyword.trim().isEmpty()) {
            getView().showErrorShouldFillGroupName();
        } else if(isKeywordContainsTag(keyword)){
            getView().showErrorGroupNameNotValid();
        } else {
            getView().showLoading();
            topAdsCheckExistGroupUseCase.execute(TopAdsSearchGroupAdsNameUseCase.createRequestParams(keyword)
                    , getSubscriberCheckGroupExistOnSubmitNewGroup());
        }
    }

    protected boolean isKeywordContainsTag(String keyword) {
        return keyword.contains("<") || keyword.contains(">");
    }

    private Subscriber<List<GroupAd>> getSubscriberSearchGroupName() {
        return new Subscriber<List<GroupAd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(!isViewAttached()){
                    return;
                }
                getView().onGetGroupAdListError();
            }

            @Override
            public void onNext(List<GroupAd> groupAds) {
                getView().onGetGroupAdList(groupAds);
            }
        };
    }

    private Subscriber<Boolean> getSubscriberCheckGroupExist() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(!isViewAttached()){
                    return;
                }
                getView().dismissLoading();
                getView().onCheckGroupExistError();
            }

            @Override
            public void onNext(Boolean isExist) {
                getView().dismissLoading();
                if (isExist) {
                    getView().onGroupExist();
                } else {
                    getView().onGroupNotExist();
                }
            }
        };
    }

    @NonNull
    private Subscriber<String> getSubscriberDebounceCheckGroupExist() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String string) {
                if (!string.equals("")) {
                    if(isKeywordContainsTag(string)){
                        getView().showErrorGroupNameNotValid();
                    }else {
                        getView().hideErrorGroupNameNotValid();
                        topAdsCheckExistGroupUseCase.execute(TopAdsSearchGroupAdsNameUseCase.createRequestParams(string)
                                , getSubscriberCheckGroupExist());
                    }
                }
            }
        };
    }

    @NonNull
    private Subscriber<String> getSubscriberDebounceSearchGroupName() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String string) {
                topAdsSearchGroupAdsNameUseCase.execute(TopAdsSearchGroupAdsNameUseCase.createRequestParams(string)
                        , getSubscriberSearchGroupName());
            }
        };
    }

    public Subscriber<Boolean> getSubscriberCheckGroupExistOnSubmitNewGroup() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(!isViewAttached()){
                    return;
                }
                getView().dismissLoading();
                getView().onCheckGroupExistError();
            }

            @Override
            public void onNext(Boolean isExist) {
                getView().dismissLoading();
                if (isExist) {
                    getView().onGroupExist();
                } else {
                    getView().onGroupNotExistOnSubmitNewGroup();
                }
            }
        };
    }

    private abstract class QueryListener {
        public abstract void getQueryString(String string);
    }

    @Override
    public void getMinimumBid(String requestType, List<DataSuggestions> dataSuggestions, String source) {
        MinimumBidRequest request = new MinimumBidRequest();
        request.setSource(source);
        request.setShopId(Integer.parseInt(sessionInterface.getShopId()));
        request.setRequestType(requestType);
        request.setDataSuggestions(dataSuggestions);
        topAdsMinimumBidUseCase.execute(topAdsMinimumBidUseCase.getBidParams(request),
                new Subscriber<MinimumBidDomain.TopadsBidInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MinimumBidDomain.TopadsBidInfo topadsBidInfo) {

                    }
                });

    }
}
