package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import java.lang.RuntimeException
import java.util.regex.Pattern

class EventPDPFormAdapter(val userSession: UserSessionInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var formData: MutableList<Form> = mutableListOf()

    fun setList(data: MutableList<Form>){
        formData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventPDPTextFieldViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPTextFieldViewHolder.LAYOUT_TEXT, parent, false),
                ::addOrRemoveData, userSession)
    }

    override fun getItemCount(): Int = formData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EventPDPTextFieldViewHolder){
            holder.bind(formData.get(position), position)
        }
    }

    private fun addOrRemoveData(index:Int, value: String){
        formData.get(index).value = value
    }

    fun getError(): Pair<String, Int>{
        formData.forEach {
            if(it.required == 1) {
                if(it.value.isBlank()) {
                    return Pair(it.title, EMPTY_TYPE)
                }
                else {
                    if(!validateRegex(it.value, it.validatorRegex)) return Pair(it.title, REGEX_TYPE)
                }
            }
        }

        return Pair("", 0)
    }

    private fun validateRegex(string: String, pattern: String): Boolean{
        try {
            val mPattern = Pattern.compile(pattern)
            val matcher = mPattern.matcher(string)
            return matcher.matches()
        }catch (e: RuntimeException){
            return false
        }
    }

    companion object{
        const val FULLNAME_TYPE = "fullname"
        const val EMAIL_TYPE = "email"
        const val PHONE_TYPE = "no-telp"
        const val EMPTY_TYPE = 1
        const val REGEX_TYPE = 2

        const val ELEMENT_TEXT = "text"
        const val ELEMENT_LIST = "list"
        const val TEXT_TYPE = 1
        const val LIST_TYPE = 2
    }
}