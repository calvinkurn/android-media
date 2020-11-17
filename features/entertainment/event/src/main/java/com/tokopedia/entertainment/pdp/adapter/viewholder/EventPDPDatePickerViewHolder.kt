package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.kotlin.extensions.toFormattedString
import kotlinx.android.synthetic.main.ent_pdp_form_date_picker_item.view.*
import kotlinx.android.synthetic.main.ent_pdp_form_edittext_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class EventPDPDatePickerViewHolder (val view: View,
                                    val listener: Listener
) : RecyclerView.ViewHolder(view){

    fun bind(element: Form, position: Int){
        with(itemView){
            tg_event_date_picker.textFieldWrapper.hint = element.title
            tg_event_date_picker.setMessage(element.helpText)
            tg_event_date_picker.textFieldInput.apply {
                keyListener = null
                setText(element.title)
                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                listener.clickDatePicker(element.title, position)
                            }
                        }
                        return v?.onTouchEvent(event) ?: true
                    }
                })
            }

            if(listener.getDate() != null){
                val dateTime = listener.getDate()?.time?.toFormattedString("dd MMMM yyyy", LocaleUtils.getIDLocale())
                val dateValue = SimpleDateFormat("yyyy-MM-dd").format(listener.getDate()?.time)
                tg_event_date_picker.textFieldInput.setText(dateTime)
            }
        }
    }

    companion object {
        val LAYOUT_DATE_PICKER = R.layout.ent_pdp_form_date_picker_item
    }

    interface Listener {
        fun getDate(): Calendar?
        fun clickDatePicker(title: String, position: Int)
    }
}