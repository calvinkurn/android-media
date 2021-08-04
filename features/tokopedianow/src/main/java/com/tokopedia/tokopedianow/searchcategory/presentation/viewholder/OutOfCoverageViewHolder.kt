package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OutOfCoverageListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.OutOfCoverageDataView
import kotlinx.android.synthetic.main.item_tokopedianow_search_category_out_of_coverage.view.*

class OutOfCoverageViewHolder(
        itemView: View,
        private val outOfCoverageListener: OutOfCoverageListener
) : AbstractViewHolder<OutOfCoverageDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_out_of_coverage
    }

    override fun bind(element: OutOfCoverageDataView?) {
        itemView.tokoNowSearchCategoryOutOfCoverageView?.setDescriptionCityName(getString(R.string.tokopedianow_city_name_empty_state_no_address))
        itemView.tokoNowSearchCategoryOutOfCoverageView?.actionListener = object: NoAddressEmptyStateView.ActionListener {
            override fun onChangeAddressClicked() {
                outOfCoverageListener.onChangeAddressClicked()
            }

            override fun onReturnClick() {
                outOfCoverageListener.onReturnClick()
            }

        }
    }

}