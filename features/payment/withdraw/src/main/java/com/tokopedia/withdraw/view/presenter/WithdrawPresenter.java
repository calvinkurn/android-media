package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.domain.model.GqlGetBankDataResponse;
import com.tokopedia.withdraw.domain.model.premiumAccount.GqlCheckPremiumAccountResponse;
import com.tokopedia.withdraw.domain.model.validatePopUp.GqlGetValidatePopUpResponse;
import com.tokopedia.withdraw.domain.usecase.GqlCheckPremiumAccountUseCase;
import com.tokopedia.withdraw.domain.usecase.GqlGetBankDataUseCase;
import com.tokopedia.withdraw.domain.usecase.GqlGetValidatePopupUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.domain.model.BankAccount;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPresenter extends BaseDaggerPresenter<WithdrawContract.View>
        implements WithdrawContract.Presenter {

    private GqlGetBankDataUseCase gqlGetBankDataUseCase;
    private GqlGetValidatePopupUseCase gqlGetValidatePopupUseCase;
    private GqlCheckPremiumAccountUseCase getGqlCheckPremiumAccountUseCase;


    @Inject
    public WithdrawPresenter(GqlGetBankDataUseCase gqlGetBankDataUseCase,
                             GqlGetValidatePopupUseCase gqlGetValidatePopupUseCase,
                             GqlCheckPremiumAccountUseCase getGqlCheckPremiumAccountUseCase) {
        this.gqlGetBankDataUseCase = gqlGetBankDataUseCase;
        this.gqlGetValidatePopupUseCase = gqlGetValidatePopupUseCase;
        this.getGqlCheckPremiumAccountUseCase = getGqlCheckPremiumAccountUseCase;
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
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(throwable.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if(isViewAttached()) {
                    GqlGetBankDataResponse gqlGetBankDataResponse = graphqlResponse.getData(GqlGetBankDataResponse.class);
                    if (gqlGetBankDataResponse != null) {
                        getView().onSuccessGetWithdrawForm(gqlGetBankDataResponse.getBankAccount().getBankAccountList());
                    }
                    getView().hideLoading();
                }
            }
        });

    }

    @Override
    public void getValidatePopUp(long bankId) {
        getView().showLoading();
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("bankID",""+bankId);
        requestParams.put("language","ID");
        gqlGetValidatePopupUseCase.setQuery(getView().loadRawString(R.raw.query_popup));
        gqlGetValidatePopupUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
                getView().hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(throwable.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if(isViewAttached()) {
                    GqlGetValidatePopUpResponse response = graphqlResponse.getData(GqlGetValidatePopUpResponse.class);
                    if (response != null) {
                        getView().showConfirmationDialog(response.getValidatePopUpWithdrawal());
                    }
                    getView().hideLoading();
                }

            }
        }, requestParams);
    }

    @Override
    public void getPremiumAccountData() {
        getView().showLoading();
        getGqlCheckPremiumAccountUseCase.setQuery(getView().loadRawString(R.raw.query_check_premium_account));
        getGqlCheckPremiumAccountUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
                getView().hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(throwable.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if(isViewAttached()) {
                    GqlCheckPremiumAccountResponse responseData = graphqlResponse.getData(GqlCheckPremiumAccountResponse.class);
                    if (responseData != null) {
                        getView().showCheckProgramData(responseData.getCheckEligible());
                    }
                    getView().hideLoading();
                }

            }
        });
    }


    @Override
    public void doWithdraw(String totalBalance, String totalWithdrawal, BankAccount selectedBank) {
        getView().resetView();
        int balance = (int) StringUtils.convertToNumeric(totalBalance, false);
        int withdrawal = (int) StringUtils.convertToNumeric(totalWithdrawal, false);
        if (balance < withdrawal) {
            getView().showErrorWithdrawal(getView().getStringResource(R.string.saldo_exceeding_withdraw_balance));
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
        getValidatePopUp(selectedBank.getBankID());
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
        if(getGqlCheckPremiumAccountUseCase != null){
            getGqlCheckPremiumAccountUseCase.unsubscribe();
        }

        if(gqlGetValidatePopupUseCase != null){
            gqlGetValidatePopupUseCase.unsubscribe();
        }
        super.detachView();
    }

}
