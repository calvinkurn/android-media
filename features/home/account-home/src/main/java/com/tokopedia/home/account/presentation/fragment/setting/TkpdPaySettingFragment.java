package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
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
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            AccountHomeRouter router = (AccountHomeRouter) getActivity().getApplication();
            switch (settingId) {
                case SettingConstant.SETTING_BANK_ACCOUNT_ID:
                    if (userSession.isHasPassword()) {
                        router.goToManageBankAccount(getActivity());
                    } else {
                        showNoPasswordDialog();
                    }
                    break;
                case SettingConstant.SETTING_CREDIT_CARD_ID:
                    router.goToManageCreditCard(getActivity());
                    break;
                case SettingConstant.SETTING_TOKOCASH_ID:
                    router.goToTokoCash(getActivity());
                    break;
                case SettingConstant.SETTING_SALDO_ID:
                    router.goToSaldo(getActivity());
                    break;
                default:
                    break;
            }
        }
    }

    private void showNoPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.error_bank_no_password_title));
        builder.setMessage(getResources().getString(R.string.error_bank_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.error_no_password_yes), (DialogInterface dialogInterface, int i) -> {
                intentToAddPassword();
                dialogInterface.dismiss();
            });
        builder.setNegativeButton(getResources().getString(R.string.error_no_password_no), (DialogInterface dialogInterface, int i) -> {
                dialogInterface.dismiss();
            });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.colorSheetTitle));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    private void intentToAddPassword() {
        if (getActivity().getApplication() instanceof AccountHomeRouter){
            startActivityForResult(((AccountHomeRouter) getActivity().getApplication())
                    .getManagePasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);
        }
    }
}
