package com.tokopedia.chatbot.view.adapter

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.unifyprinciples.Typography


class ContactUsMigrationAdapter :
    RecyclerView.Adapter<ContactUsMigrationAdapter.ContactUsViewHolder>() {

    val list = mutableListOf<Pair<Int,String>>()

    inner class ContactUsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTitle: Typography? =
            itemView.findViewById(R.id.content_text)
        private val contentIndex: Typography? =
            itemView.findViewById(R.id.content_index)

        fun bind(item: Pair<Int, String>) {
            val index = item.first.toString() + ". "
            this.contentIndex?.text = index
            this.contentTitle?.text = item.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactUsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_contact_us_migration, parent, false)
        return ContactUsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactUsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Pair<Int,String>>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}



