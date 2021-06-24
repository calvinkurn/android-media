package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
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
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography

class HomeCategoryGridViewHolder(
        itemView: View,
        private val listener: HomeCategoryGridListener? = null,
): AbstractViewHolder<HomeCategoryGridUiModel>(itemView), HomeCategoryItemViewHolder.HomeCategoryItemListener  {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_category_grid
        private const val GRID_SPAN_COUNT = 2
    }

    private var tvTitle: Typography? = null
    private var tvSeeAll: Typography? = null
    private var llCategory: LocalLoad? = null
    private var rvCategory: RecyclerView? = null
    private var categoryShimmering: View? = null

    private val adapter by lazy { HomeAdapter(HomeAdapterTypeFactory(homeCategoryItemListener = this), HomeListDiffer()) }

    init {
        initView()
    }

    override fun bind(data: HomeCategoryGridUiModel) {
        when(data.state) {
            HomeLayoutState.SHOW -> showCategoryGrid(data)
            HomeLayoutState.LOADING -> showLoadingState(data)
            HomeLayoutState.HIDE -> showLocalLoad(data)
        }
    }

    override fun onCategoryClicked(position: Int, categoryId: String) {
        listener?.onCategoryClicked(position, categoryId)
    }

    private fun initView() {
        itemView.apply {
            tvTitle = findViewById(R.id.tv_title)
            tvSeeAll = findViewById(R.id.tv_see_all)
            llCategory = findViewById(R.id.ll_category)
            rvCategory = findViewById(R.id.rv_category)
            categoryShimmering = findViewById(R.id.category_shimmering)
        }
    }

    private fun showLoadingState(data: HomeCategoryGridUiModel) {
        llCategory?.hide()
        rvCategory?.hide()
        categoryShimmering?.show()
        tvTitle?.text = data.title
    }

    private fun showCategoryGrid(data: HomeCategoryGridUiModel) {
        tvTitle?.text = data.title
        tvSeeAll?.setOnClickListener {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(itemView.context)
            RouteManager.route(itemView.context, ApplinkConstInternalTokopediaNow.CATEGORY_LIST, localCacheModel?.warehouse_id)
            listener?.onAllCategoryClicked()
        }

        rvCategory?.apply {
            adapter = this@HomeCategoryGridViewHolder.adapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        adapter.submitList(data.categoryList.orEmpty())

        categoryShimmering?.hide()
        llCategory?.hide()
        rvCategory?.show()
    }

    private fun showLocalLoad(data: HomeCategoryGridUiModel) {
        llCategory?.apply {
            progressState = false
            title?.text = itemView.context.getString(R.string.tokomart_category_is_failed_to_display_title)
            description?.text = itemView.context.getString(R.string.tokomart_category_is_failed_to_display_description)
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

    interface HomeCategoryGridListener {
        fun onCategoryRetried()
        fun onAllCategoryClicked()
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}