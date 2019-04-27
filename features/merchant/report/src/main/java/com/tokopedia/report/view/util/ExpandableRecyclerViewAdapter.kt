package com.tokopedia.report.view.util

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class ExpandableRecyclerViewAdapter<P: ParentItem<C>, C: Any, PVH: ParentViewHolder<P, C>, CVH: ChildViewHolder<C>> :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap"
        private const val TYPE_PARENT = 0
        private const val TYPE_CHILD = 1
        private const val INVALID_FLAT_POSITION = -1
    }

    private val flatItemList = mutableListOf<FlattenedWrapper<P, C>>()
    protected val parentList = mutableListOf<P>()
    var expandCollapseListener: ExpandCollapseListener? = null
    private val attachedRecyclerViewPool = mutableListOf<RecyclerView>()
    private var stateMap = mutableMapOf<P, Boolean>()

    private val parentOnExpandCollapseListener = object : ParentViewHolder.ParentOnExpandCollapseListener {
        override fun onParentExpanded(flatPos: Int) {
            parentExpandedFromViewHolder(flatPos)
        }

        override fun onParentCollapsed(flatPos: Int) {
            parentCollapsedFromViewHolder(flatPos)
        }

    }

    interface ExpandCollapseListener {
        fun onParentExpanded(parentPos: Int)
        fun onParentCollapsed(parentPos: Int)
    }

    fun setParentList(parents: List<P>){
        parentList.clear()
        parentList.addAll(parents)
        notifyParentDataSetChange()
    }

    private fun isParentViewType(viewType: Int) = viewType == TYPE_PARENT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isParentViewType(viewType)){
            onCreateParentViewHolder(parent, viewType).apply {
                recyclerAdapter = this@ExpandableRecyclerViewAdapter
                onExpandCollapseListener = parentOnExpandCollapseListener
            }
        } else onCreateChildrenViewHolder(parent, viewType).apply {
            recyclerAdapter = this@ExpandableRecyclerViewAdapter
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, flatPosition: Int) {
        if (flatPosition > flatItemList.size){
            throw IllegalStateException("Trying to bind item out of bounds, size " + flatItemList.size
                    + " flatPosition " + flatPosition + ". Was the data changed without a call to notify...()?")
        }

        val item = flatItemList[flatPosition]
        if (item.isWrappedParent){
            with(holder as PVH){
                parent = item.parent
                isExpanded = item.isExpanded
                onBindParentViewHolder(this, getNearestParentPosition(flatPosition))
            }
        } else {
            with(holder as CVH){
                child = item.child
                onBindChildViewHolder(this, getNearestParentPosition(flatPosition), getChildPosition(flatPosition))
            }
        }
    }

    abstract fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): PVH

    abstract fun onCreateChildrenViewHolder(parent: ViewGroup, viewType: Int): CVH

    abstract fun onBindParentViewHolder(holder: PVH, parentPosition: Int)

    abstract fun onBindChildViewHolder(holder: CVH, parentPosition: Int, childPosition: Int)

    override fun getItemCount(): Int = flatItemList.size

    override fun getItemViewType(position: Int): Int {
        val flatItem = flatItemList[position]
        return if (flatItem.isWrappedParent) TYPE_PARENT
        else TYPE_CHILD
    }

    fun getNearestParentPosition(flatPosition: Int): Int {
        if (flatPosition == 0) return flatPosition

        return flatItemList.filterIndexed { index, flattenedWrapper ->
            index <= flatPosition && flattenedWrapper.isWrappedParent }.size - 1
    }

    fun getChildPosition(flatPosition: Int): Int {
        if (flatPosition == 0) return flatPosition
        var childIndex = 0
        flatItemList.forEachIndexed { index, flattenedWrapper ->
            if (index == flatPosition) return childIndex
            childIndex = if (flattenedWrapper.isWrappedParent) 0
            else childIndex+1
        }
        return childIndex
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerViewPool.add(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        attachedRecyclerViewPool.remove(recyclerView)
    }

    protected fun parentExpandedFromViewHolder(flatParentPosition: Int) {
        updateExpandedParent(flatItemList[flatParentPosition], flatParentPosition, true)
    }

    protected fun parentCollapsedFromViewHolder(flatParentPosition: Int) {
        updateCollapsedParent(flatItemList[flatParentPosition], flatParentPosition, true)
    }

    fun expandParent(parent: P){
        val flatParent = FlattenedWrapper(parent)
        val flatPos = flatItemList.indexOf(flatParent)
        if (flatPos == INVALID_FLAT_POSITION) return

        expandViews(flatItemList[flatPos], flatPos)
    }

    fun expandParent(parentIndex: Int){
        expandParent(parentList[parentIndex])
    }

    fun expandParentInRange(startParentPosition: Int, range: Int){
        for (i in startParentPosition until startParentPosition+range){
            expandParent(i)
        }
    }

    fun expandAllParents(){
        parentList.forEach { expandParent(it) }
    }

    fun collapseParent(parent: P){
        val flatParent = FlattenedWrapper(parent)
        val flatPos = flatItemList.indexOf(flatParent)
        if (flatPos == INVALID_FLAT_POSITION) return

        collapseViews(flatItemList[flatPos], flatPos)
    }

    fun collapseParent(parentIndex: Int){
        collapseParent(parentList[parentIndex])
    }

    fun collapseParentInRange(startParentPosition: Int, range: Int){
        for (i in startParentPosition until startParentPosition+range){
            collapseParent(i)
        }
    }

    fun collapseAllParents(){
        parentList.forEach { collapseParent(it) }
    }

    fun onSaveInstanceState(savedInstanceState: Bundle){
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateStateMap())
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?){
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPANDED_STATE_MAP))
            return

        val _stateMap = savedInstanceState.getSerializable(EXPANDED_STATE_MAP) as? HashMap<Int, Boolean> ?: return
        flatItemList.clear()
        parentList.forEachIndexed { index, parent ->
            val flatParent = FlattenedWrapper(parent)
            flatItemList.add(flatParent)

            if (_stateMap.containsKey(index) && _stateMap[index] == true){
                flatParent.isExpanded = true
                flatParent.wrappedChildList.forEach { flatItemList.add(it) }
            }
        }
        notifyDataSetChanged()
    }

    private fun generateStateMap(): HashMap<Int,Boolean>{
        val _map = hashMapOf<Int, Boolean>()
        parentList.forEachIndexed { index, p ->
            _map[index] = stateMap[p] ?: false
        }

        return _map
    }

    @Suppress("UNCHECKED_CAST")
    private fun expandViews(flattenedWrapper: FlattenedWrapper<P, C>, flatParentPos: Int) {
        attachedRecyclerViewPool.forEach {
            val holder = it.findViewHolderForAdapterPosition(flatParentPos) as? PVH
            if (holder != null && !holder.isExpanded){
                holder.isExpanded = true
            }
        }
        updateExpandedParent(flattenedWrapper, flatParentPos, false)
    }

    @Suppress("UNCHECKED_CAST")
    private fun collapseViews(flattenedWrapper: FlattenedWrapper<P, C>, flatParentPos: Int) {
        attachedRecyclerViewPool.forEach {
            val holder = it.findViewHolderForAdapterPosition(flatParentPos) as? PVH
            if (holder != null && holder.isExpanded){
                holder.isExpanded = false
            }
        }
        updateCollapsedParent(flattenedWrapper, flatParentPos, false)
    }

    private fun updateExpandedParent(flattenedWrapper: FlattenedWrapper<P, C>, flatPos: Int, triggerByClick: Boolean) {
        if (flattenedWrapper.isExpanded) return

        flattenedWrapper.isExpanded = true
        flattenedWrapper.parent?.let { stateMap.put(it, true)  }

        flattenedWrapper.wrappedChildList.forEachIndexed { index, flatChildWrapper ->
            flatItemList.add(flatPos + index + 1, flatChildWrapper)
        }

        notifyItemRangeInserted(flatPos+1, flattenedWrapper.wrappedChildList.size)

        if (triggerByClick){
            expandCollapseListener?.onParentExpanded(getNearestParentPosition(flatPos))
        }
    }

    private fun updateCollapsedParent(flattenedWrapper: FlattenedWrapper<P, C>, flatPos: Int, triggerByClick: Boolean) {
        if (!flattenedWrapper.isExpanded) return

        flattenedWrapper.isExpanded = false
        flattenedWrapper.parent?.let { stateMap.put(it, false)  }

        val children = flattenedWrapper.wrappedChildList
        for (i in children.size-1 downTo 0){
            flatItemList.removeAt(flatPos + i + 1)
        }

        notifyItemRangeRemoved(flatPos+1, flattenedWrapper.wrappedChildList.size)

        if (triggerByClick){
            expandCollapseListener?.onParentCollapsed(getNearestParentPosition(flatPos))
        }
    }

    fun notifyParentDataSetChange(preserveExpansionState: Boolean = false){
        flatItemList.clear()
        if (preserveExpansionState){
            flatItemList.addAll(generateFlattenedParentChildList(parentList, stateMap))
        } else {
            flatItemList.addAll(generateFlattenedParentChildList(parentList))
        }
        notifyDataSetChanged()
    }

    fun notifyParentInserted(parentPos: Int){
        if (parentPos >= parentList.size) return

        val parent = parentList[parentPos]

        val flatParentPosition = if (parentPos < parentList.size - 1){
            getFlatParentPosition(parentPos)
        } else flatItemList.size

        val sizeChanged = addParentWrapper(flatParentPosition, parent)
        notifyItemRangeInserted(flatParentPosition, sizeChanged)
    }

    fun notifyParentRangeInserted(parentPositionStart: Int, range: Int){
        val initialFlatParentPos: Int = if (parentPositionStart < parentList.size - range){
            getFlatParentPosition(parentPositionStart)
        } else flatItemList.size

        var flatParentPosition = initialFlatParentPos
        var sizeChanged = 0

        for (i in parentPositionStart until parentPositionStart+range){
            val parent = parentList[i]
            val changed = addParentWrapper(flatParentPosition, parent)
            flatParentPosition += changed
            sizeChanged += changed
        }

        notifyItemRangeInserted(initialFlatParentPos, sizeChanged)
    }

    private fun addParentWrapper(flatParentPosition: Int, parent: P): Int {
        var sizeChanged = 1
        val parentWrapper = FlattenedWrapper(parent)
        flatItemList.add(flatParentPosition, parentWrapper)
        if (parent.isInitiallyExpanded){
            parentWrapper.isExpanded = true
            val flattenChild = parentWrapper.wrappedChildList
            flatItemList.addAll(flatParentPosition+sizeChanged, flattenChild)
            sizeChanged += flattenChild.size
        }

        return sizeChanged
    }

    fun notifyParentRemoved(parentPos: Int){
        val flatParentPosition = getFlatParentPosition(parentPos)
        val sizeChanged = removeParentWrapper(flatParentPosition)
        notifyItemRangeRemoved(flatParentPosition, sizeChanged)
    }

    fun notifyParentRangeRemoved(parentPositionStart: Int, range: Int){
        var sizeChanged = 0
        val flatParentPositionStart = getFlatParentPosition(parentPositionStart)
        for(i in 0 until range){
            sizeChanged += removeParentWrapper(flatParentPositionStart)
        }

        notifyItemRangeRemoved(flatParentPositionStart, sizeChanged)
    }

    private fun removeParentWrapper(flatParentPositionStart: Int): Int {
        var sizeChanged = 1
        val parentWrapper = flatItemList.removeAt(flatParentPositionStart)

        if (parentWrapper.isExpanded){
            val childSize = parentWrapper.wrappedChildList.size
            for (i in 0 until childSize){
                flatItemList.removeAt(flatParentPositionStart)
                sizeChanged++
            }
        }

        return sizeChanged
    }

    private fun notifyParentChanged(parentPos: Int){
        val parent = parentList[parentPos]
        val flatParentPosition = getFlatParentPosition(parentPos)
        val sizeChanged = changeParentWrapper(flatParentPosition, parent)
        notifyItemRangeChanged(flatParentPosition, sizeChanged)
    }

    private fun notifyParentRangeChanged(parentPositionStart: Int, range: Int){
        var _parentPositionStart = parentPositionStart
        val flatParentPositionStart: Int = getFlatParentPosition(parentPositionStart)
        var flatParentPosition = flatParentPositionStart
        var sizeChanged = 0

        for (i in 0 until range){
            val parent = parentList[_parentPositionStart]
            val changed = changeParentWrapper(flatParentPosition, parent)
            sizeChanged += changed
            flatParentPosition += changed
            _parentPositionStart++
        }

        notifyItemRangeChanged(flatParentPositionStart, sizeChanged)
    }

    private fun changeParentWrapper(flatParentPosition: Int, parent: P): Int {
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parent
        var sizeChanged = 1

        if (flatParent.isExpanded){
            val flatChild = flatParent.wrappedChildList
            for (i in 0 until flatChild.size){
                flatItemList[flatParentPosition+i+1] = flatChild[i]
                sizeChanged++
            }
        }

        return sizeChanged
    }

    fun notifyParentMoved(fromParentPosition: Int, toParentPosition: Int){
        val flatFromParentPosition = getFlatParentPosition(fromParentPosition)
        val fromFlatParent = flatItemList[flatFromParentPosition]

        if (!fromFlatParent.isExpanded || (fromFlatParent.isExpanded && fromFlatParent.wrappedChildList.size == 0)){
            val flatToParentPosition = getFlatParentPosition(toParentPosition)
            val toFlatParent = flatItemList[flatToParentPosition]
            flatItemList.removeAt(flatFromParentPosition)

            val childOffset = if (toFlatParent.isExpanded){
                toFlatParent.wrappedChildList.size
            } else 0

            flatItemList.add(flatToParentPosition+childOffset, fromFlatParent)
            notifyItemMoved(flatFromParentPosition, flatToParentPosition+childOffset)
        } else {
            var sizeChanged = 0
            val childSize = fromFlatParent.wrappedChildList.size
            for (i in 0..childSize){
                flatItemList.removeAt(flatFromParentPosition)
                sizeChanged++
            }
            notifyItemRangeRemoved(flatFromParentPosition, sizeChanged)

            var flatToParentPosition = getFlatParentPosition(toParentPosition)
            var childOffset = 0

            if (flatToParentPosition != INVALID_FLAT_POSITION){
                val toFlatParent = flatItemList[flatToParentPosition]
                if (toFlatParent.isExpanded)
                    childOffset = toFlatParent.wrappedChildList.size
            } else {
                flatToParentPosition = flatItemList.size
            }

            flatItemList.add(flatToParentPosition + childOffset, fromFlatParent)
            sizeChanged = fromFlatParent.wrappedChildList.size
            flatItemList.addAll(flatToParentPosition+childOffset+1, fromFlatParent.wrappedChildList)
            notifyItemRangeInserted(flatToParentPosition+childOffset, sizeChanged)
        }
    }

    fun notifyChildInserted(parentPosition: Int, childPosition: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            val flatChild = flatParent.wrappedChildList[childPosition]
            flatItemList.add(flatParentPosition + childPosition +1, flatChild)
            notifyItemInserted(flatParentPosition + childPosition +1)
        }
    }

    fun notifyChildRangeInserted(parentPosition: Int, childPositionStart: Int, range: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            flatParent.wrappedChildList.forEachIndexed { index, flatChild ->
                flatItemList.add(flatParentPosition + childPositionStart + index + 1, flatChild)
            }

            notifyItemRangeInserted(flatParentPosition + childPositionStart +1, range)
        }
    }

    fun notifyChildRemoved(parentPosition: Int, childPosition: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            flatItemList.removeAt(flatParentPosition + childPosition +1)
            notifyItemRemoved(flatParentPosition + childPosition +1)
        }
    }

    fun notifyChildRangeRemoved(parentPosition: Int, childPositionStart: Int, range: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            for (i in 0 until range){
                flatItemList.removeAt(flatParentPosition + childPositionStart + 1)
            }

            notifyItemRangeRemoved(flatParentPosition + childPositionStart +1, range)
        }
    }

    fun notifyChildChanged(parentPosition: Int, childPosition: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            val flatChildPosition = flatParentPosition + childPosition + 1
            val child = flatParent.wrappedChildList[childPosition]
            flatItemList[flatChildPosition] = child
            notifyItemChanged(flatChildPosition)
        }
    }

    fun notifyChildRangeChanged(parentPosition: Int, childPositionStart: Int, range: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            val flatChildPosition = flatParentPosition + childPositionStart + 1
            for (i in 0 until range){
                val child = flatParent.wrappedChildList[childPositionStart+i]
                flatItemList[flatChildPosition + i] = child
            }

            notifyItemRangeChanged(flatChildPosition, range)
        }
    }

    fun notifyChildMoved(parentPosition: Int, childPositionFrom: Int, childPositionTo: Int){
        val flatParentPosition = getFlatParentPosition(parentPosition)
        val flatParent = flatItemList[flatParentPosition]
        flatParent.parent = parentList[parentPosition]

        if (flatParent.isExpanded){
            val fromChild = flatItemList.removeAt(flatParentPosition + 1 + childPositionFrom)
            flatItemList.add(flatParentPosition + 1 + childPositionTo, fromChild)

            notifyItemMoved(flatParentPosition + 1 + childPositionFrom, flatParentPosition + 1 + childPositionTo)
        }
    }

    private fun generateFlattenedParentChildList(parentList: List<P>,
                                                 savedLastExpansionState: Map<P, Boolean>? = null): List<FlattenedWrapper<P, C>> {
        val tempList = mutableListOf<FlattenedWrapper<P, C>>()
        parentList.forEach {
            val shouldExpand = savedLastExpansionState?.get(it) ?: it.isInitiallyExpanded
            generateParentWrapper(tempList, it, shouldExpand)
        }

        return tempList
    }

    private fun generateParentWrapper(list: MutableList<FlattenedWrapper<P, C>>, parent: P, shouldExpand: Boolean) {
        val flatParent = FlattenedWrapper(parent)
        list.add(flatParent)
        if (shouldExpand)
            generateExpandedChildren(list, flatParent)
    }

    private fun generateExpandedChildren(list: MutableList<FlattenedWrapper<P, C>>, flatParent: FlattenedWrapper<P, C>) {
        flatParent.isExpanded = true
        flatParent.wrappedChildList.forEach { list.add(it) }
    }

    private fun getFlatParentPosition(parentPosition: Int): Int {
        var parentCount = 0
        flatItemList.forEachIndexed { index, flattenedWrapper ->
            if (flattenedWrapper.isWrappedParent) {
                parentCount++
                if (parentCount > parentPosition) return index
            }
        }

        return INVALID_FLAT_POSITION
    }
}