package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotHeaderViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {
    override fun bind(item: SnapshotTypeData, position: Int) {
        if (item.dataObject is Int) {
            if (item.dataObject == 1) {
                val ivHeader = itemView.findViewById<ImageUnify>(R.id.snapshot_main_img)
                ivHeader.visible()
                ivHeader.loadImageWithoutPlaceholder("https://ecs7.tokopedia.net/img/product-1/2019/7/9/8967046/8967046_5246bec9-f10a-4189-b7ad-1b07579fa8e0_836_836")
                itemView.findViewById<ViewPager2>(R.id.snapshot_header_view_pager).gone()
                itemView.findViewById<PageControl>(R.id.snapshot_page_indicator).gone()
            }
        }
    }
}