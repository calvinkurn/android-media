package com.tokopedia.home_account.account_settings.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.getGotoKYCApplink
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT_BANK
import com.tokopedia.home_account.AccountConstants.Analytics.ADDRESS_LIST
import com.tokopedia.home_account.AccountConstants.Analytics.PERSONAL_DATA
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.AccountConstants.Analytics.PASSWORD
import com.tokopedia.home_account.account_settings.AccountHomeUrl
import com.tokopedia.home_account.account_settings.analytics.AccountAnalytics
import com.tokopedia.home_account.account_settings.constant.SettingConstant
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AccountSettingActivity : BaseSimpleActivity() {

    private val PROJECT_ID = 7
    private val SOURCE = "Account"

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: AccountAnalytics

    private val viewModel: AccountSettingViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComponentFactory.instance
            .createHomeAccountComponent(this, application)
            .inject(this)
        toolbar.hide()
        setContent {
            val state by viewModel.state.observeAsState()

            NestTheme {
                Scaffold(topBar = {
                    NestHeader(
                        type = NestHeaderType.SingleLine(
                            title = stringResource(id = R.string.menu_account_title_security),
                            onBackClicked = { onBackClicked() }
                        )
                    )
                }, content = { padding ->
                        AccountSettingScreen(
                            state = state,
                            modifier = Modifier.padding(padding),
                            onItemClicked = ::onItemClicked
                        )
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0 -> {
                    NetworkErrorHelper.showSnackbar(
                        this,
                        getString(R.string.message_success_change_profile)
                    )
                    setResult(resultCode, Intent())
                }

                REQUEST_CHANGE_PASSWORD -> NetworkErrorHelper.showGreenCloseSnackbar(
                    this,
                    getString(
                        R.string.message_success_change_password
                    )
                )

                else -> {}
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onItemClicked(settingId: Int) {
        val intent: Intent
        when (settingId) {
            SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID -> {
                analytics.eventClickAccountSetting(PERSONAL_DATA)
                intent = RouteManager.getIntent(this, ApplinkConst.SETTING_PROFILE)
                startActivityForResult(intent, 0)
            }

            SettingConstant.SETTING_ACCOUNT_PASS_ID -> {
                analytics.eventClickAccountSetting(PASSWORD)
                analytics.eventClickAccountPassword()
                intent = RouteManager.getIntent(
                    this,
                    ApplinkConstInternalUserPlatform.HAS_PASSWORD
                )
                startActivity(intent)
            }

            SettingConstant.SETTING_PIN -> {
                analytics.eventClickPinSetting()
                onPinMenuClicked()
            }

            SettingConstant.SETTING_PUSH_NOTIF -> {
                analytics.eventClickSignInByPushNotifSetting()
                onPushNotifClicked()
            }

            SettingConstant.SETTING_BIOMETRIC -> onBiometricSettingClicked()
            SettingConstant.SETTING_ACCOUNT_ADDRESS_ID -> {
                analytics.eventClickAccountSetting(ADDRESS_LIST)
                intent = RouteManager.getIntent(
                    this,
                    ApplinkConstInternalLogistic.MANAGE_ADDRESS_FROM_ACCOUNT
                )
                startActivity(intent)
            }

            SettingConstant.SETTING_ACCOUNT_KYC_ID -> onKycMenuClicked()
            SettingConstant.SETTING_ACCOUNT_SAMPAI_ID -> goToTokopediaCorner()
            SettingConstant.SETTING_BANK_ACCOUNT_ID -> {
                analytics.eventClickPaymentSetting(ACCOUNT_BANK)
                gotoAccountBank()
            }

            else -> {}
        }
    }

    private fun onBackClicked() {
        finish()
    }

    private fun gotoAccountBank() {
        startActivity(
            RouteManager.getIntent(
                this,
                ApplinkConstInternalGlobal.SETTING_BANK
            )
        )
    }

    private fun goToTokopediaCorner() {
        analytics.eventClickTokopediaCornerSetting()
        val intent = RouteManager.getIntent(this, AccountHomeUrl.APPLINK_TOKOPEDIA_CORNER)
        startActivity(intent)
    }

    private fun onKycMenuClicked() {
        analytics.eventClickKycSetting(PROJECT_ID.toString())
        RouteManager.route(this, getGotoKYCApplink(PROJECT_ID.toString(), SOURCE, ""))
    }

    private fun onBiometricSettingClicked() {
        analytics.eventClickFingerprint()
        RouteManager.route(this, ApplinkConstInternalUserPlatform.BIOMETRIC_SETTING)
    }

    private fun onPushNotifClicked() {
        RouteManager.route(this, ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_SETTING)
    }

    private fun onPinMenuClicked() {
        if (userSession.isMsisdnVerified) {
            goToPinOnboarding()
        } else {
            showAddPhoneDialog()
        }
    }

    private fun goToPinOnboarding() {
        val intent = RouteManager.getIntent(
            this,
            ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
        startActivity(intent)
    }

    private fun showAddPhoneDialog() {
        val dialog =
            DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.account_home_add_phone_title))
        dialog.setDescription(getString(R.string.account_home_add_phone_message))
        dialog.setPrimaryCTAText(getString(R.string.account_home_add_phone_title))
        dialog.setSecondaryCTAText(getString(R.string.new_home_account_label_clear_cache_cancel))
        dialog.setPrimaryCTAClickListener {
            goToPinOnboarding()
            dialog.dismiss()
            Unit
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            Unit
        }
        dialog.show()
    }

    override fun getNewFragment(): Fragment? {
//        val bundle = Bundle()
//        if (intent.extras != null) {
//            bundle.putAll(intent.extras)
//        }
//        return AccountSettingFragment.createInstance(bundle)
        return null
    }

    companion object {
        private const val REQUEST_CHANGE_PASSWORD = 123
        fun createIntent(context: Context?): Intent {
            return Intent(context, AccountSettingActivity::class.java)
        }
    }
}
