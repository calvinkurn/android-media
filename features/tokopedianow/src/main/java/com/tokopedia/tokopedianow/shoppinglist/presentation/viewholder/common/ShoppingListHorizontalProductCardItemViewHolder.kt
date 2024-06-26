package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.util.ImageUtil.NORMAL_BRIGHTNESS
import com.tokopedia.tokopedianow.common.util.ImageUtil.OOS_BRIGHTNESS
import com.tokopedia.tokopedianow.common.util.ImageUtil.applyBrightnessFilter
import com.tokopedia.tokopedianow.common.util.TypographyUtil.setLeftImageDrawable
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListHorizontalProductCardBinding
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION_ADDED
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShoppingListHorizontalProductCardItemViewHolder(
    itemView: View,
    private val listener: ShoppingListHorizontalProductCardItemListener? = null
): AbstractViewHolder<ShoppingListHorizontalProductCardItemUiModel>(itemView) {
    companion object {
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
                    setupLoadingLayout()
                    setupShimmeringRightButton(data)
                    setupShimmeringCheckbox(data)
                }
                SHOW -> {
                    setupNormalLayout()
                    setupImage(data)
                    setupPrice(data)
                    setupName(data)
                    setupWeight(data)
                    setupPercentage(data)
                    setupSlashPrice(data)
                    setupOtherOption(data)
                    setupRightButton(data)
                    setupCheckbox(data)
                    setupAppLink(data)
                    setupImpression(data)
                }
                else -> { /* nothing to do */ }
            }
        }
    }

    override fun bind(
        data: ShoppingListHorizontalProductCardItemUiModel,
        payloads: MutableList<Any>
    ) {
        binding?.apply {
            if (payloads.firstOrNull() == true) {
                setupCheckbox(data)
            } else {
                bind(data)
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupNormalLayout() {
        normalLayout.show()
        loadingLayout.root.hide()
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupLoadingLayout() {
        normalLayout.hide()
        loadingLayout.root.show()
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupImage(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        iuProduct.loadImage(data.image) {
            listener(
                onSuccess = { _,_ ->
                    iuProduct.applyBrightnessFilter(getImageBrightness(data.productLayoutType))
                }
            )
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupPrice(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpPrice.showIfWithBlock(data.price.isNotBlank()) {
            text = data.price
            tpPrice.setTextColor(ContextCompat.getColor(context, if (data.productLayoutType == UNAVAILABLE_SHOPPING_LIST) unifyprinciplesR.color.Unify_NN600 else unifyprinciplesR.color.Unify_NN950))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupName(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpName.showIfWithBlock(data.name.isNotBlank()) {
            text = data.name
            tpName.setTextColor(ContextCompat.getColor(context, if (data.productLayoutType == UNAVAILABLE_SHOPPING_LIST) unifyprinciplesR.color.Unify_NN600 else unifyprinciplesR.color.Unify_NN950))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupWeight(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpWeight.showIfWithBlock(data.weight.isNotBlank()) {
            text = data.weight
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupPercentage(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpPercentage.showIfWithBlock(data.percentage.isNotBlank() && data.percentage.toIntSafely() != Int.ZERO) {
            text = data.percentage
            tpPercentage.setTextColor(ContextCompat.getColor(context, if (data.productLayoutType == UNAVAILABLE_SHOPPING_LIST) unifyprinciplesR.color.Unify_NN400 else unifyprinciplesR.color.Unify_RN500))
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupSlashPrice(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpSlashPrice.showIfWithBlock(data.slashPrice.isNotBlank() && data.percentage.toIntSafely() != Int.ZERO) {
            text = data.slashPrice
            paintFlags = tpSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupOtherOption(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpOtherOption.showIfWithBlock(isOos(data.productLayoutType)) {
            setOnClickListener {
                listener?.onClickAnotherOption(data)
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupRightButton(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        tpAddWishlist.showIfWithBlock(data.productLayoutType == PRODUCT_RECOMMENDATION || data.productLayoutType == PRODUCT_RECOMMENDATION_ADDED) {
            val color: Int
            val icon: Drawable?

            if (data.productLayoutType == PRODUCT_RECOMMENDATION) {
                color = getColor(context, unifyprinciplesR.color.Unify_GN500)
                icon = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_add)
                text = getString(R.string.tokopedianow_shopping_list_horizontal_product_card_add_to_shopping_list)
                setOnClickListener {
                    listener?.onClickAddToShoppingList(data)
                }
            } else {
                color = getColor(context, unifyprinciplesR.color.Unify_NN600)
                icon = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_check_big)
                text = getString(R.string.tokopedianow_shopping_list_horizontal_product_card_product_available)
                setOnClickListener(null)
            }

            setLeftImageDrawable(
                drawable = icon,
                width = root.getDimens(R.dimen.tokopedianow_shopping_list_plus_and_check_icon_size),
                height = root.getDimens(R.dimen.tokopedianow_shopping_list_plus_and_check_icon_size),
                color = color
            )

            setTextColor(color)

            val constraintSet = ConstraintSet().apply {
                clone(normalLayout)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.tp_add_wishlist,
                ConstraintSet.START,
                normalLayout.getDimens(unifyprinciplesR.dimen.unify_space_12)
            )
            constraintSet.applyTo(normalLayout)
        }
        icuDelete.showIfWithBlock(data.productLayoutType != PRODUCT_RECOMMENDATION && data.productLayoutType != PRODUCT_RECOMMENDATION_ADDED) {
            val constraintSet = ConstraintSet().apply {
                clone(normalLayout)
            }
            constraintSet.connect(
                R.id.layout_info,
                ConstraintSet.END,
                R.id.icu_delete,
                ConstraintSet.START,
                normalLayout.getDimens(unifyprinciplesR.dimen.unify_space_16)
            )
            constraintSet.applyTo(normalLayout)

            setOnClickListener {
                listener?.onClickDeleteIcon(data)
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupCheckbox(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (data.productLayoutType == AVAILABLE_SHOPPING_LIST) {
            iuProduct.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_8),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0)
            )
            cbProduct.show()
            cbProduct.setOnCheckedChangeListener(null)
            cbProduct.isChecked = data.isSelected
            cbProduct.setOnCheckedChangeListener { _, isSelected ->
                listener?.onSelectCheckbox(data, isSelected)
            }
        } else {
            iuProduct.setMargin(
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0),
                root.getDimens(unifyprinciplesR.dimen.unify_space_0))
            cbProduct.hide()
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupShimmeringRightButton(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        loadingLayout.luAddWishlist.showIfWithBlock(data.productLayoutType == PRODUCT_RECOMMENDATION) {
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
        loadingLayout.luDelete.showIfWithBlock(data.productLayoutType != PRODUCT_RECOMMENDATION) {
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

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupShimmeringCheckbox(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (data.productLayoutType == AVAILABLE_SHOPPING_LIST) {
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

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupAppLink(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        root.setOnClickListener {
            if (data.appLink.isNotBlank()) {
                RouteManager.route(root.context, data.appLink)
                listener?.onClickProduct(data)
            }
        }
    }

    private fun ItemTokopedianowShoppingListHorizontalProductCardBinding.setupImpression(
        data: ShoppingListHorizontalProductCardItemUiModel
    ) {
        root.addOnImpressionListener(data) {
            listener?.onImpressProduct(data)
        }
    }

    private fun isOos(type: ShoppingListProductLayoutType): Boolean = type == UNAVAILABLE_SHOPPING_LIST

    private fun getImageBrightness(type: ShoppingListProductLayoutType): Float = if (type == UNAVAILABLE_SHOPPING_LIST) OOS_BRIGHTNESS else NORMAL_BRIGHTNESS

    interface ShoppingListHorizontalProductCardItemListener {
        fun onSelectCheckbox(
            product: ShoppingListHorizontalProductCardItemUiModel,
            isSelected: Boolean
        )
        fun onClickAnotherOption(
            product: ShoppingListHorizontalProductCardItemUiModel
        )
        fun onClickDeleteIcon(
            product: ShoppingListHorizontalProductCardItemUiModel
        )
        fun onClickAddToShoppingList(
            product: ShoppingListHorizontalProductCardItemUiModel
        )
        fun onClickProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        )
        fun onImpressProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        )
    }
}
