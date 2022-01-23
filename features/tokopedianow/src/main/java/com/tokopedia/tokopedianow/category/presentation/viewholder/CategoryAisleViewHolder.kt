package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_2H
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.CATEGORY_AISLE_HEADER_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryAisleBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class CategoryAisleViewHolder(
        itemView: View,
        private val categoryAisleListener: CategoryAisleListener,
): AbstractViewHolder<CategoryAisleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_aisle
    }

    private var binding: ItemTokopedianowCategoryAisleBinding? by viewBinding()

    private val aisleContainer by lazy {
        binding?.tokoNowSearchCategoryAisleContainer
    }

    private val rightAisleCard by lazy {
        binding?.tokoNowSearchCategoryAisleGroupRight
    }

    private val txtCategoryNameRight by lazy {
        binding?.tokoNowSearchCategoryAisleNameRight
    }

    private val imgCategoryRight by lazy {
        binding?.tokoNowSearchCategoryAisleImageRight
    }

    private val txtCategoryNameLeft by lazy {
        binding?.tokoNowSearchCategoryAisleNameLeft
    }

    private val imgCategoryLeft by lazy {
        binding?.tokoNowSearchCategoryAisleImageLeft
    }

    private val txtCategoryAisleHeader by lazy {
        binding?.tokoNowSearchCategoryAisleHeader
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
                bindHeaderAisle(aisle.serviceType)
                addMarginTop(aisle.serviceType)
            }
            else -> {
                bindLeftAisle(aisle.items[0])
                bindRightAisle(aisle.items[1])
                bindHeaderAisle(aisle.serviceType)
                addMarginTop(aisle.serviceType)
            }
        }
    }

    private fun addMarginTop(serviceType: String) {
        binding?.apply {
            if (serviceType == NOW_2H) {
                tokoNowSearchCategoryAisleDivider.setMargin(
                    left = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                    top = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    right = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                    bottom = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                )
            }
        }
    }

    private fun bindHeaderAisle(serviceType: String) {
        binding?.root?.context?.let {
            getString(getServiceTypeRes(
                key = CATEGORY_AISLE_HEADER_ID,
                serviceType = serviceType).orZero()
            )?.apply {
                txtCategoryAisleHeader?.text = this
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