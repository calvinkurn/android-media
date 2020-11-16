package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Form

class EventPDPDatePickerViewHolder (val view: View) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){

    }

    companion object {
        val LAYOUT_DATE_PICKER = R.layout.ent_pdp_form_date_picker_item
    }
}