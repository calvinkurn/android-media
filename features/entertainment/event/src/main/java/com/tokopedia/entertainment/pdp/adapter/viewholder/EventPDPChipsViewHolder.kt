package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventFormChipsAdapter
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.ent_pdp_form_chips.view.*

class EventPDPChipsViewHolder (val view: View) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){
        with(itemView){
            tg_chips_form_event.text = element.title
            val chipsAdapter = EventFormChipsAdapter()
            chipsAdapter.setList(chipsList(element.helpText))
            rv_event_form_chip.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = chipsAdapter
            }
        }
    }

    private fun chipsList(chipValue: String) : List<String>{
        return chipValue.split("|")
    }

    companion object {
        val LAYOUT_CHIPS = R.layout.ent_pdp_form_chips
    }
}