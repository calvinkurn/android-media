/*
 * Created By Kulomady on 11/25/16 11:52 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:52 PM
 */

package com.tokopedia.filter.newdynamicfilter.adapter

import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import java.util.HashMap

/**
 * Multi-level expandable indentable list abstractDynamicFilterAdapter.
 * Initially all elements in the list are single items. When you want to collapse an item and all its
 * descendants call [.collapseGroup]. When you want to exapand a group call [.expandGroup].
 * Note that groups inside other groups are kept collapsed.
 *
 *
 * To collapse an item and all its descendants or expand a group at a certain position
 * you can call [.toggleGroup].
 *
 *
 * To preserve state (i.e. which items are collapsed) when a configuration change happens (e.g. screen rotation)
 * you should call [.saveGroups] inside onSaveInstanceState and save the returned value into
 * the Bundle. When the activity/fragment is recreated you can call [.restoreGroups]
 * to restore the previous state. The actual data (e.g. the comments in the sample app) is not preserved,
 * so you should save it yourself with a static field or implementing Parcelable or using setRetainInstance(true)
 * or saving data to a file or something like that.
 *
 *
 * To see an example of how to extend this abstract class see MyAdapter.java in sampleapp.
 */
abstract class MultiLevelExpIndListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = MultiLevelExpIndListAdapter::class.java.simpleName
    /**
     * Indicates whether or not the observers must be notified whenever
     * [.mData] is modified.
     */
    private var mNotifyOnChange: Boolean = true

    /**
     * List of items to display.
     */
    private var mData: MutableList<ExpIndData> = ArrayList()

    /**
     * Map an item to the relative group.
     * e.g.: if the user click on item 6 then mGroups(item(6)) = {all items/groups below item 6}
     */
    private var mGroups: HashMap<ExpIndData, List<ExpIndData>> = HashMap()


    /**
     * Interface that every item to be displayed has to implement. If an object implements
     * this interface it means that it can be expanded/collapsed and has a level of indentation.
     * Note: some methods are commented out because they're not used here, but they should be
     * implemented if you want your data to be expandable/collapsible and indentable.
     * See MyComment in the sample app to see an example of how to implement this.
     */
    interface ExpIndData {
        /**
         * @return The children of this item.
         */
        val children: List<ExpIndData>?

        /**
         * @return True if this item is a group.
         */
        /**
         * @param value True if this item is a group
         */
        var isGroup: Boolean

        /**
         * @param groupSize Set the number of items in the group.
         * Note: groups contained in other groups are counted just as one, not
         * as the number of items that they contain.
         */
        fun setGroupSize(groupSize: Int)

        /** Note: actually this method is never called in MultiLevelExpIndListAdapter,
         * that's why it's not strictly required that you implement this function and so
         * it's commented out.
         * @return The number of items in the group.
         * Note: groups contained in other groups are counted just as one, not
         * as the number of items that they contain.
         */
        //int getGroupSize();

        /** Note: actually this method is never called in MultiLevelExpIndListAdapter,
         * that's why it's not strictly required that you implement this function and so
         * it's commented out.
         * @return The level of indentation in the range [0, n-1]
         */
        //int getIndentation();

        /** Note: actually this method is never called in MultiLevelExpIndListAdapter,
         * that's why it's not strictly required that you implement this function and so
         * it's commented out.
         * @param indentation The level of indentation in the range [0, n-1]
         */
        //int setIndentation(int indentation);
    }

    fun add(item: ExpIndData?) {
        if (item != null) {
            mData.add(item)
            if (mNotifyOnChange)
                notifyItemChanged(mData.size - 1)
        }
    }

    fun addAll(position: Int, data: Collection<ExpIndData>?) {
        if (data != null && data.isNotEmpty()) {
            mData.addAll(position, data)
            if (mNotifyOnChange)
                notifyItemRangeInserted(position, data.size)
        }
    }

    fun addAll(data: Collection<ExpIndData>) {
        addAll(mData.size, data)
        initCollapse(mData)
    }

    fun insert(position: Int, item: ExpIndData) {
        mData.add(position, item)
        if (mNotifyOnChange)
            notifyItemInserted(position)
    }

    private fun initCollapse(datas: List<ExpIndData>) {
        for (d in datas) {
            if (d.children?.isNotEmpty()!!) {
                collapseGroup(getDataIndex(d))
            }
        }
    }

    private fun getDataIndex(indData: ExpIndData): Int {
        return mData.indexOf(indData)
    }

    /**
     * Clear all items and groups.
     */
    fun clear() {
        if (mData.size > 0) {
            val size = mData.size
            mData.clear()
            mGroups.clear()
            if (mNotifyOnChange)
                notifyItemRangeRemoved(0, size)
        }
    }

    /**
     * Remove an item or group.If it's a group it removes also all the
     * items and groups that it contains.
     *
     * @param item The item or group to be removed.
     * @return true if this abstractDynamicFilterAdapter was modified by this operation, false otherwise.
     */
    fun remove(item: ExpIndData): Boolean {
        return remove(item, false)
    }

    /**
     * Remove an item or group. If it's a group it removes also all the
     * items and groups that it contains if expandGroupBeforeRemoval is false.
     * If it's true the group is expanded and then only the item is removed.
     *
     * @param item                     The item or group to be removed.
     * @param expandGroupBeforeRemoval True to expand the group before removing the item.
     * False to remove also all the items and groups contained if
     * the item to be removed is a group.
     * @return true if this abstractDynamicFilterAdapter was modified by this operation, false otherwise.
     */
    fun remove(item: ExpIndData?, expandGroupBeforeRemoval: Boolean): Boolean {
        val index: Int = mData.indexOf(item)
        var removed = false
        if (item != null && index != -1) {
            removed = mData.remove(item)
            if (removed) {
                if (mGroups.containsKey(item)) {
                    if (expandGroupBeforeRemoval)
                        expandGroup(index)
                    mGroups.remove(item)
                }
                if (mNotifyOnChange)
                    notifyItemRemoved(index)
            }
        }
        return removed
    }

    fun getItemAt(position: Int): ExpIndData {
        return mData[position]
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    /**
     * Expand the group at position "posititon".
     *
     * @param position The position (range [0,n-1]) of the group that has to be expanded
     */
    private fun expandGroup(position: Int) {
        val firstItem = getItemAt(position)
        if (!firstItem.isGroup) {
            return
        }

        // get the group of the descendants of firstItem
        val group = mGroups.remove(firstItem)

        firstItem.isGroup = false
        firstItem.setGroupSize(0)

        notifyItemChanged(position)
        addAll(position + 1, group)
        initCollapse(group!!)
    }

    /**
     * Collapse the descendants of the item at position "position".
     *
     * @param position The position (range [0,n-1]) of the element that has to be collapsed
     */
    private fun collapseGroup(position: Int) {
        val firstItem = getItemAt(position)

        if (firstItem.children == null || firstItem.children!!.isEmpty())
            return

        // group containing all the descendants of firstItem
        val group = ArrayList<ExpIndData>()
        // stack for depth first search
        val stack = ArrayList<ExpIndData>()
        var groupSize = 0

        for (i in firstItem.children!!.size - 1 downTo 0)
            stack.add(firstItem.children!![i])

        while (stack.isNotEmpty()) {
            val item = stack.removeAt(stack.size - 1)
            group.add(item)
            groupSize++
            // stop when the item is a leaf or a group
            if (item.children != null && item.children!!.isNotEmpty() && !item.isGroup) {
                for (i in item.children!!.size - 1 downTo 0)
                    stack.add(item.children!![i])
            }

            mData.remove(item)
        }
        mGroups[firstItem] = group
        firstItem.isGroup = true
        firstItem.setGroupSize(groupSize)

        notifyItemChanged(position)
        notifyItemRangeRemoved(position + 1, groupSize)
    }

    /**
     * Collpase/expand the item at position "position"
     *
     * @param position The position (range [0,n-1]) of the element that has to be collapsed/expanded
     */
    fun toggleGroup(position: Int) {
        if (getItemAt(position).isGroup) {
            expandGroup(position)
        } else {
            collapseGroup(position)
        }
    }

    /**
     * In onSaveInstanceState, you should save the groups' indices returned by this function
     * in the Bundle so that later they can be restored using [.restoreGroups].
     * saveGroups() expand all the groups so you should call this function only inside onSaveInstanceState.
     *
     * @return A list of indices of items that are groups.
     */
    fun saveGroups(): ArrayList<Int> {
        val groupsIndices = ArrayList<Int>()
        try {
            val notify = mNotifyOnChange
            mNotifyOnChange = false
            for (i in mData.indices) {
                if (mData[i].isGroup) {
                    expandGroup(i)
                    groupsIndices.add(i)
                }
            }
            mNotifyOnChange = notify
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return groupsIndices
        }
    }

    /**
     * Call this function to restore the groups that were collapsed before the configuration change
     * happened (e.g. screen rotation). See [.saveGroups].
     *
     * @param groupsNum The list of indices of items that are groups and should be collapsed.
     */
    fun restoreGroups(groupsNum: List<Int>?) {
        try {
            if (groupsNum == null)
                return
            val notify = mNotifyOnChange
            mNotifyOnChange = false
            for (i in groupsNum.indices.reversed()) {
                collapseGroup(groupsNum[i])
            }
            mNotifyOnChange = notify
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getData(): List<ExpIndData> {
        return mData
    }

    fun isNotifyOnChange(): Boolean {
        return mNotifyOnChange
    }

    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }
}