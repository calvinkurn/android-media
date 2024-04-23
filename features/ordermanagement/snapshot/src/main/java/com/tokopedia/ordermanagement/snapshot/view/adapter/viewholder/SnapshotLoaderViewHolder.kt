package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by fwidjaja on 2/1/21.
 */
class SnapshotLoaderViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {

    private val imageLoader = itemView.findViewById<LoaderUnify>(R.id.snapshot_loader_img_header)

    init {
        measureScreenHeight()
    }

    override fun bind(item: SnapshotTypeData, position: Int) {
        val loaderView = itemView.findViewById<ConstraintLayout>(R.id.cl_snapshot_loader)
        loaderView.visible()
    }

    private fun measureScreenHeight() = with(itemView) {
        val screenWidth = itemView.resources.displayMetrics.widthPixels
        imageLoader.layoutParams.height = screenWidth
    }

    override fun onViewRecycled() {
        // noop
    }
}
