package com.tokopedia.topads.dashboard.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopadsEducationRvAdapter(private val list: List<Pair<Drawable?, String>>) :
    RecyclerView.Adapter<TopadsEducationRvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_education, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.image.setImageDrawable(item.first)
        holder.title.text = item.second
        holder.view.setOnClickListener { itemClick?.invoke(holder.adapterPosition) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var itemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageUnify = view.findViewById(R.id.ivEducation)
        val title: Typography = view.findViewById(R.id.txtTitleEducation)
    }

    companion object {
        fun createInstance(list: List<Pair<Drawable?, String>>) = TopadsEducationRvAdapter(list)
    }
}