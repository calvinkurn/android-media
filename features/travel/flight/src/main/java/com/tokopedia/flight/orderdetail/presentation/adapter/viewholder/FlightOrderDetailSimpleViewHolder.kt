package com.tokopedia.flight.orderdetail.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_flight_order_detail_simple.view.*

/**
 * @author by furqan on 12/11/2020
 */
class FlightOrderDetailSimpleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(element: FlightOrderDetailSimpleModel) {
        with(itemView) {
            tgFlightOrderSimpleLeft.text = element.leftValue
            tgFlightOrderSimpleRight.text = element.rightValue

            setupStrikeThrough(tgFlightOrderSimpleLeft, element.isLeftStriked)
            setupStrikeThrough(tgFlightOrderSimpleRight, element.isRightStriked)

            if (element.isLeftBold) {
                tgFlightOrderSimpleLeft.setWeight(Typography.BOLD)
            } else {
                tgFlightOrderSimpleLeft.setWeight(Typography.REGULAR)
            }

            if (element.isRightBold) {
                tgFlightOrderSimpleRight.setWeight(Typography.BOLD)
            } else {
                tgFlightOrderSimpleRight.setWeight(Typography.REGULAR)
            }

            if (element.isRightAlign) {
                tgFlightOrderSimpleRight.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            } else {
                tgFlightOrderSimpleRight.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }

        }
    }

    private fun setupStrikeThrough(textView: Typography, isStriked: Boolean) {
        if (isStriked) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_order_detail_simple
    }

}