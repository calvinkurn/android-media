package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.unifyprinciples.Typography


class GradientButton : FrameLayout {

    private var outerStartColor = 0
    private var outerEndColor = 0
    private var innerStartColor = 0
    private var innerEndColor = 0
    private var textColor = 0
    private var title: String? = ""
    private var shade: Shade = Shade.EMPTY

    private lateinit var fmOuter: FrameLayout
    private lateinit var fmInner: FrameLayout
    private lateinit var tv: Typography

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun getLayout() = R.layout.widget_egg_source

    fun init(attrs: AttributeSet?) {
        inflateLayout()
        readAttrs(attrs)
        setData()
    }

    private fun readAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.GradientButton, 0, 0)
            innerStartColor = typedArray.getColor(R.styleable.GradientButton_gbInnerStartColor, 0)
            innerEndColor = typedArray.getColor(R.styleable.GradientButton_gbInnerEndColor, 0)
            outerStartColor = typedArray.getColor(R.styleable.GradientButton_gbOuterStartColor, 0)
            outerEndColor = typedArray.getColor(R.styleable.GradientButton_gbOuterEndColor, 0)
            outerEndColor = typedArray.getColor(R.styleable.GradientButton_gbOuterEndColor, 0)
            textColor = typedArray.getColor(R.styleable.GradientButton_gbTextColor, 0)
            title = typedArray.getString(R.styleable.GradientButton_gbTitle)
            shade = Shade.values()[typedArray.getInt(R.styleable.GradientButton_gbShade, Shade.EMPTY.ordinal)]
            typedArray.recycle()
        }
    }

    private fun inflateLayout() {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        tv = findViewById(R.id.text_egg_source)
        fmOuter = findViewById(R.id.fmOuter)
        fmInner = findViewById(R.id.fmInner)
    }

    private fun setData() {
        if (!TextUtils.isEmpty(title)) {
            tv.text = title
        }

        if (textColor != 0) {
            tv.setTextColor(textColor)
        }

        when (shade) {
            Shade.BLUE -> {
                val outerColorArray = arrayOf(ContextCompat.getColor(context, R.color.gf_grad_btn_outer_start_blue),
                        ContextCompat.getColor(context, R.color.gf_grad_btn_outer_end_blue))
                val innerColorArray = arrayOf(ContextCompat.getColor(context, R.color.gf_grad_btn_inner_start_blue),
                        ContextCompat.getColor(context, R.color.gf_grad_btn_inner_end_blue))

                fmOuter.background = getBg(outerColorArray[0], outerColorArray[1])
                fmInner.background = getBg(innerColorArray[0], innerColorArray[1])
            }
            Shade.GREEN -> {

                val outerColorArray = arrayOf(ContextCompat.getColor(context, R.color.gf_grad_btn_outer_start_green),
                        ContextCompat.getColor(context, R.color.gf_grad_btn_outer_end_green))
                val innerColorArray = arrayOf(ContextCompat.getColor(context, R.color.gf_grad_btn_inner_start_green),
                        ContextCompat.getColor(context, R.color.gf_grad_btn_inner_end_green))


                fmOuter.background = getBg(outerColorArray[0], outerColorArray[1])
                fmInner.background = getBg(innerColorArray[0], innerColorArray[1])
            }
            else -> {
                if (outerStartColor != 0 && outerEndColor != 0) {
                    fmOuter.background = getBg(outerStartColor, outerEndColor)
                }

                if (innerStartColor != 0 && innerEndColor != 0) {
                    fmInner.background = getBg(innerStartColor, innerEndColor)
                }
            }
        }
    }

    fun getBg(startColor: Int, endColor: Int): Drawable {
        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(startColor, endColor))
        val radius = dpToPx(context, 20f)
        gd.cornerRadius = radius
        return gd
    }

    enum class Shade {
        EMPTY, BLUE, GREEN
    }

    private fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}