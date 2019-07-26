package com.tokopedia.groupchat.room.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.room.view.viewmodel.OverflowMenuButtonViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import java.util.*

/**
 * @author : Steven 25/06/19
 */
class OverflowMenuAdapter: RecyclerView.Adapter<OverflowMenuAdapter.OverflowMenuViewHolder>() {
    private val data = ArrayList<OverflowMenuButtonViewModel>()

    class OverflowMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)

        private val check: ImageView = itemView.findViewById(R.id.check)
        private val title: TextView = itemView.findViewById(R.id.title)

        fun bind(model: OverflowMenuButtonViewModel) {
            icon.showWithCondition(model.image!=0)
            if(icon.isVisible) ImageHandler.loadImageWithId(icon, model.image)
            check.showWithCondition(model.check)
            title.text = model.title
            itemView.setOnClickListener { model.action?.invoke()}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverflowMenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_play_menu, parent, false)
        return OverflowMenuViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OverflowMenuViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setDataList(elements: List<OverflowMenuButtonViewModel>) {
        data.clear()
        data.addAll(elements)
        notifyDataSetChanged()
    }
}