package com.tokopedia.createpost.view.searchtypeviewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.SearchTypeListAdapter
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.createpost.view.bottomSheet.SearchTypeData

/**
 * @author by shruti on 2022-03-21
 */

class SearchTypeHeaderViewHolder(val mainView: View, val listener: SearchCategoryBottomSheetListener)
    : SearchTypeListAdapter.ContentSearchTYpeItemVH(mainView) {

    private lateinit var itemCard: ConstraintLayout
    private lateinit var titleCard: TextView

    override fun bind(item: SearchTypeData) {

        itemCard = itemView.findViewById(R.id.search_category_header_layout)
        titleCard = itemView.findViewById(R.id.search_type_header_text)
        titleCard.text = item.text

    }



    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_category_header_item
    }

}