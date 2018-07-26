package com.tokopedia.changepassword.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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

        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }

    override fun getScreenName(): String {
        return ChangePasswordAnalytics.SCREEN_NAME
    }

    override fun initInjector() {

        presenter = ChangePasswordDependencyInjector.Companion.inject(activity!!.applicationContext)
        presenter.attachView(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_change_password, container, false)

        val submitButton: TextView = view.findViewById(R.id.save_button)
        submitButton.setOnClickListener {
            onSubmitClicked()
        }


        prepareHint()

        return view
    }

    override fun onResume() {
        super.onResume()
        setViewListener()

    }

    private fun setViewListener() {
        old_password.addTextChangedListener(oldPasswordWatcher(wrapper_old))
        new_password.addTextChangedListener(newPasswordWatcher(wrapper_new))
        new_password_confirmation.addTextChangedListener(confirmPasswordWatcher(wrapper_conf))
    }

    private fun prepareHint() {
        showOldPasswordHint()
        showNewPasswordHint()
        showConfirmPasswordHint()
    }

    private fun showConfirmPasswordHint() {
        setWrapperHint(wrapper_conf, resources.getString(R.string.must_equal_new_password))
    }

    private fun showNewPasswordHint() {
        setWrapperHint(wrapper_new, resources.getString(R.string.minimal_6_character))

    }

    private fun showOldPasswordHint() {
        setWrapperHint(wrapper_old, "")
    }

    private fun setWrapperHint(wrapper: TkpdHintTextInputLayout?, hint: String?) {
        wrapper?.setErrorEnabled(false)
        wrapper?.setHelperEnabled(true)
        wrapper?.setHelper(hint)
    }

    private fun oldPasswordWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher? {

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
                validateOldPassword(text)

            }
        }
    }

    private fun newPasswordWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher? {
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
                validateNewPassword(text)

            }
        }
    }

    private fun confirmPasswordWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                showConfirmPasswordHint()
                validateConfirmPassword(text)
            }
        }
    }


    private fun validateOldPassword(text: Editable) {
        if (text.isEmpty()) {
            setWrapperError(wrapper_old, getString(R.string.error_field_required))
        }
        checkIsValidForm()

    }

    private fun validateNewPassword(text: Editable) {
        if (text.isEmpty()) {
            setWrapperError(wrapper_new, getString(R.string.error_field_required))
        } else if (text.length < presenter.PASSWORD_MINIMUM_LENGTH) {
            setWrapperError(wrapper_new, getString(R.string.error_minimal_password))
        }
        checkIsValidForm()
    }

    private fun validateConfirmPassword(text: Editable) {
        when {
            text.isEmpty() -> setWrapperError(wrapper_conf, getString(R.string.error_field_required))
            text.length < presenter.PASSWORD_MINIMUM_LENGTH -> setWrapperError(wrapper_conf, getString(R.string.error_minimal_password))
            text.toString() != new_password.text.toString() -> setWrapperError(wrapper_conf,
                    getString(R.string.error_must_equal_new_password))
        }

        checkIsValidForm()
    }

    private fun checkIsValidForm() {
        val oldPassword = old_password.text.toString().trim()
        val newPassword = new_password.text.toString().trim()
        val confirmPassword = new_password_confirmation.text.toString().trim()

        if (presenter.isValidForm(oldPassword, newPassword, confirmPassword)) {
            enableSubmitButton()
        } else {
            disableSubmitButton()
        }
    }

    private fun disableSubmitButton() {
        MethodChecker.setBackground(save_button, MethodChecker.getDrawable(context, R.drawable
                .grey_button_rounded))
        save_button.setTextColor(MethodChecker.getColor(context, R.color.grey_500))
        save_button.isEnabled = false
    }

    private fun enableSubmitButton() {
        MethodChecker.setBackground(save_button, MethodChecker.getDrawable(context, R.drawable
                .green_button_rounded_unify))
        save_button.setTextColor(MethodChecker.getColor(context, R.color.white))
        save_button.isEnabled = true
    }

    private fun resetError() {
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
            wrapper.error = s
        }
    }


    private fun onSubmitClicked() {
        showLoading()
        resetError()
        presenter.submitChangePasswordForm(
                old_password.text.toString(),
                new_password.text.toString(),
                new_password_confirmation.text.toString())
    }


    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        wrapper_old.visibility = View.GONE
        wrapper_new.visibility = View.GONE
        wrapper_conf.visibility = View.GONE
        save_button.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        wrapper_old.visibility = View.VISIBLE
        wrapper_new.visibility = View.VISIBLE
        wrapper_conf.visibility = View.VISIBLE
        save_button.visibility = View.VISIBLE
    }

    override fun onSuccessChangePassword() {
        presenter.logoutToHomePage()
    }

    override fun onErrorChangePassword(errorMessage: String) {
        hideLoading()
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(activity)
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
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

    override fun onSuccessLogout() {
        if (context!!.applicationContext is ChangePasswordRouter) {
            val intent: Intent = (context!!.applicationContext as ChangePasswordRouter)
                    .getHomeIntent(activity!!)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            activity!!.finish()
        }
    }
}
