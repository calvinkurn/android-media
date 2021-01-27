package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotResponse
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotHeaderViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView) {
    override fun bind(item: SnapshotTypeData, position: Int) {
        if (item.dataObject is List<*> && item.dataObject.isNotEmpty()) {
            val ivHeader = itemView.findViewById<ImageUnify>(R.id.snapshot_main_img)
            val viewPager2 = itemView.findViewById<ViewPager2>(R.id.snapshot_header_view_pager)
            val indicator = itemView.findViewById<PageControl>(R.id.snapshot_page_indicator)

            if (item.dataObject.size > 1) {
                ivHeader.gone()
                viewPager2.visible()
                indicator.visible()
                val imgViewPagerAdapter = SnapshotImageViewPagerAdapter()

                item.dataObject.forEach {
                    // mentok dsini
                    // val productImg: SnapshotResponse.Data.GetOrderSnapshot.ProductImageSecondaryItem =
                }
                val obj: SnapshotResponse.Data.GetOrderSnapshot.ProductImageSecondaryItem = item.dataObject

                val arrayListImg = arrayListOf<String>()
                arrayListImg.add("https://ecs7.tokopedia.net/img/VqbcmM/2020/11/24/6b48441e-6808-4ac2-9810-c79f58e9f2e7.jpg")
                arrayListImg.add("https://ecs7.tokopedia.net/img/VqbcmM/2020/9/17/dfc97e7a-92c3-4e6e-a161-7854dd31bda0.jpg")
                arrayListImg.add("https://ecs7.tokopedia.net/img/product-1/2020/10/7/106506101/106506101_afb65409-e18c-4c65-af05-5cede1647728_1080_1080")
                arrayListImg.add("https://ecs7.tokopedia.net/img/VqbcmM/2020/10/13/7a252f47-4b70-46ba-b0aa-9d3fed713d98.jpg")
                imgViewPagerAdapter.listImg = arrayListImg
                viewPager2.adapter = imgViewPagerAdapter

            } else {
                viewPager2.gone()
                indicator.gone()
                ivHeader.visible()
                ivHeader.loadImageWithoutPlaceholder("https://ecs7.tokopedia.net/img/product-1/2019/7/9/8967046/8967046_5246bec9-f10a-4189-b7ad-1b07579fa8e0_836_836")
                itemView.findViewById<PageControl>(R.id.snapshot_page_indicator).gone()
            }
        }
    }
}