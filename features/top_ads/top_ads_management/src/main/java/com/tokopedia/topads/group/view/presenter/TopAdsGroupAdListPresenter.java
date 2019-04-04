package com.tokopedia.topads.group.view.presenter;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdAction;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.domain.usecase.TopAdsGetGroupAdUseCase;
import com.tokopedia.topads.group.domain.usecase.TopAdsToggleStatusUseCase;
import com.tokopedia.topads.group.view.listener.TopAdsGroupAdListView;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdListPresenter extends TopAdsBaseListPresenter<TopAdsGroupAdListView> {
    private final TopAdsGetGroupAdUseCase topAdsGetGroupAdUseCase;
    private final TopAdsToggleStatusUseCase topAdsToggleStatusUseCase;

    @Inject
    public TopAdsGroupAdListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                      TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                      TopAdsGetGroupAdUseCase topAdsGetGroupAdUseCase,
                                      TopAdsToggleStatusUseCase topAdsToggleStatusUseCase,
                                      UserSessionInterface userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetGroupAdUseCase = topAdsGetGroupAdUseCase;
        this.topAdsToggleStatusUseCase = topAdsToggleStatusUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetGroupAdUseCase.unsubscribe();
        topAdsToggleStatusUseCase.unsubscribe();
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

    public void setGroupActive(List<String> ids) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ids, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());

    }

    public void setGroupInactive(List<String> ids) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ids, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());

    }

    public void deleteGroupAd(List<String> ids) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ids, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());
    }

    private DataRequest<GroupAdBulkAction> generateActionRequest(List<String> ids, String bulkAction) {
        GroupAdBulkAction dataRequestGroupAd = new GroupAdBulkAction();
        dataRequestGroupAd.setAction(bulkAction);
        dataRequestGroupAd.setShopId(getShopId());
        dataRequestGroupAd.setAdList(generateGroupsAd(ids));
        return new DataRequest<>(dataRequestGroupAd);
    }

    private List<GroupAdAction> generateGroupsAd(List<String> adIds){
        List<GroupAdAction> dataRequestGroupAdses = new ArrayList<>();
        for (String adId : adIds){
            GroupAdAction adAction = new GroupAdAction();
            adAction.setId(adId);
            dataRequestGroupAdses.add(adAction);
        }
        return dataRequestGroupAdses;
    }

    private Subscriber<GroupAdBulkAction> getBulkSubscriber(){
        return new Subscriber<GroupAdBulkAction>() {
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
            public void onNext(GroupAdBulkAction groupAdBulkAction) {
                if (isViewAttached()){
                    getView().onBulkActionSuccess(groupAdBulkAction);
                }
            }
        };
    }
}
