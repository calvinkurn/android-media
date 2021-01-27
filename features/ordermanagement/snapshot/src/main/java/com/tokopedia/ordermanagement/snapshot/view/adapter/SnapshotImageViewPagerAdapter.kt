package com.tokopedia.ordermanagement.snapshot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.*

/**
 * Created by fwidjaja on 1/19/21.
 */
class SnapshotImageViewPagerAdapter : RecyclerView.Adapter<SnapshotImageViewPagerAdapter.BaseViewHolder<*>>() {
    var listImg = mutableListOf<String>()

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.snapshot_img_view_pager_item, parent, false)
        return SnapshotImageViewPagerItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val img = listImg[position]
        when (holder) {
            is SnapshotImageViewPagerItemViewHolder-> {
                holder.bind(img, holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = listImg.size

}