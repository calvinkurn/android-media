package com.tokopedia.product_photo_adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
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
        hideCloseButton(viewHolder)
        reduceViewHoldersTransparency(viewHolder)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        showCloseButton(viewHolder)
        restoreTransparency(viewHolder)
        recyclerView.post {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun hideCloseButton(viewHolder: RecyclerView.ViewHolder?) {
        val closeButton = viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.iv_delete_button)
        closeButton?.visibility = View.GONE
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

    private fun showCloseButton(viewHolder: RecyclerView.ViewHolder?) {
        val closeButton = viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.iv_delete_button)
        closeButton?.visibility = View.VISIBLE
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