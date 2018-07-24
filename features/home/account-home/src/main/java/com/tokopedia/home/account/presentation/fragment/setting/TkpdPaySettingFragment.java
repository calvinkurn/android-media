package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.manage.people.bank.activity.ManagePeopleBankActivity;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class TkpdPaySettingFragment extends BaseGeneralSettingFragment{
    private static final int REQUEST_CHANGE_PASSWORD = 1234;

    public static Fragment createInstance() {
        return new TkpdPaySettingFragment();
    }

    private static final String TAG = TkpdPaySettingFragment.class.getSimpleName();

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TOKOCASH_ID,
                getString(R.string.title_tokocash_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SALDO_ID,
                getString(R.string.title_saldo_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_BANK_ACCOUNT_ID,
                getString(R.string.title_bank_account_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_CREDIT_CARD_ID,
                getString(R.string.title_credit_card_setting)));

        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        switch (settingId){
            case SettingConstant.SETTING_BANK_ACCOUNT_ID:
                if (userSession.isHasPassword()) {
                    Intent intent = new Intent(getActivity(), ManagePeopleBankActivity.class);
                    startActivity(intent);
                } else {
                    showNoPasswordDialog();
                }
                break;
            case SettingConstant.SETTING_CREDIT_CARD_ID:
                if ((getActivity().getApplication() instanceof TransactionRouter)) {
                    ((TransactionRouter) getActivity().getApplication())
                            .goToUserPaymentList(getActivity());
                }
                break;
            default: break;
        }
    }

    private void showNoPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.error_bank_no_password_title));
        builder.setMessage(getResources().getString(R.string.error_bank_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.error_no_password_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intentToAddPassword();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.error_no_password_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    private void intentToAddPassword() {
        startActivityForResult(
                ((TkpdCoreRouter)getActivity().getApplicationContext())
                        .getAddPasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);
    }
}
