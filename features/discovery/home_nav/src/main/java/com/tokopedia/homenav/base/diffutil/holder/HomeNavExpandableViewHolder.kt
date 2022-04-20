package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavExpandableAdapter
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.databinding.HolderHomeNavExpandableBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by dhaba
 */
class HomeNavExpandableViewHolder (
    itemView: View,
    private val mainNavListener: MainNavListener,
    private val userSession: UserSessionInterface
): AbstractViewHolder<HomeNavExpandableDataModel>(itemView) {
    private var binding: HolderHomeNavExpandableBinding? by viewBinding()
    var adapter: HomeNavExpandableAdapter ?= null
    val recyclerView = RecyclerView(itemView.context)
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_expandable
        private const val IS_EXPANDED = false
        private val ICON_ACCORDION = null
        private const val DESCRIPTION = ""
        private const val WITHOUT_PADDING = 0
    }

    fun submitList(menus: List<Visitable<*>>) {
//        binding?.accordionExpandable?.removeGroup(0)
//        recyclerView.layoutParams =
//            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        containerLayout.addView(recyclerView)
//        initAdapter(recyclerView)
//        recyclerView.setOnClickListener {
//            recyclerView.gone()
//        }
//        val title = itemView.context.getString(R.string.title_category_section)
//        val accordionData = AccordionDataUnify(title, DESCRIPTION, ICON_ACCORDION, ICON_ACCORDION, recyclerView, IS_EXPANDED)
//        accordionData.borderBottom = false
//        accordionData.setContentPadding(
//            WITHOUT_PADDING,
//            WITHOUT_PADDING,
//            WITHOUT_PADDING,
//            WITHOUT_PADDING
//        )
//        binding?.accordionExpandable?.addGroup(accordionData)
//        recyclerView.setHasFixedSize(false)
        GlobalScope.launch {
            adapter?.submitList(menus)
            delay(1000L)
            recyclerView.invalidate()
    //        recyclerView.setHasFixedSize(true)
            recyclerView.post {
                val height = recyclerView.height
                val x = 1
                binding?.accordionExpandable?.accordionData
            }
        }
    }

    override fun bind(element: HomeNavExpandableDataModel) {
//        val containerLayout = LinearLayout(itemView.context)
//        containerLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        containerLayout.orientation = LinearLayout.VERTICAL
//        val tv = TextView(itemView.context)
//        tv.layoutParams =
//            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        tv.text = "Content"
//        containerLayout.addView(tv)
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        recyclerView.setHasFixedSize(false)
//        containerLayout.addView(recyclerView)
        initAdapter(recyclerView)
//        recyclerView.setOnClickListener {
//            recyclerView.gone()
//        }
        val title = itemView.context.getString(R.string.title_category_section)
        val accordionData = AccordionDataUnify(title, DESCRIPTION, ICON_ACCORDION, ICON_ACCORDION, recyclerView, IS_EXPANDED)
        accordionData.borderBottom = false
        accordionData.setContentPadding(
            WITHOUT_PADDING,
            WITHOUT_PADDING,
            WITHOUT_PADDING,
            WITHOUT_PADDING
        )
        binding?.accordionExpandable?.addGroup(accordionData)
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        val typeFactory = MainNavTypeFactoryImpl(mainNavListener, userSession)
        adapter = HomeNavExpandableAdapter(typeFactory)
        val layoutManager = LinearLayoutManager(itemView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}