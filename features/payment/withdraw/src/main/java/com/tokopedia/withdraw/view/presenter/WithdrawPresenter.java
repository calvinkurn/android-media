package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.domain.model.GqlGetBankDataResponse;
import com.tokopedia.withdraw.domain.usecase.GqlGetBankDataUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.domain.model.BankAccount;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPresenter extends BaseDaggerPresenter<WithdrawContract.View>
        implements WithdrawContract.Presenter {

    private GqlGetBankDataUseCase gqlGetBankDataUseCase;

    @Inject
    public WithdrawPresenter(GqlGetBankDataUseCase gqlGetBankDataUseCase) {
        this.gqlGetBankDataUseCase = gqlGetBankDataUseCase;
    }

    @Override
    public void attachView(WithdrawContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWithdrawForm() {
        getView().showLoading();

        gqlGetBankDataUseCase.setQuery(getView().loadRawString(R.raw.query_get_bank_data));

        gqlGetBankDataUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
                getView().hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                getView().hideLoading();
                getView().showError(throwable.getMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {

                GqlGetBankDataResponse gqlGetBankDataResponse = graphqlResponse.getData(GqlGetBankDataResponse.class);

                if (gqlGetBankDataResponse != null) {
                    getView().onSuccessGetWithdrawForm(gqlGetBankDataResponse.getBankAccount().getBankAccountList());
                }
                getView().hideLoading();

            }
        });

    }

    @Override
    public void doWithdraw(String totalBalance, String totalWithdrawal, BankAccount selectedBank) {
        getView().resetView();
        int balance = (int) StringUtils.convertToNumeric(totalBalance, false);
        int withdrawal = (int) StringUtils.convertToNumeric(totalWithdrawal, false);
        if (balance < withdrawal) {
            getView().showErrorWithdrawal(getView().getStringResource(R.string.error_withdraw_exceed_balance));
            return;
        }

        if (!(withdrawal > 0)) {
            getView().showErrorWithdrawal(getView().getStringResource(R.string.error_field_required));
            return;
        }

        if (selectedBank == null) {
            getView().showError(getView().getStringResource(R.string.has_no_bank));
            return;
        }

        getView().showConfirmPassword();
    }

    @Override
    public void refreshBankList() {
        getWithdrawForm();
    }

    @Override
    public void detachView() {
        if (gqlGetBankDataUseCase != null) {
            gqlGetBankDataUseCase.unsubscribe();
        }
        super.detachView();
    }

}
