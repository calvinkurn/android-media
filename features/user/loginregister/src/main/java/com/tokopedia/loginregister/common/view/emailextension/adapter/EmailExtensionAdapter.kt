package com.tokopedia.loginregister.common.view.emailextension.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.loginregister.R
import kotlinx.android.synthetic.main.layout_list_email_extension.view.*

class EmailExtensionAdapter(
        private var list: List<String>,
        private val listener: ClickListener,
        private val maxShowingItems: Int
): RecyclerView.Adapter<EmailExtensionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.layout_list_email_extension, null))
    }

    fun updateList(list: List<String>) {
        this.list = list
    }

    override fun getItemCount(): Int {
        return if(list.size > maxShowingItems) {
            maxShowingItems
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener, position)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(extension: String, listener: ClickListener?, position: Int) {
            itemView.textEmailExtension.text = extension
            itemView.setOnClickListener {
                listener?.onExtensionClick(extension, position)
            }
        }
    }

    interface ClickListener {
        fun onExtensionClick(extension: String, position: Int)
    }
}