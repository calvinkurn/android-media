package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.adapter.TokoNowCategoryGridAdapter
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridAdapterTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography

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

    private var vsTitle: ViewStub? = null
    private var tvTitle: Typography? = null
    private var tvSeeAll: Typography? = null
    private var llCategory: LocalLoad? = null
    private var rvCategory: RecyclerView? = null
    private var categoryShimmering: View? = null

    private val adapter by lazy { TokoNowCategoryGridAdapter(TokoNowCategoryGridAdapterTypeFactory(this), TokoNowCategoryGridDiffer()) }

    init {
        initView()
    }

    override fun bind(data: TokoNowCategoryGridUiModel) {
        when(data.state) {
            TokoNowLayoutState.SHOW -> showCategoryGrid(data)
            TokoNowLayoutState.LOADING -> showLoadingState(data)
            TokoNowLayoutState.HIDE -> showLocalLoad(data)
        }
    }

    override fun onCategoryClicked(position: Int, categoryId: String) {
        listener?.onCategoryClicked(position, categoryId)
    }

    private fun initView() {
        itemView.apply {
            vsTitle = findViewById(R.id.vs_title)
            tvTitle = vsTitle?.inflate()?.findViewById(R.id.channel_title)
            tvSeeAll = findViewById(R.id.tv_see_all)
            llCategory = findViewById(R.id.ll_category)
            rvCategory = findViewById(R.id.rv_category)
            categoryShimmering = findViewById(R.id.category_shimmering)
        }
    }

    private fun showLoadingState(data: TokoNowCategoryGridUiModel) {
        tvTitle?.text = data.title
        llCategory?.hide()
        rvCategory?.hide()
        categoryShimmering?.show()
    }

    private fun showCategoryGrid(data: TokoNowCategoryGridUiModel) {
        tvTitle?.text = data.title
        tvSeeAll?.setOnClickListener {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(itemView.context)
            RouteManager.route(itemView.context, ApplinkConstInternalTokopediaNow.CATEGORY_LIST, localCacheModel?.warehouse_id)
            listener?.onAllCategoryClicked()
        }

        rvCategory?.apply {
            adapter = this@TokoNowCategoryGridViewHolder.adapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        adapter.submitList(data.categoryList.orEmpty())

        categoryShimmering?.hide()
        llCategory?.hide()
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
        llCategory?.show()
    }

    interface TokoNowCategoryGridListener {
        fun onCategoryRetried()
        fun onAllCategoryClicked()
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}