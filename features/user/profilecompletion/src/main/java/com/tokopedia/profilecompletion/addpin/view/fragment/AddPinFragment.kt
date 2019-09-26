package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_pin.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class AddPinFragment: BaseDaggerFragment(){

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addChangePinViewModel by lazy { viewModelProvider.get(AddChangePinViewModel::class.java) }

    private var isConfirmPin = false
    private var pin = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_pin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayInitPin()
        inputPin.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length == 6){
                    if(isConfirmPin){
                        if(s.toString() == pin){
                            goToVerificationActivity()
                        }else{
                            inputPin.setText("")
                            inputPin.focus()
                            displayErrorPin(getString(R.string.error_wrong_pin))
                        }
                    }else{
                        showLoading()
                        addChangePinViewModel.checkPin(s.toString())
                    }
                }else{
                    hideErrorPin()
                }
            }
        })

        initObserver()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun initObserver(){
        addChangePinViewModel.addPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessAddPin(it.data)
                is Fail -> onErrorAddPin(it.throwable)
            }
        })

        addChangePinViewModel.checkPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessCheckPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })
    }

    private fun goToVerificationActivity(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COTP_PHONE_VERIFICATION)
    }

    private fun onSuccessAddPin(addChangePinData: AddChangePinData){
        if(addChangePinData.success){
            activity?.let {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
        }
    }

    private fun onSuccessCheckPin(checkPinData: CheckPinData){
        when{
            checkPinData.valid -> {
                dismissLoading()
                displayConfirmPin()
            }
            checkPinData.errorMessage.isNotEmpty() -> {
                dismissLoading()
                inputPin.setText("")
                displayErrorPin(checkPinData.errorMessage)
                inputPin.focus()
            }
        }
    }

    private fun onErrorAddPin(throwable: Throwable){
        onError(throwable)
        displayInitPin()
    }

    private fun onError(throwable: Throwable){
        dismissLoading()
        view?.run{
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
        inputPin.focus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_COTP_PHONE_VERIFICATION -> {
                        data?.extras?.run {
                            this.getString(ApplinkConstInternalGlobal.PARAM_UUID)?.let { uuid ->
                                if(uuid.isNotEmpty()) {
                                    showLoading()
                                    addChangePinViewModel.addPin(uuid)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun displayInitPin(){
        hideErrorPin()
        title.text = getString(R.string.create_pin)
        subtitle.text = getString(R.string.subtitle_create_pin)
        inputPin.setText("")
        inputPin.focus()
        isConfirmPin = false
        pin = ""
    }

    private fun displayConfirmPin(){
        hideErrorPin()
        title.text = getString(R.string.confirm_create_pin)
        subtitle.text = getString(R.string.subtitle_confirm_create_pin)
        isConfirmPin = true
        if(inputPin.text.toString().isNotEmpty()) pin = inputPin.text.toString()
        inputPin.setText("")
        inputPin.focus()
    }

    private fun displayErrorPin(error: String){
        errorPin.visibility = View.VISIBLE
        errorPin.text = error
    }

    private fun hideErrorPin(){
        errorPin.visibility = View.GONE
    }

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun onBackPressedFromConfirm(): Boolean{
        return if(isConfirmPin){
            displayInitPin()
            true
        }else{
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addChangePinViewModel.addPinResponse.removeObservers(this)
        addChangePinViewModel.checkPinResponse.removeObservers(this)
        addChangePinViewModel.validatePinResponse.removeObservers(this)
        addChangePinViewModel.clear()
    }

    companion object {

        const val REQUEST_CODE_COTP_PHONE_VERIFICATION = 101
        const val OTP_TYPE_PHONE_VERIFICATION = 124

        fun createInstance(bundle: Bundle): AddPinFragment {
            val fragment = AddPinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}