package com.tokopedia.topads.group.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.group.domain.usecase.TopAdsGetGroupAdUseCase;
import com.tokopedia.topads.group.view.listener.TopAdsGroupAdListView;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdListPresenter extends TopAdsBaseListPresenter<TopAdsGroupAdListView> {
    private final TopAdsGetGroupAdUseCase topAdsGetGroupAdUseCase;

    @Inject
    public TopAdsGroupAdListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                      TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                      TopAdsGetGroupAdUseCase topAdsGetGroupAdUseCase,
                                      UserSession userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetGroupAdUseCase = topAdsGetGroupAdUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetGroupAdUseCase.unsubscribe();
    }

    public void searchAd(Date startDate, Date endDate, String keyword, int status, final int page, @SortTopAdsOption String sort) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setPage(page);
        searchAdRequest.setSort(sort);
        topAdsGetGroupAdUseCase.execute(TopAdsGetGroupAdUseCase.createRequestParams(searchAdRequest),
                new Subscriber<PageDataResponse<List<GroupAd>>>() {
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
                    public void onNext(PageDataResponse<List<GroupAd>> listPageDataResponse) {
                        if (isViewAttached()){
                            boolean hasNextData = listPageDataResponse.getPage().getPerPage()*page < listPageDataResponse.getPage().getTotal();
                            getView().onSearchLoaded(listPageDataResponse.getData(), hasNextData);
                        }
                    }
                });
    }
}
