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
        val defaultNoFilter = FilterModel()
        val useStatusFilter = defaultNoFilter.status != filter.status
        val useVoucherTypeFilter = defaultNoFilter.voucherType != filter.voucherType
        val usePromoTypeFilter = defaultNoFilter.promoType != filter.promoType
        val useSourceFilter = defaultNoFilter.source != filter.source
        val useTargetFilter = defaultNoFilter.target != filter.target
        val useTargetBuyerFilter = defaultNoFilter.targetBuyer != filter.targetBuyer

        return useStatusFilter ||
            useVoucherTypeFilter ||
            usePromoTypeFilter ||
            useSourceFilter ||
            useTargetFilter ||
            useTargetBuyerFilter
    }

    fun getStatusName(context: Context?, filter: FilterModel): String {
        val resString = context?.resources?.getStringArray(R.array.status_items).orEmpty()
        val status = filter.status.firstOrNull() ?: return ""
        val statusId = status.id
        return resString.getOrNull(statusId).orEmpty()
    }
}
