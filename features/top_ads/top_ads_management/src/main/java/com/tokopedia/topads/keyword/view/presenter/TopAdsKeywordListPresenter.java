package com.tokopedia.topads.keyword.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsGetKeywordListUseCase;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordListView;
import com.tokopedia.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordListPresenter extends TopAdsBaseListPresenter<TopAdsKeywordListView> {
    private final TopAdsGetKeywordListUseCase topAdsGetKeywordListUseCase;
    private static final String KEYWORD_DATE_FORMAT = "yyyy-MM-dd";

    @Inject
    public TopAdsKeywordListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                      TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                      TopAdsGetKeywordListUseCase topAdsGetKeywordListUseCase,
                                      UserSession userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetKeywordListUseCase = topAdsGetKeywordListUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetKeywordListUseCase.unsubscribe();
    }

    public void searchKeyword(Date startDate, Date endDate, String keyword,
                              int filterStatus, int groupId, final int page, String sortId,
                              boolean isPositive) {

        BaseKeywordParam baseKeywordParam = new BaseKeywordParam();
        baseKeywordParam.startDate = startDate.getTime();
        baseKeywordParam.endDate = endDate.getTime();

        baseKeywordParam.startDateDesc = formatDate(startDate.getTime());
        baseKeywordParam.endDateDesc = formatDate(endDate.getTime());

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
}
