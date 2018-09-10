package com.tokopedia.payment.setting.add

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.add.di.AddCreditCardModule
import com.tokopedia.payment.setting.add.di.DaggerAddCreditCardComponent
import com.tokopedia.payment.setting.add.model.Data
import javax.inject.Inject

class AddCreditCardFragment : BaseWebViewFragment(), AddCreditCardContract.View {

    @Inject
    lateinit var userSession : UserSession
    @Inject
    lateinit var addCreditCardPresenter : AddCreditCardPresenter
    val progressDialog : ProgressDialog by lazy { ProgressDialog(context) }
    var callbackUrl : String = ""

    override fun getUrl(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run { NetworkClient.init(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.setMessage(getString(R.string.title_loading))
        addCreditCardPresenter.getIframeAddCC()
    }

    override fun getUserIdForHeader(): String? {
        return userSession.userId
    }

    override fun getAccessToken(): String? {
        return userSession.accessToken
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.hide()
    }

    override fun onErrorGetIframeData(message: String?) {
        showRetrySnackBar(message)
    }

    private fun showRetrySnackBar(message: String?) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message, {addCreditCardPresenter.getIframeAddCC() }).showRetrySnackbar()
    }

    override fun onErrorGetIframeData(e: Throwable) {
        showRetrySnackBar(ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onSuccessGetIFrameData(data: Data?) {
        loadWeb()
        webView.postUrl(data?.apiInfo?.host, data?.ccIframeEncode?.toByteArray())
        callbackUrl = data?.ccIframe?.callbackUrl?:""

    }

    override fun initInjector() {
        DaggerAddCreditCardComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .addCreditCardModule(AddCreditCardModule())
                .build()
                .inject(this)
        addCreditCardPresenter.attachView(this)
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        if(url.equals(callbackUrl)){
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }

    override fun onDestroy() {
        addCreditCardPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        fun createInstance() : AddCreditCardFragment{
            return AddCreditCardFragment()
        }
    }
}