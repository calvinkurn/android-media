package com.tokopedia.hotel.cancellation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.LayoutHotelCancellationRefundDetailItemBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 04/05/20
 */

class HotelCancellationRefundDetailWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: HotelCancellationPolicyAdapter

    private val binding = LayoutHotelCancellationRefundDetailItemBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        View.inflate(context, R.layout.layout_hotel_cancellation_refund_detail_item, this)
    }

    fun initView(title: String, amount: String, isSummary: Boolean = false) {
        with(binding) {
            hotelCancellationRefundDetailPaymentTitle.text = title
            hotelCancellationRefundDetailPaymentPrice.text = amount

            if (isSummary) {
                hotelCancellationRefundDetailPaymentTitle.setWeight(Typography.BOLD)
                hotelCancellationRefundDetailPaymentTitle.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950_96))

                hotelCancellationRefundDetailPaymentPrice.setWeight(Typography.BOLD)
                hotelCancellationRefundDetailPaymentPrice.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_YN500))
            }
        }
    }
}
