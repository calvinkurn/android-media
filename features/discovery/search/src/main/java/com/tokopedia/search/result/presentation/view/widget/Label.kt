package com.tokopedia.search.result.presentation.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.tokopedia.search.R

class Label : AppCompatTextView {

    constructor(context: Context) : super(context)

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
        val labelType = attributeArray.getString(R.styleable.Label_labelDesign)

        setLabelDesign(labelType ?: "")
    }

    fun setLabelDesign(labelDesign: String) {
        when(labelDesign) {
            lightGrey -> setBackgroundAndTextColor(R.color.product_card_label_light_grey_background, R.color.product_card_label_light_grey_text)
            lightBlue -> setBackgroundAndTextColor(R.color.product_card_label_light_blue_background, R.color.product_card_label_light_blue_text)
            lightGreen -> setBackgroundAndTextColor(R.color.product_card_label_light_green_background, R.color.product_card_label_light_green_text)
            lightRed -> setBackgroundAndTextColor(R.color.product_card_label_light_red_background, R.color.product_card_label_light_red_text)
            lightOrange -> setBackgroundAndTextColor(R.color.product_card_label_light_orange_background, R.color.product_card_label_light_orange_text)
            darkGrey -> setBackgroundAndTextColor(R.color.product_card_label_dark_grey_background, R.color.product_card_label_dark_grey_text)
            darkBlue -> setBackgroundAndTextColor(R.color.product_card_label_dark_blue_background, R.color.product_card_label_dark_blue_text)
            darkGreen -> setBackgroundAndTextColor(R.color.product_card_label_dark_green_background, R.color.product_card_label_dark_green_text)
            darkRed -> setBackgroundAndTextColor(R.color.product_card_label_dark_red_background, R.color.product_card_label_dark_red_text)
            darkOrange -> setBackgroundAndTextColor(R.color.product_card_label_dark_orange_background, R.color.product_card_label_dark_orange_text)
            else -> setBackgroundAndTextColor(R.color.product_card_label_light_grey_background, R.color.product_card_label_light_grey_text)
        }
    }

    private fun setBackgroundAndTextColor(backgroundColorResourceId: Int, textColorResourceId: Int) {
        setBackgroundColor(ContextCompat.getColor(context, backgroundColorResourceId))
        setTextColor(ContextCompat.getColor(context, textColorResourceId))
    }

    companion object {
        private const val lightGrey = "lightGrey"
        private const val lightBlue = "lightBlue"
        private const val lightGreen = "lightGreen"
        private const val lightRed = "lightRed"
        private const val lightOrange = "lightOrange"
        private const val darkGrey = "darkGrey"
        private const val darkBlue = "darkBlue"
        private const val darkGreen = "darkGreen"
        private const val darkRed = "darkRed"
        private const val darkOrange = "darkOrange"
    }
}