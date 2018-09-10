package com.tokopedia.payment.setting.authenticate

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import kotlinx.android.synthetic.main.fragment_authenticate_credit_card.*
import javax.inject.Inject

class AuthenticateCreditCardFragment : BaseListFragment<TypeAuthenticateCreditCard, AuthenticateCCAdapterFactory>(),
        AuthenticateCCContract.View {

    @Inject
    lateinit var authenticateCCPresenter: AuthenticateCCPresenter
    val progressDialog : ProgressDialog by lazy { ProgressDialog(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_authenticate_credit_card, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUse.setOnClickListener{
            authenticateCCPresenter.updateWhiteList((adapter as AuthenticateCreditCardAdapter).getSelectedState(), resources)
        }
        progressDialog.setMessage(getString(R.string.title_loading))
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
    override fun getAdapter(): BaseListAdapter<TypeAuthenticateCreditCard, AuthenticateCCAdapterFactory> {

    }

    override fun getScreenName(): String {
       return ""
    }

    override fun initInjector() {
        DaggerAuthenticateCreditCardComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .detailCreditCardModule(AuthenticateCreditCardModule())
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

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.hide()
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
        fun createInstance(): AuthenticateCreditCardFragment{
            return AuthenticateCreditCardFragment()
        }
    }
}
