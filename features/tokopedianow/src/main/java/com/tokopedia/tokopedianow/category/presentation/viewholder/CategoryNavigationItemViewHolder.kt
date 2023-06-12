package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryNavigationItemBinding
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class CategoryNavigationItemViewHolder(
    itemView: View,
    private val listener: CategoryNavigationItemListener? = null,
): AbstractViewHolder<CategoryNavigationItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_navigation_item
    }

    private var binding: ItemTokopedianowCategoryNavigationItemBinding? by viewBinding()

    override fun bind(data: CategoryNavigationItemUiModel) {
        binding?.apply {
            setTitle(data)
            setCategoryImage(data)
            setLayout(data)
        }
    }

    private fun ItemTokopedianowCategoryNavigationItemBinding.setTitle(
        data: CategoryNavigationItemUiModel
    ) {
        tpCategoryTitle.text = data.title
    }

    private fun ItemTokopedianowCategoryNavigationItemBinding.setCategoryImage(
        data: CategoryNavigationItemUiModel
    ) {
        ivCategoryImage.loadImage(data.imageUrl)
        ivCategoryImage.setBackgroundColor(
            ViewUtil.safeParseColor(
                color = if (root.context.isDarkMode()) data.backgroundDarkColor else data.backgroundLightColor,
                defaultColor = ContextCompat.getColor(
                    itemView.context,
                    R.color.tokopedianow_card_dms_color
                )
            )
        )
        ivCategoryImage.shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(
                CornerFamily.ROUNDED,
                root.context.resources.getDimension(R.dimen.tokopedianow_category_navigation_rounded_corner)
            )
            .build()
    }

    private fun ItemTokopedianowCategoryNavigationItemBinding.setLayout(
        data: CategoryNavigationItemUiModel
    ) {
        root.setOnClickListener {
            setLayoutClicked(data)
        }
        root.addOnImpressionListener(data) {
            listener?.onCategoryItemImpressed(data, layoutPosition)
        }
    }

    private fun setLayoutClicked(data: CategoryNavigationItemUiModel) {
        RouteManager.route(itemView.context, data.appLink)
        listener?.onCategoryItemClicked(data, layoutPosition)
    }

    interface CategoryNavigationItemListener {
        fun onCategoryItemClicked(data: CategoryNavigationItemUiModel, itemPosition: Int)
        fun onCategoryItemImpressed(data: CategoryNavigationItemUiModel, itemPosition: Int)
    }
}
