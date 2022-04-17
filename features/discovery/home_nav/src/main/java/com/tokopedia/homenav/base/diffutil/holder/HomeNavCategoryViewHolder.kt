package com.tokopedia.homenav.base.diffutil.holder

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavCategoryDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.databinding.HolderHomeNavAllCategoryBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeNavCategoryViewHolder (
    itemView: View,
    private val listener: HomeNavListener
): AbstractViewHolder<HomeNavCategoryDataModel>(itemView) {
    private var binding: HolderHomeNavAllCategoryBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_all_category
        private const val IS_EXPANDED = false
    }

    override fun bind(element: HomeNavCategoryDataModel) {
        val recyclerView = RecyclerView(itemView.context)
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val x = 1
        val icon = null
        val iconUrl = null
        val title = itemView.context.getString(R.string.title_category_section)
        val description = ""

        val accordionData = AccordionDataUnify(title, description, icon, iconUrl, recyclerView, IS_EXPANDED)
        accordionData.borderBottom = false
//            .setContentPadding(if (x == 0) 0 else 16.toPx(), 4.toPx(), if (x == 0) 0 else 16.toPx(), 4.toPx())

//        accordionData.dataType = AccordionDataUnify.TYPE_ALTERNATE

        binding?.accordionCategory?.addGroup(accordionData)
    }
}