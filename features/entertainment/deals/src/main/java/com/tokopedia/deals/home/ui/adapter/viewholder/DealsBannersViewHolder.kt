package com.tokopedia.deals.home.ui.adapter.viewholder

import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.Indicator
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ItemDealsBannerListBinding
import com.tokopedia.deals.home.listener.DealsBannerActionListener
import com.tokopedia.deals.home.ui.dataview.BannersDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show


/**
 * @author by jessica on 16/06/20
 */

class DealsBannersViewHolder(itemView: View, private val dealsBannerActionListener: DealsBannerActionListener)
    : BaseViewHolder(itemView) {

    fun bind(banners: BannersDataView) {

        val binding = ItemDealsBannerListBinding.bind(itemView)
        if (!banners.isLoaded) {
            binding.contentBannerShimmering.root.show()
            binding.bannerDealsHomepage.hide()
        } else {
            binding.contentBannerShimmering.root.hide()
            with(binding.bannerDealsHomepage) {
                this.show()
                val bannerImageUrls = banners.list.map { it.bannerImageUrl }
                setPromoList(bannerImageUrls)
                setOnPromoClickListener { position ->
                    dealsBannerActionListener.onBannerClicked(banners.list, position)
                }
                setBannerSeeAllTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                setBannerIndicator(Indicator.GREEN)
                bannerSeeAll.text = getString(R.string.deals_homepage_banner_see_all)
                bannerSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
                bannerSeeAll.setTypeface(null, Typeface.NORMAL)
                customHeight = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
                customWidth = resources.getDimensionPixelOffset(R.dimen.deals_item_banner_width)

                setOnPromoAllClickListener { dealsBannerActionListener.onBannerSeeAllClick(banners.seeAllUrl) }
                setOnPromoScrolledListener { position -> dealsBannerActionListener.onBannerScroll(banners.list[position], position) }

                if (banners.list.size == 1) {
                    binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                            object : OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    binding.bannerDealsHomepage.customWidth = binding.root.measuredWidth - resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl4)
                                    buildView()
                                }
                            })
                } else {
                    buildView()
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_banner_list
    }
}