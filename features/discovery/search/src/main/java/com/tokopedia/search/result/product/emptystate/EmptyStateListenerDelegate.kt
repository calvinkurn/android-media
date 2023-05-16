package com.tokopedia.search.result.product.emptystate

import android.content.Context
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.manualFilterToggleMap

class EmptyStateListenerDelegate(
    context: Context?,
    queryKeyProvider: QueryKeyProvider,
    private val filterController: FilterController,
    private val redirectionListener: RedirectionListener?,
    private val parameterListener: ProductListParameterListener,
): EmptyStateListener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onEmptyButtonClicked() {
        EmptyStateTracking.eventUserClickNewSearchOnEmptySearchProduct(queryKey)

        redirectionListener?.showSearchInputView()
    }

    override fun onEmptySearchToGlobalSearchClicked(applink: String?) {
        applink ?: return

        openApplink(context, applink)
    }

    override fun resetFilters() {
        filterController.resetAllFilters()

        val queryParams = filterController.getParameter() + manualFilterToggleMap()
        parameterListener.refreshSearchParameter(queryParams)
        parameterListener.reloadData()
    }
}
