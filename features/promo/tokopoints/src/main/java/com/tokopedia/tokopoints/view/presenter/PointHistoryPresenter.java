package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.PointHistoryContract;
import com.tokopedia.tokopoints.view.contract.SendGiftContract;
import com.tokopedia.tokopoints.view.model.PointHistoryBase;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class PointHistoryPresenter extends BaseDaggerPresenter<PointHistoryContract.View>
        implements PointHistoryContract.Presenter {
    private GraphqlUseCase mGetPointHistory;

    @Inject
    public PointHistoryPresenter(GraphqlUseCase graphqlUseCase) {
        this.mGetPointHistory = graphqlUseCase;
    }


    @Override
    public void destroyView() {
        if (mGetPointHistory != null) {
            mGetPointHistory.unsubscribe();
        }
    }

    @Override
    public void getPointsDetail() {
        getView().showLoading();
        Map<String, Object> variables = new HashMap<>();

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_current_points),
                TokoPointDetailEntity.class,
                variables, false);
        mGetPointHistory.clearRequest();
        mGetPointHistory.addRequest(request);
        mGetPointHistory.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
                getView().hideLoading();
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().hideLoading();
                TokoPointDetailEntity data = response.getData(TokoPointDetailEntity.class);
                getView().onSuccess(data.getTokoPoints().getStatus().getPoints());
            }
        });
    }
}
