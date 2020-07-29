package com.tokopedia.payment.setting.authenticate.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardComponent
import com.tokopedia.payment.setting.authenticate.di.DaggerAuthenticateCreditCardComponent
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCCAdapterFactory
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCreditCardAdapter
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCContract
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCPresenter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_authenticate_credit_card.*
import javax.inject.Inject

class AuthenticateCreditCardFragment : BaseListFragment<TypeAuthenticateCreditCard,
        AuthenticateCCAdapterFactory>(), AuthenticateCCContract.View {

    @Inject
    lateinit var authenticateCCPresenter: AuthenticateCCPresenter

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authenticate_credit_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUse.setOnClickListener {
            authenticateCCPresenter.updateWhiteList((adapter as AuthenticateCreditCardAdapter).getSelectedState(),
                    true, null)
        }
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 ->
                dividerItemDecoration.setDrawable(it1)
            }
            getRecyclerView(view).addItemDecoration(dividerItemDecoration)
        }
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        updateVisibilityButtonUse()
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

    override fun initInjector() {
        getComponent(AuthenticateCreditCardComponent::class.java).inject(this)
        authenticateCCPresenter.attachView(this)
    }

    override fun onErrorUpdateWhiteList(e: Throwable) {
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

    override fun onResultUpdateWhiteList(checkWhiteListStatus: CheckWhiteListStatus?) {
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

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.hide()
    }

    override fun goToOtpPage(phoneNumber: String) {
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
                    authenticateCCPresenter.updateWhiteList(AuthenticateCCPresenter.SINGLE_AUTH_VALUE,
                            false, uuid)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        authenticateCCPresenter.detachView()
        super.onDestroy()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun loadData(page: Int) {
        authenticateCCPresenter.checkWhiteList()
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
