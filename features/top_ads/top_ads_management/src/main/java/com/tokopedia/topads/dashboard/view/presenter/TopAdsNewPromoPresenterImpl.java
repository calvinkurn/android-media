package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;
import com.tokopedia.topads.dashboard.view.listener.TopAdsNewPromoFragmentListener;

import java.util.List;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsNewPromoPresenterImpl implements TopAdsNewPromoPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private TopAdsNewPromoFragmentListener listener;
    private Context context;

    public TopAdsNewPromoPresenterImpl(Context context, TopAdsNewPromoFragmentListener listener) {
        this.context = context;
        this.listener = listener;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public void populateGroupNameList() {
        dashboardTopadsInteractor.getCreditList(new ListenerInteractor<List<DataCredit>>() {
            @Override
            public void onSuccess(List<DataCredit> creditList) {
                listener.onGroupNameListLoaded(creditList);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onLoadGroupNameListError();
            }
        });
    }
}