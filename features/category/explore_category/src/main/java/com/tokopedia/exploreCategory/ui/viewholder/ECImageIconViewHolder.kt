package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.exploreCategory.R
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup.CategoryRow
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECImageIconVHViewModel
import com.tokopedia.unifyprinciples.Typography

class ECImageIconViewHolder(itemView: View, private val iconListener: IconListener?) : AbstractViewHolder<ECImageIconVHViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.ec_item_image_icon_layout

        const val NEW_CATEGORY = "1"
    }

    private var ecTypographyIconTitle = itemView.findViewById<Typography>(R.id.ec_typography_icon_title)
    private var ecImageBg = itemView.findViewById<ImageView>(R.id.ec_image_bg)
    private val tvNewLabel = itemView.findViewById<AppCompatTextView>(R.id.ec_new_label)

    override fun bind(element: ECImageIconVHViewModel?) {
        ecTypographyIconTitle?.text = element?.categoryRow?.name
        ImageHandler.loadImageWithoutPlaceholder(ecImageBg, element?.categoryRow?.imageUrl,
                R.drawable.status_no_result)
        itemView.setOnClickListener {
            iconListener?.onIconClick(element?.categoryRow?.applinks, element?.categoryRow?.url)
            iconListener?.onIconClickEvent(element?.categoryTitle, element?.categoryId, element?.categoryRow, adapterPosition)
        }
        tvNewLabel?.visibility = if (element?.categoryRow?.categoryLabel == NEW_CATEGORY) View.VISIBLE else View.GONE
    }

    interface IconListener {
        fun onIconClick(appLink: String?, url: String?)

        fun onIconClickEvent(categoryTitle: String?, categoryId: Int?, categoryRow: CategoryRow?, position: Int)
    }
}
