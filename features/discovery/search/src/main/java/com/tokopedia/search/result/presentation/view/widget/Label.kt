package com.tokopedia.search.result.presentation.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.tokopedia.search.R

class Label : AppCompatTextView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.Label)

            configLabelDesign(attributeArray)

            attributeArray.recycle()
        }
    }

    private fun configLabelDesign(attributeArray: TypedArray) {
        val labelType = attributeArray.getString(R.styleable.Label_labelType)
        val drawable = ContextCompat.getDrawable(context, R.drawable.search_label)

        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.green_500), PorterDuff.Mode.MULTIPLY)

//        val sdk = Build.VERSION.SDK_INT
//        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//            setBackgroundDrawable(drawable)
//        } else {
            background = drawable
//        }
    }

    companion object {

        private val lightGrey = "lightGrey"
        private val darkGrey = "darkGrey"
        private val lightRed = "lightRed"
        private val darkRed = "darkRed"
        private val lightGreen = "lightGreen"
        private val darkGreen = "darkGreen"
        private val lightBlue = "lightBlue"
        private val darkBlue = "darkBlue"
        private val lightOrange = "lightOrange"
        private val darkOrange = "darkOrange"
    }
}