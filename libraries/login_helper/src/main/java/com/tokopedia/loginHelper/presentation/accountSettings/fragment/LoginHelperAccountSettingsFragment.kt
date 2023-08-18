package com.tokopedia.loginHelper.presentation.accountSettings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperAccountSettingsBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.LoginHelperAccountSettingsViewModel
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsAction
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsEvent
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginHelperAccountSettingsFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentLoginHelperAccountSettingsBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperAccountSettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginHelperAccountSettingsBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            setUpHeader()
            setUpClickListeners()
        }

        observeUiAction()
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiAction.collect { action ->
                    handleAction(action)
                }
            }
        }
    }

    private fun handleAction(action: LoginHelperAccountSettingsAction) {
        when (action) {
            is LoginHelperAccountSettingsAction.RouteToPage -> goToPage(action.route)
        }
    }

    private fun goToPage(route: String) {
        RouteManager.route(context, route)
    }

    override fun onFragmentBackPressed(): Boolean {
        goToPage(ApplinkConstInternalGlobal.LOGIN_HELPER)
        return true
    }

    private fun FragmentLoginHelperAccountSettingsBinding.setUpHeader() {
        header.setUpHeader()
        val currentEnv = getInstance().TYPE
        loginHelperTips.description = String.format(
            context?.resources?.getString(R.string.login_helper_add_account_tips)
                .toBlankOrString(),
            currentEnv.value,
            currentEnv.value
        )
    }

    private fun FragmentLoginHelperAccountSettingsBinding.setUpClickListeners() {
        btnAddAccount.setOnClickListener {
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToAddAccount)
        }

        btnEditAccount.setOnClickListener {
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToEditAccount)
        }

        btnRemoveAccount.setOnClickListener {
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToDeleteAccount)
        }
    }

    private fun HeaderUnify.setUpHeader() {
        title =
            context?.resources?.getString(R.string.login_helper_accounts_settings)
                .toBlankOrString()
        setNavigationOnClickListener {
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToLoginHelperHome)
        }
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(R.string.login_helper_accounts_header_title)
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

    companion object {
        fun newInstance() = LoginHelperAccountSettingsFragment()
    }
}
