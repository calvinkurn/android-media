package com.tokopedia.loginHelper.presentation.searchAccount.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.databinding.FragmentLoginHelperSearchAccountBinding
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.LoginHelperSearchAdapter
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener.LoginHelperSearchListener
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.LoginHelperSearchAccountViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class LoginHelperSearchAccountFragment : BaseDaggerFragment(), LoginHelperSearchListener {

    private var binding by autoClearedNullable<FragmentLoginHelperSearchAccountBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginHelperSearchAccountViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[LoginHelperSearchAccountViewModel::class.java]
    }

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
        binding?.apply {
            header.setUpHeader(
                context?.resources?.getString(R.string.login_helper_btn_search_account)
                    .toBlankOrString()
            )
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        loginHelperAdapter = LoginHelperSearchAdapter(this@LoginHelperSearchAccountFragment)

        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.userList?.layoutManager = layoutManager
        val loginDataUiModel = mutableListOf<UserDataUiModel>()
        loginDataUiModel.add(UserDataUiModel("abc", "asdf", "asdasd"))
        loginDataUiModel.add(UserDataUiModel("abc 2", "asdf 2", "asdasd"))
        loginDataUiModel.add(UserDataUiModel("abc 3", "asdf 3", "asdasd"))
        loginHelperAdapter?.addDataList(loginDataUiModel)

        binding?.userList?.apply {
            adapter = loginHelperAdapter
        }
    }

    private fun HeaderUnify.setUpHeader(headerTitle: String) {
        title = headerTitle
        setNavigationOnClickListener {
            //     viewModel.processEvent(LoginHelperAddEditAccountEvent.TapBackButton)
        }
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
        Log.d("FATAL", "onEditAccunt: edit called")
    }

    override fun onDeleteAccount(user: UserDataUiModel) {
        context?.let { viewContext ->
            viewContext.resources.let {
                DialogUnify(viewContext, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(it.getString(R.string.login_helper_account_removal))
                    setDescription(String.format(it.getString(R.string.login_helper_confirm_removal), user.email))
                    setPrimaryCTAText(it.getString(R.string.login_helper_btn_remove_account))
                    setSecondaryCTAText(it.getString(R.string.login_helper_btn_remove_account_cancel))
                    setPrimaryCTAClickListener {
                    //    viewModel.removeDatabase(databaseDescriptor)
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
}
