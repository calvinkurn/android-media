package com.tokopedia.payment.setting.authenticate.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.di.DaggerAuthenticateCreditCardComponent
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCCAdapterFactory
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCContract
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCPresenter
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCreditCardAdapter
import com.tokopedia.payment.setting.util.PaymentSettingRouter
import kotlinx.android.synthetic.main.fragment_authenticate_credit_card.*
import javax.inject.Inject

class AuthenticateCreditCardFragment : BaseListFragment<TypeAuthenticateCreditCard, AuthenticateCCAdapterFactory>(),
        AuthenticateCCContract.View {

    @Inject
    lateinit var authenticateCCPresenter: AuthenticateCCPresenter
    var paymentSettingRouter: PaymentSettingRouter? = null

    val progressDialog : ProgressDialog by lazy { ProgressDialog(context) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSettingRouter = activity?.application as PaymentSettingRouter
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_authenticate_credit_card, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUse.setOnClickListener{
            authenticateCCPresenter.updateWhiteList((adapter as AuthenticateCreditCardAdapter).getSelectedState(), resources, true)
        }
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 -> dividerItemDecoration.setDrawable(it1) }
            getRecyclerView(view).addItemDecoration(dividerItemDecoration)
        }
        progressDialog.setMessage(getString(R.string.title_loading))
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
        DaggerAuthenticateCreditCardComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .authenticateCreditCardModule(AuthenticateCreditCardModule())
                .build()
                .inject(this)
        authenticateCCPresenter.attachView(this)
    }

    override fun onErrorUpdateWhiteList(e: Throwable) {
        showSnackbarError(ErrorHandler.getErrorMessage(activity, e))
    }

    private fun showSnackbarError(errorMessage: String?) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, errorMessage)
    }

    override fun onResultUpdateWhiteList(checkWhiteListStatus: CheckWhiteListStatus?) {
        if(checkWhiteListStatus?.statusCode == 200){
            activity?.setResult(Activity.RESULT_OK)
            NetworkErrorHelper.showGreenSnackbar(activity, checkWhiteListStatus.message)
        }else{
            showSnackbarError(checkWhiteListStatus?.message)
        }
    }

    override fun renderList(list: MutableList<TypeAuthenticateCreditCard>) {
        super.renderList(list)
        updateVisibilityButtonUse()
    }

    private fun updateVisibilityButtonUse() {
        if(adapter?.data?.size?:0 > 0){
            buttonUse.visibility = View.VISIBLE
        }else{
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
        startActivityForResult(paymentSettingRouter?.getIntentOtpPageVerifCreditCard(activity, phoneNumber), REQUEST_CODE_OTP_PAYMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OTP_PAYMENT){
            authenticateCCPresenter.updateWhiteList(AuthenticateCCPresenter.SINGLE_AUTH_VALUE, resources)
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
        authenticateCCPresenter.checkWhiteList(resources)
    }

    companion object {
        val REQUEST_CODE_OTP_PAYMENT = 1273
        fun createInstance(): AuthenticateCreditCardFragment {
            return AuthenticateCreditCardFragment()
        }
    }
}
