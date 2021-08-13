package com.tokopedia.ordermanagement.snapshot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.*
import com.tokopedia.ordermanagement.snapshot.view.fragment.SnapshotFragment

/**
 * Created by fwidjaja on 1/19/21.
 */
class SnapshotImageViewPagerAdapter : RecyclerView.Adapter<SnapshotImageViewPagerItemViewHolder>() {
    var listImg = mutableListOf<String>()
    private var actionListener: SnapshotAdapter.ActionListener? = null

    fun setActionListener(mainActionListener: SnapshotAdapter.ActionListener) {
        this.actionListener = mainActionListener
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotImageViewPagerItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.snapshot_img_view_pager_item, parent, false)
        return SnapshotImageViewPagerItemViewHolder(view, actionListener)
    }

    override fun onBindViewHolder(holder: SnapshotImageViewPagerItemViewHolder, position: Int) {
        val img = listImg[position]
        holder.bind(img, holder.adapterPosition)
    }

    override fun getItemCount(): Int = listImg.size

}