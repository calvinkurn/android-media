package com.tokopedia.category.navbottomsheet.view.adapter
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.model.ChildItem
import com.tokopedia.unifyprinciples.Typography


class CategoryLevelTwoExpandableAdapter(var levelTwoList: List<ChildItem?>?)
    : BaseExpandableListAdapter() {

    val TAG = "TestingL2"

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        Log.e(TAG, "getChild:  ${levelTwoList?.get(listPosition)?.child?.get(expandedListPosition)?.name?:""} at Listposition $listPosition at expadedListPosition at $expandedListPosition")
        return levelTwoList?.get(listPosition)?.child?.get(expandedListPosition)?.name?:""
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        Log.e(TAG, "getChildId:  ${expandedListPosition.toLong()} at Listposition $listPosition at expadedListPosition at $expandedListPosition")
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertViewTemp = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        Log.e(TAG, "getChildView:  $expandedListText at Listposition $listPosition at expadedListPosition at $expandedListPosition")
        if (convertViewTemp == null) {
            val layoutInflater = parent.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewTemp = layoutInflater.inflate(R.layout.item_cat_level_three, null)
            Log.e(TAG, "getChildView: convertViewTemp was null for $expandedListText at Listposition $listPosition at expadedListPosition at $expandedListPosition")
        }
        val expandedListTextView = convertViewTemp?.findViewById<View>(R.id.expandedListItem) as? Typography
        Log.e(TAG, "getChildView:  $expandedListText at Listposition $listPosition at expadedListPosition at $expandedListPosition textView is ${expandedListTextView}")
        expandedListTextView?.text = expandedListText
        return convertViewTemp!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        Log.e(TAG, "getChildrenCount:  ${levelTwoList?.get(listPosition)?.child?.size?:0} at Listposition $listPosition")
        return levelTwoList?.get(listPosition)?.child?.size?:0
    }

    override fun getGroup(listPosition: Int): Any {
        Log.e(TAG, "getGroup:  ${levelTwoList?.get(listPosition)?.name?:""} at Listposition $listPosition")
        return levelTwoList?.get(listPosition)?.name?:""
    }

    override fun getGroupCount(): Int {
        Log.e(TAG, "getGroupCount:  ${levelTwoList?.size?:0} ")
        return levelTwoList?.size?:0
    }

    override fun getGroupId(listPosition: Int): Long {
        Log.e(TAG, "getGroupId:  ${listPosition.toLong()} ")
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var convertViewTemp = convertView
        val listTitle = getGroup(listPosition) as String
        Log.e(TAG, "getGroupView:  $listTitle at Listposition $listPosition ")
        if (convertView == null) {
            val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewTemp = layoutInflater.inflate(R.layout.item_cat_level_two, null)
            Log.e(TAG, "getGroupView: convertView was null $listTitle at Listposition $listPosition ")
        }
        val listTitleTextView = convertViewTemp?.findViewById<View>(R.id.listTitle) as? Typography
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = listTitle
        Log.e(TAG, "getGroupView:  $listTitle at Listposition $listPosition lisTitleTextView was $listTitleTextView")
        return convertViewTemp!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

}