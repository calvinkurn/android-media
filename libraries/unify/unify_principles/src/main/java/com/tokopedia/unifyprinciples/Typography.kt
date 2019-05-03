package com.tokopedia.unifyprinciples

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import android.util.TypedValue.COMPLEX_UNIT_SP

class Typography : AppCompatTextView {

    private val nunitoSans = Typeface.createFromAsset(context.assets, "fonts/NunitoSans-ExtraBold.ttf")
    private val robotoRegular = Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
    private val robotoBold = Typeface.createFromAsset(context.assets, "fonts/Roboto-Bold.ttf")

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.Typography)

            configFontSize(attributeArray)
            configLineHeight(attributeArray)
            configLetterSpacing(attributeArray)

            attributeArray.recycle()
        }
    }

    private fun configFontSize(attributeArray: TypedArray) {
        val fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0)
        val weightType = attributeArray.getInteger(R.styleable.Typography_typographyWeight, REGULER)

        when (fontType) {
            HEADING_1 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 28f)
                this.minimumHeight = 34
                this.typeface = nunitoSans
            }
            HEADING_2 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 20f)
                this.minimumHeight = 26
                this.typeface = nunitoSans
            }
            HEADING_3 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 18f)
                this.minimumHeight = 24
                this.typeface = nunitoSans
            }
            HEADING_4 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 16f)
                this.minimumHeight = 22
                this.typeface = nunitoSans
            }
            HEADING_5 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 14f)
                this.minimumHeight = 18
                this.typeface = nunitoSans
            }
            HEADING_6 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 12f)
                this.minimumHeight = 16
                this.typeface = nunitoSans
            }
            BODY_1 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 16f)
                this.minimumHeight = 22
                configFontWeight(weightType)
            }
            BODY_2 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 14f)
                this.minimumHeight = 20
                configFontWeight(weightType)
            }
            BODY_3 -> {
                this.setTextSize(COMPLEX_UNIT_SP, 12f)
                this.minimumHeight = 18
                configFontWeight(weightType)
            }
            SMALL -> {
                this.setTextSize(COMPLEX_UNIT_SP, 10f)
                this.minimumHeight = 14
                configFontWeight(weightType)
            }
        }
    }

    private fun configLineHeight(attributeArray: TypedArray) {
        val fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0)

        when (fontType) {
            HEADING_1 -> this.setLineSpacing(4.0.toFloat(), 0.82.toFloat())
            HEADING_2 -> this.setLineSpacing((-15.0).toFloat(), 1.25.toFloat())
            HEADING_3 -> this.setLineSpacing((-12.0).toFloat(), 1.3.toFloat())
            BODY_1 -> this.setLineSpacing((-4).toFloat(), 1.375.toFloat())
            BODY_2 -> this.setLineSpacing((-3).toFloat(), 1.429.toFloat())
            BODY_3 -> this.setLineSpacing((-5).toFloat(), 1.5.toFloat())
            SMALL -> this.setLineSpacing((-6).toFloat(), 1.6.toFloat())
        }
    }

    private fun configFontWeight(fontWeightState: Int) {
        if (fontWeightState == REGULER) {
            this.typeface = robotoRegular
            this.setTypeface(typeface, Typeface.NORMAL)
        } else if (fontWeightState == BOLD) {
            this.typeface = robotoBold
            this.setTypeface(typeface, Typeface.BOLD)
        }
    }

    private fun configLetterSpacing(attributeArray: TypedArray) {
        val fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0)
        var kerningValueNewVer = 0f
        var kerningValueOldVer = 0f

        if (fontType == HEADING_1) {
            kerningValueNewVer = (-0.013).toFloat()
            kerningValueOldVer = 1.toFloat()
        } else if (fontType == HEADING_2 || fontType == HEADING_3) {
            kerningValueNewVer = (-0.01).toFloat()
            kerningValueOldVer = 1.toFloat()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.letterSpacing = kerningValueNewVer
        } else {
            this.textScaleX = kerningValueOldVer
        }
    }

    companion object {

        private val HEADING_1 = 1
        private val HEADING_2 = 2
        private val HEADING_3 = 3
        private val HEADING_4 = 4
        private val HEADING_5 = 5
        private val HEADING_6 = 6

        private val BODY_1 = 7
        private val BODY_2 = 8
        private val BODY_3 = 9

        private val SMALL = 10

        private val REGULER = 1
        private val BOLD = 2
    }
}
