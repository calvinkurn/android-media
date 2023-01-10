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
import com.tokopedia.tokopedianow.common.adapter.TokoNowCategoryGridAdapter
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuAdapterTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryMenuViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryGridListener? = null,
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
        TokoNowCategoryGridAdapter(
            typeFactory = TokoNowCategoryMenuAdapterTypeFactory(
                tokoNowCategoryMenuItemListener = this@TokoNowCategoryMenuViewHolder,
                tokoNowCategoryMenuItemSeeAllListener = this@TokoNowCategoryMenuViewHolder
            ),
            differ = TokoNowCategoryGridDiffer()
        )
    }

    override fun bind(data: TokoNowCategoryMenuUiModel) {
        binding?.apply {
            when(data.state) {
                TokoNowLayoutState.SHOW -> showSuccessState(data)
                TokoNowLayoutState.LOADING -> showLoadingState()
                TokoNowLayoutState.HIDE -> showErrorState(data)
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

    override fun onCategoryClicked(
        position: Int,
        categoryId: String,
        headerName: String,
        categoryName: String
    ) {
        listener?.onCategoryClicked(
            position = position,
            categoryId = categoryId,
            headerName = headerName,
            categoryName = categoryName
        )
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

        showCategoryGrid(
            data = data
        )
    }

    private fun ItemTokopedianowCategoryMenuBinding.showCategoryGrid(
        data: TokoNowCategoryMenuUiModel
    ) {
        binding.apply {
            setHeader(data)
            setCategoryList(data)
            root.addOnImpressionListener(data) {
                listener?.onCategoryImpression(data)
            }
        }
    }

    private fun ItemTokopedianowCategoryMenuBinding.setHeader(data: TokoNowCategoryMenuUiModel) {
        header.setListener(this@TokoNowCategoryMenuViewHolder)
        header.setModel(
            model = TokoNowDynamicHeaderUiModel(
                title = data.title.ifEmpty { root.context.getString(R.string.tokopedianow_repurchase_category_grid_title) },
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

    private fun ItemTokopedianowCategoryMenuBinding.showErrorState(
        data: TokoNowCategoryMenuUiModel
    ) {
        binding.apply {
            llCategory.apply {
                progressState = false
                title?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_title)
                description?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_description)
                refreshBtn?.setOnClickListener {
                    progressState = true
                    listener?.onCategoryRetried()
                }
            }

            categoryShimmering.categoryShimmeringLayout.hide()
            rvCategory.hide()
            header.show()
            header.setModel(
                model = TokoNowDynamicHeaderUiModel(
                    title = data.title
                )
            )
        }
    }

    private fun onClickSeeAll(appLink: String) {
        RouteManager.route(itemView.context, appLink)
    }

    interface TokoNowCategoryGridListener {
        fun onCategoryRetried()
        fun onAllCategoryClicked()
        fun onCategoryClicked(position: Int, categoryId: String, headerName: String, categoryName: String)
        fun onCategoryImpression(data: TokoNowCategoryMenuUiModel)
    }
}
