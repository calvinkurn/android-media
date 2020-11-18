package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.ent_pdp_form_chips.view.*
import java.util.*

class EventPDPChipsViewHolder (val view: View,
                               val addOrRemoveData: (Int, String, String) -> Unit) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){
        with(itemView){
            tg_chips_form_event.text = element.title
            chip_event_form.listener = object : FilterChipAdapter.OnClickListener {
                override fun onChipClickListener(string: String, isSelected: Boolean) {
                    if (isSelected) {
                        addOrRemoveData(position, string, "")
                    }
                }
            }

            chip_event_form.setItem(ArrayList(chipsList(element.helpText)),
                    initialSelectedItemPos = if (element.value.isNotEmpty()) getId(chipsList(element.helpText), element.value)  else null)

            chip_event_form.selectOnlyOneChip(true)
            chip_event_form.canDiselectAfterSelect(false)
        }
    }

    private fun chipsList(chipValue: String) : List<String>{
        return chipValue.split("|")
    }

    private fun getId(list: List<String>, selected: String): Int {
        for(i in 0..list.size-1){
            if(selected.equals(list.get(i))){
                return i
            }
        }
        return 0
    }

    companion object {
        val LAYOUT_CHIPS = R.layout.ent_pdp_form_chips
    }
}