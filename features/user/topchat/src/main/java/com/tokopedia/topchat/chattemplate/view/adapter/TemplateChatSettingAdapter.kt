package com.tokopedia.topchat.chattemplate.view.adapter

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract
import com.tokopedia.topchat.common.util.ItemTouchHelperAdapter
import java.util.*

class TemplateChatSettingAdapter(
    private val typeFactory: TemplateChatSettingTypeFactory,
    private val view: TemplateChatContract.View
) : RecyclerView.Adapter<AbstractViewHolder<*>>(), ItemTouchHelperAdapter {

    private val list: MutableList<Visitable<*>> = arrayListOf()
    private val listString: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as? AbstractViewHolder<Visitable<*>>)?.bind(list[holder.adapterPosition])
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as? Visitable<TemplateChatSettingTypeFactory>)?.type(typeFactory) ?: 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getList(): List<Visitable<*>?> {
        return list
    }

    fun getListString(): ArrayList<String> {
        listString.clear()
        list.forEach {
            val temp = (it as TemplateChatUiModel).message ?: ""
            listString.add(temp)
        }
        return listString
    }

    fun setList(list: List<Visitable<*>>?) {
        this.list.clear()
        if (list != null) {
            this.list.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {}
    override fun onReallyMoved(from: Int, to: Int) {
        view.reArrange(from, to)
    }

    fun startDrag(ini: ItemTemplateChatViewHolder?) {}

    fun edit(index: Int, string: String?) {
        if (list.size > 0) {
            (list[index] as TemplateChatUiModel?)?.message = string
            notifyItemChanged(index)
        }
    }

    fun delete(index: Int) {
        if (list.size > 0) {
            list.removeAt(index)
            notifyItemRemoved(index)
            list.removeAt(list.size - 1)
            notifyItemRemoved(list.size - 1)
            list.add(TemplateChatUiModel(false, list.size))
            notifyItemInserted(list.size)
        }
    }

    fun add(string: String?) {
        if (list.size > 0) {
            list.removeAt(list.size - 1)
            notifyItemRemoved(list.size - 1)
            list.add(TemplateChatUiModel(string))
            list.add(TemplateChatUiModel(false, list.size))
            notifyItemRangeInserted(list.size, 2)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        revertArrange(fromPosition, toPosition)
        return true
    }

    fun revertArrange(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}