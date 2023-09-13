package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter

/**
 * Created by fwidjaja on 1/22/21.
 */
class SnapshotImageViewPagerItemViewHolder(
    itemView: View, private val actionListener: SnapshotAdapter.ActionListener?
) : SnapshotImageViewPagerAdapter.BaseViewHolder<String>(itemView) {
    override fun bind(item: String, position: Int) {
        val imgViewHolder = itemView.findViewById<ImageView>(R.id.img_item)
        imgViewHolder.run {
            loadProductImage(
                url = item,
                archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_LARGE,
                cornerRadius = 0f
            ) { isArchived ->
                actionListener?.onProductImageLoaded(isArchived)
            }
            setOnClickListener {
                actionListener?.onSnapshotImgClicked(position)
            }
        }
    }
}