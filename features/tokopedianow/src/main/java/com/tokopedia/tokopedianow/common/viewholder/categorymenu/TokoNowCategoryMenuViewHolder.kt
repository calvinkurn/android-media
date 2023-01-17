package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.adapter.TokoNowCategoryMenuAdapter
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryMenuDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuAdapterTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryMenuViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryMenuListener? = null,
): AbstractViewHolder<TokoNowCategoryMenuUiModel>(itemView),
    TokoNowCategoryMenuItemViewHolder.TokoNowCategoryMenuItemListener,
    TokoNowCategoryMenuItemSeeAllViewHolder.TokoNowCategoryMenuItemSeeAllListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_menu
    }

    private var binding: ItemTokopedianowCategoryMenuBinding? by viewBinding()

    private val adapter by lazy {
        TokoNowCategoryMenuAdapter(
            typeFactory = TokoNowCategoryMenuAdapterTypeFactory(
                tokoNowCategoryMenuItemListener = this@TokoNowCategoryMenuViewHolder,
                tokoNowCategoryMenuItemSeeAllListener = this@TokoNowCategoryMenuViewHolder
            ),
            differ = TokoNowCategoryMenuDiffer()
        )
    }

    override fun bind(data: TokoNowCategoryMenuUiModel) {
        binding?.apply {
            when(data.state) {
                TokoNowLayoutState.SHOW -> showSuccessState(data)
                TokoNowLayoutState.LOADING -> showLoadingState()
                TokoNowLayoutState.HIDE -> showErrorState()
                else -> { /* nothing to do */ }
            }
        }
    }

    override fun onSeeAllClicked(appLink: String) {
        onClickSeeAll(appLink)
    }

    override fun onSeeAllClicked(
        headerName: String,
        appLink: String
    ) {
        onClickSeeAll(appLink)
    }

    override fun onCategoryItemClicked(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int) {
        listener?.onCategoryMenuItemClicked(data, itemPosition)
    }

    override fun onCategoryItemImpressed(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int) {
        listener?.onCategoryMenuItemImpressed(data, itemPosition)
    }

    override fun onChannelExpired() { /* nothing to do */ }

    private fun ItemTokopedianowCategoryMenuBinding.showLoadingState() {
        llCategory.hide()
        rvCategory.hide()
        header.hide()
        categoryShimmering.categoryShimmeringLayout.show()
    }

    private fun ItemTokopedianowCategoryMenuBinding.showSuccessState(
        data: TokoNowCategoryMenuUiModel
    ) {
        categoryShimmering.categoryShimmeringLayout.hide()
        llCategory.hide()
        header.show()
        rvCategory.show()

        showCategoryMenu(
            data = data
        )
    }

    private fun ItemTokopedianowCategoryMenuBinding.showCategoryMenu(
        data: TokoNowCategoryMenuUiModel
    ) {
        binding.apply {
            setHeader(data)
            setCategoryList(data)
            root.addOnImpressionListener(data) {
                listener?.onCategoryMenuWidgetImpression(data)
            }
        }
    }

    private fun ItemTokopedianowCategoryMenuBinding.setHeader(data: TokoNowCategoryMenuUiModel) {
        header.setListener(this@TokoNowCategoryMenuViewHolder)
        header.setModel(
            model = TokoNowDynamicHeaderUiModel(
                title = data.title.ifEmpty { root.context.getString(R.string.tokopedianow_repurchase_category_menu_title) },
                ctaText = root.context.getString(R.string.tokopedianow_see_all),
                ctaTextLink = data.seeAllAppLink
            )
        )
    }

    private fun ItemTokopedianowCategoryMenuBinding.setCategoryList(data: TokoNowCategoryMenuUiModel) {
        rvCategory.run {
            adapter = this@TokoNowCategoryMenuViewHolder.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        adapter.submitList(data.categoryListUiModel.orEmpty())
    }

    private fun ItemTokopedianowCategoryMenuBinding.showErrorState() {
        binding.apply {
            llCategory.apply {
                show()
                progressState = false
                title?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_title)
                description?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_description)
                refreshBtn?.setOnClickListener {
                    progressState = true
                    listener?.onCategoryMenuWidgetRetried()
                }
            }

            categoryShimmering.categoryShimmeringLayout.hide()
            rvCategory.hide()
            header.hide()
        }
    }

    private fun onClickSeeAll(appLink: String) {
        RouteManager.route(itemView.context, appLink)
        listener?.onSeeAllCategoriesClicked()
    }

    interface TokoNowCategoryMenuListener {
        fun onCategoryMenuWidgetRetried()
        fun onSeeAllCategoriesClicked()
        fun onCategoryMenuItemClicked(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int)
        fun onCategoryMenuItemImpressed(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int)
        fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel)
    }
}
