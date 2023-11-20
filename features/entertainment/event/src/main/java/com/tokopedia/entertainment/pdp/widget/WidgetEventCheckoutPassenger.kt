package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.WidgetEventCheckoutPassengerBinding
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutPassengerDataAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.mapFormToString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetEventCheckoutPassenger @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_event_checkout_passenger, this)
    }

    private val binding = WidgetEventCheckoutPassengerBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    fun renderRecycleView(list : List<Form>){
        with(binding) {
            tgEventCheckoutWidgetPessangerName.text =
                context.getString(R.string.ent_event_checkout_passenger_new_title)
            tgEventCheckoutWidgetPessangerEmail.gone()
            rvEventCheckoutPassenger.show()

            val adapter = EventCheckoutPassengerDataAdapter()
            adapter.setList(mapFormToString(list))
            rvEventCheckoutPassenger.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                this.adapter = adapter
            }
        }
    }

    fun clickEditPassenger(updatePassenger: () -> Unit) {
        with(binding) {
            btnEventCheckoutPassenger.setOnClickListener {
                updatePassenger()
            }
        }
    }

    fun hideBtnCheckoutPassenger() {
        binding.btnEventCheckoutPassenger.hide()
    }
}
