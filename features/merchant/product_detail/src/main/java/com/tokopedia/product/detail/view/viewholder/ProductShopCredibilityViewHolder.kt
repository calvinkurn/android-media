package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.getRelativeDate
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.*
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.iv_badge
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.shop_ava
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.shop_name

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopCredibilityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_credibility
        const val TRUE_VALUE = 1
    }

    override fun bind(element: ProductShopCredibilityDataModel) {
        with(view) {
            showShopLoading()
            element.shopInfo?.let {
                shop_name.text = MethodChecker.fromHtml(it.shopCore.name)
                shop_location_online.text = context.getString(R.string.location_dot_builder, it.location)
                setupLastActive(element.shopInfo?.shopLastActive.orEmpty())
                setupBadgeAndImage(it)
                setupGoApotik(element.shopFeature)
                setupInfoRegion(element)
                hideShopLoading()

                shop_ava.setOnClickListener {
                    listener.gotoShopDetail(getComponentTrackData(element))
                }

                shop_name.setOnClickListener {
                    listener.gotoShopDetail(getComponentTrackData(element))
                }

                iv_badge.setOnClickListener {
                    listener.gotoShopDetail(getComponentTrackData(element))
                }
            }
        }
    }

    private fun setupGoApotik(shopFeature: ShopFeatureData) = with(view) {
        shop_feature.shouldShowWithAction(shopFeature.value) {
            shop_feature.text = shopFeature.title
        }
    }

    private fun setupLastActive(shopLastActive: String) = with(view) {
        val lastActive = shopLastActive.getRelativeDate(context)
        shop_last_active.text = MethodChecker.fromHtml(lastActive)
        if (lastActive == context.getString(R.string.shop_online)) {
            shop_last_active.setWeight(Typography.BOLD)
            shop_last_active.setTextColor(MethodChecker.getColor(context, R.color.g_500))
        } else {
            shop_last_active.setType(Typography.BODY_3)
            shop_last_active.setTextColor(MethodChecker.getColor(context, R.color.Neutral_N700_68))
        }
    }

    private fun setupInfoRegion(element: ProductShopCredibilityDataModel) = with(view) {
        val data = element.getLastThreeHierarchyData(context)
        shop_info_title_1.text = data.getOrNull(0)?.value.orEmpty()
        shop_info_desc_1.text = MethodChecker.fromHtml(data.getOrNull(0)?.desc.orEmpty())
        if (data.getOrNull(0)?.icon != null) {
            shop_info_ic_1.setImageDrawable(MethodChecker.getDrawable(context, data.getOrNull(0)?.icon
                    ?: 0))
        } else {
            shop_info_ic_1.hide()
        }

        shop_info_title_2.text = data.getOrNull(1)?.value.orEmpty()
        shop_info_desc_2.text = MethodChecker.fromHtml(data.getOrNull(1)?.desc.orEmpty())
        if (data.getOrNull(1)?.icon != null) {
            shop_info_ic_2.setImageDrawable(MethodChecker.getDrawable(context, data.getOrNull(1)?.icon
                    ?: 0))
        } else {
            shop_info_ic_2.hide()
        }

        shop_info_title_3.text = data.getOrNull(2)?.value ?: ""
        shop_info_desc_3.text = MethodChecker.fromHtml(data.getOrNull(2)?.desc ?: "")
        if (data.getOrNull(2)?.icon != null) {
            shop_info_ic_3.setImageDrawable(MethodChecker.getDrawable(context, data.getOrNull(2)?.icon
                    ?: 0))
        } else {
            shop_info_ic_3.hide()
        }
    }

    private fun setupBadgeAndImage(shopData: ShopInfo) = with(view) {
        val drawable = if (shopData.goldOS.isOfficial == TRUE_VALUE) {
            MethodChecker.getDrawable(context, R.drawable.ic_official_store_product)
        } else if (shopData.goldOS.isGold == TRUE_VALUE && shopData.goldOS.isGoldBadge == TRUE_VALUE) {
            MethodChecker.getDrawable(context, R.drawable.ic_power_merchant)
        } else {
            shop_ava.hide()
            null
        }

        iv_badge.shouldShowWithAction(drawable != null) {
            shop_ava.show()
            iv_badge.setImageDrawable(drawable)
            shop_ava.loadImageCircle(shopData.shopAssets.avatar)
        }
    }

    private fun hideShopLoading() = with(view) {
        shop_credibility_container.show()
        shop_credibility_shimmering.hide()
    }

    private fun showShopLoading() = with(view) {
        shop_credibility_container.hide()
        shop_credibility_shimmering.show()
    }

    private fun getComponentTrackData(element: ProductShopCredibilityDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}