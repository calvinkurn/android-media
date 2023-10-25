package com.tokopedia.cartrevamp.view.customview

import java.util.*


class ViewBinderHelper {
    private var mapStates = Collections.synchronizedMap(HashMap<String, Int>())
    private val mapLayouts: MutableMap<String, CartSwipeRevealLayout> = Collections.synchronizedMap(
        HashMap()
    )
    private val lockedSwipeSet = Collections.synchronizedSet(HashSet<String>())

    @Volatile
    private var openOnlyOne = false
    private val stateChangeLock = Any()

    fun bind(swipeLayout: CartSwipeRevealLayout, id: String) {
        if (swipeLayout.shouldRequestLayout()) {
            swipeLayout.requestLayout()
        }
        mapLayouts.values.remove(swipeLayout)
        mapLayouts[id] = swipeLayout
        swipeLayout.abort()
        swipeLayout.setDragStateChangeListener(object :
            CartSwipeRevealLayout.DragStateChangeListener {
            override fun onDragStateChanged(state: Int) {
                mapStates[id] = state
                if (openOnlyOne) {
                    closeOthers(id, swipeLayout)
                }
            }
        })

        if (!mapStates.containsKey(id)) {
            mapStates[id] = CartSwipeRevealLayout.STATE_CLOSE
            swipeLayout.close(false)
        } else {
            val state = mapStates[id]!!
            if (state == CartSwipeRevealLayout.STATE_CLOSE || state == CartSwipeRevealLayout.STATE_CLOSING || state == CartSwipeRevealLayout.STATE_DRAGGING) {
                swipeLayout.close(false)
            } else {
                swipeLayout.open(false)
            }
        }

        swipeLayout.setLockDrag(lockedSwipeSet.contains(id))
    }

    fun setOpenOnlyOne(openOnlyOne: Boolean) {
        this.openOnlyOne = openOnlyOne
    }

    private fun closeOthers(id: String, swipeLayout: CartSwipeRevealLayout?) {
        synchronized(stateChangeLock) {
            if (openCount > 1) {
                for (entry in mapStates.entries) {
                    if (entry.key != id) {
                        entry.setValue(CartSwipeRevealLayout.STATE_CLOSE)
                    }
                }
                for (layout in mapLayouts.values) {
                    if (layout !== swipeLayout) {
                        layout.close(true)
                    }
                }
            }
        }
    }

    fun closeAll() {
        synchronized(stateChangeLock) {
            if (openCount > 0) {
                for (entry in mapStates.entries) {
                    entry.setValue(CartSwipeRevealLayout.STATE_CLOSE)
                }
                for (layout in mapLayouts.values) {
                    layout.close(true)
                }
            }
        }
    }

    private val openCount: Int
        get() {
            var total = 0
            for (state in mapStates.values) {
                if (state == CartSwipeRevealLayout.STATE_OPEN || state == CartSwipeRevealLayout.STATE_OPENING) {
                    total++
                }
            }
            return total
        }

    private fun setLockSwipe(lock: Boolean, vararg id: String) {
        if (id.isEmpty()) return
        if (lock) lockedSwipeSet.addAll(listOf(*id)) else lockedSwipeSet.removeAll(
            listOf(*id).toSet()
        )
        for (s in id) {
            mapLayouts[s]?.setLockDrag(lock)
        }
    }

    fun lockSwipe(vararg id: String) {
        setLockSwipe(true, *id)
    }
}
