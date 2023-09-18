package com.tokopedia.shop.common.view.viewholder

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.databinding.ItemShopShowcaseListImageBinding
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 07/04/2021
 */
abstract class ShopShowcaseListImageBaseViewHolder(
        private val itemViewBinding: ItemShopShowcaseListImageBinding
) : RecyclerView.ViewHolder(itemViewBinding.root) {

    var itemTvShowcaseName: Typography? = null
    var itemTvShowcaseCount: Typography? = null
    var itemIvShowcaseImage: ImageUnify? = null
    var itemShowcaseCampaignLabel: Label? = null
    var itemShowcaseActionButton: View? = null
    var itemSeparatorView: View? = null
    private var showcaseNameColor: Int = MethodChecker.getColor(itemViewBinding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    private var showcaseCountColor: Int = MethodChecker.getColor(itemViewBinding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
    private var itemSeparatorColor: Int = MethodChecker.getColor(itemViewBinding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN200)

    abstract fun bind(element: Any)

    init {
        itemViewBinding.apply {
            itemTvShowcaseName = tvShowcaseName
            itemTvShowcaseCount = tvShowcaseCount
            itemIvShowcaseImage = ivShowcaseImage
            itemShowcaseCampaignLabel = showcaseCampaignLabel
            itemSeparatorView = itemSeparator
        }
    }

    fun renderShowcaseMainInfo(
        element: Any,
        isMyShop: Boolean = false,
        isOverrideTheme: Boolean = false,
        shopPageColorSchema: ShopPageColorSchema = ShopPageColorSchema()
    ) {
        when (element) {
            is ShopEtalaseModel -> renderShowcaseMainInfo(element, isMyShop)
            is ShopEtalaseUiModel -> renderShowcaseUiModelMainInfo(element, isMyShop, isOverrideTheme, shopPageColorSchema)
        }
    }

    inline fun setItemShowcaseClickListener(crossinline onItemClicked: () -> Unit) {
        itemView.setOnClickListener {
            onItemClicked()
        }
    }

    private fun isShowCampaignLabel(type: Int): Boolean {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN, ShopEtalaseTypeDef.ETALASE_FLASH_SALE -> true
            else -> false
        }
    }

    private fun isShowActionButton(type: Int): Boolean {
        return !isShowCampaignLabel(type) && !isShowcaseTypeGenerated(type)
    }

    private fun getCampaignLabelTitle(type: Int): String {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN -> {
                itemView.context.getString(R.string.shop_page_showcase_npl_text)
            }
            ShopEtalaseTypeDef.ETALASE_FLASH_SALE -> {
                itemView.context.getString(R.string.shop_page_showcase_flash_sale_text)
            }
            else -> ""
        }
    }

    private fun adjustShowcaseNameConstraintPosition() {
        itemViewBinding.apply {
            val parentConstraintLayout = parentLayout
            val tvShowcaseNameId = tvShowcaseName.id
            val labelShowcaseCampaignId = showcaseCampaignLabel.id
            val verticalGuidelineId = guidelineActionPicker2.id
            val constraintSet = ConstraintSet()
            constraintSet.clone(parentConstraintLayout)
            if (itemShowcaseCampaignLabel?.visibility == View.VISIBLE) {
                constraintSet.connect(
                        tvShowcaseNameId,
                        ConstraintSet.RIGHT,
                        labelShowcaseCampaignId,
                        ConstraintSet.LEFT,
                        0
                )
            } else {
                constraintSet.connect(
                        tvShowcaseNameId,
                        ConstraintSet.RIGHT,
                        verticalGuidelineId,
                        ConstraintSet.LEFT,
                        0
                )
            }
            constraintSet.applyTo(parentConstraintLayout)
        }
    }

    private fun isShowcaseTypeGenerated(type: Int) = type == ShopEtalaseTypeDef.ETALASE_DEFAULT

    private fun renderShowcaseMainInfo(element: ShopEtalaseModel, isMyShop: Boolean) {
        // set showcase name & count
        setShowcaseInfo(name = element.name, count = element.count)

        // set showcase campaign label
        setCampaignLabel(element.type, isSellerView = isMyShop)

        // set showcase action button
        setActionButton(isMyShop, element.type)

        // set showcase image
        setShowcaseImage(element.imageUrl)
    }

    private fun renderShowcaseUiModelMainInfo(
        element: ShopEtalaseUiModel,
        isMyShop: Boolean,
        isOverrideTheme: Boolean,
        shopPageColorSchema: ShopPageColorSchema
    ) {
        // set showcase name & count
        setShowcaseInfo(name = element.name, count = element.count)

        // set showcase campaign label
        setCampaignLabel(element.type, isSellerView = isMyShop)

        // set showcase image
        setShowcaseImage(element.imageUrl)
        configColorTheme(isOverrideTheme, shopPageColorSchema)
    }

    private fun configColorTheme(
        isOverrideTheme: Boolean,
        shopPageColorSchema: ShopPageColorSchema
    ) {
        if (isOverrideTheme) {
            configReimaginedColor(shopPageColorSchema)
        } else {
            configDefaultColor()
        }
        itemTvShowcaseName?.setTextColor(showcaseNameColor)
        itemTvShowcaseCount?.setTextColor(showcaseCountColor)
        itemSeparatorView?.background = ColorDrawable(itemSeparatorColor)
    }
    private fun configReimaginedColor(shopPageColorSchema: ShopPageColorSchema) {
        showcaseNameColor = shopPageColorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        showcaseCountColor = shopPageColorSchema.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
        )
        itemSeparatorColor = shopPageColorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
        )
    }

    private fun configDefaultColor() {
        showcaseNameColor = MethodChecker.getColor(
            itemViewBinding.root.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        showcaseCountColor = MethodChecker.getColor(
            itemViewBinding.root.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        )
        itemSeparatorColor = MethodChecker.getColor(
            itemViewBinding.root.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN200
        )
    }


    private fun setShowcaseInfo(name: String, count: Int) {
        itemTvShowcaseName?.text = name
        itemTvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_product_count_text,
                count.toString()
        )
    }

    private fun setCampaignLabel(type: Int, isSellerView: Boolean) {
        itemShowcaseCampaignLabel?.apply {
            if (isSellerView)
                shouldShowWithAction(isShowCampaignLabel(type), action = { adjustShowcaseNameConstraintPosition() })
            else
                showWithCondition(isShowCampaignLabel(type))

            setLabel(getCampaignLabelTitle(type))
        }
    }

    private fun setActionButton(isMyShop: Boolean, type: Int) {
        itemShowcaseActionButton?.let { actionButton ->
            actionButton.apply {
                shouldShowWithAction(
                        shouldShow = (isMyShop && isShowActionButton(type)),
                        action = { adjustShowcaseNameConstraintPosition() }
                )
            }
        }
    }

    private fun setShowcaseImage(imageUrl: String?) {
        // try catch to avoid crash ImageUnify on loading image with Glide
        try {
            imageUrl?.let {
                if (itemIvShowcaseImage?.context?.isValidGlideContext() == true) {
                    itemIvShowcaseImage?.setImageUrl(it)
                }
            } ?: itemIvShowcaseImage?.setImageUrl("")
        } catch (e: Throwable) {
        }
    }
}
