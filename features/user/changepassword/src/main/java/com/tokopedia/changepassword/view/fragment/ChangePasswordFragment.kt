package com.tokopedia.changepassword.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.changepassword.ChangePasswordRouter
import com.tokopedia.changepassword.R
import com.tokopedia.changepassword.common.analytics.ChangePasswordAnalytics
import com.tokopedia.changepassword.common.di.ChangePasswordDependencyInjector
import com.tokopedia.changepassword.view.listener.ChangePasswordContract
import com.tokopedia.changepassword.view.presenter.ChangePasswordPresenter
import com.tokopedia.unifycomponents.TextFieldUnify
import kotlinx.android.synthetic.main.fragment_change_password.*

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordFragment : ChangePasswordContract.View, BaseDaggerFragment() {
    lateinit var presenter: ChangePasswordPresenter
    lateinit var oldPasswordTextField : TextFieldUnify
    lateinit var newPasswordTextField : TextFieldUnify
    lateinit var confPasswordTextField : TextFieldUnify

    override fun getScreenName(): String {
        return ChangePasswordAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        activity?.let {
            presenter = ChangePasswordDependencyInjector.Companion.inject(activity!!.applicationContext)
            presenter.attachView(this)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun processInput(input: String, textField: TextFieldUnify) {
        if(input.isBlank()){
            textField.setError(true)
        } else {
            textField.setError(false)
        }
        checkIsValidForm()
    }

    private fun onGoToForgotPass() {
        if (activity != null && activity!!.applicationContext != null) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, presenter.userSession.email)
            startActivity(intent)
            activity!!.finish()
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
        MethodChecker.setBackground(submit_button, MethodChecker.getDrawable(context, R.drawable
                .bg_button_disabled))
        submit_button.setTextColor(MethodChecker.getColor(context, R.color.grey_500))
        submit_button.isEnabled = false
    }

    private fun enableSubmitButton() {
        MethodChecker.setBackground(submit_button, MethodChecker.getDrawable(context, R.drawable
                .button_curvy_green))
        submit_button.setTextColor(MethodChecker.getColor(context, R.color.white))
        submit_button.isEnabled = true
    }

    private fun onSubmitClicked() {
        showLoading()
        presenter.submitChangePasswordForm(
                oldPasswordTextField.textFieldInput.text.toString(),
                newPasswordTextField.textFieldInput.text.toString(),
                confPasswordTextField.textFieldInput.text.toString())

    }

    override fun onSuccessLogout() {



        val stickyPref = activity?.getSharedPreferences(STICKY_LOGIN_PREF, Context.MODE_PRIVATE)
        stickyPref?.edit()?.clear()?.apply()

        val intent = RouteManager.getIntent(activity, ApplinkConst.HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun sendBroadcast() {
        val intent = Intent(BROADCAST_LOGOUT)
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
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
        if (activity != null && context != null && context!!.applicationContext is
                        ChangePasswordRouter) {
            (context!!.applicationContext as ChangePasswordRouter)
                    .logoutToHome(activity!!)
        } else {
            activity!!.finish()
        }
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
            NetworkErrorHelper.showRedSnackbar(activity, getString(R.string
                    .default_request_error_unknown))
        } else {
            NetworkErrorHelper.showRedSnackbar(activity, errorMessage)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onErrorLogout(errorMessage: String) {
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(activity)
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }
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
        private const val BROADCAST_LOGOUT = "BROADCAST_LOGOUT"
        private const val STICKY_LOGIN_PREF = "sticky_login_widget.pref"
    }
}
