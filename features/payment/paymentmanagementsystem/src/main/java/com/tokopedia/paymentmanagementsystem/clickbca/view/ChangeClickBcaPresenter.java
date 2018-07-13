package com.tokopedia.paymentmanagementsystem.clickbca.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.clickbca.data.model.DataEditKlikBca;
import com.tokopedia.paymentmanagementsystem.common.Constant;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeClickBcaPresenter extends BaseDaggerPresenter<ChangeClickBcaContract.View> implements ChangeClickBcaContract.Presenter {

    private GraphqlUseCase changeClickBcaUseCase;

    public ChangeClickBcaPresenter(GraphqlUseCase changeClickBcaUseCase) {
        this.changeClickBcaUseCase = changeClickBcaUseCase;
    }


    @Override
    public void changeClickBcaUserId(Resources resources, String transactionId, String merchantCode, String newClickBcaUserId) {
        getView().showLoadingDialog();
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionId);
        variables.put(Constant.MERCHANT_CODE, merchantCode);
        variables.put(Constant.NEW_KLIKBCA_USER_ID, newClickBcaUserId);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.change_click_bca), DataEditKlikBca.class, variables);
        changeClickBcaUseCase.setRequest(graphqlRequest);
        changeClickBcaUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().hideLoadingDialog();
                    getView().onErrorChangeClickBcaUserID(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                getView().hideLoadingDialog();
                DataEditKlikBca editKlikbca = objects.getData(DataEditKlikBca.class);
                getView().onResultChangeClickBcaUserId(editKlikbca.getEditKlikbca().isSuccess(), editKlikbca.getEditKlikbca().getMessage());
            }
        });
    }
}
