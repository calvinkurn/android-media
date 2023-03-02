package com.tokopedia.login_helper.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.login_helper.databinding.FragmentLoginHelperBinding
import com.tokopedia.login_helper.di.component.DaggerLoginHelperComponent
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel
import com.tokopedia.login_helper.presentation.adapter.LoginHelperRecyclerAdapter
import com.tokopedia.login_helper.presentation.adapter.viewholder.LoginHelperClickListener
import com.tokopedia.login_helper.presentation.viewmodel.LoginHelperViewModel
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import com.tokopedia.login_helper.util.showToaster
import com.tokopedia.login_helper.util.showToasterError
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoginHelperFragment : BaseDaggerFragment(), LoginHelperClickListener {

    private var binding by autoClearedNullable<FragmentLoginHelperBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var loginHelperAdapter: LoginHelperRecyclerAdapter? = null

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
            bindSearch()
        }
        observeUiState()
        observeUiAction()
        setEnvValue()
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
        handleLoginUserDataList(state)
        handleLoginToken(state.loginToken)
        handleProfileResponse(state.profilePojo)
    }

    private fun handleAction(action: LoginHelperAction) {
        when (action) {
            is LoginHelperAction.TapBackAction -> backToPreviousScreen()
            is LoginHelperAction.GoToLoginPage -> goToLoginPage()
        }
    }

    private fun goToLoginPage() {
        RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGIN)
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
            showChipToasterError()
        }
        loginHelperChipProd.setOnClickListener {
            showChipToasterError()
        }
    }

    private fun FragmentLoginHelperBinding.showChipToasterError() {
        footer.showToaster(
            context?.resources?.getString(com.tokopedia.login_helper.R.string.login_helper_warning_chip_click)
                .toBlankOrString()
        )
    }

    private fun FragmentLoginHelperBinding.bindSearch() {
        searchBar.searchBarTextField.addTextChangedListener(getTextListener())
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

    private fun handleLoginUserDataList(state: LoginHelperUiState) {
        if (state.searchText.isEmpty()) {
            when (val loginDataList = state.loginDataList) {
                is Success -> {
                    handleLoginUserDataListSuccess(loginDataList.data)
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable)
                }
            }
        } else {
            when (val loginDataList = state.filteredUserList) {
                is Success -> {
                    handleLoginUserDataListSuccess(loginDataList.data)
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable)
                }
            }
        }
    }

    private fun handleLoginUserDataListSuccess(loginDataList: LoginDataUiModel) {
        loginHelperAdapter = LoginHelperRecyclerAdapter(this@LoginHelperFragment)

        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.userList?.layoutManager = layoutManager
        loginHelperAdapter?.clearAllElements()
        loginHelperAdapter?.addElement(loginDataList.count)
        loginHelperAdapter?.addElement(loginDataList.users)

        binding?.userList?.apply {
            adapter = loginHelperAdapter
        }
    }

    private fun handleLoginUserDataListFailure(throwable: Throwable) {
        binding?.globalError?.run {
            setActionClickListener {
                viewModel.processEvent(LoginHelperEvent.GetLoginData)
            }
            show()
        }
    }

    private fun handleLoginToken(loginToken: Result<LoginToken>?) {
        when (loginToken) {
            is Success -> {
                handleLoginTokenSuccess()
            }
            is Fail -> {
                handleFailure(loginToken.throwable)
            }
        }
    }

    private fun handleLoginTokenSuccess() {
        viewModel.processEvent(LoginHelperEvent.GetUserInfo)
    }

    private fun handleProfileResponse(profilePojo: Result<ProfilePojo>?) {
        when (profilePojo) {
            is Success -> {
                handleProfileResponseSuccess(profilePojo)
            }
            is Fail -> {
                handleFailure(profilePojo.throwable)
            }
        }
    }

    private fun handleProfileResponseSuccess(profilePojo: Success<ProfilePojo>) {
        binding?.footer?.showToaster(profilePojo.data.profileInfo.email)
        backToPreviousScreen()
    }

    private fun handleFailure(throwable: Throwable) {
        binding?.footer?.showToasterError(throwable.message.toString(), "Go to Login") {
            RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGIN)
        }
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
        if (Env.STAGING == currentEnv) {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
        } else {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION))
        }
    }

    companion object {
        fun newInstance() = LoginHelperFragment()
    }

    override fun onClickUserData(data: UserDataUiModel?) {
        Toast.makeText(context, "Logging in with ${data?.email}", Toast.LENGTH_SHORT).show()
        viewModel.processEvent(
            LoginHelperEvent.LoginUser(
                data?.email.toBlankOrString(),
                data?.password.toBlankOrString()
            )
        )
    }

    fun getMessage(): String {
        return binding?.searchBar?.searchBarTextField?.text?.toString().toBlankOrString()
    }

    private fun getTextListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.processEvent(LoginHelperEvent.QueryEmail(getMessage()))
            }
        }
    }
}
