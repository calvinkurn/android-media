package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotInfoViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {
    override fun bind(item: SnapshotTypeData, position: Int) {
        if (item.dataObject is String) {
            val hargaCoret = itemView.findViewById<Typography>(R.id.snapshot_harga_coret)
            hargaCoret.paintFlags = hargaCoret.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}