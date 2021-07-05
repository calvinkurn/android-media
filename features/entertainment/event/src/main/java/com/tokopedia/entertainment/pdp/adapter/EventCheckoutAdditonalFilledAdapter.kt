package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.item_checkout_event_additional_item_filled.view.*

class EventCheckoutAdditonalFilledAdapter : RecyclerView.Adapter<EventCheckoutAdditonalFilledAdapter.EventCheckoutAdditionalFilledViewHolder>() {

    private var listForm = emptyList<Form>()

    inner class EventCheckoutAdditionalFilledViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(form: Form) {
            with(itemView) {
                event_item_title.text = resources.getString(R.string.ent_checkout_data_tambahan_item_title, form.title)
                event_item_value.text = resources.getString(R.string.ent_checkout_data_tambahan_item_value, if (!form.elementType.equals(ELEMENT_LIST)) form.value
                else form.valueList)
            }
        }
    }

    override fun onBindViewHolder(holder: EventCheckoutAdditionalFilledViewHolder, position: Int) {
        holder.bind(listForm[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCheckoutAdditionalFilledViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_additional_item_filled, parent, false)
        return EventCheckoutAdditionalFilledViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listForm.size
    }

    fun setList(list: List<Form>) {
        listForm = list
        notifyDataSetChanged()
    }
}