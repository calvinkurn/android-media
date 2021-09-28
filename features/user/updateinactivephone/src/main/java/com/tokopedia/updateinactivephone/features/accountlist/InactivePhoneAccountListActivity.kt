package com.tokopedia.updateinactivephone.features.accountlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_PHONE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_USER_DETAIL_DATA
import com.tokopedia.updateinactivephone.databinding.ActivityInactivePhoneAccountListBinding
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

open class InactivePhoneAccountListActivity : BaseSimpleActivity(), HasComponent<InactivePhoneComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(InactivePhoneAccountListViewModel::class.java) }

    private var viewBinding: ActivityInactivePhoneAccountListBinding? = null
    private var phoneNUmber = ""

    private var accountListAdapter = AccountListAdapter(object : AccountListViewHolder.Listener {
        override fun onItemClick(userDetailDataModel: AccountListDataModel.UserDetailDataModel) {
            onUserSelected(userDetailDataModel)
        }
    })

    override fun getLayoutRes(): Int = R.layout.activity_inactive_phone_account_list
    override fun getToolbarResourceID(): Int = R.id.toolbarInactivePhoneAccountList

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): InactivePhoneComponent {
        return InactivePhoneComponentBuilder.getComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityInactivePhoneAccountListBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        component.inject(this)

        intent?.extras?.let {
            phoneNUmber = it.getString(PARAM_PHONE).orEmpty()
        }

        initObserver()
        initView()
    }

    private fun initView() {
        updateTitle(getString(R.string.text_title))
        viewBinding?.toolbarInactivePhoneAccountList?.setTitleTextAppearance(this, R.style.BoldToolbar)
        viewBinding?.toolbarInactivePhoneAccountList?.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(viewBinding?.toolbarInactivePhoneAccountList)
        supportActionBar?.apply {
            elevation = 0f
        }

        viewBinding?.recyclerViewAccountList?.apply {
            adapter = accountListAdapter
        }

        showLoading()
        viewModel.getAccountList(phoneNUmber)
    }

    private fun initObserver() {
        viewModel.accountList.observe(this, Observer { result ->
            hideLoading()
            when (result) {
                is Success -> {
                    onGetAccountListSuccess(result.data)
                }
                is Fail -> {
                    onGetAccountListFail(result.throwable)
                }
            }
        })
    }

    private fun onGetAccountListSuccess(accountListDataModel: AccountListDataModel) {
        val accountList = accountListDataModel.accountList.userDetailDataModels

        when {
            accountList.size > 1 -> {
                accountListAdapter.clearAllItems()
                accountListAdapter.addAccountList(accountListDataModel.accountList.userDetailDataModels)
                accountListAdapter.notifyDataSetChanged()
            }
            accountList.size == 1 -> {
                onUserSelected(accountList[0])
            }
            else -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun onUserSelected(userDetailDataModel: AccountListDataModel.UserDetailDataModel) {
        val intent = Intent()
        intent.putExtra(PARAM_USER_DETAIL_DATA, userDetailDataModel)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onGetAccountListFail(throwable: Throwable) {

    }

    private fun showLoading() {
        viewBinding?.loader?.show()
    }

    private fun hideLoading() {
        viewBinding?.loader?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.accountList.removeObservers(this)
    }

    companion object {
        fun createIntent(context: Context, phoneNumber: String): Intent {
            val intent = Intent(context, InactivePhoneAccountListActivity::class.java)
            intent.putExtra(PARAM_PHONE, phoneNumber)
            return intent
        }
    }
}