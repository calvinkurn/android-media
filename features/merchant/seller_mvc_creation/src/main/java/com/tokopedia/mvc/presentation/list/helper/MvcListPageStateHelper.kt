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
        if (isKeywordEmpty) {
            if (isVoucherNoData && page == PaginationConstant.INITIAL_PAGE) {
                return PageState.NO_DATA_PAGE
            }
        } else {
            if (isVoucherNoData && page == PaginationConstant.INITIAL_PAGE) {
                return PageState.NO_DATA_SEARCH_PAGE
            }
        }
        return PageState.DISPLAY_LIST
    }

    fun getStatusName(context: Context?, filter: FilterModel): String {
        val resString = context?.resources?.getStringArray(R.array.status_items).orEmpty()
        val status = filter.status.firstOrNull() ?: return ""
        val statusId = status.id
        return resString.getOrNull(statusId).orEmpty()
    }
}
