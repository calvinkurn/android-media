package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.adapter.TokoNowCategoryGridAdapter
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridAdapterTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeCategoryGridBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryGridViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryGridListener? = null,
): AbstractViewHolder<TokoNowCategoryGridUiModel>(itemView),
    TokoNowCategoryItemViewHolder.TokoNowCategoryItemListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_category_grid
        private const val GRID_SPAN_COUNT = 2
    }

    private var binding: ItemTokopedianowHomeCategoryGridBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private var tvTitle: Typography? = null
    private var llCategory: LocalLoad? = null
    private var rvCategory: RecyclerView? = null
    private var categoryShimmering: View? = null
    private var categoryHeader: RelativeLayout? = null

    private val adapter by lazy { TokoNowCategoryGridAdapter(TokoNowCategoryGridAdapterTypeFactory(this), TokoNowCategoryGridDiffer()) }

    init {
        initView()
    }

    override fun bind(data: TokoNowCategoryGridUiModel) {
        when(data.state) {
            TokoNowLayoutState.SHOW -> showCategoryGrid(data)
            TokoNowLayoutState.LOADING -> showLoadingState()
            TokoNowLayoutState.HIDE -> showLocalLoad(data)
        }
    }

    override fun onAllCategoryClicked() {
        listener?.onAllCategoryClicked()
    }

    override fun onCategoryClicked(position: Int, categoryId: String) {
        listener?.onCategoryClicked(position, categoryId)
    }

    private fun initView() {
        binding?.vsTitle?.setOnInflateListener { _, inflated ->
            stubBinding = PartialTokopedianowViewStubDcTitleBinding.bind(inflated)
        }
        binding?.vsTitle?.inflate()
        tvTitle = stubBinding?.channelTitle
        llCategory = binding?.llCategory
        rvCategory = binding?.rvCategory
        categoryShimmering = binding?.categoryShimmering?.categoryShimmeringLayout
        categoryHeader = binding?.categoryHeader
    }

    private fun showLoadingState() {
        llCategory?.hide()
        rvCategory?.hide()
        categoryHeader?.hide()
        categoryShimmering?.show()
    }

    private fun showCategoryGrid(data: TokoNowCategoryGridUiModel) {
        tvTitle?.text = if (data.title.isEmpty()) itemView.context.getString(R.string.tokopedianow_repurchase_category_grid_title) else data.title

        rvCategory?.apply {
            adapter = this@TokoNowCategoryGridViewHolder.adapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT, RecyclerView.HORIZONTAL, false)
        }

        adapter.submitList(data.categoryList.orEmpty())

        categoryShimmering?.hide()
        llCategory?.hide()
        categoryHeader?.show()
        rvCategory?.show()
    }

    private fun showLocalLoad(data: TokoNowCategoryGridUiModel) {
        llCategory?.apply {
            progressState = false
            title?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_title)
            description?.text = itemView.context.getString(R.string.tokopedianow_category_is_failed_to_display_description)
            refreshBtn?.setOnClickListener {
                progressState = true
                listener?.onCategoryRetried()
            }
            tvTitle?.text = data.title
        }
        categoryShimmering?.hide()
        rvCategory?.hide()
        categoryHeader?.show()
        llCategory?.show()
    }

    interface TokoNowCategoryGridListener {
        fun onCategoryRetried()
        fun onAllCategoryClicked()
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}