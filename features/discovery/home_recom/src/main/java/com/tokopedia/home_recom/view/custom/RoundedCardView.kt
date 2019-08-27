package com.tokopedia.home_recom.view.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.util.ViewUtils

/**
 * Created by Lukas on 27/08/19
 */
class RoundedCardView : LinearLayout{

    companion object{
        private const val CENTER = "center"
        private const val TOP = "top"
        private const val BOTTOM = "bottom"
    }

    constructor(context: Context) : super(context){
        initBackground()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        applyCustomBackground(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        applyCustomBackground(context, attrs)
    }

    private fun initBackground(){
        background = ViewUtils.generateBackgroundWithShadow(this, android.R.color.white, R.dimen.dp_8, R.color.shadow_6, R.dimen.dp_2, Gravity.CENTER)
        val padding = context.resources.getDimensionPixelOffset(R.dimen.dp_8)
        setPadding(padding, padding, padding, padding)
    }

    private fun applyCustomBackground(context: Context, attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedCardView)
        val radius = typedArray.getDimension(R.styleable.RoundedCardView_radius, 21.0f)
        val elevation = typedArray.getDimensionPixelSize(R.styleable.RoundedCardView_elevation, 5)
        val contentPadding = typedArray.getDimensionPixelSize(R.styleable.RoundedCardView_contentPadding, 21)
        val gravity = when(typedArray.getString(R.styleable.RoundedCardView_shadowGravity)){
            CENTER -> Gravity.CENTER
            TOP -> Gravity.TOP
            BOTTOM -> Gravity.BOTTOM
            else -> Gravity.CENTER
        }
        background = ViewUtils.generateBackgroundWithShadow(this,
                ContextCompat.getColor(context, android.R.color.white),
                radius,
                ContextCompat.getColor(context, R.color.shadow_6),
                elevation,
                gravity)

        setPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        typedArray.recycle()
    }

}