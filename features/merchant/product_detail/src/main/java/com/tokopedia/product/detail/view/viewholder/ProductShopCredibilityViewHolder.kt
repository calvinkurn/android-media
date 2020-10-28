package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.getRelativeDate
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.*

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopCredibilityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_credibility
    }

    override fun bind(element: ProductShopCredibilityDataModel) {
        with(view) {
            showShopLoading()
            if (element.shopName.isNotEmpty()) {
                val componentTracker = getComponentTrackData(element)

                shop_name.text = MethodChecker.fromHtml(element.shopName)
                shop_location_online.text = context.getString(R.string.location_dot_builder, element.shopLocation)
                setupLastActive(element.shopLastActive)
                setupBadgeAndImage(element.shopAva, element.isOs, element.isPm)
                setupGoApotik(element.isGoApotik)
                setupInfoRegion(element)
                setupFollow(element.isFavorite, componentTracker)

                shop_ava.setOnClickListener {
                    listener.gotoShopDetail(componentTracker)
                }

                shop_name.setOnClickListener {
                    listener.gotoShopDetail(componentTracker)
                }

                iv_badge.setOnClickListener {
                    listener.gotoShopDetail(componentTracker)
                }

                hideShopLoading()
            }
        }
    }

    override fun bind(element: ProductShopCredibilityDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            PAYLOAD_TOOGLE_FAVORITE -> toggleClickableFavoriteBtn(element.enableButtonFavorite)
            else -> {
                renderFollow(element.isFavorite)
                toggleClickableFavoriteBtn(element.enableButtonFavorite)
            }
        }
    }

    private fun setupFollow(isFavorite: Boolean, componentTrackDataModel: ComponentTrackDataModel) = with(view) {
        if (listener.isOwner()) {
            btn_follow.hide()
        } else {
            btn_follow.show()
        }

        renderFollow(isFavorite)

        btn_follow.setOnClickListener {
            listener.onShopInfoClicked(it.id, componentTrackDataModel)
        }
    }

    private fun renderFollow(isFavorite: Boolean) = with(view) {
        if (isFavorite) {
            btn_follow.text = getString(R.string.label_favorited)
            btn_follow.buttonType = UnifyButton.Type.ALTERNATE
        } else {
            btn_follow.text = getString(R.string.label_follow)
            btn_follow.buttonType = UnifyButton.Type.MAIN
        }
    }

    private fun setupGoApotik(isGoApotik: Boolean) = with(view) {
        shop_feature.shouldShowWithAction(isGoApotik) {
            shop_feature.text = context.getString(R.string.label_go_apotik)
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

        if (data.getOrNull(0)?.value?.isEmpty() == true) {
            shop_info_container_1.hide()
        } else {
            shop_info_container_1.show()
            shop_info_title_1.text = data.getOrNull(0)?.value.orEmpty()
            shop_info_desc_1.text = MethodChecker.fromHtml(data.getOrNull(0)?.desc.orEmpty())

            if (data.getOrNull(0)?.icon != null) {
                shop_info_ic_1.show()
                shop_info_ic_1.setImageDrawable(MethodChecker.getDrawable(context, data.getOrNull(0)?.icon
                        ?: 0))
            } else {
                shop_info_ic_1.hide()
            }
        }

        if (data.getOrNull(1)?.value?.isEmpty() == true) {
            shop_info_container_2.hide()
        } else {
            shop_info_container_2.show()
            shop_info_title_2.text = data.getOrNull(1)?.value.orEmpty()
            shop_info_desc_2.text = MethodChecker.fromHtml(data.getOrNull(1)?.desc.orEmpty())

            if (data.getOrNull(1)?.icon != null) {
                shop_info_ic_2.show()
                shop_info_ic_2.setImageDrawable(MethodChecker.getDrawable(context, data.getOrNull(1)?.icon
                        ?: 0))
            } else {
                shop_info_ic_2.hide()
            }
        }
    }

    private fun setupBadgeAndImage(avatar: String, isOs: Boolean, isPm: Boolean) = with(view) {
        val drawable = when {
            isOs -> {
                MethodChecker.getDrawable(context, R.drawable.ic_official_store_product)
            }
            isPm -> {
                MethodChecker.getDrawable(context, R.drawable.ic_power_merchant)
            }
            else -> {
                null
            }
        }

        iv_badge.shouldShowWithAction(drawable != null) {
            iv_badge.setImageDrawable(drawable)
        }

        ImageUtils.loadImageCircleWithPlaceHolder(context, shop_ava, avatar)
    }

    private fun hideShopLoading() = with(view) {
        shop_credibility_container.show()
        shop_credibility_shimmering.hide()
    }

    private fun showShopLoading() = with(view) {
        shop_credibility_container.hide()
        shop_credibility_shimmering.show()
    }

    private fun toggleClickableFavoriteBtn(enable: Boolean) = with(view) {
        btn_follow.isClickable = enable
    }

    private fun getComponentTrackData(element: ProductShopCredibilityDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}