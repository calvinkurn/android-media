package com.tokopedia.mvc.presentation.list.helper

import android.content.Context
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.PaginationConstant
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.model.FilterModel

object MvcListPageStateHelper {
    fun getPageState(vouchers: List<Voucher>, filter: FilterModel, page: Int): PageState {
        val isVoucherNoData = vouchers.isEmpty()
        val isKeywordEmpty = filter.keyword.isEmpty()
        val isFiltering = getIsFiltering(filter)
        if (isVoucherNoData && page == PaginationConstant.INITIAL_PAGE) {
            return if (isFiltering || !isKeywordEmpty) {
                PageState.NO_DATA_SEARCH_PAGE
            } else {
                PageState.NO_DATA_PAGE
            }
        }
        return PageState.DISPLAY_LIST
    }

    private fun getIsFiltering(filter: FilterModel): Boolean {
        return useStatusFilter(filter) ||
            useVoucherTypeFilter(filter) ||
            usePromoTypeFilter(filter) ||
            useSourceFilter(filter) ||
            useTargetFilter(filter) ||
            useTargetBuyerFilter(filter)
    }

    private fun useStatusFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.status != filter.status
    }

    private fun useVoucherTypeFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.voucherType != filter.voucherType
    }

    private fun usePromoTypeFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.promoType != filter.promoType
    }

    private fun useSourceFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.source != filter.source
    }

    private fun useTargetFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.target != filter.target
    }

    private fun useTargetBuyerFilter(filter: FilterModel): Boolean {
        val defaultNoFilter = FilterModel()
        return defaultNoFilter.targetBuyer != filter.targetBuyer
    }

    fun getStatusName(context: Context?, filter: FilterModel): String {
        val resString = context?.resources?.getStringArray(R.array.status_items).orEmpty()
        val status = filter.status.firstOrNull() ?: return ""
        val statusId = status.id
        return resString.getOrNull(statusId).orEmpty()
    }
}
