package com.tokopedia.browse.homepage.presentation.adapter.viewholder

import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.browse.R
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel

/**
 * @author by furqan on 07/09/18.
 */

class DigitalBrowseServiceViewHolder(itemView: View, private val categoryListener: CategoryListener) : AbstractViewHolder<DigitalBrowseServiceCategoryViewModel>(itemView) {

    private val ivProduct: AppCompatImageView = itemView.findViewById(R.id.iv_product)
    private val tvProduct: AppCompatTextView = itemView.findViewById(R.id.tv_product)
    private val tvNewLabel: AppCompatTextView = itemView.findViewById(R.id.tv_new_label)
    private val containerItem: LinearLayout = itemView.findViewById(R.id.container_item)
    private var item: DigitalBrowseServiceCategoryViewModel? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvNewLabel.letterSpacing = DEFAULT_LETTER_SPACING
        }
    }

    override fun bind(element: DigitalBrowseServiceCategoryViewModel) {
        this.item = element
        if (element.isTitle) {
            ivProduct.visibility = View.GONE
            tvProduct.setTextAppearance(ivProduct.context, R.style.TextView_Title_Bold)
            containerItem.gravity = Gravity.LEFT
            tvNewLabel.visibility = View.GONE
            itemView.isClickable = false
            itemView.setPadding(0, itemView.resources.getDimensionPixelSize(R.dimen.dp_16), 0, 0)
            itemView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            setItemView()

            itemView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            categoryListener.sendImpressionAnalytics(element, adapterPosition)
        }

        tvProduct.text = element.name

        itemView.setOnClickListener {
            if (!this@DigitalBrowseServiceViewHolder.item!!.isTitle) {
                this@DigitalBrowseServiceViewHolder.categoryListener.onCategoryItemClicked(item, adapterPosition)
            }
        }
    }

    fun bindLastItem(element: DigitalBrowseServiceCategoryViewModel) {
        this.item = element

        setItemView()

        itemView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        tvProduct.text = element.name

        categoryListener.sendImpressionAnalytics(element, adapterPosition)
    }

    private fun setItemView() {
        ivProduct.visibility = View.VISIBLE
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, this@DigitalBrowseServiceViewHolder.item!!.imageUrl,
                R.drawable.status_no_result)
        tvProduct.setTextAppearance(ivProduct.context, R.style.TextView_Micro)
        containerItem.gravity = Gravity.CENTER_HORIZONTAL
        itemView.isClickable = true
        itemView.setPadding(0, itemView.resources.getDimensionPixelSize(R.dimen.dp_8), 0, itemView.resources.getDimensionPixelSize(R.dimen.dp_8))

        if (this@DigitalBrowseServiceViewHolder.item!!.categoryLabel == "1") {
            tvNewLabel.visibility = View.VISIBLE
        } else {
            tvNewLabel.visibility = View.GONE
        }
    }

    interface CategoryListener {
        fun onCategoryItemClicked(viewModel: DigitalBrowseServiceCategoryViewModel?, itemPosition: Int)

        fun sendImpressionAnalytics(viewModel: DigitalBrowseServiceCategoryViewModel, itemPosition: Int)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_digital_browse_image_with_title

        private val DEFAULT_LETTER_SPACING = 0.1f
    }
}
