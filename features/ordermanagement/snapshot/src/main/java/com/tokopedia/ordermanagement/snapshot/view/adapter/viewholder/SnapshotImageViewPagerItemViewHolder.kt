package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by fwidjaja on 1/22/21.
 */
class SnapshotImageViewPagerItemViewHolder(itemView: View) : SnapshotImageViewPagerAdapter.BaseViewHolder<String>(itemView) {
    override fun bind(item: String, position: Int) {
        val imgViewHolder = itemView.findViewById<ImageUnify>(R.id.img_item)
        imgViewHolder.loadImageWithoutPlaceholder(item)
    }
}