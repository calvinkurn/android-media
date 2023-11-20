package com.tokopedia.loginHelper.presentation.searchAccount.fragment

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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperSearchAccountBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.presentation.addEditAccount.LoginHelperAddEditAccountActivity
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.LoginHelperSearchAdapter
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener.LoginHelperSearchListener
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.LoginHelperSearchAccountViewModel
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountAction
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountEvent
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountUiState
import com.tokopedia.loginHelper.util.showToasterError
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class LoginHelperSearchAccountFragment : BaseDaggerFragment(), LoginHelperSearchListener {

    private var binding by autoClearedNullable<FragmentLoginHelperSearchAccountBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperSearchAccountViewModel by viewModels { viewModelFactory }

    private var loginHelperAdapter: LoginHelperSearchAdapter? = null

    override fun getScreenName(): String {
        return context?.resources?.getString(
            R.string.login_helper_search_header_title
        )
            .toBlankOrString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginHelperSearchAccountBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEnvValue()
        binding?.apply {
            header.setUpHeader(
                context?.resources?.getString(R.string.login_helper_btn_search_account)
                    .toBlankOrString()
            )
            bindSearch()
        }

        observeUiState()
        observeUiAction()
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

    private fun handleAction(action: LoginHelperSearchAccountAction) {
        when (action) {
            is LoginHelperSearchAccountAction.TapBackSearchAccountAction -> {
                handleBackButtonPress()
            }
            is LoginHelperSearchAccountAction.OnSuccessDeleteAccountAction -> {
                RouteManager.route(context, ApplinkConstInternalGlobal.LOGIN_HELPER)
            }
            is LoginHelperSearchAccountAction.OnFailureDeleteAccountAction -> {
                showErrorOnFailure()
            }
        }
    }

    private fun showErrorOnFailure() {
        context?.resources?.let {
            binding?.footer?.showToasterError(
                it.getString(R.string.login_helper_delete_from_remote_failure),
                it.getString(R.string.login_helper_save_to_local_go_to_home)
            ) {
                RouteManager.route(context, ApplinkConstInternalGlobal.LOGIN_HELPER)
            }
        }
    }

    private fun handleUiState(state: LoginHelperSearchAccountUiState) {
        handleLoginUserDataList(state)
        handleLoader(state.isLoading)
    }

    private fun handleLoginUserDataList(state: LoginHelperSearchAccountUiState) {
        if (state.searchText.isEmpty()) {
            when (val loginDataList = state.loginDataList) {
                is Success -> {
                    initRecyclerView(loginDataList.data)
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable)
                }
                else -> {}
            }
        } else {
            when (val loginDataList = state.filteredUserList) {
                is Success -> {
                    initRecyclerView(loginDataList.data)
                }
                is Fail -> {
                    handleLoginUserDataListFailure(loginDataList.throwable)
                }
                else -> {}
            }
        }
    }

    private fun setEnvValue() {
        val currentEnv = TokopediaUrl.getInstance().TYPE
        if (Env.STAGING == currentEnv) {
            viewModel.processEvent(
                LoginHelperSearchAccountEvent.SetEnvType(LoginHelperEnvType.STAGING)
            )
        } else {
            viewModel.processEvent(
                LoginHelperSearchAccountEvent.SetEnvType(LoginHelperEnvType.PRODUCTION)
            )
        }
        setLoginDataFromAssets(viewModel.uiState.value.envType)
    }

    private fun FragmentLoginHelperSearchAccountBinding.bindSearch() {
        searchBar.searchBarTextField.addTextChangedListener(getTextListener())
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
        viewModel.processEvent(
            LoginHelperSearchAccountEvent.GetUserData(loginUserDetails)
        )
    }

    private fun initRecyclerView(loginDataList: LoginDataUiModel) {
        loginHelperAdapter = LoginHelperSearchAdapter(this@LoginHelperSearchAccountFragment)

        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.userList?.layoutManager = layoutManager
        loginDataList.users?.let { loginHelperAdapter?.addDataList(it) }

        binding?.userList?.apply {
            adapter = loginHelperAdapter
        }
    }

    private fun handleLoginUserDataListFailure(throwable: Throwable) {
        handleLoader(shouldShow = false)
        binding?.globalError?.run {
            show()
        }
        binding?.footer?.showToasterError(throwable.message.toBlankOrString())
    }

    private fun handleLoader(shouldShow: Boolean) {
        binding?.apply {
            loginDataLoader.showWithCondition(shouldShow)
            loginDataLoader.bringToFront()
        }
    }

    private fun HeaderUnify.setUpHeader(headerTitle: String) {
        title = headerTitle
        setNavigationOnClickListener {
            viewModel.processEvent(LoginHelperSearchAccountEvent.TapBackButton)
        }
    }

    private fun handleBackButtonPress() {
        RouteManager.route(context, ApplinkConstInternalGlobal.LOGIN_HELPER_ACCOUNTS_SETTINGS)
    }

    override fun onFragmentBackPressed(): Boolean {
        handleBackButtonPress()
        return true
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
        @JvmStatic
        fun newInstance(): LoginHelperSearchAccountFragment {
            return LoginHelperSearchAccountFragment()
        }
    }

    override fun onEditAccount(user: UserDataUiModel) {
        context?.let {
            LoginHelperAddEditAccountActivity.buildEditAccountModeIntent(
                it,
                user.email,
                user.password,
                user.tribe,
                user.id ?: 0
            )
        }
    }

    override fun onDeleteAccount(user: UserDataUiModel) {
        context?.let { viewContext ->
            viewContext.resources.let {
                DialogUnify(viewContext, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(it.getString(R.string.login_helper_account_removal))
                    setDescription(
                        String.format(
                            it.getString(R.string.login_helper_confirm_removal),
                            user.email
                        )
                    )
                    setPrimaryCTAText(it.getString(R.string.login_helper_btn_remove_account))
                    setSecondaryCTAText(
                        it.getString(R.string.login_helper_btn_remove_account_cancel)
                    )
                    setPrimaryCTAClickListener {
                        user.id?.let {
                            viewModel.processEvent(
                                LoginHelperSearchAccountEvent.DeleteUserDetailsFromRemote(
                                    user.id
                                )
                            )
                        }
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        dismiss()
                    }
                    show()
                }
            }
        }
    }

    fun getMessage(): String {
        return binding?.searchBar?.searchBarTextField?.text?.toString().toBlankOrString()
    }

    private fun getTextListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                viewModel.processEvent(LoginHelperSearchAccountEvent.QueryEmail(getMessage()))
            }
        }
    }
}
