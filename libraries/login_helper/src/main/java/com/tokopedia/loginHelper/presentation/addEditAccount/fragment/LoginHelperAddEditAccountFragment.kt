package com.tokopedia.loginHelper.presentation.addEditAccount.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperAddEditAccountBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.entity.PageMode
import com.tokopedia.loginHelper.presentation.accountSettings.LoginHelperAccountSettingsActivity
import com.tokopedia.loginHelper.presentation.addEditAccount.customview.SaveToLocalCoachmark
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.LoginHelperAddEditAccountViewModel
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import com.tokopedia.loginHelper.util.BundleConstants
import com.tokopedia.loginHelper.util.showToaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoginHelperAddEditAccountFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentLoginHelperAddEditAccountBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperAddEditAccountViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[LoginHelperAddEditAccountViewModel::class.java]
    }

    @Inject
    lateinit var saveToLocalCoachmark: SaveToLocalCoachmark

    private val pageMode by lazy {
        arguments?.getParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE) as? PageMode
    }

    private val emailText by lazy {
        arguments?.getString(BundleConstants.LOGIN_HELPER_EMAIL, "")
    }

    private val passwordText by lazy {
        arguments?.getString(BundleConstants.LOGIN_HELPER_PASSWORD, "")
    }

    private val tribeText by lazy {
        arguments?.getString(BundleConstants.LOGIN_HELPER_TRIBE, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginHelperAddEditAccountBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEnvValue()
        binding?.apply {
            when (pageMode) {
                PageMode.ADD -> {
                    header.setUpHeader(
                        context?.resources?.getString(R.string.login_helper_btn_add_account)
                            .toBlankOrString()
                    )
                    addGroup.show()
                    btnSaveToDb.isEnabled = false
                    setUpCheckBoxListener()
                    saveToLocalCoachmark.showReplyBubbleOnBoarding(btnSaveToLocal, context)
                }
                PageMode.EDIT -> {
                    header.setUpHeader(
                        context?.resources?.getString(R.string.login_helper_btn_edit_account)
                            .toBlankOrString()
                    )
                    btnSaveToLocal.hide()
                    btnSaveToDb.show()
                    addGroup.hide()
                    setUpTextFields()
                }
            }
        }
        binding?.setUpButtonClickListeners()
        observeUiAction()
    }

    private fun setEnvValue() {
        val currentEnv = TokopediaUrl.getInstance().TYPE
        if (Env.STAGING == currentEnv) {
            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.ChangeEnvType(LoginHelperEnvType.STAGING)
            )
        } else {
            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION)
            )
        }
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiAction.collect { action ->
                handleAction(action)
            }
        }
    }

    private fun handleAction(action: LoginHelperAddEditAccountAction) {
        when (action) {
            is LoginHelperAddEditAccountAction.TapBackAction -> {
                goToAccountSettingsScreen()
            }
            is LoginHelperAddEditAccountAction.GoToLoginHelperHome -> {
                binding?.btnSaveToLocal?.isLoading = false
                binding?.btnSaveToDb?.isLoading = false
                showToastGoToHomePage()
            }
        }
    }

    private fun goToAccountSettingsScreen() {
        val intent = Intent(activity, LoginHelperAccountSettingsActivity::class.java)
        startActivity(intent)
    }

    private fun showToastGoToHomePage() {
        context?.resources?.let {
            binding?.footer?.showToaster(
                it.getString(R.string.login_helper_save_to_local_successful),
                it.getString(R.string.login_helper_save_to_local_go_to_home)
            ) {
                RouteManager.route(context, ApplinkConstInternalGlobal.LOGIN_HELPER)
            }
        }
    }

    private fun HeaderUnify.setUpHeader(headerTitle: String) {
        title = headerTitle
        setNavigationOnClickListener {
            viewModel.processEvent(LoginHelperAddEditAccountEvent.TapBackButton)
        }
    }

    private fun FragmentLoginHelperAddEditAccountBinding.setUpCheckBoxListener() {
        consentCheckbox.setOnCheckedChangeListener { _, state ->
            btnSaveToDb.isEnabled = state
        }
    }

    private fun FragmentLoginHelperAddEditAccountBinding.setUpButtonClickListeners() {
        btnSaveToDb.setOnClickListener {
            hideKeyboard()
            val email = binding?.etUsername?.editText?.text.toString()
            val password = binding?.etPassword?.editText?.text.toString()
            val tribe = binding?.etTribe?.editText?.text.toString()
            binding?.btnSaveToDb?.isLoading = true
            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.AddUserToRemoteDB(
                    email,
                    password,
                    tribe
                )
            )
        }

        btnSaveToLocal.setOnClickListener {
            hideKeyboard()
            val email = binding?.etUsername?.editText?.text.toString()
            val password = binding?.etPassword?.editText?.text.toString()
            binding?.btnSaveToLocal?.isLoading = true
            viewModel.processEvent(LoginHelperAddEditAccountEvent.AddUserToLocalDB(email, password))
        }
    }

    private fun FragmentLoginHelperAddEditAccountBinding.setUpTextFields() {
        etUsername.editText.setText(emailText)
        etPassword.editText.setText(passwordText)
        etTribe.editText.setText(tribeText)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.root?.windowToken, 0)
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(R.string.login_helper_add_edit_header_title)
            .toBlankOrString()
    }

    override fun initInjector() {
        DaggerLoginHelperComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToLocalCoachmark.dismiss()
        saveToLocalCoachmark.flush()
    }

    companion object {
        fun newInstance(pageMode: PageMode, email: String?, password: String?, tribe: String?): LoginHelperAddEditAccountFragment {
            return LoginHelperAddEditAccountFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE, pageMode)
                    putString(BundleConstants.LOGIN_HELPER_EMAIL, email)
                    putString(BundleConstants.LOGIN_HELPER_PASSWORD, password)
                    putString(BundleConstants.LOGIN_HELPER_TRIBE, tribe)
                }
            }
        }
    }
}
