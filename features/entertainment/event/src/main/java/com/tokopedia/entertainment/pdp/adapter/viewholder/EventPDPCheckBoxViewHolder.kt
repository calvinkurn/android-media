package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.ent_pdp_form_check_box_item.view.*
import kotlinx.android.synthetic.main.partial_event_checkout_footer.*

class EventPDPCheckBoxViewHolder (val view: View,
                                  val addOrRemoveData: (Int, String, String) -> Unit) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){
        with(itemView){
            tg_cb_form_event.text = element.title
            cb_form_event.setOnCheckedChangeListener { _, isChecked ->
                val value = if (isChecked) 1 else 0
                addOrRemoveData(position, value.toString() ,"")
            }

            if(!element.value.isNullOrEmpty() && !element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))){
                cb_form_event.isChecked = element.value.equals("1")
            }
        }
    }

    companion object {
        val LAYOUT_CHECKBOX = R.layout.ent_pdp_form_check_box_item
    }
}