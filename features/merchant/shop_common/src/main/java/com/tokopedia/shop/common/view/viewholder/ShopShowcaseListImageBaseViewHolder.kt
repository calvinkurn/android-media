package com.tokopedia.shop.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 07/04/2021
 */
abstract class ShopShowcaseListImageBaseViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_showcase_list_image
    }

    var tvShowcaseName: Typography? = null
    var tvShowcaseCount: Typography? = null
    var ivShowcaseImage: ImageUnify? = null
    var showcaseCampaignLabel: Label? = null
    var showcaseActionButton: View? = null

    abstract fun bind(element: Any)

    init {
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
        ivShowcaseImage = itemView.findViewById(R.id.ivShowcaseImage)
        showcaseCampaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
    }

    fun renderShowcaseMainInfo(element: Any, isMyShop: Boolean = false) {
        when (element) {
            is ShopEtalaseModel -> renderShowcaseMainInfo(element, isMyShop)
            is ShopEtalaseUiModel -> renderShowcaseMainInfo(element, isMyShop)
        }
    }

    inline fun setItemShowcaseClickListener(crossinline onItemClicked: () -> Unit) {
        itemView.setOnClickListener {
            onItemClicked()
        }
    }

    private fun isShowCampaignLabel(type: Int): Boolean {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN -> true
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
            else -> ""
        }
    }

    private fun adjustShowcaseNameConstraintPosition() {
        val parentConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.parent_layout)
        val constraintSet = ConstraintSet()
        val tvShowcaseNameId = R.id.tvShowcaseName
        val labelShowcaseCampaignId = R.id.showcaseCampaignLabel
        val verticalGuidelineId = R.id.guideline_action_picker2
        constraintSet.clone(parentConstraintLayout)
        if (showcaseCampaignLabel?.visibility == View.VISIBLE) {
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

    private fun renderShowcaseMainInfo(element: ShopEtalaseUiModel, isMyShop: Boolean) {
        // set showcase name & count
        setShowcaseInfo(name = element.name, count = element.count)

        // set showcase campaign label
        setCampaignLabel(element.type, isSellerView = isMyShop)

        // set showcase image
        setShowcaseImage(element.imageUrl)
    }

    private fun setShowcaseInfo(name: String, count: Int) {
        tvShowcaseName?.text = name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_product_count_text,
                count.toString()
        )
    }

    private fun setCampaignLabel(type: Int, isSellerView: Boolean) {
        showcaseCampaignLabel?.apply {
            if (isSellerView)
                shouldShowWithAction(isShowCampaignLabel(type), action = { adjustShowcaseNameConstraintPosition() })
            else
                showWithCondition(isShowCampaignLabel(type))

            setLabel(getCampaignLabelTitle(type))
        }
    }

    private fun setActionButton(isMyShop: Boolean, type: Int) {
        showcaseActionButton?.let { actionButton ->
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
                if (ivShowcaseImage?.context?.isValidGlideContext() == true) {
                    ivShowcaseImage?.setImageUrl(it)
                }
            } ?: ivShowcaseImage?.setImageUrl("")
        } catch (e: Throwable) {
        }
    }
}