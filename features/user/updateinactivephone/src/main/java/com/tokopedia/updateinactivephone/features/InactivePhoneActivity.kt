package com.tokopedia.updateinactivephone.features

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.utils.removeRegionCodeAndCharacter
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneRegularActivity
import com.tokopedia.updateinactivephone.features.onboarding.withpin.InactivePhoneWithPinActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneActivity : BaseSimpleActivity(), HasComponent<InactivePhoneComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(InactivePhoneViewModel::class.java) }

    private var container: ConstraintLayout? = null

    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): InactivePhoneComponent {
        return InactivePhoneComponentBuilder.getComponent(application)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inactive_phone)
        container = findViewById(R.id.containerInactivePhone)

        component.inject(this)
        initObserver()
        remoteConfig = FirebaseRemoteConfigImpl(this)

        intent?.extras?.let {
            inactivePhoneUserDataModel = InactivePhoneUserDataModel(
                oldPhoneNumber = it.getString(ApplinkConstInternalGlobal.PARAM_PHONE)?.removeRegionCodeAndCharacter().orEmpty(),
                email = it.getString(ApplinkConstInternalGlobal.PARAM_EMAIL).orEmpty()
            )
        }

        if (isVersionValid()) {
            viewModel.userValidation(
                inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                inactivePhoneUserDataModel?.email.orEmpty()
            )
        }
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(this, {
            when (it) {
                is Success -> {
                    if (it.data.validation.status == InactivePhoneConstant.STATUS_SUCCESS) {
                        inactivePhoneUserDataModel?.userIndex = 1
                        isHasEmailAndPin(
                            inactivePhoneUserDataModel?.email,
                            inactivePhoneUserDataModel?.oldPhoneNumber,
                            0)
                    } else if (it.data.validation.status == InactivePhoneConstant.STATUS_MULTIPLE_ACCOUNT) {
                        gotoChoseAccount(inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty())
                    }
                }
                is Fail -> {
                    onError(it.throwable)
                }
            }
        })

        viewModel.getStatusPhoneNumber.observe(this, {
            when (it) {
                is Success -> {
                    onSuccessGetStatus(it.data)
                }
                is Fail -> {
                    onError(it.throwable)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CHOOSE_ACCOUNT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.extras?.let {
                        it.getParcelable<AccountListDataModel.UserDetailDataModel>(InactivePhoneConstant.PARAM_USER_DETAIL_DATA)?.let { userDetail ->
                            inactivePhoneUserDataModel?.userIndex = userDetail.index
                            isHasEmailAndPin(
                                inactivePhoneUserDataModel?.email,
                                inactivePhoneUserDataModel?.oldPhoneNumber,
                                inactivePhoneUserDataModel?.userIndex
                            )
                        }
                    }
                } else {
                    finish()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun isHasEmailAndPin(email: String?, phone: String?, index: Int?) {
        viewModel.getStatusPhoneNumber(
            email.orEmpty(),
            phone.orEmpty(),
            index.orZero()
        )
    }

    private fun onSuccessGetStatus(statusInactivePhoneNumberDataModel: StatusInactivePhoneNumberDataModel) {
        val status = statusInactivePhoneNumberDataModel.statusInactivePhoneNumber
        if (status.isSuccess && status.isAllowed && status.userIdEnc.isNotEmpty()) {
            gotoWithPinFlow(InactivePhoneUserDataModel(
                userIdEnc = status.userIdEnc,
                oldPhoneNumber = inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                email = inactivePhoneUserDataModel?.email.orEmpty(),
                userIndex = inactivePhoneUserDataModel?.userIndex.orZero()
            ))
        } else {
            gotoRegularRegularFlow()
        }
    }

    private fun gotoChoseAccount(phone: String) {
        startActivityForResult(InactivePhoneAccountListActivity.createIntent(this, phone), REQUEST_CODE_CHOOSE_ACCOUNT)
    }

    private fun gotoRegularRegularFlow() {
        startActivity(inactivePhoneUserDataModel?.let {
            InactivePhoneRegularActivity.createIntent(this, it)
        })
        finish()
    }

    private fun gotoWithPinFlow(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        startActivity(InactivePhoneWithPinActivity.createIntent(this, inactivePhoneUserDataModel))
        finish()
    }

    private fun isVersionValid(): Boolean {
        val minimumVersionSeller = remoteConfig?.getLong(KEY_MINIMUM_VERSION_SELLER) ?: 0
        val minimumVersionCustomer = remoteConfig?.getLong(KEY_MINIMUM_VERSION_CUSTOMER) ?: 0

        if (GlobalConfig.isSellerApp()) {
            if (GlobalConfig.VERSION_CODE < minimumVersionSeller) {
                showDialogChecker()
                return false
            }
        } else {
            if (GlobalConfig.VERSION_CODE < minimumVersionCustomer) {
                showDialogChecker()
                return false
            }
        }

        return true
    }

    private fun showDialogChecker() {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.text_update_dialog_title))
            setDescription(getString(R.string.text_update_dialog_description))
            setPrimaryCTAText(getString(R.string.text_update_cta_primary))
            setSecondaryCTAText(getString(R.string.text_update_cta_secondary))
            setPrimaryCTAClickListener {
                gotoPlayStore()
            }
            setSecondaryCTAClickListener {
                finish()
            }
            setCancelable(false)
            setOverlayClose(false)
        }.show()
    }

    private fun gotoPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAY_STORE + getAppPackageName()))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAY_STORE + getAppPackageName()))
            startActivity(intent)
        }
    }

    private fun onError(throwable: Throwable) {
        container?.let {
            Toaster.build(it, throwable.message.orEmpty(), Toaster.LENGTH_LONG).show()
        }
    }

    private fun getAppPackageName(): String {
        return applicationContext.packageName
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.phoneValidation.removeObservers(this)
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE_ACCOUNT = 100;

        private const val APPLINK_PLAY_STORE = "market://details?id="
        private const val URL_PLAY_STORE = "https://play.google.com/store/apps/details?id="

        private const val KEY_MINIMUM_VERSION_CUSTOMER = "key_android_inactive_phone_minimum_version_customer"
        private const val KEY_MINIMUM_VERSION_SELLER = "key_android_inactive_phone_minimum_version_seller"

        fun getIntent(context: Context): Intent {
            return Intent(context, InactivePhoneActivity::class.java)
        }
    }
}
