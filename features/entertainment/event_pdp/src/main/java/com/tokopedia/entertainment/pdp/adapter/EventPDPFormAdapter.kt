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

    private var formData: MutableList<Form> = mutableListOf()
    var dataset: HashMap<String, String> = hashMapOf()

    fun setList(data: MutableList<Form>){
        formData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventPDPTextFieldViewHolder(LayoutInflater.from(parent.context).inflate(EventPDPTextFieldViewHolder.LAYOUT, parent, false), ::addOrRemoveData, userSession)
    }

    override fun getItemCount(): Int = formData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EventPDPTextFieldViewHolder){
            holder.bind(formData.get(position), position)
        }
    }

    private fun addOrRemoveData(field:String, value: String){
        if(dataset.containsKey(field)){
            if(dataset[field] == value || value.isBlank()) dataset.remove(field)
            else {
                dataset.remove(field)
                dataset[field] = value
            }
        } else {
            dataset[field] = value
        }

        Timber.tag("DATASET").w(dataset.toString())
    }

    fun getError(): Pair<String, Int>{
        formData.forEach {
            if(it.required == 1) {
                if(!dataset.containsKey(it.name)) {
                    return Pair(it.title, EMPTY_TYPE)
                }
                else {
                    if(!validateRegex(dataset[it.name]!!, it.validatorRegex)) return Pair(it.title, REGEX_TYPE)
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
        val FULLNAME_TYPE = "fullname"
        val EMAIL_TYPE = "email"
        val PHONE_TYPE = "no-telp"
        val EMPTY_TYPE = 1
        val REGEX_TYPE = 2
    }
}