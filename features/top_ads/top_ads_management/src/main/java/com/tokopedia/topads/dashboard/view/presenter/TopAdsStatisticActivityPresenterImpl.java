package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.request.StatisticRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticActivityViewListener;

import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticActivityPresenterImpl implements TopAdsStatisticActivityPresenter {
    private final TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private final TopAdsProductAdInteractor topAdsProductAdInteractor;
    StatisticRequest statisticRequest = new StatisticRequest();

    public TopAdsStatisticActivityPresenterImpl(TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener,
                                                TopAdsProductAdInteractor topAdsProductAdInteractor, Context context) {
        this.topAdsStatisticActivityViewListener = topAdsStatisticActivityViewListener;
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
    }

    @Override
    public void getStatisticFromNet(Date startDate, Date endDate, int typeRequest, String shopId) {
        topAdsStatisticActivityViewListener.showLoading();
        statisticRequest.setStartDate(startDate);
        statisticRequest.setEndDate(endDate);
        statisticRequest.setShopId(shopId);
        statisticRequest.setType(typeRequest);
        topAdsProductAdInteractor.getStatistic(statisticRequest, new ListenerInteractor<List<Cell>>() {
            @Override
            public void onSuccess(List<Cell> cells) {
                if (cells != null) {
                    topAdsStatisticActivityViewListener.updateDataCell(cells);
                } else {
                    topAdsStatisticActivityViewListener.onError(new NullPointerException());
                }
                topAdsStatisticActivityViewListener.dismissLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsStatisticActivityViewListener.dismissLoading();
                topAdsStatisticActivityViewListener.onError(throwable);
            }
        });
    }
}
