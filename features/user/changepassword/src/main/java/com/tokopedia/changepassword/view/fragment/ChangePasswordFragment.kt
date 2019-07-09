package com.tokopedia.changepassword.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.changepassword.ChangePasswordRouter
import com.tokopedia.changepassword.R
import com.tokopedia.changepassword.common.analytics.ChangePasswordAnalytics
import com.tokopedia.changepassword.common.di.ChangePasswordDependencyInjector
import com.tokopedia.changepassword.view.listener.ChangePasswordContract
import com.tokopedia.changepassword.view.presenter.ChangePasswordPresenter
import com.tokopedia.design.text.TkpdHintTextInputLayout
import kotlinx.android.synthetic.main.fragment_change_password.*

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordFragment : ChangePasswordContract.View, BaseDaggerFragment() {
    lateinit var presenter: ChangePasswordPresenter

    companion object {

        fun newInstance() = ChangePasswordFragment()

    }

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
        submit_button.setOnClickListener {
            onSubmitClicked()
        }

        forgot_pass_tv.setOnClickListener {
            onGoToForgotPass()
        }
        prepareHint()
        disableSubmitButton()
    }

    private fun onGoToForgotPass() {
        if (activity != null && activity!!.applicationContext != null) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.PARAM_EMAIL)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, presenter.userSession.email)
            startActivity(intent)
            activity!!.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        setViewListener()

    }

    private fun setViewListener() {
        old_password_et.addTextChangedListener(watcherOldPassword(wrapper_old))
        new_password_et.addTextChangedListener(watcherNewPassword(wrapper_new))
        new_password_confirmation_et.addTextChangedListener(watcherConfPassword(wrapper_conf))
    }

    private fun prepareHint() {
        showOldPasswordHint()
        showNewPasswordHint()
        showConfirmPasswordHint()
    }

    private fun showConfirmPasswordHint() {
        setWrapperHint(wrapper_conf, "")
    }

    private fun showNewPasswordHint() {
        setWrapperHint(wrapper_new, resources.getString(R.string.minimal_6_character))

    }

    private fun showOldPasswordHint() {
        setWrapperHint(wrapper_old, resources.getString(R.string.insert_password_not_other_pass))
    }

    private fun setWrapperHint(wrapper: TkpdHintTextInputLayout?, hint: String?) {
        wrapper?.run {
            setErrorEnabled(false)
            setHelperEnabled(true)
            setHelper(hint)
        }
    }

    private fun watcherOldPassword(wrapper: TkpdHintTextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                showOldPasswordHint()

                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
                checkIsValidForm()
            }
        }
    }

    private fun watcherConfPassword(wrapper: TkpdHintTextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
                checkIsValidForm()
            }
        }
    }

    private fun watcherNewPassword(wrapper: TkpdHintTextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                showNewPasswordHint()

                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
                checkIsValidForm()
            }
        }
    }

    private fun checkIsValidForm() {
        val oldPassword = old_password_et.text.toString().trim()
        val newPassword = new_password_et.text.toString().trim()
        val confirmPassword = new_password_confirmation_et.text.toString().trim()

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

    private fun resetError() {
        showOldPasswordHint()
        showNewPasswordHint()
        showConfirmPasswordHint()
        setWrapperError(wrapper_old, null)
        setWrapperError(wrapper_new, null)
        setWrapperError(wrapper_conf, null)
    }

    private fun setWrapperError(wrapper: TkpdHintTextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.setHint("")
            wrapper.error = s
        }
    }


    private fun onSubmitClicked() {
        showLoading()
        resetError()

        presenter.submitChangePasswordForm(
                old_password_et.text.toString(),
                new_password_et.text.toString(),
                new_password_confirmation_et.text.toString())

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
        setWrapperHint(wrapper_old, "")
        setWrapperError(wrapper_old, errorMessage)
    }

    override fun onErrorNewPass(errorMessage: String?) {
        hideLoading()
        setWrapperHint(wrapper_new, "")
        setWrapperError(wrapper_new, errorMessage)
    }

    override fun onErrorConfirmPass(errorMessage: String?) {
        hideLoading()
        setWrapperHint(wrapper_conf, "")
        setWrapperError(wrapper_conf, errorMessage)
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
}
