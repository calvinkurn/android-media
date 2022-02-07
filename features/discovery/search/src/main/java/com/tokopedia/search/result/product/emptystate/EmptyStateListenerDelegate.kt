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

    override fun onSelectedFilterRemoved(uniqueId: String?) {
        removeSelectedFilter(uniqueId ?: "")
    }

    private fun removeSelectedFilter(uniqueId: String) {
        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)

        removeFilterFromFilterController(option)

        val queryParams = filterController.getParameter().addFilterOrigin()
        parameterListener.refreshSearchParameter(queryParams)
        parameterListener.reloadData()
    }

    private fun removeFilterFromFilterController(option: Option) {
        when {
            option.isCategoryOption -> filterController.setFilter(
                option,
                isFilterApplied = false,
                isCleanUpExistingFilterWithSameKey = true
            )
            option.isMinOrMaxPriceOption -> {
                filterController.setFilter(
                    Option(key = Option.KEY_PRICE_MIN),
                    isFilterApplied = false,
                    isCleanUpExistingFilterWithSameKey = true
                )
                filterController.setFilter(
                    Option(key = Option.KEY_PRICE_MAX),
                    isFilterApplied = false,
                    isCleanUpExistingFilterWithSameKey = true
                )
            }
            else -> {
                filterController.setFilter(option, false)
            }
        }
    }

    override fun onEmptyButtonClicked() {
        val queryKey = parameterListener.queryKey
        EmptyStateTracking.eventUserClickNewSearchOnEmptySearchProduct(queryKey)

        redirectionListener?.showSearchInputView()
    }

    override fun getSelectedFilterAsOptionList(): List<Option> {
        val combinedPriceFilterName =
            context?.resources?.getString(R.string.empty_state_selected_filter_price_name) ?: ""

        return OptionHelper.combinePriceFilterIfExists(
            filterController.getActiveFilterOptionList(),
            combinedPriceFilterName
        )
    }

    override fun onEmptySearchToGlobalSearchClicked(applink: String?) {
        applink ?: return

        openApplink(context, applink)
    }
}