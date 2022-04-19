package com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.util.DateHeaderFormatter
import com.tokopedia.review.feature.reputationhistory.view.adapter.SellerReputationPenaltyAdapter.Companion.PAYLOAD_DATE_FILTER
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationHeaderViewHelper
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationDateUiModel

class SellerReputationDateViewHolder(view: View, private val fragment: Fragment?) :
    AbstractViewHolder<ReputationDateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.widget_header_reputation
    }

    private var reputationViewHelper: ReputationHeaderViewHelper? = null
    private val dateHeaderFormatter = DateHeaderFormatter(
        itemView.resources.getStringArray(com.tokopedia.datepicker.range.R.array.lib_date_picker_month_entries)
    )

    init {
        reputationViewHelper = ReputationHeaderViewHelper(itemView)
    }

    override fun bind(element: ReputationDateUiModel?) {
        if (element == null) return

        with(itemView) {
            reputationViewHelper?.bindDate(
                dateHeaderFormatter,
                element.startDate,
                element.endDate,
                DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE,
                DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
            )
            setOnClickListener {
                if (fragment != null) {
                    reputationViewHelper?.onClick(fragment)
                }
            }
        }
    }

    override fun bind(element: ReputationDateUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNullOrEmpty() || element == null) return

        when (payloads.getOrNull(0) as? Int) {
            PAYLOAD_DATE_FILTER -> {
                reputationViewHelper?.bindDate(
                    dateHeaderFormatter,
                    element.startDate,
                    element.endDate,
                    DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE,
                    DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
                )
            }
        }
    }
}