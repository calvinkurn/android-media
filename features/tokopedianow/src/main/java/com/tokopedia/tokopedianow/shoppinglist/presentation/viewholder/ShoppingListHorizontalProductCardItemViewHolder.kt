package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ImageUtil.applyBrightnessFilter
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListHorizontalProductCardBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.ATC_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.EMPTY_STOCK
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutState.NORMAL
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutState.LOADING
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShoppingListHorizontalProductCardItemViewHolder(
    itemView: View,
    private val listener: ShoppingListHorizontalProductCardItemListener? = null
): AbstractViewHolder<ShoppingListHorizontalProductCardItemUiModel>(itemView) {
    companion object {
        private const val NORMAL_BRIGHTNESS = 1f
        private const val OOS_BRIGHTNESS = 0.5f

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_horizontal_product_card
    }

    private var binding: ItemTokopedianowShoppingListHorizontalProductCardBinding? by viewBinding()

    override fun bind(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        binding?.apply {
            when(data.state) {
                LOADING -> {
                    normalLayout.hide()
                    loadingLayout.root.show()

                    initShimmeringRightButton(data)
                    initShimmeringCheckbox(data)
                }
                NORMAL -> {
                    normalLayout.show()
                    loadingLayout.root.hide()

                    initImage(data)
                    initPrice(data)
                    initName(data)
                    initWeight(data)
                    initPercentage(data)
                    initSlashPrice(data)
                    initOtherOption(data)
                    initRightButton(data)
                    initCheckbox(data)
                }
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initImage(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        iuProduct.loadImage(data.image) {
            listener(
                onSuccess = { _,_ ->
                    iuProduct.applyBrightnessFilter(getImageBrightness(data.type))
                }
            )
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initPrice(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpPrice.showIfWithBlock(data.price.isNotBlank()) {
            text = data.price
            tpPrice.setTextColor(ContextCompat.getColor(context, if (data.type == EMPTY_STOCK) unifyprinciplesR.color.Unify_NN600 else unifyprinciplesR.color.Unify_NN950))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initName(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpName.showIfWithBlock(data.name.isNotBlank()) {
            text = data.name
            tpName.setTextColor(ContextCompat.getColor(context, if (data.type == EMPTY_STOCK) unifyprinciplesR.color.Unify_NN600 else unifyprinciplesR.color.Unify_NN950))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initWeight(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpWeight.showIfWithBlock(data.weight.isNotBlank()) {
            text = data.weight
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initPercentage(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpPercentage.showIfWithBlock(data.percentage.isNotBlank()) {
            text = data.percentage
            tpPercentage.setTextColor(ContextCompat.getColor(context, if (data.type == EMPTY_STOCK) unifyprinciplesR.color.Unify_NN400 else unifyprinciplesR.color.Unify_RN500))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initSlashPrice(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpSlashPrice.showIfWithBlock(data.slashPrice.isNotBlank()) {
            text = data.slashPrice
            paintFlags = tpSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initOtherOption(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpOtherOption.showIfWithBlock(isOos(data.type)) {
            setOnClickListener {
                listener?.onClickOtherOptions()
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initRightButton(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpAddWishlist.showIfWithBlock(data.type == PRODUCT_RECOMMENDATION) {
            val constraintSet = ConstraintSet().apply {
                clone(root)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.tp_add_wishlist,
                ConstraintSet.START,
                root.getDimens(unifyprinciplesR.dimen.unify_space_12)
            )
            constraintSet.applyTo(root)

            setOnClickListener { }
        }
        icuDelete.showIfWithBlock(data.type != PRODUCT_RECOMMENDATION) {
            val constraintSet = ConstraintSet().apply {
                clone(root)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.icu_delete,
                ConstraintSet.START,
                root.getDimens(unifyprinciplesR.dimen.unify_space_16)
            )
            constraintSet.applyTo(root)

            setOnClickListener { }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initCheckbox(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (data.type == ATC_WISHLIST) {
            iuProduct.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_8),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0)
            )
            cbProduct.show()
        } else {
            iuProduct.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0))
            cbProduct.hide()
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initShimmeringRightButton(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        loadingLayout.luAddWishlist.showIfWithBlock(data.type == PRODUCT_RECOMMENDATION) {
            val constraintSet = ConstraintSet().apply {
                clone(root)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.tp_add_wishlist,
                ConstraintSet.START,
                root.getDimens(unifyprinciplesR.dimen.unify_space_12)
            )
            constraintSet.applyTo(root)
        }
        loadingLayout.luDelete.showIfWithBlock(data.type != PRODUCT_RECOMMENDATION) {
            val constraintSet = ConstraintSet().apply {
                clone(root)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.icu_delete,
                ConstraintSet.START,
                root.getDimens(unifyprinciplesR.dimen.unify_space_16)
            )
            constraintSet.applyTo(root)
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.initShimmeringCheckbox(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (data.type == ATC_WISHLIST) {
            loadingLayout.luImage.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_8),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0)
            )
            loadingLayout.luCheckbox.show()
        } else {
            loadingLayout.luImage.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0))
            loadingLayout.luCheckbox.hide()
        }
    }

    private fun isOos(layoutType: LayoutType): Boolean = layoutType == EMPTY_STOCK

    private fun getImageBrightness(layoutType: LayoutType): Float = if (layoutType == EMPTY_STOCK) OOS_BRIGHTNESS else NORMAL_BRIGHTNESS

    interface ShoppingListHorizontalProductCardItemListener {
        fun onClickOtherOptions()
    }
}
