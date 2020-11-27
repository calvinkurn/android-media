package com.tokopedia.entertainment.pdp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPCheckBoxViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPChipsViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPDatePickerViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.entertainment.pdp.common.util.EventConst.BLANK_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.user.session.UserSessionInterface
import java.util.regex.Pattern

class EventPDPFormAdapter(val userSession: UserSessionInterface,
                          val onClickFormListener: OnClickFormListener,
                          val textFormListener: EventPDPTextFieldViewHolder.TextFormListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var formData: MutableList<Form> = mutableListOf()

    fun setList(data: MutableList<Form>) {
        formData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TEXT_TYPE -> EventPDPTextFieldViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPTextFieldViewHolder.LAYOUT_TEXT, parent, false),
                    ::addOrRemoveData, userSession, onClickFormListener, textFormListener)
            CHECKBOX_TYPE -> EventPDPCheckBoxViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPCheckBoxViewHolder.LAYOUT_CHECKBOX, parent, false),
                    ::addOrRemoveData)
            DATEPICKER_TYPE -> EventPDPDatePickerViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPDatePickerViewHolder.LAYOUT_DATE_PICKER, parent, false), 
                    ::addOrRemoveData, onClickFormListener)
            CHIP_TYPE -> EventPDPChipsViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPChipsViewHolder.LAYOUT_CHIPS, parent, false),
                    ::addOrRemoveData)
            else -> EventPDPTextFieldViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPTextFieldViewHolder.LAYOUT_TEXT, parent, false),
                ::addOrRemoveData, userSession, onClickFormListener, textFormListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(formData.get(position).elementType){
            TEXT_TYPE_TEXT, LIST_TYPE_TEXT -> TEXT_TYPE
            CHECKBOX_TYPE_TEXT -> CHECKBOX_TYPE
            DATEPICKER_TYPE_TEXT -> DATEPICKER_TYPE
            CHIP_TYPE_TEXT -> CHIP_TYPE
            else -> TEXT_TYPE
        }
    }

    override fun getItemCount(): Int = formData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder.itemViewType){
            TEXT_TYPE -> { holder as EventPDPTextFieldViewHolder
                           holder.bind(formData.get(position), position)}
            CHECKBOX_TYPE -> { holder as EventPDPCheckBoxViewHolder
                           holder.bind(formData.get(position), position)}
            DATEPICKER_TYPE -> { holder as EventPDPDatePickerViewHolder
                           holder.bind(formData.get(position), position)}
            CHIP_TYPE -> { holder as EventPDPChipsViewHolder
                           holder.bind(formData.get(position), position)}
            else -> { holder as EventPDPTextFieldViewHolder
                holder.bind(formData.get(position), position)}
        }
    }

    private fun addOrRemoveData(index: Int, value: String, valueList: String) {
        if (formData.get(index).elementType.equals(ELEMENT_LIST)) {
            formData.get(index).valuePosition = value
            formData.get(index).valueList = valueList
        } else {
            formData.get(index).value = value
        }
    }

    fun getError(resources: Resources): List<Pair<String, Int>> {
        val listError = mutableListOf<Pair<String, Int>>()
        formData.forEachIndexed {index, it ->
            if (it.required == 1) {
                if (((it.value.isBlank() || it.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) && !it.elementType.equals(ELEMENT_LIST)) ||
                     ((it.value.isBlank() || (it.value.equals(CHECKBOX_FALSE)) || it.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) && it.elementType.equals(CHECKBOX_TYPE_TEXT))  ||
                        (it.valuePosition.equals(BLANK_LIST) && it.elementType.equals(ELEMENT_LIST))) {
                    listError.add(Pair(it.title, EMPTY_TYPE))
                    it.isError = true
                    it.errorType = EMPTY_TYPE
                    notifyItemChanged(index, it)
                } else if (!validateRegex(it.value, it.validatorRegex)) {
                    listError.add(Pair(it.title, EMPTY_TYPE))
                    it.isError = true
                    it.errorType = REGEX_TYPE
                    notifyItemChanged(index, it)
                } else {
                    it.isError = false
                }
            }
        }

        return listError
    }


    private fun validateRegex(string: String, pattern: String): Boolean {
        try {
            if (!pattern.isNullOrEmpty()) {
                val mPattern = Pattern.compile(pattern)
                val matcher = mPattern.matcher(string)
                return matcher.matches()
            } else return true
        } catch (e: RuntimeException) {
            return false
        }
    }

    companion object {
        const val EMPTY_TYPE = 1
        const val REGEX_TYPE = 2

        const val TEXT_TYPE = 1
        const val LIST_TYPE = 1
        const val CHECKBOX_TYPE = 2
        const val DATEPICKER_TYPE = 3
        const val CHIP_TYPE = 4

        const val CHECKBOX_FALSE = "0"

        const val TEXT_TYPE_TEXT = "text"
        const val LIST_TYPE_TEXT = "list"
        const val CHECKBOX_TYPE_TEXT = "checkbox"
        const val DATEPICKER_TYPE_TEXT = "date"
        const val CHIP_TYPE_TEXT = "chips"
    }
}