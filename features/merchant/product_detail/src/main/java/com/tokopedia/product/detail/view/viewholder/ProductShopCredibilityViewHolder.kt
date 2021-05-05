package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_dynamic_shop_credibility.view.*

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopCredibilityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_credibility
    }

    init {
        showShopLoading()
    }

    var componentTracker: ComponentTrackDataModel? = null

    override fun bind(element: ProductShopCredibilityDataModel) {
        with(view) {
            if (element.shopName.isNotEmpty()) {
                if (componentTracker == null) {
                    componentTracker = getComponentTrackData(element)
                }
                shop_name.text = MethodChecker.fromHtml(element.shopName)
                shop_location_online?.shouldShowWithAction(element.shopLocation.isNotEmpty()) {
                    shop_location_online.text = context.getString(R.string.location_dot_builder, element.shopLocation)
                }
                setupLastActive(element.shopLastActive)
                setupBadgeAndImage(element.shopAva, element.isOs, element.isPm, element.shopTierBadgeUrl)
                setupGoApotik(element.isGoApotik)
                setupInfoRegion(element)
                setupFollow(element.isFavorite, componentTracker!!)

                shop_ava.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
                }

                shop_name.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
                }

                iv_badge.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
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
            PAYLOAD_TOOGLE_FAVORITE -> enableButton() //Will only invoke if fail follow shop
            else -> {
                renderFollow(element.isFavorite)
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
            btn_follow.isClickable = false
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
        btn_follow.isClickable = true
    }

    private fun setupGoApotik(isGoApotik: Boolean) = with(view) {
        shop_feature.shouldShowWithAction(isGoApotik) {
            shop_feature.text = context.getString(R.string.label_go_apotik)
        }
    }

    private fun setupLastActive(shopLastActive: String) = with(view) {
        shop_last_active.text = MethodChecker.fromHtml(shopLastActive)
        if (shopLastActive == context.getString(R.string.shop_online)) {
            shop_last_active.setWeight(Typography.BOLD)
            shop_last_active.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            shop_last_active.setType(Typography.BODY_3)
            shop_last_active.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    private fun setupInfoRegion(element: ProductShopCredibilityDataModel) = with(view) {
        val data = element.infoShopData

        if (data.getOrNull(0)?.value?.isEmpty() == true) {
            shop_info_container_1.hide()
        } else {
            shop_info_container_1.show()
            shop_info_title_1.text = data.getOrNull(0)?.value.orEmpty()
            shop_info_desc_1.text = data.getOrNull(0)?.desc.orEmpty()

            if (data.getOrNull(0)?.iconIsNotEmpty() == true) {
                shop_info_ic_1.show()
                shop_info_ic_1.setImage(data.getOrNull(0)?.icon)
            } else {
                shop_info_ic_1.hide()
            }
        }

        if (data.getOrNull(1)?.value?.isEmpty() == true) {
            shop_info_container_2.hide()
        } else {
            shop_info_container_2.show()
            shop_info_title_2.text = data.getOrNull(1)?.value.orEmpty()
            shop_info_desc_2.text = data.getOrNull(1)?.desc.orEmpty()

            if (data.getOrNull(1)?.iconIsNotEmpty() == true) {
                shop_info_ic_2.show()
                shop_info_ic_2.setImage(data.getOrNull(1)?.icon)
            } else {
                shop_info_ic_2.hide()
            }
        }
    }

    private fun setupBadgeAndImage(avatar: String,
                                   isOs: Boolean,
                                   isPm: Boolean,
                                   shopTierBadgeUrl: String) = with(view) {
        if (isNewShopBadgeEnabled()) {
            showNewBadge(shopTierBadgeUrl)
        } else {
            showOldBadge(isOs, isPm)
        }

        shop_ava.loadImageCircle(avatar)
    }

    private fun isNewShopBadgeEnabled() = true

    private fun showNewBadge(shopTierBadgeUrl: String) = with(view) {
        iv_badge.shouldShowWithAction(shopTierBadgeUrl.isNotEmpty()) {
            iv_badge.loadImage(shopTierBadgeUrl)
        }
    }

    private fun showOldBadge(isOs: Boolean, isPm: Boolean) = with(view) {
        val drawable = when {
            isOs -> {
                MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_official_store_product)
            }
            isPm -> {
                MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant)
            }
            else -> {
                null
            }
        }
        iv_badge.shouldShowWithAction(drawable != null) {
            iv_badge.loadImage(drawable)
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

    private fun enableButton() = with(view) {
        btn_follow.isClickable = true
    }

    private fun getComponentTrackData(element: ProductShopCredibilityDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}