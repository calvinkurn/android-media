package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter

/**
 * Created by fwidjaja on 2/1/21.
 */
class SnapshotLoaderViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {
    override fun bind(item: SnapshotTypeData, position: Int) {
        val loaderView = itemView.findViewById<ConstraintLayout>(R.id.cl_snapshot_loader)
        loaderView.visible()
    }
}