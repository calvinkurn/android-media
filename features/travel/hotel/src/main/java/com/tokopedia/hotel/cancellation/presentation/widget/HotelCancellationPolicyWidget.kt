package com.tokopedia.hotel.cancellation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.databinding.WidgetHotelCancellationPolicyBinding
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by jessica on 04/05/20
 */

class HotelCancellationPolicyWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: HotelCancellationPolicyAdapter

    private val binding: WidgetHotelCancellationPolicyBinding = WidgetHotelCancellationPolicyBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun initView(title: String, policies: List<HotelCancellationModel.CancelPolicy.Policy>) {
        with(binding) {
            hotelCancellationPolicyTitle.text = title

            hotelCancellationPolicyRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            hotelCancellationPolicyRv.setHasFixedSize(true)
            hotelCancellationPolicyRv.isNestedScrollingEnabled = false
            adapter = HotelCancellationPolicyAdapter()
            adapter.updatePolicy(policies)
            hotelCancellationPolicyRv.adapter = adapter
        }
    }
}
