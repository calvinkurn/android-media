package com.tokopedia.topads.keyword.view.presenter;

import android.text.TextUtils;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.group.domain.usecase.TopAdsSearchGroupAdUseCase;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.Keyword;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsGetKeywordListUseCase;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeywordActionBulkUseCase;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordListView;
import com.tokopedia.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordListPresenter extends TopAdsBaseListPresenter<TopAdsKeywordListView> {
    private static final String KEYWORD_DATE_FORMAT = "yyyy-MM-dd";

    private final TopAdsGetKeywordListUseCase topAdsGetKeywordListUseCase;
    private final TopAdsKeywordActionBulkUseCase topAdsKeywordActionBulkUseCase;
    private final TopAdsSearchGroupAdUseCase topAdsSearchGroupAdUseCase;

    private final BehaviorSubject<String> searchGroupName = BehaviorSubject.create();
    private final Subscription subscriptionSearchGroupName;

    @Inject
    public TopAdsKeywordListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                      TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                      TopAdsGetKeywordListUseCase topAdsGetKeywordListUseCase,
                                      TopAdsKeywordActionBulkUseCase topAdsKeywordActionBulkUseCase,
                                      TopAdsSearchGroupAdUseCase topAdsSearchGroupAdUseCase,
                                      UserSession userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetKeywordListUseCase = topAdsGetKeywordListUseCase;
        this.topAdsKeywordActionBulkUseCase = topAdsKeywordActionBulkUseCase;
        this.topAdsSearchGroupAdUseCase = topAdsSearchGroupAdUseCase;

        subscriptionSearchGroupName = searchGroupName
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceSearchGroupName());
    }

    private Subscriber<String> getSubscriberDebounceSearchGroupName() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String keyword) {
                topAdsSearchGroupAdUseCase.execute(TopAdsSearchGroupAdUseCase
                                .createRequestParams(keyword, userSession.getShopId())
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

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetKeywordListUseCase.unsubscribe();
        topAdsKeywordActionBulkUseCase.unsubscribe();
        subscriptionSearchGroupName.unsubscribe();
        topAdsSearchGroupAdUseCase.unsubscribe();
    }

    public void searchKeyword(Date startDate, Date endDate, String keyword,
                              int filterStatus, int groupId, final int page, String sortId,
                              boolean isPositive) {

        BaseKeywordParam baseKeywordParam = new BaseKeywordParam();
        if (startDate != null){
            baseKeywordParam.startDate = startDate.getTime();
            baseKeywordParam.startDateDesc = formatDate(startDate.getTime());
        }

        if (endDate != null){
            baseKeywordParam.endDate = endDate.getTime();
            baseKeywordParam.endDateDesc = formatDate(endDate.getTime());
        }

        baseKeywordParam.groupId = groupId;
        baseKeywordParam.sortingParam = sortId;
        baseKeywordParam.keywordStatus = filterStatus;
        baseKeywordParam.isPositive = isPositive;
        if (keyword != null)
            baseKeywordParam.keywordTag = keyword;
        baseKeywordParam.page = page;

        topAdsGetKeywordListUseCase.execute(TopAdsGetKeywordListUseCase.createRequestParams(baseKeywordParam, userSession.getShopId()),
                new Subscriber<KeywodDashboardViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().showListError(e);
                        }
                    }

                    @Override
                    public void onNext(KeywodDashboardViewModel keywodDashboardViewModel) {
                        if (isViewAttached()){
                            boolean hasNextData =keywodDashboardViewModel.getPage().getPerPage()*page < keywodDashboardViewModel.getPage().getTotal();
                            getView().onSearchLoaded(keywodDashboardViewModel.getData(), hasNextData);
                        }
                    }
                });
    }

    private String formatDate(long date) {
        return DateFormatUtils.getFormattedDate(date, KEYWORD_DATE_FORMAT);
    }

    public void deleteKeywords(List<String> ids) {
        DataRequest<DataBulkKeyword> dataRequest = generateActionRequest(generateKeywordBulk(ids, null, null),
                TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(dataRequest),
                getSubscriberBulkAction());
    }

    public void setKeywordActive(List<String> ids) {
        DataRequest<DataBulkKeyword> dataRequest = generateActionRequest(generateKeywordBulk(ids, null, null),
                TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(dataRequest),
                getSubscriberBulkAction());
    }

    public void setKeywordInactive(List<String> ids) {
        DataRequest<DataBulkKeyword> dataRequest = generateActionRequest(generateKeywordBulk(ids, null, null),
                TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(dataRequest),
                getSubscriberBulkAction());
    }

    public void copyKeyword(List<String> ids, String selectedGroupAdId) {
        DataRequest<DataBulkKeyword> dataRequest = generateActionRequest(generateKeywordBulk(ids, selectedGroupAdId, null),
                TopAdsNetworkConstant.ACTION_BULK_COPY_KEYWORD);
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(dataRequest),
                getSubscriberBulkAction());
    }

    public void updatePrice(List<String> ids, String price) {
        DataRequest<DataBulkKeyword> dataRequest = generateActionRequest(generateKeywordBulk(ids, null, price),
                TopAdsNetworkConstant.ACTION_BULK_CHANGE_BID_KEYWORD);
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(dataRequest),
                getSubscriberBulkAction());
    }

    private static double getPrice(String price) {
        String valueString = CurrencyFormatHelper
                .removeCurrencyPrefix(price);
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if (TextUtils.isEmpty(valueString)) {
            return 0;
        }
        return Double.parseDouble(valueString);
    }

    private Subscriber<PageDataResponse<DataBulkKeyword>> getSubscriberBulkAction() {
        return new Subscriber<PageDataResponse<DataBulkKeyword>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onBulkActionError(e);
                }
            }

            @Override
            public void onNext(PageDataResponse<DataBulkKeyword> AdBulkActions) {
                if (isViewAttached()){
                    getView().onBulkActionSuccess(AdBulkActions);
                }
            }
        };
    }

    private DataRequest<DataBulkKeyword> generateActionRequest(List<Keyword> keywords, String bulkAction) {
        DataBulkKeyword dataBulkKeyword = new DataBulkKeyword();
        dataBulkKeyword.setShopId(userSession.getShopId());
        dataBulkKeyword.setAction(bulkAction);
        dataBulkKeyword.setKeyword(keywords);
        return new DataRequest<>(dataBulkKeyword);
    }

    private List<Keyword> generateKeywordBulk(List<String> adIds, String groupId, String price){
        List<Keyword> keywords = new ArrayList<>();
        for (String adId : adIds){
            Keyword keyword = new Keyword();
            if (!TextUtils.isEmpty(groupId)) {
                keyword.setGroupId(groupId);
            }
            if (!TextUtils.isEmpty(price)) {
                keyword.setPriceBid(getPrice(price));
            }
            keyword.setKeywordId(adId);
            keywords.add(keyword);
        }
        return keywords;
    }

    public void searchGroupName(String keyword) {
        searchGroupName.onNext(keyword);
    }
}
