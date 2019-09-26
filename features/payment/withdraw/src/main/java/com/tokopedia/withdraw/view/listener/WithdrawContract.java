package com.tokopedia.withdraw.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.domain.model.premiumAccount.CheckEligible;
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal;

import java.util.List;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawContract {

    public interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetWithdrawForm(List<BankAccount> bankAccount);

        void showError(String throwable);

        String getStringResource(int id);

        void showErrorWithdrawal(String stringResource);

        void resetView();

        void showConfirmPassword();

        void goToAddBank();

        void goToSettingBank();

        void itemSelected();

        String loadRawString(int resId);

        void showConfirmationDialog(ValidatePopUpWithdrawal validatePopUpWithdrawal);

        void showCheckProgramData(CheckEligible checkEligible);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void getWithdrawForm();

        void getValidatePopUp(long bankId);

        void getPremiumAccountData();

        void doWithdraw(String totalBalance, String totalWithdrawal, BankAccount selectedBank);

        void refreshBankList();
    }
}
