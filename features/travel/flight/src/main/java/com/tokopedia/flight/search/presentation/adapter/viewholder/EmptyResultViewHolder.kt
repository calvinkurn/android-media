package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.common.view.model.EmptyResultModel
import kotlinx.android.synthetic.main.item_flight_empty_search.view.*

/**
 * @author by furqan on 20/04/2020
 */
class EmptyResultViewHolder(itemView: View) : AbstractViewHolder<EmptyResultModel>(itemView) {

    override fun bind(element: EmptyResultModel) {
        with(itemView) {
            if (element.iconRes != 0) {
                ivFlightEmpty.setImageResource(element.iconRes)
            }

            if (element.title.isEmpty()) {
                tvFlightEmptyTitle.visibility = View.GONE
            } else {
                tvFlightEmptyTitle.text = element.title
                tvFlightEmptyTitle.visibility = View.VISIBLE
            }

            if (element.contentRes != 0) {
                tvFlightEmptyDescription.setText(element.contentRes)
                tvFlightEmptyDescription.visibility = View.VISIBLE
            } else {
                if (element.content.isNotEmpty()) {
                    tvFlightEmptyDescription.text = element.content
                    tvFlightEmptyDescription.visibility = View.VISIBLE
                } else {
                    tvFlightEmptyDescription.visibility = View.GONE
                }
            }

            if (element.buttonTitleRes != 0) {
                btnFlightEmpty.setText(element.buttonTitleRes)
                btnFlightEmpty.setOnClickListener {
                    element.callback?.onEmptyButtonClicked()
                }
                btnFlightEmpty.visibility = View.VISIBLE
            } else {
                if (element.buttonTitle.isEmpty()) {
                    btnFlightEmpty.visibility = View.GONE
                } else {
                    btnFlightEmpty.text = element.buttonTitle
                    btnFlightEmpty.setOnClickListener {
                        element.callback?.onEmptyButtonClicked()
                    }
                    btnFlightEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    interface Callback {
        fun onEmptyButtonClicked()
    }

    companion object {
        val LAYOUT = R.layout.item_flight_empty_search
    }

}