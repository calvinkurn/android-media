package com.tokopedia.browse.homepage.presentation.adapter.viewholder

import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.browse.R
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowseCategoryViewHolder(itemView: View, private val categoryListener: CategoryListener) :
        AbstractViewHolder<DigitalBrowseRowViewModel>(itemView) {

    private val ivProduct: AppCompatImageView = itemView.findViewById(R.id.iv_product)
    private val tvProduct: AppCompatTextView = itemView.findViewById(R.id.tv_product)
    private val tvNewLabel: AppCompatTextView = itemView.findViewById(R.id.tv_new_label)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvNewLabel.letterSpacing = DEFAULT_LETTER_SPACING
        }
    }

    override fun bind(element: DigitalBrowseRowViewModel) {
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, element.imageUrl,
                R.drawable.status_no_result)
        tvProduct.text = element.name

        if (element.categoryLabel == "1") {
            tvNewLabel.visibility = View.VISIBLE
        } else {
            tvNewLabel.visibility = View.GONE
        }

        itemView.setOnClickListener { categoryListener.onCategoryItemClicked(element, adapterPosition) }

        categoryListener.sendImpressionAnalytics(element.name, adapterPosition + 1)
    }

    interface CategoryListener {
        fun onCategoryItemClicked(viewModel: DigitalBrowseRowViewModel, itemPosition: Int)

        fun sendImpressionAnalytics(iconName: String?, iconPosition: Int)
    }

    companion object {

        @LayoutRes
        var LAYOUT = R.layout.item_digital_browse_image_with_title

        private val DEFAULT_LETTER_SPACING = 0.1f
    }
}
