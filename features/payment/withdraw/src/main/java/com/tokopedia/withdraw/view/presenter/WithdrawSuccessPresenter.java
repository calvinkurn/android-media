package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.withdraw.domain.usecase.WithdrawalFormSubmitUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawSuccessPageContract;

public class WithdrawSuccessPresenter extends BaseDaggerPresenter<WithdrawSuccessPageContract.View> implements WithdrawSuccessPageContract.Presenter{

    private WithdrawalFormSubmitUseCase withdrawalFormSubmitUseCase;

    @Override
    public void executeSuccessUseCase(Object withdrawFormData) {
        //execute call for withdrawal submit
    }

    @Override
    public void detachView() {
        if (withdrawalFormSubmitUseCase != null) {
            withdrawalFormSubmitUseCase.unsubscribe();
        }
        super.detachView();
    }
}
