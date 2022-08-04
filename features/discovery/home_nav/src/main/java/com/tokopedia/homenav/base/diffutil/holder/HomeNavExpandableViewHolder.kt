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
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.databinding.HolderHomeNavExpandableBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeNavExpandableViewHolder (
    itemView: View,
    private val mainNavListener: MainNavListener,
    private val userSession: UserSessionInterface,
    private val tokopediaPlusListener: TokopediaPlusListener
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
        private const val DEFAULT_POSITION_ACCORDION = 0
        private const val DEFAULT_TITLE = ""
    }

    private fun removeFirstPositionAccordion() {
        if (binding?.accordionExpandable?.accordionData?.isNotEmpty() == true) {
            binding?.accordionExpandable?.removeGroup(DEFAULT_POSITION_ACCORDION)
        }
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        val typeFactory = MainNavTypeFactoryImpl(mainNavListener, userSession, tokopediaPlusListener)
        adapter = HomeNavExpandableAdapter(typeFactory)
        recyclerView.layoutManager = NpaLayoutManager(itemView.context)
        recyclerView.adapter = adapter
    }

    private fun setAccordionData(element: HomeNavExpandableDataModel, recyclerView: RecyclerView) {
        val title =
            if (element.id == ClientMenuGenerator.IDENTIFIER_TITLE_ALL_CATEGORIES) itemView.context.getString(
                R.string.title_category_section
            ) else DEFAULT_TITLE
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
    }

    override fun bind(element: HomeNavExpandableDataModel) {
        removeFirstPositionAccordion()

        val recyclerView = RecyclerView(itemView.context)
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        initAdapter(recyclerView)
        setAccordionData(element, recyclerView)

        adapter?.submitList(element.menus)
    }
}