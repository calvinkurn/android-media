package com.tokopedia.groupchat.chatroom.view.adapter.chatroom

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.BaseDynamicButtonViewHolder
import com.tokopedia.groupchat.room.view.viewmodel.BaseDynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton

/**
 * @author : Steven 22/05/19
 */

class DynamicButtonsAdapter(var typeFactory: DynamicButtonTypeFactory)
    : RecyclerView.Adapter<BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>>>() {


    private val list: ArrayList<Visitable<DynamicButtonTypeFactory>> = arrayListOf()

    fun addList(list: ArrayList<DynamicButton>) {
        list.let {
            this.list.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun addList(interactiveButton: InteractiveButton) {
        this.list.addAll(arrayListOf(interactiveButton))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>> {
        val view = onCreateViewItem(parent, viewType)
        return typeFactory.createViewHolder(view, viewType)
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>>, position: Int) {
        holder.bind(list[position])
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= list.size) {
            HideViewHolder.LAYOUT
        } else list[position].type(typeFactory)
    }
}