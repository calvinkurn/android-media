package com.tokopedia.login_helper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.login_helper.databinding.FragmentLoginHelperBinding
import com.tokopedia.login_helper.di.component.DaggerLoginHelperComponent
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.presentation.viewmodel.LoginHelperViewModel
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoginHelperFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentLoginHelperBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(LoginHelperViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginHelperBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            setUpView()
            handleChipClick()
        }
        observeUiState()
        observeUiAction()
        viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
    }

    override fun initInjector() {
        DaggerLoginHelperComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                    state ->
                handleUiState(state)
            }
        }
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiAction.collect {
                    action ->
                handleAction(action)
            }
        }
    }

    private fun handleUiState(state: LoginHelperUiState) {
        setEnvTypeChip(state.envType)
    }

    private fun handleAction(action: LoginHelperAction) {
        when (action) {
            is LoginHelperAction.TapBackAction -> backToPreviousScreen()
        }
    }

    private fun setEnvTypeChip(envType: LoginHelperEnvType) {
        binding?.apply {
            when (envType) {
                is LoginHelperEnvType.STAGING -> {
                    loginHelperChipStaging.chipType = ChipsUnify.TYPE_SELECTED
                    loginHelperChipProd.chipType = ChipsUnify.TYPE_NORMAL
                }
                is LoginHelperEnvType.PRODUCTION -> {
                    loginHelperChipProd.chipType = ChipsUnify.TYPE_SELECTED
                    loginHelperChipStaging.chipType = ChipsUnify.TYPE_NORMAL
                }
            }
        }
    }

    private fun FragmentLoginHelperBinding.setUpView() {
        header.setUpHeader()
    }

    private fun FragmentLoginHelperBinding.handleChipClick() {
        loginHelperChipStaging.setOnClickListener {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
        }
        loginHelperChipProd.setOnClickListener {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION))
        }
    }

    private fun HeaderUnify.setUpHeader() {
        title =
            context?.resources?.getString(com.tokopedia.login_helper.R.string.login_helper_header_title)
                .toBlankOrString()
        setNavigationOnClickListener {
            viewModel.processEvent(LoginHelperEvent.TapBackButton)
        }
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.login_helper.R.string.login_helper_header_title)
            .toBlankOrString()
    }

    private fun backToPreviousScreen() {
        activity?.finish()
    }

    companion object {
        fun newInstance() = LoginHelperFragment()
    }
}
