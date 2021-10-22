package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE
import com.tokopedia.product.detail.databinding.ItemDynamicShopCredibilityBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopCredibilityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_credibility
    }

    private val binding = ItemDynamicShopCredibilityBinding.bind(view)

    var componentTracker: ComponentTrackDataModel? = null

    init {
        showShopLoading()
    }

    override fun bind(element: ProductShopCredibilityDataModel) {
        with(binding) {
            if (element.shopName.isNotEmpty()) {
                if (componentTracker == null) {
                    componentTracker = getComponentTrackData(element)
                }
                shopName.text = MethodChecker.fromHtml(element.shopName)
                shopLocationOnline.shouldShowWithAction(element.shopLocation.isNotEmpty()) {
                    shopLocationOnline.text = view.context.getString(R.string.location_dot_builder, element.shopLocation)
                }
                setupLastActive(element.shopLastActive)
                setupBadgeAndImage(element.shopAva, element.isOs, element.isPm, element.shopTierBadgeUrl)
                setupGoApotik(element.isGoApotik)
                setupInfoRegion(element)
                setupFollow(element.isFavorite, componentTracker!!)

                shopAva.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
                }

                shopName.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
                }

                ivBadge.setOnClickListener {
                    listener.gotoShopDetail(componentTracker!!)
                }

                hideShopLoading()
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
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

    private fun setupFollow(isFavorite: Boolean, componentTrackDataModel: ComponentTrackDataModel) = with(binding) {
        if (listener.isOwner()) {
            btnFollow.hide()
        } else {
            btnFollow.show()
        }

        renderFollow(isFavorite)

        btnFollow.setOnClickListener {
            btnFollow.isClickable = false
            listener.onShopInfoClicked(it.id, componentTrackDataModel)
        }
    }

    private fun renderFollow(isFavorite: Boolean) = with(binding) {
        if (isFavorite) {
            btnFollow.text = getString(R.string.label_favorited)
            btnFollow.buttonType = UnifyButton.Type.ALTERNATE
        } else {
            btnFollow.text = getString(R.string.label_follow)
            btnFollow.buttonType = UnifyButton.Type.MAIN
        }
        btnFollow.isClickable = true
    }

    private fun setupGoApotik(isGoApotik: Boolean) = with(binding) {
        shopFeature.shouldShowWithAction(isGoApotik) {
            shopFeature.text = view.context.getString(R.string.label_go_apotik)
        }
    }

    private fun setupLastActive(shopLastActiveData: String) = with(binding) {
        val context = view.context
        shopLastActive.text = MethodChecker.fromHtml(shopLastActiveData)
        if (shopLastActiveData == context.getString(R.string.shop_online)) {
            shopLastActive.setWeight(Typography.BOLD)
            shopLastActive.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            shopLastActive.setType(Typography.BODY_3)
            shopLastActive.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    private fun setupInfoRegion(element: ProductShopCredibilityDataModel) = with(binding) {
        val data = element.infoShopData

        if (data.getOrNull(0)?.value?.isEmpty() == true) {
            shopInfoContainer1.hide()
        } else {
            shopInfoContainer1.show()
            shopInfoTitle1.text = data.getOrNull(0)?.value.orEmpty()
            shopInfoDesc1.text = data.getOrNull(0)?.desc.orEmpty()

            if (data.getOrNull(0)?.iconIsNotEmpty() == true) {
                shopInfoIc1.show()
                shopInfoIc1.setImage(data.getOrNull(0)?.icon)
            } else {
                shopInfoIc1.hide()
            }
        }

        if (data.getOrNull(1)?.value?.isEmpty() == true) {
            shopInfoContainer2.hide()
        } else {
            shopInfoContainer2.show()
            shopInfoTitle2.text = data.getOrNull(1)?.value.orEmpty()
            shopInfoDesc2.text = data.getOrNull(1)?.desc.orEmpty()

            if (data.getOrNull(1)?.iconIsNotEmpty() == true) {
                shopInfoIc2.show()
                shopInfoIc2.setImage(data.getOrNull(1)?.icon)
            } else {
                shopInfoIc2.hide()
            }
        }
    }

    private fun setupBadgeAndImage(avatar: String,
                                   isOs: Boolean,
                                   isPm: Boolean,
                                   shopTierBadgeUrl: String) = with(binding) {
        if (isNewShopBadgeEnabled()) {
            showNewBadge(shopTierBadgeUrl)
        } else {
            showOldBadge(isOs, isPm)
        }

        shopAva.loadImageCircle(avatar)
    }

    private fun isNewShopBadgeEnabled() = true

    private fun showNewBadge(shopTierBadgeUrl: String) = with(binding) {
        ivBadge.shouldShowWithAction(shopTierBadgeUrl.isNotEmpty()) {
            ivBadge.scaleType = ImageView.ScaleType.FIT_XY
            ivBadge.loadImage(shopTierBadgeUrl)
        }
    }

    private fun showOldBadge(isOs: Boolean, isPm: Boolean) = with(binding) {
        val drawable = when {
            isOs -> {
                MethodChecker.getDrawable(view.context, com.tokopedia.gm.common.R.drawable.ic_official_store_product)
            }
            isPm -> {
                MethodChecker.getDrawable(view.context, com.tokopedia.gm.common.R.drawable.ic_power_merchant)
            }
            else -> {
                null
            }
        }
        ivBadge.shouldShowWithAction(drawable != null) {
            ivBadge.loadImage(drawable)
        }
    }

    private fun hideShopLoading() = with(binding) {
        shopCredibilityContainer.show()
        shopCredibilityShimmering.root.hide()
    }

    private fun showShopLoading() = with(binding) {
        shopCredibilityContainer.hide()
        shopCredibilityShimmering.root.show()
    }

    private fun enableButton() = with(binding) {
        btnFollow.isClickable = true
    }

    private fun getComponentTrackData(element: ProductShopCredibilityDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}