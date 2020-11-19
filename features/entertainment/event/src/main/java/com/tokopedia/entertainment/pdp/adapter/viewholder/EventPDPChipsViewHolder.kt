package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.synthetic.main.ent_pdp_form_chips.view.*
import kotlinx.android.synthetic.main.ent_pdp_form_date_picker_item.view.*
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

            val valuePosition = if (!element.value.isNotEmpty() && !element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) 0 else getId(chipsList(element.helpText), element.value)
            addOrRemoveData(position, chipsList(element.helpText).get(valuePosition),"")
            chip_event_form.setItem(ArrayList(chipsList(element.helpText)),
                    initialSelectedItemPos = valuePosition)

            chip_event_form.selectOnlyOneChip(true)
            chip_event_form.canDiselectAfterSelect(false)

            if (element.isError) {
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tg_chips_form_event_error.text = resources.getString(R.string.ent_pdp_form_error_all_msg, element.title)
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tg_chips_form_event_error.text = element.errorMessage
                }
            }
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