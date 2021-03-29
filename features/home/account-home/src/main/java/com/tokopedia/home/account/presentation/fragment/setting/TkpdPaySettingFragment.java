package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.di.component.DaggerTkpdPaySettingComponent;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.user.session.UserSession;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.Analytics.BALANCE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CREDIT_CARD;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOKOCASH;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT;

public class TkpdPaySettingFragment extends BaseGeneralSettingFragment {

    private static final int REQUEST_CHANGE_PASSWORD = 1234;
    @Inject
    WalletPref walletPref;

    private AccountAnalytics accountAnalytics;
    private UserSession pvtUserSession;

    public static Fragment createInstance() {
        return new TkpdPaySettingFragment();
    }

    private static final String TAG = TkpdPaySettingFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
        pvtUserSession = new UserSession(getContext());
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
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TOKOCASH_ID, walletModel.getText()));
        }

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SALDO_ID,
                getString(R.string.title_saldo_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_CREDIT_CARD_ID,
                getString(R.string.title_credit_card_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_DEBIT_INSTANT,
                getString(R.string.title_debit_instant_setting)));

        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        switch (settingId) {
            case SettingConstant.SETTING_CREDIT_CARD_ID:
                accountAnalytics.eventClickPaymentSetting(CREDIT_CARD);
                RouteManager.route(getActivity(), ApplinkConstInternalPayment.PAYMENT_SETTING);
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
                RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
                if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
                    if (pvtUserSession.hasShownSaldoIntroScreen()) {
                        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
                            RouteManager.route(getContext(), ApplinkConstInternalGlobal.SALDO_DEPOSIT);
                        } else {
                            RouteManager.route(getContext(), String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                                    ApplinkConst.WebViewUrl.SALDO_DETAIL));
                        }

                        accountAnalytics.homepageSaldoClick(getContext(),
                                "com.tokopedia.saldodetails.activity.SaldoDepositActivity");
                    } else {
                        pvtUserSession.setSaldoIntroPageStatus(true);
                        RouteManager.route(getContext(), ApplinkConstInternalGlobal.SALDO_INTRO);
                    }
                } else {
                    RouteManager.route(getActivity(), String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                            ApplinkConst.WebViewUrl.SALDO_DETAIL));
                }
                break;

            case SettingConstant.SETTING_DEBIT_INSTANT:
                String debitInstantUrl = walletPref.retrieveDebitInstantUrl();
                if (!TextUtils.isEmpty(debitInstantUrl)) {
                    RouteManager.route(getActivity(), SettingConstant.Url.BASE_WEBVIEW_APPLINK + encodeUrl(debitInstantUrl));
                }
                break;
            default:
                break;
        }

    }

    private String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
