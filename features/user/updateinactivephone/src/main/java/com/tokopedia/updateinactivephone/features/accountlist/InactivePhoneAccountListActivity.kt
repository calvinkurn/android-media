package com.tokopedia.updateinactivephone.features.accountlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
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
        viewBinding?.toolbarInactivePhoneAccountList?.apply {
            title = getString(R.string.text_title)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }

        viewBinding?.recyclerViewAccountList?.apply {
            adapter = accountListAdapter
        }

        showLoading()
        viewModel.getAccountList(phoneNUmber)
    }

    private fun initObserver() {
        viewModel.accountList.observe(this) {
            hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.accountList.errors.isEmpty()) {
                        onGetAccountListSuccess(it.data)
                    } else {
                        onGetAccountListFail(MessageErrorException(it.data.accountList.errors.first().message))
                    }
                }
                is Fail -> {
                    onGetAccountListFail(it.throwable)
                }
            }
        }
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
        val message = ErrorHandler.getErrorMessage(this, throwable)
        viewBinding?.containerAccountList?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG).show()
        }
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
