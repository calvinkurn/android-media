package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OutOfCoverageListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.OutOfCoverageDataView

class OutOfCoverageViewHolder(
        itemView: View,
        private val outOfCoverageListener: OutOfCoverageListener
) : AbstractViewHolder<OutOfCoverageDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_out_of_coverage
    }

    override fun bind(element: OutOfCoverageDataView?) {
        val tokoNowSearchCategoryOutOfCoverageView: NoAddressEmptyStateView = itemView.findViewById(R.id.tokoNowSearchCategoryOutOfCoverageView)
        tokoNowSearchCategoryOutOfCoverageView.actionListener = object: NoAddressEmptyStateView.ActionListener {
            override fun onChangeAddressClicked() {
                outOfCoverageListener.onChangeAddressClicked()
            }

            override fun onReturnClick() {
                outOfCoverageListener.onReturnClick()
            }

            override fun onGetNoAddressEmptyStateEventCategoryTracker(): String {
                return ""
            }

        }
    }

}