package com.tokopedia.topchat.chattemplate.view.adapter

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import java.util.ArrayList

class TemplateChatAdapter(
    private val typeFactory: TemplateChatTypeFactory
) : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    val list: MutableList<Visitable<*>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as? AbstractViewHolder<Visitable<*>>)?.bind(list[holder.adapterPosition])
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as? Visitable<TemplateChatTypeFactory>)?.type(typeFactory) ?: 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Visitable<*>>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun update(strings: ArrayList<String?>) {
        list.clear()
        for (i in strings.indices) {
            list.add(TemplateChatUiModel(strings[i]))
        }
        list.add(TemplateChatUiModel(false))
        notifyDataSetChanged()
    }

    fun hasTemplateChat(): Boolean {
        return list.isNotEmpty()
    }
}