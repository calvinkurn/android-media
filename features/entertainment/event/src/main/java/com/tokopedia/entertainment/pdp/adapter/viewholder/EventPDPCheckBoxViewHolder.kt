package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.ent_pdp_form_check_box_item.view.*
import kotlinx.android.synthetic.main.ent_pdp_form_date_picker_item.view.*
import kotlinx.android.synthetic.main.partial_event_checkout_footer.*

class EventPDPCheckBoxViewHolder(val view: View,
                                 val addOrRemoveData: (Int, String, String) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bind(element: Form, position: Int) {
        with(itemView) {
            tg_cb_form_event.text = element.title
            cb_form_event.setOnCheckedChangeListener { _, isChecked ->
                val value = if (isChecked) CHECKBOX_TRUE_VALUE else CHECKBOX_FALSE_VALUE
                addOrRemoveData(position, value.toString(), "")

                if (!element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form)) || element.value.isNullOrEmpty()) {
                    tg_cb_form_event_error.visibility = View.INVISIBLE
                    element.isError = false
                }
            }

            cb_form_event.isChecked = element.value.equals(CHECKBOX_TRUE)
            val value = if (element.value.equals(CHECKBOX_TRUE)) CHECKBOX_TRUE_VALUE else CHECKBOX_FALSE_VALUE
            addOrRemoveData(position, value.toString(), "")

            if (element.isError) {
                tg_cb_form_event_error.show()
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tg_cb_form_event_error.text = resources.getString(R.string.ent_pdp_form_error_all_msg, element.title)
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tg_cb_form_event_error.text = element.errorMessage
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