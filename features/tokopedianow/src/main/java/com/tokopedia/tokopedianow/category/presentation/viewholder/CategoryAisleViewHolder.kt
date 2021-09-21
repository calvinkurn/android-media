package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.unifycomponents.ImageUnify

class CategoryAisleViewHolder(
        itemView: View,
        private val categoryAisleListener: CategoryAisleListener,
): AbstractViewHolder<CategoryAisleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_aisle
    }

    private val aisleContainer by lazy {
        itemView.findViewById<ConstraintLayout?>(R.id.tokoNowSearchCategoryAisleContainer)
    }

    private val rightAisleCard by lazy {
        itemView.findViewById<Group?>(R.id.tokoNowSearchCategoryAisleGroupRight)
    }

    private val txtCategoryNameRight by lazy {
        itemView.findViewById<TextView?>(R.id.tokoNowSearchCategoryAisleNameRight)
    }

    private val imgCategoryRight by lazy {
        itemView.findViewById<ImageUnify?>(R.id.tokoNowSearchCategoryAisleImageRight)
    }

    private val txtCategoryNameLeft by lazy {
        itemView.findViewById<TextView?>(R.id.tokoNowSearchCategoryAisleNameLeft)
    }

    private val imgCategoryLeft by lazy {
        itemView.findViewById<ImageUnify?>(R.id.tokoNowSearchCategoryAisleImageLeft)
    }

    init {
        setContainerBackground(itemView)
    }

    private fun setContainerBackground(itemView: View) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return

            val drawable = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.tokopedianow_ic_aisle_background,
            )
            aisleContainer?.background = drawable
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    override fun bind(aisle: CategoryAisleDataView) {
        when (aisle.items.size) {
            0 -> {
                aisleContainer?.visibility = View.GONE
            }
            1 -> {
                rightAisleCard?.visibility = View.INVISIBLE
                bindLeftAisle(aisle.items[0])
            }
            else -> {
                bindLeftAisle(aisle.items[0])
                bindRightAisle(aisle.items[1])
            }
        }
    }

    private fun bindLeftAisle(item: CategoryAisleItemDataView) {
        bindNavigationItem(item, txtCategoryNameLeft, imgCategoryLeft)
    }

    private fun bindRightAisle(item: CategoryAisleItemDataView) {
        bindNavigationItem(item, txtCategoryNameRight, imgCategoryRight)
    }

    private fun bindNavigationItem(
            item: CategoryAisleItemDataView,
            txtCategoryName: TextView?,
            imgCategory: ImageUnify?,
    ) {
        txtCategoryName?.text = item.name
        imgCategory?.loadImage(item.imgUrl)
        imgCategory?.setOnClickListener {
            categoryAisleListener.onAisleClick(item)
        }
    }
}