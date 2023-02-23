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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.login_helper.databinding.FragmentLoginHelperBinding
import com.tokopedia.login_helper.di.component.DaggerLoginHelperComponent
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.presentation.viewmodel.LoginHelperViewModel
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
        setEnvValue()

        binding?.apply {
            loginBtn.setOnClickListener {
                viewModel.processEvent(LoginHelperEvent.LoginUser(
                    "pbs-bagas.priyadi+01@tokopedia.com",
                    "toped123"
                ))
            }
        }
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
        handleLoginToken(state.loginToken)
        handleProfileResponse(state.profilePojo)
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

    private fun handleLoader(shouldShow: Boolean) {
        binding?.apply {
            loginDataLoader.showWithCondition(shouldShow)
        }
    }

    private fun handleLoginToken(loginToken: Result<LoginToken>?) {
        when(loginToken) {
           is Success -> {
               handleLoginTokenSuccess(loginToken.data)
           }
            is Fail -> {
                handleLoginTokenFailure(loginToken.throwable)
            }
        }
    }

    private fun handleLoginTokenSuccess(data: LoginToken) {
        //Call for user Profile
        view?.let { Toaster.build(it,data.toString(), Toaster.LENGTH_LONG).show() }
        viewModel.getUserInfo()
    }

    private fun handleLoginTokenFailure(throwable: Throwable) {
        view?.let { Toaster.build(it,"Failure", Toaster.LENGTH_LONG).show() }
    }

    private fun handleProfileResponse(profilePojo: Result<ProfilePojo>?) {
        when(profilePojo) {
            is Success -> {
                handleProfileResponseSuccess(profilePojo.data)
            }
            is Fail -> {
                handleProfileResponseFailure(profilePojo.throwable)
            }
        }
    }

    private fun handleProfileResponseSuccess(data: ProfilePojo) {
        view?.let { Toaster.build(it,data.toString(), Toaster.LENGTH_LONG).show() }
    }

    private fun handleProfileResponseFailure(throwable: Throwable) {
        view?.let { Toaster.build(it,throwable.message.toString(), Toaster.LENGTH_LONG).show() }
    }


    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.login_helper.R.string.login_helper_header_title)
            .toBlankOrString()
    }

    private fun backToPreviousScreen() {
        activity?.finish()
    }

    override fun initInjector() {
        DaggerLoginHelperComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    private fun setEnvValue() {
        val currentEnv = getInstance().TYPE
        if(Env.STAGING == currentEnv) {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
        } else {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION))
            setEnvTypeChip(LoginHelperEnvType.PRODUCTION)
        }
    }

    companion object {
        fun newInstance() = LoginHelperFragment()
    }
}
