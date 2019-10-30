package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.feedcomponent.view.widget.RatingBarReview
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify

/**
 * @author by yfsx on 18/09/18.
 */
class DashboardItemViewHolder(
        itemView: View,
        private val onItemClick: (DashboardItemViewModel) -> Unit,
        private val onBuyClick: (applink: String) -> Unit
) : AbstractViewHolder<DashboardItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_af_dashboard
    }

    private val ivItem: ImageView = itemView.findViewById(R.id.iv_item)
    private val tvName: TextView = itemView.findViewById(R.id.tv_name)
    private val tvBuyCount: TextView = itemView.findViewById(R.id.tv_buy_count)
    private val tvClickCount: TextView = itemView.findViewById(R.id.tv_click_count)
    private val tvComission: TextView = itemView.findViewById(R.id.tv_comission)
    private val llFromPost: LinearLayout = itemView.findViewById(R.id.ll_from_post)
    private val llFromTraffic: LinearLayout = itemView.findViewById(R.id.ll_from_traffic)
    private val cardView: CardUnify = itemView.findViewById(R.id.card_view)
    private val tvSection: TextView = itemView.findViewById(R.id.tv_section)
    private val rbCuratedTraffic: RatingBarReview = itemView.findViewById(R.id.rb_curated_traffic)
    private val tvRating: TextView = itemView.findViewById(R.id.tv_rating)

    private val llBtnCreatePost: LinearLayout = itemView.findViewById(R.id.ll_btn_create_post)

    override fun bind(element: DashboardItemViewModel) {
        initView(element)
    }

    private fun initView(element: DashboardItemViewModel) {
        ImageHandler.loadImageRounded2(ivItem.context, ivItem, element.imageUrl, 6.0f)
        tvName.text = MethodChecker.fromHtml(element.title)
        tvClickCount.text = element.itemClicked
        tvBuyCount.text = element.itemSold
        cardView.setOnClickListener { onItemClick(element) }
        tvSection.text = element.sectionName
        tvComission.text = element.earnedComission
        if (element.productRating >= 0) {
            val rating = (element.productRating / 100.0f * 5).toInt()
            rbCuratedTraffic
                    .apply { visible() }
                    .also { it.updateRating(rating) }
        }
        else rbCuratedTraffic.gone()
        if (element.reviewCount >= 0) tvRating.text = getString(R.string.af_review_count_format, element.reviewCount.toString())
        if (element.isShouldShowSection) tvSection.visible() else tvSection.gone()

        when (element.type) {
            null, 1 -> {
                llFromPost.visible()
                llFromTraffic.gone()
            }
            2 -> {
                llFromPost.gone()
                llFromTraffic.visible()
            }
        }

        llBtnCreatePost.setOnClickListener { onBuyClick(element.createPostApplink) }
        if (element.isActive && element.isShouldShowButtonCreatePost) llBtnCreatePost.visible() else llBtnCreatePost.gone()
    }
}
