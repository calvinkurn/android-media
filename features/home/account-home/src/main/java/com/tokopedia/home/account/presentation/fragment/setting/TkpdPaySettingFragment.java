package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.di.component.DaggerTkpdPaySettingComponent;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.navigation_common.model.VccUserStatus;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.WalletPref;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT_BANK;
import static com.tokopedia.home.account.AccountConstants.Analytics.BALANCE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CREDIT_CARD;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOKOCASH;

public class TkpdPaySettingFragment extends BaseGeneralSettingFragment {

    private static final int REQUEST_CHANGE_PASSWORD = 1234;
    @Inject
    WalletPref walletPref;

    private AccountAnalytics accountAnalytics;
    private VccUserStatus vccUserStatus;

    public static Fragment createInstance() {
        return new TkpdPaySettingFragment();
    }

    private static final String TAG = TkpdPaySettingFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DaggerTkpdPaySettingComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build().inject(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        WalletModel walletModel = walletPref.retrieveWallet();
        if (walletModel != null) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TOKOCASH_ID,
                    walletModel.getText()));
        }

        vccUserStatus = walletPref.retrieveVccUserStatus();
        if(vccUserStatus != null && vccUserStatus.getTitle() != null && !vccUserStatus.getTitle().isEmpty()) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_OVO_PAY_LATER_ID,
                    vccUserStatus.getTitle()));
        }

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
                    accountAnalytics.eventClickPaymentSetting(ACCOUNT_BANK);
                    if (userSession.hasPassword()) {
                        startActivity(router.getSettingBankIntent(getActivity()));
                    } else {
                        showNoPasswordDialog();
                    }
                    break;
                case SettingConstant.SETTING_CREDIT_CARD_ID:
                    accountAnalytics.eventClickPaymentSetting(CREDIT_CARD);
                    router.goToManageCreditCard(getActivity());
                    break;
                case SettingConstant.SETTING_TOKOCASH_ID:
                    accountAnalytics.eventClickPaymentSetting(TOKOCASH);
                    WalletModel walletModel = walletPref.retrieveWallet();
                    if (walletModel != null) {
                        if (walletModel.isLinked()) {
                            RouteManager.route(getActivity(), walletModel.getApplink());
                        } else {
                            RouteManager.route(getActivity(), walletModel.getAction().getApplink());
                        }
                    }
                    break;
                case SettingConstant.SETTING_SALDO_ID:
                    accountAnalytics.eventClickPaymentSetting(BALANCE);
                    router.goToSaldo(getActivity());
                    break;
                case SettingConstant.SETTING_OVO_PAY_LATER_ID:
                    accountAnalytics.eventClickOVOPayLater(AccountConstants.Analytics.OVO_PAY_LATER_CATEGORY,
                            AccountConstants.Analytics.OVO_PAY_LATER_CLICK,
                            String.format(AccountConstants.Analytics.OVO_PAY_LATER_LABEL, vccUserStatus.getStatus()));
                    String url = vccUserStatus.getRedirectionUrl();
                    if (URLUtil.isValidUrl(url)) {
                        RouteManager.route(getActivity(), String.format("%s?url=%s",
                                ApplinkConst.WEBVIEW, url));
                    } else {
                        RouteManager.route(getActivity(), String.format("%s?url=%s",
                                ApplinkConst.WEBVIEW,
                                AccountHomeUrl.WEB_DOMAIN + AccountHomeUrl.TOKOCARD_URL));
                    }

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
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            startActivityForResult(((AccountHomeRouter) getActivity().getApplication())
                    .getAddPasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);
        }
    }
}
