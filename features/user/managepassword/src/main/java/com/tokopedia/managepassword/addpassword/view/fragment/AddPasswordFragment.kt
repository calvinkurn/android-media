package com.tokopedia.managepassword.addpassword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.managepassword.addpassword.analytics.AddPasswordAnalytics
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_password.*
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 */

class AddPasswordFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddPasswordViewModel::class.java) }

    private val tracker: AddPasswordAnalytics = AddPasswordAnalytics()

    override fun getScreenName(): String {
        return SCREEN_ADD_PASSWORD
    }

    override fun initInjector() {
        getComponent(ManagePasswordComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!userSession.isLoggedIn) {
            activity?.let {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_LOGIN)
            }
        }
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.managepassword.R.layout.fragment_add_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtPassword?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.validatePassword(s)
            }
        })

        btnSubmit?.setOnClickListener {
            val password = txtPassword?.textFieldInput?.text?.toString() ?: ""
            if (txtPassword?.isTextFieldError != true && password.isNotEmpty()) {
                showLoading()
                btnSubmit?.isEnabled = false
                tracker.onClickSubmit()
                viewModel.createPassword(password)
            }
        }
    }

    private fun initObserver() {
        viewModel.validate.observe(this, Observer {
            when (it) {
                is Success -> {
                    clearErrorMessage()
                }
                is Fail -> {
                    setTextFieldError(it.throwable.message.toString())
                }
            }
        })

        viewModel.response.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessAdd()
                }
                is Fail -> {
                    onFailedAdd(it.throwable.message.toString())
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
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

    private fun clearErrorMessage() {
        txtPassword?.setError(false)
        txtPassword?.setMessage("")
        btnSubmit?.isEnabled = true
    }

    private fun setTextFieldError(message: String) {
        txtPassword?.setError(true)
        txtPassword?.setMessage(message)
        btnSubmit?.isEnabled = false
    }

    private fun onSuccessAdd() {
        activity?.let {
            tracker.onSuccessAddPassword()
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedAdd(message: String) {
        hideLoading()
        btnSubmit?.isEnabled = true
        tracker.onFailedAddPassword(message)
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun showLoading() {
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loader?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.response.removeObservers(this)
    }

    companion object {
        private const val SCREEN_ADD_PASSWORD = "Add password page"
        private const val REQUEST_LOGIN = 1000

        fun createInstance(): AddPasswordFragment {
            return AddPasswordFragment()
        }
    }
}