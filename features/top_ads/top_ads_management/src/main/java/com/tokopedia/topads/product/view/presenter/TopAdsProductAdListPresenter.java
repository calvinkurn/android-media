package com.tokopedia.topads.product.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.product.domain.usecase.TopAdsGetProductAdsUseCase;
import com.tokopedia.topads.product.view.listener.TopAdsProductAdListView;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdListPresenter extends TopAdsBaseListPresenter<TopAdsProductAdListView> {

    private final TopAdsGetProductAdsUseCase topAdsGetProductAdsUseCase;

    @Inject
    public TopAdsProductAdListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                        TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                        TopAdsGetProductAdsUseCase topAdsGetProductAdsUseCase,
                                        UserSession userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetProductAdsUseCase = topAdsGetProductAdsUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetProductAdsUseCase.unsubscribe();
    }

    public void searchAd(Date startDate, Date endDate, String keyword, int status, long groupId,
                         final int page, @SortTopAdsOption String sortId) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setGroup(groupId);
        searchAdRequest.setPage(page);
        searchAdRequest.setSort(sortId);
        topAdsGetProductAdsUseCase.execute(TopAdsGetProductAdsUseCase.createRequestParams(searchAdRequest),
                new Subscriber<PageDataResponse<List<ProductAd>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PageDataResponse<List<ProductAd>> listPageDataResponse) {
                if (isViewAttached()){
                    getView().onSearchLoaded(listPageDataResponse.getData(), page < listPageDataResponse.getPage().getTotal());
                }
            }
        });
    }
}
