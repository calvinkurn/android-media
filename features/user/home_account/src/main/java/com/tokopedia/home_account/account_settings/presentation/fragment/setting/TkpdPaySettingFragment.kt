package com.tokopedia.home_account.account_settings.presentation.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.ACTIVITY_NAME_SALDO_DEPOSIT
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.BALANCE
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.CREDIT_CARD
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.GOPAY
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.TOKOCASH
import com.tokopedia.home_account.account_settings.analytics.AccountAnalytics
import com.tokopedia.home_account.account_settings.constant.SettingConstant
import com.tokopedia.home_account.account_settings.di.component.DaggerTkpdPaySettingComponent
import com.tokopedia.home_account.account_settings.di.module.TkpdPaySettingModule
import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

class TkpdPaySettingFragment : BaseGeneralSettingFragment() {

    @Inject
    lateinit var walletPref: WalletPref

    @Inject
    lateinit var accountAnalytics: AccountAnalytics

    @Inject
    lateinit var pvtUserSession: UserSessionInterface

    private val gopayUrl = TokopediaUrl.getInstance().WEB + "user/settings/payment/gopay"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTkpdPaySettingComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .tkpdPaySettingModule(TkpdPaySettingModule())
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity
            )
        )
    }

    override fun getSettingItems(): List<SettingItemUIModel> {
        val settingItems: MutableList<SettingItemUIModel> = ArrayList<SettingItemUIModel>()
        val walletModel = walletPref.retrieveWallet()

        if(walletModel != null) {
            settingItems.add(
                SettingItemUIModel(
                    SettingConstant.SETTING_TOKOCASH_ID,
                    walletModel.text
                )
            )
        }

        settingItems.add(
            SettingItemUIModel(
                SettingConstant.SETTING_SALDO_ID,
                getString(R.string.title_saldo_setting)
            )
        )
        settingItems.add(
            SettingItemUIModel(
                SettingConstant.SETTING_CREDIT_CARD_ID,
                getString(R.string.title_credit_card_setting)
            )
        )
        settingItems.add(
            SettingItemUIModel(
                SettingConstant.SETTING_DEBIT_INSTANT,
                getString(R.string.title_debit_instant_setting)
            )
        )

        if(settingItems.isNotEmpty()) {
            settingItems.add(0,
                SettingItemUIModel(
                    SettingConstant.SETTING_GOPAY,
                    getString(R.string.title_gopay_setting)
                )
            )
        }
        return settingItems
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onItemClicked(settingId: Int) {
        when (settingId) {
            SettingConstant.SETTING_CREDIT_CARD_ID -> {
                accountAnalytics.eventClickPaymentSetting(CREDIT_CARD)
                RouteManager.route(activity, ApplinkConstInternalPayment.PAYMENT_SETTING)
            }
            SettingConstant.SETTING_TOKOCASH_ID -> {
                accountAnalytics.eventClickPaymentSetting(TOKOCASH)
                val walletModel = walletPref.retrieveWallet()
                if (walletModel?.isLinked == true) {
                    RouteManager.route(activity, walletModel.applink)
                } else {
                    RouteManager.route(activity, walletModel?.action?.applink)
                }
            }
            SettingConstant.SETTING_SALDO_ID -> {
                accountAnalytics.eventClickPaymentSetting(BALANCE)
                val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(
                    context
                )
                if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT, false)) {
                    if (pvtUserSession.hasShownSaldoIntroScreen()) {
                        if (remoteConfig.getBoolean(
                                RemoteConfigKey.APP_ENABLE_SALDO_SPLIT,
                                false
                            )
                        ) {
                            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
                        } else {
                            RouteManager.route(
                                context, String.format(
                                    "%s?url=%s", ApplinkConst.WEBVIEW,
                                    ApplinkConst.WebViewUrl.SALDO_DETAIL
                                )
                            )
                        }
                        accountAnalytics.homepageSaldoClick(
                            context,
                            ACTIVITY_NAME_SALDO_DEPOSIT
                        )
                    } else {
                        pvtUserSession.setSaldoIntroPageStatus(true)
                        RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_INTRO)
                    }
                }
                else {
                    RouteManager.route(
                        activity, String.format(
                            "%s?url=%s", ApplinkConst.WEBVIEW,
                            ApplinkConst.WebViewUrl.SALDO_DETAIL
                        )
                    )
                }
            }
            SettingConstant.SETTING_DEBIT_INSTANT -> {
                val debitInstantUrl = walletPref.retrieveDebitInstantUrl()
                if(debitInstantUrl?.isNotEmpty() == true) {
                    RouteManager.route(
                        activity,
                        SettingConstant.Url.BASE_WEBVIEW_APPLINK + encodeUrl(debitInstantUrl)
                    )
                }
            }

            SettingConstant.SETTING_GOPAY -> {
                accountAnalytics.eventClickPaymentSetting(GOPAY)
                RouteManager.route(
                    activity,
                    SettingConstant.Url.BASE_WEBVIEW_APPLINK + gopayUrl
                )
            }
        }
    }

    private fun encodeUrl(url: String): String {
        return try {
            URLEncoder.encode(url, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        fun createInstance(): Fragment {
            return TkpdPaySettingFragment()
        }
        private val TAG = TkpdPaySettingFragment::class.java.simpleName
    }
}