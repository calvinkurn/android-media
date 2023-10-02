package com.tokopedia.loginHelper.presentation.home.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.domain.LoginHelperDataSourceType
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.presentation.home.adapter.LoginHelperRecyclerAdapter
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginHelperClickListener
import com.tokopedia.loginHelper.presentation.home.viewmodel.LoginHelperViewModel
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperAction
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperEvent
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperUiState
import com.tokopedia.loginHelper.util.exception.NoUserInfoException
import com.tokopedia.loginHelper.util.showToaster
import com.tokopedia.loginHelper.util.showToasterError
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class LoginHelperFragment : BaseDaggerFragment(), LoginHelperClickListener {

    private var binding by autoClearedNullable<FragmentLoginHelperBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var loginHelperAdapter: LoginHelperRecyclerAdapter? = null

    private val viewModel: LoginHelperViewModel by viewModels { viewModelFactory }

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
            setUpDataSourceSwitcher()
        }
        setUpClickListener()
        observeUiState()
        observeUiAction()
        setEnvValue()
        viewModel.processEvent(LoginHelperEvent.GetRemoteLoginData)
    }

    private fun setUpClickListener() {
        binding?.btnGoToSettings?.setOnClickListener {
            viewModel.processEvent(LoginHelperEvent.GoToAccountsSetting)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
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

    private fun handleUiState(state: LoginHelperUiState) {
        setEnvTypeChip(state.envType)
        handleLoginUserDataList(state)
        handleLoginToken(state.loginToken)
        handleProfileResponse(state.profilePojo)
        handleLoadingState(state.isLoading)
    }

    private fun handleAction(action: LoginHelperAction) {
        when (action) {
            is LoginHelperAction.TapBackAction -> backToPreviousScreen()
            is LoginHelperAction.GoToLoginPage -> goToLoginPage()
            is LoginHelperAction.GoToAccountSettings -> goToLoginHelperAccountSettings()
        }
    }

    private fun handleLoadingState(state: Boolean) {
        binding?.progressBar?.apply {
            showWithCondition(state)
            bringToFront()
        }
    }

    private fun goToLoginPage() {
        RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGIN)
    }

    private fun goToLoginHelperAccountSettings() {
        RouteManager.route(context, ApplinkConstInternalGlobal.LOGIN_HELPER_ACCOUNTS_SETTINGS)
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
            setLoginDataFromAssets(envType)
        }
    }

    private fun setLoginDataFromAssets(envType: LoginHelperEnvType) {
        val fileName = if (envType is LoginHelperEnvType.STAGING) {
            "user_details_staging.json"
        } else {
            "user_details_prod.json"
        }
        var jsonString = ""
        context?.let { context ->
            try {
                jsonString = context.assets.open(fileName)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                FirebaseCrashlytics.getInstance().recordException(ioException)
            }
        }
        val loginUserDetails = Gson().fromJson(jsonString, LoginDataResponse::class.java)
        viewModel.processEvent(LoginHelperEvent.GetLocalLoginData(loginUserDetails))
    }

    private fun FragmentLoginHelperBinding.setUpView() {
        header.setUpHeader()
    }

    private fun FragmentLoginHelperBinding.handleChipClick() {
        val currentEnv = getInstance().TYPE
        loginHelperChipStaging.setOnClickListener {
            if (currentEnv == Env.STAGING) {
                showEnvironmentChipToast()
                return@setOnClickListener
            }
            changeEnvForApp(LoginHelperEnvType.STAGING)
        }
        loginHelperChipProd.setOnClickListener {
            if (currentEnv == Env.LIVE) {
                showEnvironmentChipToast()
                return@setOnClickListener
            }
            changeEnvForApp(LoginHelperEnvType.PRODUCTION)
        }
    }

    private fun FragmentLoginHelperBinding.showEnvironmentChipToast() {
        context?.resources?.let {
            footer.showToaster(
                getString(
                    com.tokopedia.loginHelper.R.string.login_helper_warning_chip_click
                ).toBlankOrString()
            )
        }
    }

    private fun FragmentLoginHelperBinding.bindSearch() {
        searchBar.searchBarTextField.addTextChangedListener(getTextListener())
    }

    private fun HeaderUnify.setUpHeader() {
        title =
            context?.resources?.getString(
                com.tokopedia.loginHelper.R.string.login_helper_header_title
            )
                .toBlankOrString()
        setNavigationOnClickListener {
            viewModel.processEvent(LoginHelperEvent.TapBackButton)
        }
    }

    private fun FragmentLoginHelperBinding.setUpDataSourceSwitcher() {
        dataSourceSelector.isChecked = true
        dataSourceSelector.setOnClickListener {
            viewModel.processEvent(LoginHelperEvent.QueryEmail(""))
            searchBar.searchBarTextField.text?.clear()
            globalError.hide()
            val isChecked = dataSourceSelector.isChecked
            if (isChecked) {
                dataSourceSelector.text =
                    context?.resources?.getString(
                        com.tokopedia.loginHelper.R.string.login_helper_data_fetching_from_remote
                    )
                viewModel.processEvent(
                    LoginHelperEvent.ChangeDataSourceType(
                        LoginHelperDataSourceType.REMOTE
                    )
                )
            } else {
                dataSourceSelector.text =
                    context?.resources?.getString(
                        com.tokopedia.loginHelper.R.string.login_helper_data_fetching_from_local
                    )
                viewModel.processEvent(
                    LoginHelperEvent.ChangeDataSourceType(
                        LoginHelperDataSourceType.LOCAL
                    )
                )
            }
        }
    }

    private fun changeEnvForApp(envType: LoginHelperEnvType) {
        val currentEnvType = if (envType == LoginHelperEnvType.STAGING) {
            Env.STAGING
        } else {
            Env.LIVE
        }
        context?.let {
            TokopediaUrl.setEnvironment(it, currentEnvType)
            TokopediaUrl.deleteInstance()
            TokopediaUrl.init(it)
            viewModel.processEvent(LoginHelperEvent.LogOutUser)
            binding?.footer?.showToasterError(
                context?.resources?.getString(
                    com.tokopedia.loginHelper.R.string.login_helper_app_restart
                )
                    .toBlankOrString()
            )
        }
    }

    private fun handleLoader(shouldShow: Boolean) {
        binding?.apply {
            loginDataLoader.showWithCondition(shouldShow)
            loginDataLoader.bringToFront()
        }
    }

    private fun handleLoginUserDataList(state: LoginHelperUiState) {
        // Edge case for handling success in Persistence Cache with empty user list, and empty user list from remote
        when (state.loginDataList) {
            is Success -> {
                val userData = state.loginDataList.data
                if (userData.persistentCacheUserData?.users?.isEmpty() == true && (userData.remoteUserData?.users == null || userData.remoteUserData.users.isEmpty())) {
                    handleLoginUserDataListFailure(NoUserInfoException(), state.dataSourceType)
                }
            }
            else -> {}
        }

        if (state.dataSourceType == LoginHelperDataSourceType.REMOTE) {
            if (state.searchText.isEmpty()) {
                fillRecyclerViewData(
                    state.searchText,
                    state.loginDataList,
                    state.dataSourceType
                )
            } else {
                fillRecyclerViewData(
                    state.searchText,
                    state.filteredUserList,
                    state.dataSourceType
                )
            }
        } else {
            if (state.searchText.isEmpty()) {
                fillRecyclerViewData(
                    state.searchText,
                    state.localLoginDataList,
                    state.dataSourceType
                )
            } else {
                fillRecyclerViewData(
                    state.searchText,
                    state.localFilteredLoginDataList,
                    state.dataSourceType
                )
            }
        }
    }

    private fun fillRecyclerViewData(
        searchText: String,
        loginDataList: Result<UnifiedLoginHelperData>?,
        dataSourceType: LoginHelperDataSourceType
    ) {
        if (searchText.isEmpty()) {
            when (val loginDataList = loginDataList) {
                is Success -> {
                    if (dataSourceType == LoginHelperDataSourceType.REMOTE) {
                        handleLoginUserDataListSuccess(loginDataList.data)
                    } else {
                        handleLoginUserDataListSuccess(loginDataList.data)
                    }
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable, dataSourceType)
                }
                else -> {
                    // no op
                }
            }
        } else {
            when (val loginDataList = loginDataList) {
                is Success -> {
                    handleLoginUserDataListSuccess(loginDataList.data)
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable, dataSourceType)
                }
                else -> {
                    // no op
                }
            }
        }
    }

    private fun handleLoginUserDataListSuccess(
        loginDataList: UnifiedLoginHelperData
    ) {
        loginHelperAdapter = LoginHelperRecyclerAdapter(this@LoginHelperFragment)

        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.userList?.layoutManager = layoutManager
        loginHelperAdapter?.clearAllElements()
        loginHelperAdapter?.addData(loginDataList)
        binding?.userList?.apply {
            adapter = loginHelperAdapter
        }
    }

    private fun handleLoginUserDataListFailure(
        throwable: Throwable,
        dataSourceType: LoginHelperDataSourceType
    ) {
        viewModel.processEvent(LoginHelperEvent.HandleLoader(false))
        if (dataSourceType == LoginHelperDataSourceType.REMOTE) {
            binding?.footer?.showToasterError(throwable.message.toBlankOrString())
            binding?.globalError?.apply {
                errorTitle.text =
                    context?.resources?.getString(
                        com.tokopedia.loginHelper.R.string.login_helper_empty_data_title
                    )
                errorDescription.text =
                    context?.resources?.getString(
                        com.tokopedia.loginHelper.R.string.login_helper_empty_data_description
                    )
                setActionClickListener {
                    viewModel.processEvent(LoginHelperEvent.GetRemoteLoginData)
                }
                show()
            }
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
            else -> {
                // no op
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
            else -> {
                // no op
            }
        }
    }

    private fun handleProfileResponseSuccess(profilePojo: Success<ProfilePojo>) {
        handleLoader(shouldShow = false)
        binding?.footer?.showToaster(profilePojo.data.profileInfo.email)
        backToPreviousScreen()
    }

    private fun handleFailure(throwable: Throwable) {
        handleLoader(shouldShow = false)
        binding?.footer?.showToasterError(throwable.message.toString(), "Go to Login") {
            RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGIN)
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        backToPreviousScreen()
        return true
    }

    private fun backToPreviousScreen() {
        val state = RouteManager.route(context, ApplinkConst.HOME)
        if (!state) {
            activity?.finish()
        }
    }

    private fun setEnvValue() {
        val currentEnv = getInstance().TYPE
        if (Env.STAGING == currentEnv) {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
        } else {
            viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION))
        }
    }

    override fun onClickUserData(data: UserDataUiModel?) {
        handleLoader(shouldShow = true)
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                viewModel.processEvent(LoginHelperEvent.QueryEmail(getMessage()))
            }
        }
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(
            com.tokopedia.loginHelper.R.string.login_helper_header_title
        )
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
        fun newInstance() = LoginHelperFragment()
    }
}
