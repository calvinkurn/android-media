package com.tokopedia.reksadana.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.view.data.dashboard.DashBoardData;

import javax.inject.Inject;

import rx.Subscriber;

public class DashBoardPresenter extends BaseDaggerPresenter<DashBoardContract.View> implements DashBoardContract.Presenter{
    @Inject
    public DashBoardPresenter() {
    }

    @Override
    public void getData() {
        GraphqlUseCase dashboardUseCase = new GraphqlUseCase();
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.user_info_detail), DashBoardData.class);
        dashboardUseCase.addRequest(graphqlRequest);
        dashboardUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("sandeep",e.toString());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                DashBoardData data = graphqlResponse.getData(DashBoardData.class);
                Log.e("sandeep","data = "+data);
            }
        });
    }
}
