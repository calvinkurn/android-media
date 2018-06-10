package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditFragmentListener;

import java.util.List;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsAddCreditPresenterImpl implements TopAdsAddCreditPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private TopAdsAddCreditFragmentListener listener;
    private Context context;

    public TopAdsAddCreditPresenterImpl(Context context, TopAdsAddCreditFragmentListener listener) {
        this.context = context;
        this.listener = listener;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public void populateCreditList() {
        dashboardTopadsInteractor.getCreditList(new ListenerInteractor<List<DataCredit>>() {
            @Override
            public void onSuccess(List<DataCredit> creditList) {
                listener.onCreditListLoaded(creditList);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onLoadCreditListError();
            }
        });
    }

    @Override
    public void unSubscribe() {
        if (dashboardTopadsInteractor != null) {
            dashboardTopadsInteractor.unSubscribe();
        }
    }
}