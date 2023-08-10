package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntPdpFormCheckBoxItemBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class EventPDPCheckBoxViewHolder(val view: View,
                                 val addOrRemoveData: (Int, String, String) -> Unit) : RecyclerView.ViewHolder(view) {

    private var binding: EntPdpFormCheckBoxItemBinding? by viewBinding()
    fun bind(element: Form, position: Int) {
        binding?.run {
            tgCbFormEvent.text = element.title
            cbFormEvent.setOnCheckedChangeListener { _, isChecked ->
                val value = if (isChecked) CHECKBOX_TRUE_VALUE else CHECKBOX_FALSE_VALUE
                addOrRemoveData(position, value.toString(), "")

                if (!element.value.equals(root.context.resources.getString(R.string.ent_checkout_data_nullable_form)) || element.value.isNullOrEmpty()) {
                    tgCbFormEventError.visibility = View.INVISIBLE
                    element.isError = false
                }
            }

            cbFormEvent.isChecked = element.value.equals(CHECKBOX_TRUE)
            val value = if (element.value.equals(CHECKBOX_TRUE)) CHECKBOX_TRUE_VALUE else CHECKBOX_FALSE_VALUE
            addOrRemoveData(position, value.toString(), "")

            if (element.isError) {
                tgCbFormEventError.show()
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tgCbFormEventError.text = root.context.resources.getString(R.string.ent_pdp_form_error_all_msg, element.title)
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tgCbFormEventError.text = element.errorMessage
                }
            }
        }
    }

    companion object {
        val LAYOUT_CHECKBOX = R.layout.ent_pdp_form_check_box_item

        const val CHECKBOX_TRUE = "1"
        const val CHECKBOX_TRUE_VALUE = 1
        const val CHECKBOX_FALSE_VALUE = 0
    }
}
