package com.tokopedia.loginregister.common.view.emailextension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.loginregister.databinding.LayoutListEmailExtensionBinding

class EmailExtensionAdapter(
    private var list: List<String>,
    private val listener: ClickListener,
    private val maxShowingItems: Int,
) : RecyclerView.Adapter<EmailExtensionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewBinding by lazy {
            LayoutListEmailExtensionBinding.bind(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutListEmailExtensionBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view.root)
    }

    fun updateList(list: List<String>) {
        this.list = list
    }

    override fun getItemCount(): Int {
        return if (list.size > maxShowingItems) {
            maxShowingItems
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let { item ->
            holder.viewBinding.run {
                textEmailExtension.text = item
                root.setOnClickListener {
                    listener.onExtensionClick(item, position)
                }
            }
        }
    }

    interface ClickListener {
        fun onExtensionClick(extension: String, position: Int)
    }
}
