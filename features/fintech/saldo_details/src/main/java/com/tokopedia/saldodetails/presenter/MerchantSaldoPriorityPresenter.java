package com.tokopedia.saldodetails.presenter;

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

    @Inject
    SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;

    @Inject
    public MerchantSaldoPriorityPresenter() {
    }


    @Override
    public void onDestroyView() {
        if (setMerchantSaldoStatusUseCase != null) {
            setMerchantSaldoStatusUseCase.unsubscribe();
        }
    }

    @Override
    public void updateSellerSaldoStatus(boolean isChecked) {
        if (!isViewAttached()) {
            return;
        }
        getView().showProgressLoading();
        setMerchantSaldoStatusUseCase.execute(isChecked, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressLoading();
                    getView().onSaldoStatusUpdateError(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (!isViewAttached()) {
                    return;
                }
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
