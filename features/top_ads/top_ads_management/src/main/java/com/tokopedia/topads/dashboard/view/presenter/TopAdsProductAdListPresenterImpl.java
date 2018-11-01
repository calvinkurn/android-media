package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.common.util.TopAdsSourceTaggingUseCaseUtil;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsProductAdListPresenterImpl extends TopAdsAdListPresenterImpl<ProductAd> implements TopAdsProductAdListPresenter {

    protected final TopAdsProductAdInteractor productAdInteractor;
    private final TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;

    public TopAdsProductAdListPresenterImpl(Context context, BaseListViewListener topadsListViewListener) {
        super(context, topadsListViewListener);
        this.productAdInteractor = new TopAdsProductAdInteractorImpl(new TopAdsManagementService(new SessionHandler(context)),
                new TopAdsCacheDataSourceImpl(context));
        this.topAdsAddSourceTaggingUseCase = TopAdsSourceTaggingUseCaseUtil.getTopAdsAddSourceTaggingUseCase(context);
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, long groupId, int page, @SortTopAdsOption String sortId) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setGroup(groupId);
        searchAdRequest.setPage(page);
        searchAdRequest.setSort(sortId);
        productAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<ProductAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<ProductAd>> pageDataResponse) {
                baseListViewListener.onSearchLoaded(pageDataResponse.getData(), pageDataResponse.getPage().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                baseListViewListener.onLoadSearchError(throwable);
            }
        });
    }

    @Override
    public void saveSourceTagging(@TopAdsSourceOption String source) {
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                new Subscriber<Void>() {
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
        if (productAdInteractor != null) {
            productAdInteractor.unSubscribe();
        }
        if (topAdsAddSourceTaggingUseCase != null){
            topAdsAddSourceTaggingUseCase.unsubscribe();
        }
    }
}