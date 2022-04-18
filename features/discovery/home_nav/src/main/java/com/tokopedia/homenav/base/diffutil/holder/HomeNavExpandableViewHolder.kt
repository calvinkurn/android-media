package com.tokopedia.homenav.base.diffutil.holder

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.category.view.adapter.CategoryListAdapter
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactory
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactoryImpl
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.databinding.HolderHomeNavExpandableBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeNavExpandableViewHolder (
    itemView: View,
    private val listener: HomeNavListener
): AbstractViewHolder<HomeNavExpandableDataModel>(itemView) {
    private var binding: HolderHomeNavExpandableBinding? by viewBinding()
    private var adapter: CategoryListAdapter ?= null
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_expandable
        private const val IS_EXPANDED = false
        private val ICON_ACCORDION = null
        private const val DESCRIPTION = ""
    }

    override fun bind(element: HomeNavExpandableDataModel) {
        val recyclerView = RecyclerView(itemView.context)
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initAdapter(recyclerView)
        val title = itemView.context.getString(R.string.title_category_section)
        val accordionData = AccordionDataUnify(title, DESCRIPTION, ICON_ACCORDION, ICON_ACCORDION, recyclerView, IS_EXPANDED)
        accordionData.borderBottom = false
        binding?.accordionExpandable?.addGroup(accordionData)
    }

    private fun initAdapter(recyclerView: RecyclerView) {
//        val typeFactory: CategoryListTypeFactory = CategoryListTypeFactoryImpl(listener)
//        adapter = CategoryListAdapter(typeFactory)
//        val layoutManager = NpaLayoutManager(itemView.context)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
    }
}