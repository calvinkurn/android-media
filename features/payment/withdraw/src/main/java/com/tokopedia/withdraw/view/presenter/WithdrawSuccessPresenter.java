package com.tokopedia.withdraw.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.withdraw.domain.usecase.WithdrawalFormSubmitUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawSuccessPageContract;

import java.util.HashMap;

import rx.Subscriber;

public class WithdrawSuccessPresenter extends BaseDaggerPresenter<WithdrawSuccessPageContract.View> implements WithdrawSuccessPageContract.Presenter{

    private WithdrawalFormSubmitUseCase withdrawalFormSubmitUseCase;

    @Override
    public void executeSuccessUseCase(HashMap<String, Object> withdrawFormData, Context context) {
        //execute call for withdrawal submit
        withdrawalFormSubmitUseCase.execute(getSubscriber(), context, withdrawFormData);
    }

    @Override
    public void detachView() {
        if (withdrawalFormSubmitUseCase != null) {
            withdrawalFormSubmitUseCase.unsubscribe();
        }
        super.detachView();
    }

    private Subscriber<GraphqlResponse> getSubscriber(){
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {

            }
        };
    }
}
