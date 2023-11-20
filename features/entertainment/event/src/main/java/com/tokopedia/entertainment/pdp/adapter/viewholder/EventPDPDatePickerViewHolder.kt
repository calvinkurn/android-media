package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntPdpFormDatePickerItemBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.utils.view.binding.viewBinding
import java.text.SimpleDateFormat

@SuppressLint("ClickableViewAccessibility")
class EventPDPDatePickerViewHolder(val view: View,
                                   val addOrRemoveData: (Int, String, String) -> Unit,
                                   val listener: OnClickFormListener
) : RecyclerView.ViewHolder(view) {

    private val binding: EntPdpFormDatePickerItemBinding? by viewBinding()
    fun bind(element: Form, position: Int) {
        binding?.run {

            tgEventDatePicker.textFieldWrapper.hint = element.title
            tgEventDatePicker.textFieldInput.apply {
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
                tgEventDatePicker.textFieldInput.setText(dateTime)
                addOrRemoveData(position, dateValue, "")
            }

            if (!element.value.equals(root.context.resources.getString(R.string.ent_checkout_data_nullable_form))) {
                tgEventDatePicker.setError(false)
                element.isError = false
            }

            if (element.isError) {
                if (element.errorType == EventPDPFormAdapter.EMPTY_TYPE) {
                    tgEventDatePicker.setMessage(root.context.resources.getString(R.string.ent_pdp_form_error_all_msg, element.title))
                } else if (element.errorType == EventPDPFormAdapter.REGEX_TYPE) {
                    tgEventDatePicker.setMessage(element.errorMessage)
                }
                tgEventDatePicker.setError(true)
            }


        }
    }

    companion object {
        const val SHOW_FORMAT = "dd MMMM yyyy"
        val LAYOUT_DATE_PICKER = R.layout.ent_pdp_form_date_picker_item
    }
}
