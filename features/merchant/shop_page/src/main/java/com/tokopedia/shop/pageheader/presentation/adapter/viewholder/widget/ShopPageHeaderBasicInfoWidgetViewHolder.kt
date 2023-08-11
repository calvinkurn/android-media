package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.ImageView
import androidx.compose.ui.unit.dp
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.LayoutShopHeaderBasicInfoWidgetBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentType.IMAGE_ONLY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.stories.common.StoriesAvatarManager
import com.tokopedia.stories.common.StoriesAvatarView
import com.tokopedia.stories.common.StoriesBorderLayout
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderBasicInfoWidgetViewHolder(
    itemView: View,
    private val shopHeaderBasicInfoWidgetListener: Listener,
    private val storiesAvatarManager: StoriesAvatarManager,
) : AbstractViewHolder<ShopPageHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_basic_info_widget
    }

    interface Listener {
        fun onShopBasicInfoWidgetComponentClicked(
            componentModel: ShopPageHeaderBadgeTextValueComponentUiModel?,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?
        )

        fun onImpressionShopBasicInfoWidgetComponent(
            componentModel: ShopPageHeaderBadgeTextValueComponentUiModel?,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?
        )
    }

    private val viewBinding: LayoutShopHeaderBasicInfoWidgetBinding? by viewBinding()
    private val shopLogoContainer: StoriesBorderLayout? = viewBinding?.imageShopContainer
    private val shopLogoImageView: ImageView? = viewBinding?.imageShopLogo
    private val shopBadgeImageView: ImageView? = viewBinding?.imageShopBadge
    private val shopChevronImageView: ImageView? = viewBinding?.shopPageChevronShopInfo
    private val shopNameTextView: Typography? = viewBinding?.textShopName
    private val shopOnlineImageView: ImageView? = viewBinding?.ivOnlineIcon
    private val shopBasicInfoAdditionalInfoTextView: Typography? = viewBinding?.textShopBasicInfoAdditionalInfo
    private var shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel? = null

    init {
//        shopLogoImageView?.updateSizeConfig {
//            it.copy(imageToBorderGap = 6.dp)
//        }
    }

    override fun bind(modelPage: ShopPageHeaderWidgetUiModel) {
        shopPageHeaderWidgetUiModel = modelPage
        for (component in modelPage.componentPages) {
            when {
                isMatchWidgetIdentifier(
                    component,
                    IMAGE_ONLY,
                    SHOP_LOGO
                ) -> setShopLogo(component as? ShopPageHeaderImageOnlyComponentUiModel)
                isMatchWidgetIdentifier(
                    component,
                    BADGE_TEXT_VALUE,
                    SHOP_NAME
                ) -> setShopNameAndInfoSection(component as? ShopPageHeaderBadgeTextValueComponentUiModel)
            }
        }
    }

    private fun setShopLogo(component: ShopPageHeaderImageOnlyComponentUiModel?) {
        val shopLogoUrl = component?.image.orEmpty()
        val shopId = component?.shopId.orEmpty()
        shopLogoImageView?.loadImageCircle(shopLogoUrl)
        shopLogoContainer?.startAnimation()

//        shopLogoImageView?.run {
//            storiesAvatarManager.manage(this, shopId)
//        }
//        shopLogoImageView?.setImageUrl(shopLogoUrl)
    }

    private fun setShopNameAndInfoSection(component: ShopPageHeaderBadgeTextValueComponentUiModel?) {
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
                        shopPageHeaderWidgetUiModel
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
                    shopPageHeaderWidgetUiModel
                )
            }
        }

        // Handle dark mode - last online status
        val lastOnlineColor = ShopUtil.getColorHexString(itemView.context, R.color.clr_dms_31353B)
        val lastOnlineUnifyColor = ShopUtil.getColorHexString(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val unifiedShopAdditionalInfo = shopAdditionalInfo.replace(lastOnlineColor, lastOnlineUnifyColor)
        shopBasicInfoAdditionalInfoTextView?.text = MethodChecker.fromHtml(unifiedShopAdditionalInfo)

        shopChevronImageView?.apply {
            if (shopChevronImageUrl.isNotEmpty()) {
                show()
                loadImage(shopChevronImageUrl)
                setOnClickListener {
                    shopHeaderBasicInfoWidgetListener.onShopBasicInfoWidgetComponentClicked(
                        component,
                        shopPageHeaderWidgetUiModel
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
                    shopPageHeaderWidgetUiModel
                )
            }
        }
    }

    private fun isMatchWidgetIdentifier(
        componentPage: BaseShopPageHeaderComponentUiModel,
        componentType: String,
        componentName: String
    ): Boolean = componentPage.let {
        it.type.toLowerCase() == componentType.toLowerCase() && it.name.toLowerCase() == componentName.toLowerCase()
    }
}
