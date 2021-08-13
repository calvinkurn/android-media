package com.tokopedia.category.navbottomsheet.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.model.ChildItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography


class CategoryLevelTwoExpandableAdapter(var levelTwoList: List<ChildItem?>?, var selectedL3Position: Int = -1)
    : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
//        Expanded List Position -1 because expandedList position 0 signifies Semua L2
        return if (expandedListPosition == 0)
            "Semua ${levelTwoList?.get(listPosition)?.name}"
        else levelTwoList?.get(listPosition)?.child?.get(expandedListPosition - 1)?.name ?: ""
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertViewTemp = convertView
        if (convertViewTemp == null) {
            val layoutInflater = parent.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewTemp = layoutInflater.inflate(R.layout.item_cat_level_three, null)
        }
        val expandedListTextView = convertViewTemp?.findViewById<Typography>(R.id.expandedListItem)
        expandedListTextView?.text = getChild(listPosition, expandedListPosition) as String
        val selectedIcon = convertViewTemp?.findViewById<ImageView>(R.id.selected_icon)
        if (selectedL3Position == expandedListPosition) {
            selectedIcon?.loadImageDrawable(R.drawable.category_bottom_nav_ic_checklist)
            selectedIcon?.show()
        } else {
            selectedIcon?.invisible()
        }
        return convertViewTemp!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return levelTwoList?.get(listPosition)?.child?.size?.let { it + 1 } ?: 0
    }

    override fun getGroup(listPosition: Int): Any {
        return levelTwoList?.get(listPosition)?.name ?: ""
    }

    override fun getGroupCount(): Int {
        return levelTwoList?.size ?: 0
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {

        return if (levelTwoList?.get(listPosition)?.viewType == 1)
            inflateL2View(listPosition, isExpanded, convertView, parent)
        else
            inflateL2ShimmerView(convertView, parent)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    private fun inflateL2View(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var convertViewTemp = convertView
        if (convertView == null) {
            val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewTemp = layoutInflater.inflate(R.layout.item_cat_level_two, null)
        }
        val listTitleTextView = convertViewTemp?.findViewById<Typography>(R.id.listTitle)
        listTitleTextView?.text = getGroup(listPosition) as String
        val expandedIcon = convertViewTemp?.findViewById<IconUnify>(R.id.expand_icon)
        expandedIcon?.setImage(if (isExpanded)IconUnify.CHEVRON_UP else IconUnify.CHEVRON_DOWN)
        return convertViewTemp!!
    }

    private fun inflateL2ShimmerView(convertView: View?, parent: ViewGroup): View {
        var convertViewTemp = convertView
        if (convertView == null) {
            val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewTemp = layoutInflater.inflate(R.layout.item_shimmer_cat_level_two, null)
        }
        return convertViewTemp!!
    }

}