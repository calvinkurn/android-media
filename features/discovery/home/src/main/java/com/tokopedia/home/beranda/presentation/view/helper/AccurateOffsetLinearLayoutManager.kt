package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import java.lang.reflect.Type

class AccurateOffsetLinearLayoutManager(context: Context, val adapter: HomeRecycleAdapter? = null) : LinearLayoutManager(context) {

    private val cacheManager = SaveInstanceCacheManager(context, ACCURATE_LAYOUT_MANAGER)
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

    override fun onSaveInstanceState(): Parcelable? {
        cacheManager.put(SAVED_CHILD_SIZE_MAP, childSizesMap)
        return super.onSaveInstanceState()
    }

    @SuppressLint("RestrictedApi")
    override fun onRestoreInstanceState(state: Parcelable?) {
        try {
            val type: Type = object : TypeToken<HashMap<Int, Int>>() {}.type
            val childSizesMap = cacheManager.get(SAVED_CHILD_SIZE_MAP, type, hashMapOf<Int, Int>())
            childSizesMap?.let {
                this.childSizesMap.clear()
                this.childSizesMap.putAll(it)
            }
            super.onRestoreInstanceState(state)
        } catch (_: Exception) { }
    }

    companion object {
        private const val ACCURATE_LAYOUT_MANAGER = "accurate_layout_manager"
        private const val SAVED_CHILD_SIZE_MAP = "saved_child_size_map"
    }
}
