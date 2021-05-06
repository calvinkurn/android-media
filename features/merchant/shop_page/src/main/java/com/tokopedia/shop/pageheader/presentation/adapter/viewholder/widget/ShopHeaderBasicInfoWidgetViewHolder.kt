package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentType.IMAGE_ONLY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

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

    private val shopLogoImageView: ImageView? = itemView.findViewById(R.id.image_shop_logo)
    private val shopBadgeImageView: ImageView? = itemView.findViewById(R.id.image_shop_badge)
    private val shopChevronImageView: ImageView? = itemView.findViewById(R.id.shop_page_chevron_shop_info)
    private val shopNameTextView: Typography? = itemView.findViewById(R.id.text_shop_name)
    private val shopOnlineImageView: ImageView? = itemView.findViewById(R.id.iv_online_icon)
    private val shopBasicInfoAdditionalInfoTextView: Typography? = itemView.findViewById(R.id.text_shop_basic_info_additional_info)
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
        shopBasicInfoAdditionalInfoTextView?.text = MethodChecker.fromHtml(shopAdditionalInfo)
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
            if(shopOnlineIcon.isNotEmpty()){
                show()
                loadImage(shopOnlineIcon)
            }else{
                hide()
            }
        }
        component?.let{
            itemView.addOnImpressionListener(component){
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