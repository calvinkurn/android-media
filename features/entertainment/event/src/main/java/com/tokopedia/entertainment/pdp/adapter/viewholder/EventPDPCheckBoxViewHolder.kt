package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.ent_pdp_form_check_box_item.view.*

class EventPDPCheckBoxViewHolder (val view: View) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){
        with(itemView){
            tg_cb_form_event.text = element.title
        }
    }

    companion object {
        val LAYOUT_CHECKBOX = R.layout.ent_pdp_form_check_box_item
    }
}