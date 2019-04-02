package com.tokopedia.pms.bankaccount.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.R;
import com.tokopedia.pms.bankaccount.data.model.DataEditTransfer;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeBankAccountPresenter extends BaseDaggerPresenter<ChangeBankAccountContract.View> implements ChangeBankAccountContract.Presenter {

    private GraphqlUseCase saveDetailAccountUseCase;

    public ChangeBankAccountPresenter(GraphqlUseCase saveDetailAccountUseCase) {
        this.saveDetailAccountUseCase = saveDetailAccountUseCase;
    }

    public void saveDetailAccount(Resources resources, String transactionId, String merchantCode, Integer destbank, String accountNo, String accountName, String notes) {
        getView().showLoadingDialog();
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.TRANSACTION_ID, transactionId);
        variables.put(Constant.MERCHANT_CODE, merchantCode);
        variables.put(Constant.ACC_NAME, accountName);
        variables.put(Constant.ACC_NO, accountNo);
        variables.put(Constant.NOTE, notes);
        variables.put(Constant.BANK_ID, destbank);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.edit_account_detail), DataEditTransfer.class, variables, false);
        saveDetailAccountUseCase.clearRequest();
        saveDetailAccountUseCase.setRequest(graphqlRequest);
        saveDetailAccountUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().hideLoadingDialog();
                    getView().onErrorEditDetailAccount(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                getView().hideLoadingDialog();
                DataEditTransfer editTransfer = objects.getData(DataEditTransfer.class);
                getView().onResultEditDetailAccount(editTransfer.getEditTransfer().isSuccess(), editTransfer.getEditTransfer().getMessage());
            }
        });
    }

    @Override
    public void detachView() {
        saveDetailAccountUseCase.unsubscribe();
        super.detachView();
    }
}
