package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryJumperBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryJumperChipsBinding
import com.tokopedia.tokopedianow.search.presentation.listener.CategoryJumperListener
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class SearchCategoryJumperViewHolder(
        itemView: View,
        private val categoryJumperListener: CategoryJumperListener,
): AbstractViewHolder<CategoryJumperDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_jumper
    }

    private var binding: ItemTokopedianowSearchCategoryJumperBinding? by viewBinding()

    private val divider: View? by lazy {
        binding?.tokoNowSearchCategoryJumperTopDivider
    }

    private val titleTypography: Typography? by lazy {
        binding?.tokoNowSearchCategoryJumperTitle
    }

    private val seeAllCategoryTypography: Typography? by lazy {
        binding?.tokoNowSearchCategoryJumperSeeAll
    }

    private val recyclerView: RecyclerView? by lazy {
        binding?.tokoNowSearchCategoryJumperRecyclerView
    }

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    private val spacingItemDecoration = ItemDecoration(
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    override fun bind(element: CategoryJumperDataView?) {
        element ?: return

        bindTitle(element)
        bindSeeAllCategory()
        bindItemList(element)
        addMarginTop(element.serviceType)
    }

    private fun bindTitle(element: CategoryJumperDataView) {
        titleTypography?.text = element.title
    }

    private fun bindSeeAllCategory() {
        seeAllCategoryTypography?.setOnClickListener {
            categoryJumperListener.onSeeAllCategoryClicked()
        }
    }

    private fun bindItemList(element: CategoryJumperDataView) {
        val recyclerView = recyclerView ?: return

        val itemList = element.itemList
        if (itemList.isEmpty()) return

        recyclerView.adapter = Adapter(itemList, categoryJumperListener)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecorationIfNotExists(spacingItemDecoration)
    }

    private fun addMarginTop(serviceType: String) {
        binding?.apply {
            if (serviceType == ServiceType.NOW_2H) {
                divider?.setMargin(
                    left = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                    top = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    right = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                    bottom = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                )
            }
        }
    }

    private fun RecyclerView.addItemDecorationIfNotExists(
            itemDecoration: RecyclerView.ItemDecoration
    ) {
        val hasNoItemDecoration = itemDecorationCount == 0
        if (hasNoItemDecoration) addItemDecoration(itemDecoration)
    }

    private class ItemDecoration(
            private val horizontalSpacing: Int,
            private val verticalSpacing: Int
    ): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
        ) {
            outRect.right = this.horizontalSpacing
            outRect.bottom = this.verticalSpacing
        }
    }

    private class Adapter(
            private val itemList: List<CategoryJumperDataView.Item>,
            private val categoryJumperListener: CategoryJumperListener,
    ) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                    ViewHolder.LAYOUT,
                    parent,
                    false,
            )
            return ViewHolder(view, categoryJumperListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        override fun getItemCount() = itemList.size
    }

    private class ViewHolder(
            itemView: View,
            private val categoryJumperListener: CategoryJumperListener,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            @JvmField
            @LayoutRes
            val LAYOUT = R.layout.item_tokopedianow_search_category_jumper_chips
        }

        private var binding: ItemTokopedianowSearchCategoryJumperChipsBinding? by viewBinding()

        fun bind(item: CategoryJumperDataView.Item) {
            binding?.tokoNowSearchCategoryJumperChips?.apply {
                chipText = item.title
                chipType = ChipsUnify.TYPE_ALTERNATE
                chipSize = ChipsUnify.SIZE_SMALL
                setOnClickListener {
                    categoryJumperListener.onCategoryJumperItemClick(item)
                }
            }
        }
    }
}