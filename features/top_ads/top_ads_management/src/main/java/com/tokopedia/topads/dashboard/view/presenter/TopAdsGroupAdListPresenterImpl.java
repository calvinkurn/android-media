package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.topads.common.util.TopAdsSourceTaggingUseCaseUtil;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListPresenterImpl extends TopAdsAdListPresenterImpl<GroupAd> implements TopAdsGroupAdListPresenter {

    protected final TopAdsGroupAdInteractor groupAdInteractor;
    private final TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;

    public TopAdsGroupAdListPresenterImpl(Context context, BaseListViewListener baseListViewListener) {
        super(context, baseListViewListener);
        this.groupAdInteractor = new TopAdsGroupAdInteractorImpl(context);
        this.topAdsAddSourceTaggingUseCase = TopAdsSourceTaggingUseCaseUtil.getTopAdsAddSourceTaggingUseCase(context);
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, final int page, @SortTopAdsOption String sortId) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setPage(page);
        searchAdRequest.setSort(sortId);
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<GroupAd>> pageDataResponse) {
                baseListViewListener.onSearchLoaded(pageDataResponse.getData(), pageDataResponse.getPage().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                baseListViewListener.onLoadSearchError(throwable);
            }
        });
    }

    @Override
    public void saveSourceTagging(@TopAdsSourceOption String source){
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source), new Subscriber<Void>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }


    @Override
    public void unSubscribe() {
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }

        if (topAdsAddSourceTaggingUseCase != null){
            topAdsAddSourceTaggingUseCase.unsubscribe();
        }
    }
}
