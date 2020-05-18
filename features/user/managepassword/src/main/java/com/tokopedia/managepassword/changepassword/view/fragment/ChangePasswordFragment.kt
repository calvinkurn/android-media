package com.tokopedia.managepassword.changepassword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.managepassword.changepassword.common.analytics.ChangePasswordAnalytics
import com.tokopedia.managepassword.changepassword.common.di.ChangePasswordDependencyInjector
import com.tokopedia.managepassword.changepassword.view.listener.ChangePasswordContract
import com.tokopedia.managepassword.changepassword.view.presenter.ChangePasswordPresenter
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.managepassword.R
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_change_password.*

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordFragment : ChangePasswordContract.View, BaseDaggerFragment() {
    lateinit var presenter: ChangePasswordPresenter
    lateinit var oldPasswordTextField : TextFieldUnify
    lateinit var newPasswordTextField : TextFieldUnify
    lateinit var confPasswordTextField : TextFieldUnify

    private lateinit var userSession : UserSessionInterface

    override fun getScreenName(): String {
        return ChangePasswordAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        activity?.let {
            presenter = ChangePasswordDependencyInjector.Companion.inject(it.applicationContext)
            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSession = UserSession(context)
        if (!userSession.isLoggedIn) {
            activity?.let {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_LOGIN)
            }
        }

        oldPasswordTextField = view.findViewById(R.id.wrapper_old)
        newPasswordTextField = view.findViewById(R.id.wrapper_new)
        confPasswordTextField = view.findViewById(R.id.wrapper_conf)

        oldPasswordTextField.textFieldInput.setSimpleListener { processInput(it.toString(), oldPasswordTextField) }
        newPasswordTextField.textFieldInput.setSimpleListener { processInput(it.toString(), newPasswordTextField) }
        confPasswordTextField.textFieldInput.setSimpleListener { processInput(it.toString(), confPasswordTextField) }

        submit_button?.setOnClickListener {
            onSubmitClicked()
        }

        forgot_pass_tv?.setOnClickListener {
            onGoToForgotPass()
        }
        disableSubmitButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_LOGOUT -> {
                if (resultCode == Activity.RESULT_OK) {
                    context?.let {
                        DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                            setTitle(getString(R.string.password))
                            setDescription(getString(R.string.success_change_password))
                            setPrimaryCTAText("Ya")
                            setPrimaryCTAClickListener {
                                RouteManager.route(context, ApplinkConst.HOME)
                            }
                        }.show()
                    }
                }
            }
            REQUEST_LOGIN -> {
                if (!userSession.isLoggedIn) {
                    activity?.apply {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
            }
        }
    }

    private fun processInput(input: String, textField: TextFieldUnify) {
        if(input.isBlank()){
            textField.setError(true)
        } else {
            textField.setError(false)
        }
        checkIsValidForm()
    }

    private fun onGoToForgotPass() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
            startActivity(intent)
            it.finish()
        }
    }

    private fun checkIsValidForm() {
        val oldPassword = oldPasswordTextField.textFieldInput.text.toString()
        val newPassword = newPasswordTextField.textFieldInput.text.toString()
        val confirmPassword = confPasswordTextField.textFieldInput.text.toString()

        if (presenter.isValidForm(oldPassword, newPassword, confirmPassword)) {
            enableSubmitButton()
        } else {
            disableSubmitButton()
        }

    }

    private fun disableSubmitButton() {
        submit_button.isEnabled = false
    }

    private fun enableSubmitButton() {
        submit_button.isEnabled = true
    }

    private fun onSubmitClicked() {
        showLoading()
        presenter.submitChangePasswordForm(
                oldPasswordTextField.textFieldInput.text.toString(),
                newPasswordTextField.textFieldInput.text.toString(),
                confPasswordTextField.textFieldInput.text.toString())

    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        main_view.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        main_view.visibility = View.VISIBLE
    }

    override fun onSuccessChangePassword() {
        hideLoading()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LOGOUT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, false)
        startActivityForResult(intent, REQUEST_LOGOUT)
    }

    override fun onErrorOldPass(errorMessage: String?) {
        hideLoading()
        oldPasswordTextField.setError(true)
        errorMessage?.let{
            oldPasswordTextField.setMessage(it)
        }
    }

    override fun onErrorNewPass(errorMessage: String?) {
        hideLoading()
        newPasswordTextField.setError(true)
        errorMessage?.let{
            newPasswordTextField.setMessage(it)
        }
    }

    override fun onErrorConfirmPass(errorMessage: String?) {
        hideLoading()
        confPasswordTextField.setError(true)
        errorMessage?.let{
            confPasswordTextField.setMessage(it)
        }
    }

    override fun onErrorChangePassword(errorMessage: String) {
        hideLoading()
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showRedSnackbar(activity, getString(com.tokopedia.abstraction.R.string.default_request_error_unknown))
        } else {
            NetworkErrorHelper.showRedSnackbar(activity, errorMessage)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    private fun EditText.setSimpleListener(listener : (p0: CharSequence?) -> Unit) {
        this.addTextChangedListener(TextWatcherFactory().create(listener))
    }

    class TextWatcherFactory {
        fun create( afterTextChanged : (p0: CharSequence?) -> Unit) : TextWatcher  {
            return object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) = afterTextChanged(p0)

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){

                }
            }
        }
    }

    companion object {
        private const val REQUEST_LOGOUT = 1000
        private const val REQUEST_LOGIN = 2000
    }
}
