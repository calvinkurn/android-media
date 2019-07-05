package com.tokopedia.profilecompletion.addphone.view.fragment

//import com.tokopedia.unifycomponents.Toaster
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_phone.*
import javax.inject.Inject


class AddPhoneFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AddPhoneViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionComponent::class.java).inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_phone, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        buttonSubmit.setOnClickListener {
            val phone = etPhone.text.toString()
            if (phone.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
            } else if (!isValidPhone(phone)) {
                setErrorText(getString(R.string.wrong_phone_format))
            } else {
                showLoading()
                //TODO GET OTP FIRST
//                goToVerificationActivity()
            }
        }
    }

    private fun goToVerificationActivity() {
        //TODO
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra("is_show_choose_method", false)
        startActivityForResult(intent, REQUEST_COTP_PHONE_VERIFICATION)
    }

    private fun setErrorText(s: String) {
        if (TextUtils.isEmpty(s)) {
            tvMessage.visibility = View.VISIBLE
            tvError.visibility = View.GONE
            buttonSubmit.isEnabled = true
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY
            wrapperPhone.setErrorEnabled(true)
        } else {
            wrapperPhone.setErrorEnabled(false)
            tvError.visibility = View.VISIBLE
            tvError.text = s
            tvMessage.visibility = View.GONE
            buttonSubmit.isEnabled = false
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY_DISABLED

        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private fun setObserver() {
        viewModel.mutateAddPhoneResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessAddPhone(it.data)
                        is Fail -> onErrorAddPhone(it.throwable)
                    }
                }
        )

    }

    private fun onErrorAddPhone(throwable: Throwable) {
        dismissLoading()
        Log.d("NISNIS", throwable.message)
        //TODO uncomment after unify is fixed
//        view?.run {
//            Toaster.showError(
//                    this,
//                    ErrorHandlerSession.getErrorMessage(throwable, context, true),
//                    Snackbar.LENGTH_LONG)
//        }
    }

    private fun onSuccessAddPhone(pojo: AddPhonePojo) {
        dismissLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, pojo.data.userProfileCompletionUpdate.completionScore)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
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

    private fun onSuccessVerifyPhone(data: Intent?) {

        data?.extras?.run {
            val otpCode = getString("otp", "")
            if (otpCode.isNotBlank()) {
                val phone = etPhone.text.toString()
                viewModel.mutateAddPhone(phone.trim(), otpCode)
            } else {
                onErrorAddPhone(MessageErrorException(getString(R.string.default_request_error_unknown),
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW.toString()))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_COTP_PHONE_VERIFICATION
                && resultCode == Activity.RESULT_OK) {
            onSuccessVerifyPhone(data)
        }
    }

    companion object {
        val EXTRA_PROFILE_SCORE = "profile_score"
        val REQUEST_COTP_PHONE_VERIFICATION = 101

        fun createInstance(bundle: Bundle): AddPhoneFragment {
            val fragment = AddPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}