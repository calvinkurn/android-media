package com.tokopedia.affiliate.feature.explore.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileChildViewModel
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_af_popular_profile_child.view.*

/**
 * @author by milhamj on 12/03/19.
 */
class PopularProfileAdapter: RecyclerView.Adapter<PopularProfileAdapter.ViewHolder>() {
    val list: MutableList<PopularProfileChildViewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_af_popular_profile_child,null))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        val itemView = holder.itemView

        itemView.avatar.loadImageCircle(element.avatar)
        itemView.name.text = element.name
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v)
}