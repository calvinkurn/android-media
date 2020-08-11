package com.tokopedia.hotel.cancellation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_hotel_cancellation_policy.view.*

/**
 * @author by jessica on 04/05/20
 */

class HotelCancellationPolicyWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: HotelCancellationPolicyAdapter

    init {
        View.inflate(context, R.layout.widget_hotel_cancellation_policy, this)
    }

    fun initView(title: String, policies: List<HotelCancellationModel.CancelPolicy.Policy>) {
        hotel_cancellation_policy_title.text = title

        hotel_cancellation_policy_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        hotel_cancellation_policy_rv.setHasFixedSize(true)
        hotel_cancellation_policy_rv.isNestedScrollingEnabled = false
        adapter = HotelCancellationPolicyAdapter()
        adapter.updatePolicy(policies)
        hotel_cancellation_policy_rv.adapter = adapter
    }
}