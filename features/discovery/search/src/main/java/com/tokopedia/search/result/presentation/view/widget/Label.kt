package com.tokopedia.search.result.presentation.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
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

            setLabelDesignFromAttrs(attributeArray)

            attributeArray.recycle()
        }
    }

    private fun setLabelDesignFromAttrs(attributeArray: TypedArray) {
        val labelType = attributeArray.getString(R.styleable.Label_labelDesign)

        setLabelDesign(labelType ?: "")
    }

    fun setLabelDesign(labelDesign: String) {
        when(labelDesign) {
            lightGrey -> setBackgroundAndTextStyle(R.color.product_card_label_light_grey_background, R.color.product_card_label_light_grey_text)
            lightBlue -> setBackgroundAndTextStyle(R.color.product_card_label_light_blue_background, R.color.product_card_label_light_blue_text)
            lightGreen -> setBackgroundAndTextStyle(R.color.product_card_label_light_green_background, R.color.product_card_label_light_green_text)
            lightRed -> setBackgroundAndTextStyle(R.color.product_card_label_light_red_background, R.color.product_card_label_light_red_text)
            lightOrange -> setBackgroundAndTextStyle(R.color.product_card_label_light_orange_background, R.color.product_card_label_light_orange_text)
            darkGrey -> setBackgroundAndTextStyle(R.color.product_card_label_dark_grey_background, R.color.product_card_label_dark_grey_text)
            darkBlue -> setBackgroundAndTextStyle(R.color.product_card_label_dark_blue_background, R.color.product_card_label_dark_blue_text)
            darkGreen -> setBackgroundAndTextStyle(R.color.product_card_label_dark_green_background, R.color.product_card_label_dark_green_text)
            darkRed -> setBackgroundAndTextStyle(R.color.product_card_label_dark_red_background, R.color.product_card_label_dark_red_text)
            darkOrange -> setBackgroundAndTextStyle(R.color.product_card_label_dark_orange_background, R.color.product_card_label_dark_orange_text)
            else -> setBackgroundAndTextStyle(R.color.product_card_label_light_grey_background, R.color.product_card_label_light_grey_text)
        }
    }

    private fun setBackgroundAndTextStyle(backgroundColorResourceId: Int, textColorResourceId: Int) {
        setLabelBackground()
        setBackgroundColorWithExistingBackground(ContextCompat.getColor(context, backgroundColorResourceId))
        setLabelPadding()

        setTextColor(ContextCompat.getColor(context, textColorResourceId))
        setFontTypefaceBold()
        setTextSize()
    }

    private fun setLabelBackground() {
        background = ContextCompat.getDrawable(context, R.drawable.search_label_background)
    }

    private fun setBackgroundColorWithExistingBackground(backgroundColorResourceId: Int) {
        val gradientDrawable = background

        if(gradientDrawable != null && gradientDrawable is GradientDrawable) {
            gradientDrawable.setColor(backgroundColorResourceId)
        }
        else {
            setBackgroundColor(backgroundColorResourceId)
        }
    }

    private fun setLabelPadding() {
        val paddingDp = 4
        val scale = resources.displayMetrics.density
        val paddingPixel = (paddingDp * scale + 0.5f).toInt()

        setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel)
    }

    private fun setFontTypefaceBold() {
        setTypeface(typeface, Typeface.BOLD)
    }

    private fun setTextSize() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
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