package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by fwidjaja on 1/22/21.
 */
class SnapshotImageViewPagerItemViewHolder(itemView: View, private val actionListener: SnapshotAdapter.ActionListener?) : SnapshotImageViewPagerAdapter.BaseViewHolder<String>(itemView) {
    override fun bind(item: String, position: Int) {
        val imgViewHolder = itemView.findViewById<ImageView>(R.id.img_item)
        ImageHandler.loadImageFromUriFitCenter(itemView.context, imgViewHolder, Uri.parse(item))
        imgViewHolder.setOnClickListener {
            actionListener?.onSnapshotImgClicked(position)
        }
    }
}