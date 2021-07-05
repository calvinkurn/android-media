package com.tokopedia.hotel.cancellation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_hotel_cancellation_refund_detail_item.view.*

/**
 * @author by jessica on 04/05/20
 */

class HotelCancellationRefundDetailWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: HotelCancellationPolicyAdapter

    init {
        View.inflate(context, R.layout.layout_hotel_cancellation_refund_detail_item, this)
    }

    fun initView(title: String, amount: String, isSummary: Boolean = false) {
        hotel_cancellation_refund_detail_payment_title.text = title
        hotel_cancellation_refund_detail_payment_price.text = amount

        if (isSummary) {
            hotel_cancellation_refund_detail_payment_title.setWeight(Typography.BOLD)
            hotel_cancellation_refund_detail_payment_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

            hotel_cancellation_refund_detail_payment_price.setWeight(Typography.BOLD)
            hotel_cancellation_refund_detail_payment_price.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }
    }
}