package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_add_pin.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class AddPinFragment: BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addChangePinViewModel by lazy { viewModelProvider.get(AddChangePinViewModel::class.java) }

    private var isConfirmPin = false
    private var pin = ""
    private var validateToken = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_pin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVar()

        inputPin.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length == 6){
                    if(isConfirmPin){
                        showLoading()
                        addChangePinViewModel.validatePin(s.toString())
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

    private fun initVar(){
        val uuid = arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID)
        if(!uuid.isNullOrEmpty()){
            validateToken = uuid
        }
    }

    private fun initObserver(){
        addChangePinViewModel.addPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessAddPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        addChangePinViewModel.checkPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessCheckPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        addChangePinViewModel.validatePinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessValidatePin(it.data)
                is Fail -> onError(it.throwable)
            }
        })
    }

    private fun onSuccessAddPin(addChangePinData: AddChangePinData){
        dismissLoading()
        if(addChangePinData.success) displayConfirmPin()
    }

    private fun onSuccessCheckPin(checkPinData: CheckPinData){
        when{
            checkPinData.valid -> {
                if(validateToken.isNotEmpty()) addChangePinViewModel.addPin(validateToken)
            }
            checkPinData.errorMessage.isNotEmpty() -> {
                dismissLoading()
                displayErrorPin(checkPinData.errorMessage)
            }
        }
    }

    private fun onSuccessValidatePin(validatePinData: ValidatePinData){
        dismissLoading()
        when{
            validatePinData.valid -> {
                activity?.let {
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                }
            }
            validatePinData.errorMessage.isNotEmpty() -> {
                displayErrorPin(validatePinData.errorMessage)
            }
        }
    }

    private fun onError(throwable: Throwable){
        dismissLoading()
        view?.run{
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun displayInitPin(){
        hideErrorPin()
        title.text = getString(R.string.create_pin)
        subtitle.text = getString(R.string.subtitle_create_pin)
        inputPin.setText("")
        isConfirmPin = false
        pin = ""
    }

    private fun displayConfirmPin(){
        hideErrorPin()
        title.text = getString(R.string.confirm_create_pin)
        subtitle.text = getString(R.string.subtitle_confirm_create_pin)
        inputPin.setText("")
        isConfirmPin = true
        if(!inputPin.text.toString().isEmpty()) pin = inputPin.text.toString()
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
        fun createInstance(bundle: Bundle): AddPinFragment {
            val fragment = AddPinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}