package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.addpin.data.StatusPinData
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.profilecompletion.customview.UnifyDialog
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

class PinOnboardingFragment: BaseDaggerFragment(){

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addChangePinViewModel by lazy { viewModelProvider.get(AddChangePinViewModel::class.java) }

    private var isFromLogin: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_onboard_pin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVar()

        ImageHandler.LoadImage(onboardImage, ONBOARD_PICT_URL)

        btnNext.setOnClickListener {
            trackingPinUtil.trackClickCreateButton()
            addChangePinViewModel.getStatusPin()
        }

        btnIgnore.setOnClickListener {
            trackingPinUtil.trackClickLaterButton()
            activity?.finish()
        }

        initObserver()
    }

    override fun onStart() {
        super.onStart()
        trackingPinUtil.trackScreen(screenName)
    }

    override fun getScreenName(): String = TrackingPinConstant.Screen.SCREEN_POPUP_PIN_WELCOME

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun initObserver(){
        addChangePinViewModel.getStatusPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetStatusPin(it.data)
                is Fail -> onErrorGetStatusPin(it.throwable)
            }
        })
    }

    private fun initVar() {
        val isFromLogin = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_LOGIN, false)
        if(isFromLogin != null)
            this.isFromLogin = isFromLogin
    }

    private fun onSuccessGetStatusPin(statusPinData: StatusPinData){
        if(statusPinData.isPhoneNumberExist && statusPinData.isPhoneNumberVerified){
            goToAddPin()
        }else{
            showAddPhoneDialog()
        }
    }

    private fun onErrorGetStatusPin(throwable: Throwable){
        view?.run{
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun showAddPhoneDialog() {
        val dialog = UnifyDialog(activity as Activity, UnifyDialog.VERTICAL_ACTION, UnifyDialog.NO_HEADER)
        dialog.setTitle(getString(R.string.title_dialog_add_phone))
        dialog.setDescription(getString(R.string.subtitle_dialog_add_phone))
        dialog.setOk(getString(R.string.title_dialog_add_phone))
        dialog.setOkOnClickListner(View.OnClickListener {
            goToAddPhone()
            dialog.dismiss()
        })
        dialog.setSecondary(getString(R.string.cancel_dialog_add_phone))
        dialog.setSecondaryOnClickListner(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun goToAddPhone(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
    }

    private fun goToAddPin(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_LOGIN, isFromLogin)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        activity?.finish()
    }

    private fun onSuccessAddPhoneNumber(data: Intent?){
        data?.extras?.run {
            val phone = getString(AddPhoneFragment.EXTRA_PHONE, "")
            if(!phone.isNullOrEmpty())
                userSession.phoneNumber = phone
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_ADD_PHONE -> {
                        onSuccessAddPhoneNumber(data)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        addChangePinViewModel.getStatusPinResponse.removeObservers(this)
        addChangePinViewModel.clear()
    }

    fun onBackPressed(){
        trackingPinUtil.trackClickBackButtonWelcome()
    }

    companion object {

        const val REQUEST_CODE_ADD_PHONE = 100

        const val ONBOARD_PICT_URL = "https://ecs7.tokopedia.net/img/android/others/onboard_add_pin.png"

        fun createInstance(bundle: Bundle): PinOnboardingFragment {
            val fragment = PinOnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}