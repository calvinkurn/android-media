package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.ProductDetailDevActivity
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.presentation.model.ShopPageDevUiModel
import com.tokopedia.developer_options.shop_page_dev_option.ShopPageDevActivity
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.UnifyButton

class ShopPageDevOptViewHolder(
    itemView: View
) : AbstractViewHolder<ShopPageDevUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opt_shop_page_dev
    }

    override fun bind(element: ShopPageDevUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.shop_page_dev_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                DevOpsTracker.trackEntryEvent(DevopsFeature.SHOP_PAGE_DEV)
                val intent = Intent(this, ShopPageDevActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
