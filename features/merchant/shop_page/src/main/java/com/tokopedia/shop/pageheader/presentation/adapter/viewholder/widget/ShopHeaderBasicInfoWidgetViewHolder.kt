package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.LayoutShopHeaderBasicInfoWidgetBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentType.IMAGE_ONLY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHeaderBasicInfoWidgetViewHolder(
    itemView: View,
    private val shopHeaderBasicInfoWidgetListener: Listener
) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_basic_info_widget
    }

    interface Listener {
        fun onShopBasicInfoWidgetComponentClicked(
            componentModel: ShopHeaderBadgeTextValueComponentUiModel?,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel?
        )

        fun onImpressionShopBasicInfoWidgetComponent(
            componentModel: ShopHeaderBadgeTextValueComponentUiModel?,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel?
        )
    }

    private val viewBinding: LayoutShopHeaderBasicInfoWidgetBinding? by viewBinding()
    private val shopLogoImageView: ImageView? = viewBinding?.imageShopLogo
    private val shopBadgeImageView: ImageView? = viewBinding?.imageShopBadge
    private val shopChevronImageView: ImageView? = viewBinding?.shopPageChevronShopInfo
    private val shopNameTextView: Typography? = viewBinding?.textShopName
    private val shopOnlineImageView: ImageView? = viewBinding?.ivOnlineIcon
    private val shopBasicInfoAdditionalInfoTextView: Typography? = viewBinding?.textShopBasicInfoAdditionalInfo
    private var shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel? = null

    override fun bind(model: ShopHeaderWidgetUiModel) {
        shopHeaderWidgetUiModel = model
        for (component in model.components) {
            when {
                isMatchWidgetIdentifier(
                    component,
                    IMAGE_ONLY,
                    SHOP_LOGO
                ) -> setShopLogo(component as? ShopHeaderImageOnlyComponentUiModel)
                isMatchWidgetIdentifier(
                    component,
                    BADGE_TEXT_VALUE,
                    SHOP_NAME
                ) -> setShopNameAndInfoSection(component as? ShopHeaderBadgeTextValueComponentUiModel)
            }
        }
    }

    private fun setShopLogo(component: ShopHeaderImageOnlyComponentUiModel?) {
        val shopLogoUrl = component?.image.orEmpty()
        shopLogoImageView?.loadImageCircle(shopLogoUrl)
    }

    private fun setShopNameAndInfoSection(component: ShopHeaderBadgeTextValueComponentUiModel?) {
        val badgeImageUrl = component?.text?.getOrNull(0)?.icon.orEmpty()
        val shopName = component?.text?.getOrNull(0)?.textHtml.orEmpty()
        val shopChevronImageUrl = component?.ctaIcon.orEmpty()
        val shopAdditionalInfo = component?.text?.getOrNull(1)?.textHtml.orEmpty()
        val shopOnlineIcon = component?.text?.getOrNull(1)?.icon.orEmpty()
        shopBadgeImageView?.apply {
            if (badgeImageUrl.isNotEmpty()) {
                show()
                loadImage(badgeImageUrl)
                setOnClickListener {
                    shopHeaderBasicInfoWidgetListener.onShopBasicInfoWidgetComponentClicked(
                        component,
                        shopHeaderWidgetUiModel
                    )
                }
            } else {
                hide()
            }
        }
        shopNameTextView?.apply {
            text = MethodChecker.fromHtml(shopName)
            setOnClickListener {
                shopHeaderBasicInfoWidgetListener.onShopBasicInfoWidgetComponentClicked(
                    component,
                    shopHeaderWidgetUiModel
                )
            }
        }

        // Handle dark mode - last online status
        val lastOnlineColor = ShopUtil.getColorHexString(itemView.context, R.color.clr_dms_31353B)
        val lastOnlineUnifyColor = ShopUtil.getColorHexString(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
        val unifiedShopAdditionalInfo = shopAdditionalInfo.replace(lastOnlineColor, lastOnlineUnifyColor)
        shopBasicInfoAdditionalInfoTextView?.text = MethodChecker.fromHtml(unifiedShopAdditionalInfo)

        shopChevronImageView?.apply {
            if (shopChevronImageUrl.isNotEmpty()) {
                show()
                loadImage(shopChevronImageUrl)
                setOnClickListener {
                    shopHeaderBasicInfoWidgetListener.onShopBasicInfoWidgetComponentClicked(
                        component,
                        shopHeaderWidgetUiModel
                    )
                }
            } else {
                hide()
            }
        }
        shopOnlineImageView?.apply {
            if (shopOnlineIcon.isNotEmpty()) {
                show()
                loadImage(shopOnlineIcon)
            } else {
                hide()
            }
        }
        component?.let {
            itemView.addOnImpressionListener(component) {
                shopHeaderBasicInfoWidgetListener.onImpressionShopBasicInfoWidgetComponent(
                    it,
                    shopHeaderWidgetUiModel
                )
            }
        }
    }

    private fun isMatchWidgetIdentifier(
        component: BaseShopHeaderComponentUiModel,
        componentType: String,
        componentName: String
    ): Boolean = component.let {
        it.type.toLowerCase() == componentType.toLowerCase() && it.name.toLowerCase() == componentName.toLowerCase()
    }
}
