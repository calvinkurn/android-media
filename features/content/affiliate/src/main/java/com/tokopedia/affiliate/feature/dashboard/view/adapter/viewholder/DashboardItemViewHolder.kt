package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.CardView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * @author by yfsx on 18/09/18.
 */
class DashboardItemViewHolder(
        itemView: View,
        private val onItemClick: (DashboardItemViewModel) -> Unit
) : AbstractViewHolder<DashboardItemViewModel>(itemView) {

    private val ivItem: ImageView = itemView.findViewById(R.id.iv_item)
    private val layoutStatus: FrameLayout = itemView.findViewById(R.id.layout_status)
    private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    private val tvName: TextView = itemView.findViewById(R.id.tv_name)
    private val tvCommission: TextView = itemView.findViewById(R.id.tv_commission)
    private val tvBuyCount: TextView = itemView.findViewById(R.id.tv_buy_count)
    private val tvClickCount: TextView = itemView.findViewById(R.id.tv_click_count)
    private val tvProductCommission: TextView = itemView.findViewById(R.id.tv_product_commission)
    private val layoutActive: LinearLayout = itemView.findViewById(R.id.layout_active)
    private val layoutInactive: LinearLayout = itemView.findViewById(R.id.layout_inactive)
    private val cardView: CardView = itemView.findViewById(R.id.card_view)
    private val tvSection: TextView = itemView.findViewById(R.id.tv_section)

    override fun bind(element: DashboardItemViewModel) {
        initView(element)
    }

    private fun initView(element: DashboardItemViewModel) {
        ImageHandler.loadImageRounded2(ivItem.context, ivItem, element.imageUrl, 6.0f)
        tvName.text = MethodChecker.fromHtml(element.title)
        if (element.isActive && element.productCommission.isNotEmpty()) {
            layoutInactive.visibility = View.GONE
            layoutActive.visibility = View.VISIBLE
            tvProductCommission.text = element.productCommission
        } else {
            layoutInactive.visibility = View.VISIBLE
            layoutActive.visibility = View.GONE
            tvCommission.text = element.value
        }
        tvClickCount.text = element.itemClicked
        tvBuyCount.text = element.itemSold
//        layoutStatus.background = MethodChecker.getDrawable(
//                layoutStatus.context,
//                if (element.isActive)
//                    R.drawable.bg_af_ongoing
//                else
//                    R.drawable.bg_af_finished)
        tvStatus.text = tvStatus.context.getString(
                if (element.isActive)
                    R.string.text_af_ongoing
                else
                    R.string.text_af_finished)
        tvStatus.setTextColor(MethodChecker.getColor(
                tvStatus.context,
                if (element.isActive)
                    R.color.color_ongoing_text
                else
                    R.color.font_black_secondary_54
        )
        )
        cardView.setOnClickListener { onItemClick(element) }
        tvSection.text = element.sectionName
        if (element.isShouldShowSection) tvSection.visible() else tvSection.gone()
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_af_dashboard
    }
}
