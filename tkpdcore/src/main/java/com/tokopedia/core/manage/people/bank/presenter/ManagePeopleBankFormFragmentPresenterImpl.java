package com.tokopedia.core.manage.people.bank.presenter;

import android.os.Bundle;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFormFragment;
import com.tokopedia.core.manage.people.bank.listener.ManagePeopleBankFormFragmentView;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.util.AppUtils;

import java.util.List;

/**
 * Created by Nisie on 6/13/16.
 */
public class ManagePeopleBankFormFragmentPresenterImpl implements ManagePeopleBankFormFragmentPresenter,
        ManagePeopleBankConstant {

    ManagePeopleBankFormFragmentView viewListener;
    LocalCacheHandler handler;
    ManagePeopleBankFormFragment.DoActionListener listener;

    public ManagePeopleBankFormFragmentPresenterImpl(ManagePeopleBankFormFragmentView viewListener) {
        this.viewListener = viewListener;
        this.handler = new LocalCacheHandler(viewListener.getActivity(), SEND_OTP_SETTING_BANK_CACHE_KEY);
        this.listener = (ManagePeopleBankFormFragment.DoActionListener) viewListener.getActivity();
    }

    @Override
    public void onSaveClicked() {
        if (isValid()) {
            viewListener.showDialogLoading();
            viewListener.setActionsEnabled(false);
            ActSettingBankPass editPass = viewListener.getArguments().getParcelable(PARAM_EDIT_BANK_ACCOUNT);
            if (editPass != null) {
                Bundle param = new Bundle();
                ActSettingBankPass pass = getAddBankParam();
                pass.setAccountId(editPass.getAccountId());
                if (viewListener.getBankId().equals("")) {
                    pass.setBankId(editPass.getBankId());
                }
                param.putParcelable(PARAM_EDIT_BANK_ACCOUNT, pass);
                listener.editBankAccount(param);
            } else {
                Bundle param = new Bundle();
                param.putParcelable(PARAM_ADD_BANK_ACCOUNT, getAddBankParam());
                listener.addBankAccount(param);
            }
        }
    }

    private ActSettingBankPass getAddBankParam() {
        ActSettingBankPass pass = new ActSettingBankPass();
        pass.setAccountName(viewListener.getAccountName().getText().toString());
        pass.setAccountNumber(viewListener.getAccountNumber().getText().toString());
        pass.setBankBranch(viewListener.getBranchName().getText().toString());
        pass.setBankName(viewListener.getBankName().getText().toString());
        pass.setBankId(viewListener.getBankId());
        pass.setOtpCode(viewListener.getOTP().getText().toString());
        pass.setUserPassword(viewListener.getPassword().getText().toString());
        return pass;
    }

    private boolean isValid() {
        boolean isValid = true;
        viewListener.removeError();

        if (viewListener.getPassword().length() == 0) {
            viewListener.notifyError(viewListener.getPasswordWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            isValid = false;
        }

        if (viewListener.getOTP().length() == 0) {
            viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            isValid = false;
        } else if (viewListener.getOTP().length() > 6) {
            viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_max_otp));
            isValid = false;
        } else if (viewListener.getOTP().length() < 6) {
            viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_min_otp));
            isValid = false;
        }

        if (viewListener.getAccountName().length() == 0) {
            viewListener.notifyError(viewListener.getAccountNameWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            isValid = false;
        }
        if (viewListener.getAccountNumber().length() == 0) {
            viewListener.notifyError(viewListener.getAccountNumberWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            viewListener.getAccountNumber().requestFocus();
            isValid = false;
        } else if (viewListener.getAccountNumber().length() > 30) {
            viewListener.notifyError(viewListener.getAccountNumberWrapper(), viewListener.getActivity().getString(R.string.error_max_account_number));
            isValid = false;
        }

        if (viewListener.getBranchName().length() == 0) {
            viewListener.notifyError(viewListener.getBranchNameWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public List<Bank> getListBankFromDB(String query) {
        List<Bank> bankList;
        if (query.equals(""))
            bankList = new Select().from(Bank.class).queryList();
        else {
            bankList = new Select().from(Bank.class).where(Bank_Table.bank_name.like("%" + query + "%")).queryList();
        }
        return bankList;
    }

    @Override
    public void sendOTP() {
        if (handler.isExpired()) {
            AppUtils.sendOTP(viewListener.getActivity());
            viewListener.getOTP().setEnabled(true);
            handler.setExpire(EXPIRE_TIME);
        } else {
            Long diff = System.currentTimeMillis() / 1000 - handler.getLong(TIMESTAMP_KEY);
            int interval = handler.getInt(EXPIRED_TIME_KEY);
            int remainingTime = interval - diff.intValue();
            CommonUtils.UniversalToast(viewListener.getActivity(), "Silahkan coba " + remainingTime + " detik lagi");
        }
    }
}
