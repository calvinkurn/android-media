package com.tokopedia.product_photo_adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PhotoItemTouchHelperCallback(private val recyclerView: RecyclerView) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // dragging is limited to horizontal directions
        val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val adapter = recyclerView.adapter as ProductPhotoAdapter
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        adapter.onMoveItem(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        reduceViewHoldersTransparency(viewHolder)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        restoreTransparency(viewHolder)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun reduceViewHoldersTransparency(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            val currentPosition = viewHolder.adapterPosition
            recyclerView.adapter?.let {
                for (i in 0 until it.itemCount) {
                    if (i == currentPosition) continue
                    recyclerView.getChildAt(i)?.alpha = 0.5f
                }
            }
        }
    }

    private fun restoreTransparency(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            val currentPosition = viewHolder.adapterPosition
            recyclerView.adapter?.let {
                for (i in 0 until it.itemCount) {
                    if (i == currentPosition) continue
                    recyclerView.getChildAt(i)?.alpha = 1f
                }
            }
        }
    }
}