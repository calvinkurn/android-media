package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotShopViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {
    override fun bind(item: SnapshotTypeData, position: Int) {
        /*if (item.dataObject is String) {
            itemView.findViewById<Typography>(R.id.snapshot_shop_name).text = "HALO"
        }*/
    }
}