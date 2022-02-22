package com.tokopedia.updateinactivephone.features

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.utils.reformatPhoneNumber
import com.tokopedia.updateinactivephone.databinding.ActivityInactivePhoneBinding
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

    private var viewBinding: ActivityInactivePhoneBinding? = null
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

        component.inject(this)
        initObserver()
        remoteConfig = FirebaseRemoteConfigImpl(applicationContext)

        intent?.let {
            when {
                it.data != null -> {
                    inactivePhoneUserDataModel = InactivePhoneUserDataModel(
                            oldPhoneNumber = it.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PHONE)?.reformatPhoneNumber().orEmpty(),
                            email = it.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_EMAIL).orEmpty()
                    )
                }
                it.extras != null -> {
                    inactivePhoneUserDataModel = InactivePhoneUserDataModel(
                            oldPhoneNumber = it.extras?.getString(ApplinkConstInternalGlobal.PARAM_PHONE)?.reformatPhoneNumber().orEmpty(),
                            email = it.extras?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL).orEmpty()
                    )
                }
                else -> {
                    onError(MessageErrorException(INVALID_PARAM))
                }
            }
        }


        if (isVersionValid()) {
            inactivePhoneUserDataModel?.let {
                viewModel.userValidation(it)
            }
        }
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(this, {
            when (it) {
                is Success -> {
                    when (it.data.validation.status) {
                        InactivePhoneConstant.STATUS_SUCCESS -> {
                            isHasEmailAndPin()
                        }
                        InactivePhoneConstant.STATUS_MULTIPLE_ACCOUNT -> {
                            gotoChoseAccount(inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty())
                        }
                        InactivePhoneConstant.STATUS_FAIL -> {
                            onError(MessageErrorException(it.data.validation.error))
                        }
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
                    if (it.data.statusInactivePhoneNumber.isSuccess) {
                        onSuccessGetStatus(it.data)
                    } else {
                        onError(MessageErrorException(it.data.statusInactivePhoneNumber.errorMessage))
                    }
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
                            isHasEmailAndPin()
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

    private fun isHasEmailAndPin() {
        inactivePhoneUserDataModel?.let {
            viewModel.getStatusPhoneNumber(it)
        }
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
            inactivePhoneUserDataModel?.userIndex = 1
            gotoRegularRegularFlow()
        }
    }

    private fun gotoChoseAccount(phone: String) {
        startActivityForResult(InactivePhoneAccountListActivity.createIntent(applicationContext, phone), REQUEST_CODE_CHOOSE_ACCOUNT)
    }

    private fun gotoRegularRegularFlow() {
        startActivity(inactivePhoneUserDataModel?.let {
            InactivePhoneRegularActivity.createIntent(applicationContext, it)
        })
        finish()
    }

    private fun gotoWithPinFlow(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        startActivity(InactivePhoneWithPinActivity.createIntent(applicationContext, inactivePhoneUserDataModel))
        finish()
    }

    private fun isVersionValid(): Boolean {
        val minimumVersionSeller = remoteConfig?.getLong(KEY_MINIMUM_VERSION_SELLER).orZero()
        val minimumVersionCustomer = remoteConfig?.getLong(KEY_MINIMUM_VERSION_CUSTOMER).orZero()

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
        DialogUnify(applicationContext, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
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
        val message = ErrorHandler.getErrorMessage(applicationContext, throwable)
        setResult(Activity.RESULT_CANCELED, Intent().apply {
            putExtras(Bundle().apply {
                putString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, message)
            })
        })
        finish()
    }

    private fun getAppPackageName(): String {
        return applicationContext.packageName
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.phoneValidation.removeObservers(this)
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE_ACCOUNT = 100

        private const val INVALID_PARAM = "Invalid parameters"

        private const val APPLINK_PLAY_STORE = "market://details?id="
        private const val URL_PLAY_STORE = "https://play.google.com/store/apps/details?id="

        private const val KEY_MINIMUM_VERSION_CUSTOMER = "key_android_inactive_phone_minimum_version_customer"
        private const val KEY_MINIMUM_VERSION_SELLER = "key_android_inactive_phone_minimum_version_seller"

        fun createIntent(context: Context, phoneNumber: String, email: String): Intent {
            return Intent(context, InactivePhoneActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString(ApplinkConstInternalGlobal.PARAM_PHONE, phoneNumber)
                    putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                })
            }
        }
    }
}
