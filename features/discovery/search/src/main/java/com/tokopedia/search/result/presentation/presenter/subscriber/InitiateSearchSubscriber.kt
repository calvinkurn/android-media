package com.tokopedia.search.result.presentation.presenter.subscriber

import android.text.TextUtils
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper
import rx.Subscriber

private const val DISCOVERY_URL_SEARCH = 1
private const val DISCOVERY_APPLINK = 2

open class InitiateSearchSubscriber(private val initiateSearchListener: InitiateSearchListener) : Subscriber<InitiateSearchModel>() {

    override fun onNext(initiateSearchModel: InitiateSearchModel?) {
        if(initiateSearchModel == null) initiateSearchListener.onHandleResponseError()

        val redirectApplink = getRedirectApplink(initiateSearchModel)

        when (defineRedirectApplink(redirectApplink)) {
            DISCOVERY_URL_SEARCH -> onHandleSearch(initiateSearchModel)
            DISCOVERY_APPLINK -> initiateSearchListener.onHandleApplink(redirectApplink)
            else -> initiateSearchListener.onHandleResponseUnknown()
        }
    }

    private fun getRedirectApplink(initiateSearchModel: InitiateSearchModel?) : String {
        return initiateSearchModel?.searchProduct?.redirection?.redirectApplink ?: ""
    }

    private fun defineRedirectApplink(applink: String): Int {
        return if (TextUtils.isEmpty(applink)) {
            DISCOVERY_URL_SEARCH
        } else {
            DISCOVERY_APPLINK
        }
    }

    private fun onHandleSearch(initiateSearchModel: InitiateSearchModel?) {
        val isHasCatalog = ListHelper.isContainItems(initiateSearchModel?.searchProduct?.catalogs)
        initiateSearchListener.onHandleResponseSearch(isHasCatalog)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        initiateSearchListener.onHandleResponseError()
    }
}