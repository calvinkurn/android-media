package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeCategoryBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryItemViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryItemListener? = null,
): AbstractViewHolder<TokoNowCategoryItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_category
    }

    private var binding: ItemTokopedianowHomeCategoryBinding? by viewBinding()

    override fun bind(data: TokoNowCategoryItemUiModel) {
        binding?.apply {
            val isFirstCategory = data.warehouseId.isNotBlank()
            checkTitleCategory(data.title)
            checkFirstData(isFirstCategory, data.imageUrl.orEmpty())
            checkLayoutClicked(data, isFirstCategory)
        }
    }

    private fun checkFirstData(isFirstCategory: Boolean, imageUrl: String) {
        binding?.apply {
            if (isFirstCategory) {
                sivCategory.loadImage(ContextCompat.getDrawable(itemView.context, R.drawable.tokopedianow_bg_all_category))
                tpAllCategory.show()
                iuChevron.show()
            } else {
                sivCategory.loadImage(imageUrl)
                tpAllCategory.hide()
                iuChevron.hide()
            }
        }
    }

    private fun checkTitleCategory(title: String) {
        binding?.apply {
            if (title.isBlank()) {
                tpCategory.hide()
            } else {
                tpCategory.text = title
            }
        }
    }

    private fun checkLayoutClicked(data: TokoNowCategoryItemUiModel, isFirstCategory: Boolean) {
        binding?.root?.setOnClickListener {
            if (isFirstCategory) {
                RouteManager.route(itemView.context, data.appLink, data.warehouseId)
                listener?.onAllCategoryClicked()
            } else {
                RouteManager.route(itemView.context, data.appLink)
                listener?.onCategoryClicked(adapterPosition, data.id)
            }
        }
    }

    interface TokoNowCategoryItemListener {
        fun onAllCategoryClicked()
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}
