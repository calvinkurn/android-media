package com.tokopedia.groupchat.chatroom.view.adapter.chatroom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.room.view.listener.PlayContract

/**
 * @author by StevenFredian on 05/06/18.
 */

class DynamicButtonAdapter(
        private val context: Context,
        private val listener: PlayContract.View
) : RecyclerView.Adapter<DynamicButtonAdapter.ViewHolder>() {

    private val list: MutableList<ButtonsPojo.Button>

    init {
        this.list = arrayListOf()
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var icon : ImageView = view.findViewById(R.id.icon)
        var notification : View = view.findViewById(R.id.notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dynamic_icon_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var element = list[position]
        ImageHandler.loadImage(context, holder.icon, element.buttonType, R.drawable.ic_play_dynamic_icon)
        holder.icon.setOnClickListener { listener.openOverlay(element.linkUrl) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getList(): List<ButtonsPojo.Button> {
        return list
    }

    fun setList(list: List<ButtonsPojo.Button>?) {
        list?.let {
            this.list.clear()
            this.list.addAll(it)
            notifyDataSetChanged()
        }
    }
}