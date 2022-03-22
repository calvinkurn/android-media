package com.tokopedia.createpost.view.searchtypeviewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.SearchTypeListAdapter
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.createpost.view.bottomSheet.SearchTypeData
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.ImageUnify


class SearchTypeContentViewHolder(
    val mainView: View,
    val listener: SearchCategoryBottomSheetListener
)
    : SearchTypeListAdapter.ContentSearchTYpeItemVH(mainView) {

    private lateinit var itemCard: ConstraintLayout
    private lateinit var titleCard: TextView
    private lateinit var icon: ImageUnify

    override fun bind(searchTypeData: SearchTypeData) {

        itemCard = itemView.findViewById(R.id.search_category_content_layout)
        titleCard = itemView.findViewById(R.id.search_type_content_text)
        titleCard.text = searchTypeData.text
        itemCard.setOnClickListener(getItemClickNavigationListener(listener, searchTypeData))
        icon = itemView.findViewById(R.id.content_creator_icon)

         if (searchTypeData.iconId!=0)
             icon.setImageDrawable(
                 getIconUnifyDrawable(mainView.context, searchTypeData.iconId,
                     MethodChecker.getColor(mainView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)))

         if (searchTypeData.iconUrl.isNotEmpty())
             icon.setImageUrl(searchTypeData.iconUrl)

    }

    private fun getItemClickNavigationListener(
        listener: SearchCategoryBottomSheetListener,
        item: SearchTypeData
    )
            : View.OnClickListener {
        return View.OnClickListener {
            listener.onItemCLick(item)

        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_category_item
    }
}