package com.tokopedia.search.result.product.emptystate

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.utils.addFilterOrigin
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider

class EmptyStateListenerDelegate(
    context: Context?,
    private val filterController: FilterController,
    private val redirectionListener: RedirectionListener?,
    private val parameterListener: ProductListParameterListener,
): EmptyStateListener,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onEmptyButtonClicked() {
        val queryKey = parameterListener.queryKey
        EmptyStateTracking.eventUserClickNewSearchOnEmptySearchProduct(queryKey)

        redirectionListener?.showSearchInputView()
    }

    override fun onEmptySearchToGlobalSearchClicked(applink: String?) {
        applink ?: return

        openApplink(context, applink)
    }

    override fun resetFilters() {
        filterController.resetAllFilters()
        parameterListener.refreshSearchParameter(filterController.getParameter())
        parameterListener.reloadData()
    }
}