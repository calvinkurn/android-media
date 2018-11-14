package com.tokopedia.saldodetails.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.contract.MerchantSaldoPriorityContract;
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;

import javax.inject.Inject;

import rx.Subscriber;

public class MerchantSaldoPriorityPresenter extends BaseDaggerPresenter<MerchantSaldoPriorityContract.View>
        implements MerchantSaldoPriorityContract.Presenter {

    private SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;

    @Inject
    public MerchantSaldoPriorityPresenter(@NonNull SetMerchantSaldoStatus setMerchantSaldoStatus) {
        this.setMerchantSaldoStatusUseCase = setMerchantSaldoStatus;
    }


    @Override
    public void onDestroyView() {
        if (setMerchantSaldoStatusUseCase != null) {
            setMerchantSaldoStatusUseCase.unsubscribe();
        }
    }

    @Override
    public void updateSellerSaldoStatus(boolean isChecked) {
        getView().showProgressLoading();
        setMerchantSaldoStatusUseCase.execute(isChecked, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressLoading();
                getView().onSaldoStatusUpdateError(ErrorHandler.getErrorMessage(getView().getContext(), e));
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (graphqlResponse != null &&
                        graphqlResponse.getData(GqlSetMerchantSaldoStatus.class) != null) {

                    GqlSetMerchantSaldoStatus gqlSetMerchantSaldoStatus =
                            graphqlResponse.getData(GqlSetMerchantSaldoStatus.class);

                    if (gqlSetMerchantSaldoStatus.getMerchantSaldoStatus().isSuccess()) {
                        getView().onSaldoStatusUpdateSuccess(isChecked);
                    } else {
                        getView().onSaldoStatusUpdateError("");
                    }

                } else {
                    getView().onSaldoStatusUpdateError("");
                }
                getView().hideProgressLoading();
            }
        });
    }

}
