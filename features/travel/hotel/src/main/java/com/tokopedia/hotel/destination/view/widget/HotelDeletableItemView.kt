package com.tokopedia.hotel.destination.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView

open class HotelDeletableItemView : BaseCustomView {
    private var view: View? = null
    private var textView: TextView? = null
    private var buttonView: View? = null
    private var imageView: ImageView? = null
    private var onDeleteListener: OnDeleteListener? = null
    private var onTextClickListener: OnTextClickListener? = null

    @LayoutRes
    private var layoutRef = R.layout.layout_widget_deletable_item

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        init(context)
    }

    protected open fun initView(context: Context?) {
        view = View.inflate(context, R.layout.layout_widget_deletable_item, this)
        textView = view?.findViewById(R.id.item_name)
        buttonView = view?.findViewById(R.id.delete_button)
    }

    private fun init(context: Context) {
        initView(context)
        textView!!.setOnClickListener {
            if (onTextClickListener != null) {
                onTextClickListener!!.onClick()
            }
        }
        buttonView!!.setOnClickListener {
            if (onDeleteListener != null) {
                onDeleteListener!!.onDelete()
            }
        }
    }

    fun setItemName(itemName: String?) {
        if (imageView != null) {
            imageView!!.visibility = View.GONE
        }
        if (textView != null) {
            textView!!.text = itemName
            textView!!.visibility = View.VISIBLE
            textView!!.requestLayout()
        }
    }

    fun setItemDrawable(resId: Int) {
        if (textView != null) {
            textView!!.visibility = View.GONE
        }
        if (imageView != null) {
            imageView!!.setImageResource(resId)
            imageView!!.visibility = View.VISIBLE
            imageView!!.requestLayout()
        }
    }

    fun setOnDeleteListener(onDeleteListener: OnDeleteListener) {
        this.onDeleteListener = onDeleteListener
    }

    fun setOnTextClickListener(onTextClickListener: OnTextClickListener) {
        this.onTextClickListener = onTextClickListener
    }

    interface OnDeleteListener {
        fun onDelete()
    }

    interface OnTextClickListener {
        fun onClick()
    }
}