package com.tokopedia.design.component

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import com.tokopedia.design.R

class UnifyButton : AppCompatButton {
    private val disableFillDrawable: GradientDrawable by lazy {
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.unify_N75))
            cornerRadius = resources.getDimension(R.dimen.dp_8)
            setStroke(resources.getDimensionPixelSize(R.dimen.dp_1), ContextCompat.getColor(context, R.color.unify_N75))
        }
    }
    var buttonSize = Size.LARGE
        set(value) {
            if (buttonSize == value) return
            field = value
            refresh()
        }
    var buttonVariant = Variant.FILLED
        set(value) {
            if (buttonVariant == value) return
            field = value
            refresh()
        }
    var isInverse = false
        set(value) {
            field = value
            if (buttonVariant != Variant.GHOST) return
            refresh()
        }
    var buttonType = Type.MAIN
        set(value) {
            if (buttonType == value) return
            field = value
            refresh()
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWittAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWittAttrs(context, attrs)
    }

    private fun initWittAttrs(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.UnifyButton)
        buttonSize = attributeArray.getInt(R.styleable.UnifyButton_btnSize, Size.LARGE)
        buttonVariant = attributeArray.getInt(R.styleable.UnifyButton_buttonVariant, Variant.FILLED)
        buttonType = attributeArray.getInt(R.styleable.UnifyButton_buttonType, Type.MAIN)
        isInverse = attributeArray.getBoolean(R.styleable.UnifyButton_invers, false)
        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        refresh()
        super.onFinishInflate()
    }

    private fun refresh() {
        initButtonBackground()
        initButtonSize()
        initButtonPadding()
        initButtonText()
        invalidate()
        requestLayout()
    }

    private fun initButtonPadding() {
        val padding = resources.getDimensionPixelSize(
                if (buttonSize == Size.LARGE || buttonSize == Size.MEDIUM) R.dimen.dp_12 else R.dimen.dp_8)
        setPadding(padding, paddingTop, padding, paddingBottom)
    }

    private fun initButtonText() {
        typeface = Typeface.createFromAsset(context.assets, "fonts/NunitoSans-ExtraBold.ttf")
        when (buttonSize){
            Size.LARGE -> { setTextSize(COMPLEX_UNIT_SP, 16f) }
            Size.MEDIUM -> { setTextSize(COMPLEX_UNIT_SP, 14f) }
            Size.SMALL, Size.MICRO -> { setTextSize(COMPLEX_UNIT_SP, 12f) }
        }
    }

    private fun initButtonSize() {
        minHeight = resources.getDimensionPixelSize(
                when(buttonSize){
                    Size.MEDIUM -> R.dimen.dp_40
                    Size.SMALL -> R.dimen.dp_32
                    Size.MICRO -> R.dimen.dp_24
                    else -> R.dimen.dp_48
        })
    }

    private fun initButtonBackground() {
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val background = ContextCompat.getColor(context, when(buttonType){
            Type.MAIN -> R.color.unify_G500
            Type.TRANSACTION -> R.color.unify_Y500
            else -> R.color.unify_N75
        })

        disableFillDrawable.cornerRadius =
                resources.getDimension(if (buttonSize == Size.MICRO) R.dimen.dp_6 else R.dimen.dp_8)

        val enableFillDrawable = disableFillDrawable

        when(buttonVariant){
            Variant.FILLED -> {
                enableFillDrawable.setColorFilter(background, PorterDuff.Mode.SRC_ATOP)
                enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.dp_1), background)
                setTextColor(whiteColor)
            }
            Variant.GHOST -> {
                if (isInverse){
                    enableFillDrawable.setColorFilter(background, PorterDuff.Mode.SRC_ATOP)
                    enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.dp_1), whiteColor)
                    disableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.dp_1), whiteColor)
                    setTextColor(whiteColor)
                } else {
                    enableFillDrawable.setColorFilter(whiteColor, PorterDuff.Mode.SRC_ATOP)
                    enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.dp_1), background)
                    disableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.dp_1),
                            ContextCompat.getColor(context, R.color.unify_N75))
                    setTextColor(background)
                }
            }
            else -> {
                setBackground(null)
                setTextColor(ContextCompat.getColor(context, R.color.unify_N700_44))
            }
        }

        if (buttonVariant != Variant.TEXT_ONLY && !isEnabled)
            setTextColor(ContextCompat.getColor(context, R.color.unify_N700_32))

        val stateListDefaultDrawable = StateListDrawable().apply {
            addState(intArrayOf(-android.R.attr.state_enabled), disableFillDrawable)
            addState(intArrayOf(),enableFillDrawable)
        }

        setBackground(stateListDefaultDrawable)

    }

    override fun setEnabled(enabled: Boolean) {
        if (isEnabled != enabled) initButtonBackground()
        super.setEnabled(enabled)
    }

    object Size {
        const val LARGE = 1
        const val MEDIUM = 2
        const val SMALL = 3
        const val MICRO = 4
    }

    object Variant {
        const val FILLED = 1
        const val GHOST = 2
        const val TEXT_ONLY = 3
    }

    object Type {
        const val MAIN = 1
        const val TRANSACTION = 2
    }
}