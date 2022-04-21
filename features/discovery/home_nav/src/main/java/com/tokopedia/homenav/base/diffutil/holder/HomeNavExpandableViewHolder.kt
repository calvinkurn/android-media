package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavExpandableAdapter
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.databinding.HolderHomeNavExpandableBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeNavExpandableViewHolder (
    itemView: View,
    private val mainNavListener: MainNavListener,
    private val userSession: UserSessionInterface
): AbstractViewHolder<HomeNavExpandableDataModel>(itemView) {
    private var binding: HolderHomeNavExpandableBinding? by viewBinding()
    lateinit var accordionData : AccordionDataUnify
    var adapter: HomeNavExpandableAdapter ?= null
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_expandable
        private val ICON_ACCORDION = null
        private const val DESCRIPTION = ""
        private const val WITHOUT_PADDING = 0
    }

    override fun bind(element: HomeNavExpandableDataModel) {
        if (binding?.accordionExpandable?.accordionData?.isNotEmpty() == true) {
            binding?.accordionExpandable?.removeGroup(0)
        }

        val recyclerView = RecyclerView(itemView.context)
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initAdapter(recyclerView)
        val title = itemView.context.getString(R.string.title_category_section)
        accordionData = AccordionDataUnify(
            title,
            DESCRIPTION,
            ICON_ACCORDION,
            ICON_ACCORDION,
            recyclerView,
            element.isExpanded
        )
        accordionData.borderBottom = false
        accordionData.setContentPadding(
            WITHOUT_PADDING,
            WITHOUT_PADDING,
            WITHOUT_PADDING,
            WITHOUT_PADDING
        )
        binding?.accordionExpandable?.addGroup(accordionData)
        binding?.accordionExpandable?.onItemClick = { _, isExpanded ->
            element.isExpanded = isExpanded
        }
        adapter?.submitList(element.menus)
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        val typeFactory = MainNavTypeFactoryImpl(mainNavListener, userSession)
        adapter = HomeNavExpandableAdapter(typeFactory)
        recyclerView.layoutManager = NpaLayoutManager(itemView.context)
        recyclerView.adapter = adapter
    }
}