package com.tokopedia.payment.setting.authenticate.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.model.AuthException
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCCAdapterFactory
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCreditCardAdapter
import com.tokopedia.payment.setting.authenticate.view.viewmodel.AuthenticateCCViewModel
import com.tokopedia.payment.setting.di.SettingPaymentComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_authenticate_credit_card.*
import javax.inject.Inject

class AuthenticateCreditCardFragment : BaseListFragment<TypeAuthenticateCreditCard,
        AuthenticateCCAdapterFactory>() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: AuthenticateCCViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(AuthenticateCCViewModel::class.java)
    }

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }

    override fun initInjector() {
        getComponent(SettingPaymentComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authenticate_credit_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUse.setOnClickListener {
            showProgressLoading()
            viewModel.updateWhiteList((adapter as AuthenticateCreditCardAdapter).getSelectedState(),
                    true, null)
        }
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 ->
                dividerItemDecoration.setDrawable(it1)
            }
            getRecyclerView(view)?.addItemDecoration(dividerItemDecoration)
        }
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        updateVisibilityButtonUse()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.whiteListResultLiveData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Success -> onCheckWhiteListSuccess(it.data)
                    is Fail -> onUpdateWhiteListError(it.throwable)
                }

        })
        viewModel.whiteListStatusResultLiveData.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Success -> onUpdateWhiteListSuccess(it.data)
                is Fail -> onUpdateWhiteListError(it.throwable)
            }
        })

    }

    private fun onCheckWhiteListSuccess(data: List<TypeAuthenticateCreditCard>) {
        hideProgressLoading()
        renderList(ArrayList(data))
    }

    private fun onUpdateWhiteListSuccess(data: CheckWhiteListStatus) {
        hideProgressLoading()
        onResultUpdateWhiteList(data)
    }

    private fun onUpdateWhiteListError(throwable: Throwable) {
        hideProgressLoading()
        when (throwable) {
            is AuthException.CheckOtpException -> goToOtpPage(throwable.phoneNumber)
            else -> onErrorUpdateWhiteList(throwable)
        }
    }

    override fun getAdapterTypeFactory(): AuthenticateCCAdapterFactory {
        return AuthenticateCCAdapterFactory(this)
    }

    override fun onItemClicked(t: TypeAuthenticateCreditCard?) {
        (adapter as AuthenticateCreditCardAdapter).selectAuth(t)
    }

    override fun createAdapterInstance(): BaseListAdapter<TypeAuthenticateCreditCard, AuthenticateCCAdapterFactory> {
        val authenticateCreditCardAdapter = AuthenticateCreditCardAdapter(adapterTypeFactory)
        authenticateCreditCardAdapter.setOnAdapterInteractionListener(this)
        return authenticateCreditCardAdapter

    }

    override fun getScreenName(): String {
        return ""
    }

    private fun onErrorUpdateWhiteList(e: Throwable) {
        context?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(context, e))
        }
    }

    private fun showSnackbarError(errorMessage: String?) {
        errorMessage?.let {
            view?.let {
                Toaster.make(it, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun onResultUpdateWhiteList(checkWhiteListStatus: CheckWhiteListStatus?) {
        if (checkWhiteListStatus?.statusCode == 200) {
            activity?.setResult(Activity.RESULT_OK)
            checkWhiteListStatus.message?.let { message ->
                view?.let {
                    Toaster.make(it, message, Toaster.LENGTH_SHORT)
                }
            }
        } else {
            showSnackbarError(checkWhiteListStatus?.message)
        }
    }

    override fun renderList(list: MutableList<TypeAuthenticateCreditCard>) {
        super.renderList(list)
        updateVisibilityButtonUse()
    }

    private fun updateVisibilityButtonUse() {
        if (adapter?.data?.size ?: 0 > 0) {
            buttonUse.visibility = View.VISIBLE
        } else {
            buttonUse.visibility = View.GONE
        }
    }

    override fun showGetListError(throwable: Throwable?) {
        super.showGetListError(throwable)
        updateVisibilityButtonUse()
    }

    private fun showProgressLoading() {
        progressDialog.show()
    }

    private fun hideProgressLoading() {
        progressDialog.hide()
    }

    private fun goToOtpPage(phoneNumber: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_VERIFY_AUTH_CREDIT_CARD)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, MODE_SMS)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_OTP_PAYMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OTP_PAYMENT) {
            data?.let {
                if (data.hasExtra(ApplinkConstInternalGlobal.PARAM_UUID)) {
                    val uuid = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_UUID)
                    showProgressLoading()
                    viewModel.updateWhiteList(AuthenticateCCViewModel.SINGLE_AUTH_VALUE,
                            false, uuid)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun loadData(page: Int) {
        context?.let { context ->
            showProgressLoading()
            viewModel.checkWhiteList(
                    context.getString(R.string.payment_authentication_title_1),
                    context.getString(R.string.payment_authentication_description_1),
                    context.getString(R.string.payment_authentication_title_2),
                    context.getString(R.string.payment_authentication_description_2)
            )
        }
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    companion object {
        const val REQUEST_CODE_OTP_PAYMENT = 1273
        const val OTP_TYPE_VERIFY_AUTH_CREDIT_CARD = 122
        const val MODE_SMS = "sms"
        fun createInstance(): AuthenticateCreditCardFragment {
            return AuthenticateCreditCardFragment()
        }
    }
}
