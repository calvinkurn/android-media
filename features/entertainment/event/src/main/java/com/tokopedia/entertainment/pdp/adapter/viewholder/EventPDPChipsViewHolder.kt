package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntPdpFormChipsBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class EventPDPChipsViewHolder(val view: View,
                              val addOrRemoveData: (Int, String, String) -> Unit) : RecyclerView.ViewHolder(view) {

    private var binding: EntPdpFormChipsBinding? by viewBinding()

    fun bind(element: Form, position: Int) {
        binding?.run {
            tgChipsFormEvent.text = element.title
            chipEventForm.listener = object : FilterChipAdapter.OnClickListener {
                override fun onChipClickListener(string: String, isSelected: Boolean) {
                    if (isSelected) {
                        addOrRemoveData(position, string, "")
                        if (!element.value.equals(root.context.resources.getString(R.string.ent_checkout_data_nullable_form)) || element.value.isNullOrEmpty()) {
                            tgChipsFormEventError.visibility = View.INVISIBLE
                            element.isError = false
                        }
                    }
                }
            }

            val valuePosition = if (!element.value.isNullOrEmpty() && !element.value.equals(root.context.resources.getString(R.string.ent_checkout_data_nullable_form))) getId(chipsList(element.options), element.value) else null
            if (valuePosition != null) {
                addOrRemoveData(position, chipsList(element.options).get(valuePosition), "")
            }
            chipEventForm.setItem(ArrayList(chipsList(element.options)), initialSelectedItemPos = valuePosition)

            chipEventForm.selectOnlyOneChip(true)
            chipEventForm.canDiselectAfterSelect(false)

            if (element.isError) {
                tgChipsFormEventError.show()
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tgChipsFormEventError.text = root.context.resources.getString(R.string.ent_pdp_form_error_all_msg, element.title)
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tgChipsFormEventError.text = element.errorMessage
                }
            }
        }
    }

    private fun chipsList(chipValue: String): List<String> {
        return chipValue.split("|")
    }

    private fun getId(list: List<String>, selected: String): Int {
        for (i in 0..list.size - 1) {
            if (selected.equals(list.get(i))) {
                return i
            }
        }
        return 0
    }

    companion object {
        val LAYOUT_CHIPS = R.layout.ent_pdp_form_chips
    }
}
