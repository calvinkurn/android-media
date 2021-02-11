package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.kotlin.extensions.toFormattedString
import kotlinx.android.synthetic.main.ent_pdp_form_date_picker_item.view.*
import java.text.SimpleDateFormat

@SuppressLint("ClickableViewAccessibility")
class EventPDPDatePickerViewHolder(val view: View,
                                   val addOrRemoveData: (Int, String, String) -> Unit,
                                   val listener: OnClickFormListener
) : RecyclerView.ViewHolder(view) {

    fun bind(element: Form, position: Int) {
        with(itemView) {

            tg_event_date_picker.textFieldWrapper.hint = element.title
            tg_event_date_picker.textFieldInput.apply {
                keyListener = null
                if (element.value.isNullOrEmpty() || element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) {
                    setText(resources.getString(R.string.ent_pdp_form_date_picker_placeholder))
                } else {
                    val format = SimpleDateFormat(element.options)
                    val date = format.parse(element.value)
                    val dateShow = date.toFormattedString(SHOW_FORMAT, LocaleUtils.getIDLocale())
                    setText(dateShow)
                }
                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                listener.clickDatePicker(element.title, element.helpText, position)
                            }
                        }
                        return v?.onTouchEvent(event) ?: true
                    }
                })
            }

            if (listener.getDate() != null) {
                val dateTime = listener.getDate()?.time?.toFormattedString(SHOW_FORMAT, LocaleUtils.getIDLocale())
                val dateValue = SimpleDateFormat(element.options).format(listener.getDate()?.time)
                tg_event_date_picker.textFieldInput.setText(dateTime)
                addOrRemoveData(position, dateValue, "")
            }

            if (!element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) {
                tg_event_date_picker.setError(false)
                element.isError = false
            }

            if (element.isError) {
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tg_event_date_picker.setMessage(resources.getString(R.string.ent_pdp_form_error_all_msg, element.title))
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tg_event_date_picker.setMessage(element.errorMessage)
                }
                tg_event_date_picker.setError(true)
            }


        }
    }

    companion object {
        const val VALUE_FORMAT = "yyyy-MM-dd"
        const val SHOW_FORMAT = "dd MMMM yyyy"
        val LAYOUT_DATE_PICKER = R.layout.ent_pdp_form_date_picker_item
    }
}