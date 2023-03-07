package com.tokopedia.chatbot.view.adapter

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.ListitemContactUsMigrationBinding
import com.tokopedia.unifyprinciples.Typography


class ContactUsMigrationAdapter :
    RecyclerView.Adapter<ContactUsMigrationAdapter.ContactUsViewHolder>() {

    val list = mutableListOf<Pair<Int,String>>()

    inner class ContactUsViewHolder(itemView: ListitemContactUsMigrationBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val contentTitle: Typography? =
            itemView.contentText
        private val contentIndex: Typography? =
            itemView.contentIndex

        fun bind(item: Pair<Int, String>) {
            val index = item.first.toString() + ". "
            this.contentIndex?.text = index
            this.contentTitle?.text = item.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactUsViewHolder {
        val view = ListitemContactUsMigrationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



