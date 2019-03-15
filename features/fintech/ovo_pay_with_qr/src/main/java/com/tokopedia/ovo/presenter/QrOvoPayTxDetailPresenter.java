package com.tokopedia.ovo.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.ThanksData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

import static com.tokopedia.ovo.view.PaymentQRSummaryFragment.SUCCESS_STATUS;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSFER_ID;

public class QrOvoPayTxDetailPresenter extends BaseDaggerPresenter<QrOvoPayTxDetailContract.View> implements QrOvoPayTxDetailContract.Presenter {
    @Override
    public void requestForThankYouPage(Context context, int transferId) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(TRANSFER_ID, transferId);
        GraphqlUseCase useCase = new GraphqlUseCase();
        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.qr_thanks),
                ThanksData.class, variables);
        useCase.addRequest(request);
        useCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().setError(e.getMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ThanksData goalQRThanks = graphqlResponse.getData(ThanksData.class);
                if (goalQRThanks != null && goalQRThanks.getGoalQRThanks() != null) {
                    if (!TextUtils.isEmpty(goalQRThanks.getGoalQRThanks().getStatus())) {
                        if (goalQRThanks.getGoalQRThanks().getStatus().equalsIgnoreCase(SUCCESS_STATUS))
                            getView().setSuccessThankYouData(goalQRThanks.getGoalQRThanks());
                        else {
                            getView().setFailThankYouData(goalQRThanks.getGoalQRThanks());
                        }
                    } else {
                        getView().setError(getView().getErrorMessage());
                    }
                } else {
                    getView().setError(getView().getErrorMessage());
                }
            }
        });
    }
}
