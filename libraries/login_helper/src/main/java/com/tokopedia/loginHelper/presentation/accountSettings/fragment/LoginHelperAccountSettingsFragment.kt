package com.tokopedia.loginHelper.presentation.accountSettings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperAccountSettingsBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.LoginHelperAccountSettingsViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class LoginHelperAccountSettingsFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentLoginHelperAccountSettingsBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperAccountSettingsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[LoginHelperAccountSettingsViewModel::class.java]
    }

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
    }

    private fun FragmentLoginHelperAccountSettingsBinding.setUpHeader() {
        header.setUpHeader()
        loginHelperTips.description = String.format( context?.resources?.getString(com.tokopedia.loginHelper.R.string.login_helper_add_account_tips)
            .toBlankOrString(), "ABC" , "ABC" )
    }

    private fun FragmentLoginHelperAccountSettingsBinding.setUpClickListeners() {
        btnAddAccount.setOnClickListener {

        }

        btnEditAccount.setOnClickListener {

        }

        btnRemoveAccount.setOnClickListener {

        }
    }

    private fun HeaderUnify.setUpHeader() {
        title =
            context?.resources?.getString(com.tokopedia.loginHelper.R.string.login_helper_accounts_settings)
                .toBlankOrString()
        setNavigationOnClickListener {
          //  viewModel.processEvent(LoginHelperEvent.TapBackButton)
        }
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.loginHelper.R.string.login_helper_accounts_header_title)
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
