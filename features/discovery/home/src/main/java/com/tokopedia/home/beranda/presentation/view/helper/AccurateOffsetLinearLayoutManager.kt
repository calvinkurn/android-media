package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter

class AccurateOffsetLinearLayoutManager(context: Context?, val adapter: HomeRecycleAdapter? = null) : LinearLayoutManager(context) {

    // map of child adapter position to its height.
    private val childSizesMap = hashMapOf<Int, Int>()

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        for (i in 0 until childCount) {
            getChildAt(i)?.let {
                childSizesMap[getPosition(it)] = it.height
            }
        }
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        if (childCount == 0) {
            return 0
        }
        if(findLastVisibleItemPosition() >= (adapter?.itemCount ?: 0) - 1) {
            return super.computeVerticalScrollOffset(state)
        }

        val firstChildPosition = findFirstVisibleItemPosition()
        val firstChild = findViewByPosition(firstChildPosition)

        var scrolledY: Int = -(firstChild?.y?.toInt()?: 0)
        for (i in 0 until firstChildPosition) {
            scrolledY += childSizesMap[i] ?: 0
        }
        return scrolledY
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(SAVED_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putSerializable(SAVED_CHILD_SIZE_MAP, childSizesMap)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var savedInstanceState = state
        if(state is Bundle) {
            savedInstanceState = state.getParcelable(SAVED_INSTANCE_STATE)
            val childSizesMap = state.getSerializable(SAVED_CHILD_SIZE_MAP) as? Map<Int, Int>
            childSizesMap?.let {
                this.childSizesMap.clear()
                this.childSizesMap.putAll(it)
            }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object {
        private const val SAVED_INSTANCE_STATE = "saved_instance_state"
        private const val SAVED_CHILD_SIZE_MAP = "saved_child_size_map"
    }
}
