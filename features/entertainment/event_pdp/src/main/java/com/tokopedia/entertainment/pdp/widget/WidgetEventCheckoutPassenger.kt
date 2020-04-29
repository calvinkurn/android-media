package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutPassengerDataAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_event_checkout_passenger.view.*

class WidgetEventCheckoutPassenger @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_event_checkout_passenger, this)
    }

    fun renderRecycleView(hashMap: HashMap<String, String>){
        tg_event_checkout_widget_pessanger_name.text = context.getString(R.string.ent_event_checkout_passenger_new_title)
        tg_event_checkout_widget_pessanger_email.gone()
        rv_event_checkout_passenger.show()

        val adapter = EventCheckoutPassengerDataAdapter()
        adapter.setList(mapHashToList(hashMap))
        rv_event_checkout_passenger.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }

    }

    fun mapHashToList(hashMap: HashMap<String, String>): List<String>{
       return hashMap.map {
            it.value
        }
    }
}