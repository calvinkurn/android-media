package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.entertainment.pdp.common.util.EventConst.BLANK_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.user.session.UserSessionInterface
import java.lang.RuntimeException
import java.util.regex.Pattern

class EventPDPFormAdapter(val userSession: UserSessionInterface,
                          val onClickFormListener: OnClickFormListener,
                          val textFormListener: EventPDPTextFieldViewHolder.TextFormListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var formData: MutableList<Form> = mutableListOf()

    fun setList(data: MutableList<Form>){
        formData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventPDPTextFieldViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPTextFieldViewHolder.LAYOUT_TEXT, parent, false),
                ::addOrRemoveData, userSession, onClickFormListener, textFormListener)
    }

    override fun getItemCount(): Int = formData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EventPDPTextFieldViewHolder){
            holder.bind(formData.get(position), position)
        }
    }

    private fun addOrRemoveData(index:Int, value: String, valueList:String){
        if(formData.get(index).elementType.equals(ELEMENT_LIST)){
            formData.get(index).valuePosition = value
            formData.get(index).valueList = valueList
        }else {
            formData.get(index).value = value
        }
    }

    fun getError(): Pair<String, Int>{
        formData.forEach {
            if(it.required == 1) {
                if((it.value.isBlank() && it.elementType.equals(ELEMENT_TEXT)) ||
                        (it.valuePosition.equals(BLANK_LIST) && it.elementType.equals(ELEMENT_LIST))) {
                    return Pair(it.title, EMPTY_TYPE)
                } else {
                    if(!validateRegex(it.value, it.validatorRegex)) return Pair(it.title, REGEX_TYPE)
                }
            }
        }

        return Pair("", 0)
    }

    private fun validateRegex(string: String, pattern: String): Boolean{
        try {
            if (!pattern.isNullOrEmpty()) {
                val mPattern = Pattern.compile(pattern)
                val matcher = mPattern.matcher(string)
                return matcher.matches()
            } else return true
        }catch (e: RuntimeException){
            return false
        }
    }

    companion object{
        const val EMPTY_TYPE = 1
        const val REGEX_TYPE = 2
    }
}