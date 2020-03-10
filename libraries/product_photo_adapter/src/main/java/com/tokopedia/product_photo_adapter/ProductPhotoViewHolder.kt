package com.tokopedia.product_photo_adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler

class ProductPhotoViewHolder(itemView: View,
                             private val onDeleteButtonClickListener: OnDeleteButtonClickListener,
                             private val onStartDragListener: OnStartDragListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClicked(position: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    private var context: Context? = null
    private var productPhotoLayoutView: View? = null
    private var primaryStrokeView: View? = null
    private var productPhotoView: AppCompatImageView? = null
    private var primaryBadgeView: View? = null
    private var dragHandleView: AppCompatImageView? = null
    private var deleteButton: AppCompatImageView? = null

    init {
        // get the context from item view
        itemView.context?.let { context = it }

        // setup the views
        productPhotoLayoutView = itemView.findViewById(R.id.product_photo_layout)
        primaryStrokeView = itemView.findViewById(R.id.primary_stroke_view)
        productPhotoView = itemView.findViewById(R.id.iv_product_photo)
        primaryBadgeView = itemView.findViewById(R.id.primary_badge)
        dragHandleView = itemView.findViewById(R.id.iv_drag_handle)
        deleteButton = itemView.findViewById(R.id.iv_delete_button)

        dragHandleView?.setOnTouchListener { _, _ ->
            onStartDragListener.onStartDrag(this)
            true
        }

        deleteButton?.setOnClickListener {
            onDeleteButtonClickListener.onDeleteButtonClicked(adapterPosition)
        }
    }

    fun bindData(position: Int, productPhotoPath: String) {
        // load product photo to image view
        context?.let {
            loadProductPhotoToImageView(it, productPhotoView, productPhotoPath)
        }
        // show product photo attributes
        if (position == 0) showPrimaryAttribute()
        else showDragHandle()
    }

    private fun showPrimaryAttribute() {
        primaryStrokeView?.visibility = View.VISIBLE
        primaryBadgeView?.visibility = View.VISIBLE
        dragHandleView?.visibility = View.GONE
    }

    private fun showDragHandle() {
        primaryStrokeView?.visibility = View.GONE
        primaryBadgeView?.visibility = View.GONE
        dragHandleView?.visibility = View.VISIBLE
    }

    private fun loadProductPhotoToImageView(context: Context, productPhotoView: AppCompatImageView?, productPhotoPath: String) {
        productPhotoView?.let {
            ImageHandler.loadImageFit2(context, productPhotoView, productPhotoPath)
        }
    }
}