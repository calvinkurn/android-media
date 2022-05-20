package com.tokopedia.chatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.unifyprinciples.Typography


class ContactUsMigrationAdapter :
    RecyclerView.Adapter<ContactUsMigrationAdapter.ContactUsViewHolder>() {

    val list = mutableListOf<String>()

    inner class ContactUsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTitle: Typography? =
            itemView.findViewById(R.id.content_text)

        fun bind(item: String) {
            this.contentTitle?.text = item
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

    fun setList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}



