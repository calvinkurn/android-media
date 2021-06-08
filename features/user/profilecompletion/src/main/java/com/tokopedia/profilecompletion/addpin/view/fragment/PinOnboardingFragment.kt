package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.StatusPinData
import com.tokopedia.profilecompletion.addpin.view.activity.PinOnboardingActivity
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.changepin.view.activity.ChangePinActivity
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.common.LoadingDialog
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_onboard_pin.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class PinOnboardingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var loadingDialog: LoadingDialog

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addChangePinViewModel by lazy { viewModelProvider.get(AddChangePinViewModel::class.java) }

    private var isSkipOtp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboard_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVar()

        ImageHandler.LoadImage(onboardImage, ONBOARD_PICT_URL)

        btnNext.setOnClickListener {
            trackingPinUtil.trackClickCreateButton()
            goToAddPin()
        }

        initObserver()

        if (!userSession.isMsisdnVerified) {
            goToAddPhone()
        } else {
            showLoading()
            addChangePinViewModel.getStatusPin()
        }
    }

    override fun onStart() {
        super.onStart()
        trackingPinUtil.trackScreen(screenName)
    }

    override fun getScreenName(): String = TrackingPinConstant.Screen.SCREEN_POPUP_PIN_WELCOME

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun initObserver() {
        addChangePinViewModel.getStatusPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetStatusPin(it.data)
                is Fail -> onErrorGetStatusPin(it.throwable)
            }
        })
    }

    private fun initVar() {
        val isSkipOtp = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, false)
        if (isSkipOtp != null)
            this.isSkipOtp = isSkipOtp
    }

    private fun onSuccessGetStatusPin(statusPinData: StatusPinData) {
        if (statusPinData.isRegistered) {
            goToChangePin()
        } else {
            if (activity is PinOnboardingActivity) {
                (activity as PinOnboardingActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            hideLoading()
        }
    }

    private fun goToChangePin() {
        RouteManager.route(activity, ApplinkConstInternalGlobal.CHANGE_PIN)
        activity?.finish()
    }

    private fun onErrorGetStatusPin(throwable: Throwable) {
        view?.run {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun goToAddPhone() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
    }

    private fun goToAddPin() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, isSkipOtp)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        activity?.finish()
    }

    private fun onSuccessAddPhoneNumber() {
        hideLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ADD_PHONE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        onSuccessAddPhoneNumber()
                    }
                    Activity.RESULT_CANCELED -> {
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        loader?.show()
        container?.hide()
    }

    private fun hideLoading() {
        loader?.hide()
        container?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        addChangePinViewModel.getStatusPinResponse.removeObservers(this)
        addChangePinViewModel.flush()
    }

    fun onBackPressed() {
        trackingPinUtil.trackClickBackButtonWelcome()
    }

    companion object {

        const val REQUEST_CODE_ADD_PHONE = 100

        const val ONBOARD_PICT_URL = "https://ecs7.tokopedia.net/android/user/high_onboard_create_pin.png"

        fun createInstance(bundle: Bundle): PinOnboardingFragment {
            val fragment = PinOnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}