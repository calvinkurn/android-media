package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.unifyprinciples.Typography

class CardValueCountdownView: FrameLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    companion object {
        private const val ANIM_DURATION: Long = 200
    }

    private var previousTypography: Typography? = null
    private var nextTypography: Typography? = null

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.shc_card_widget_counter, this)

        previousTypography = view.findViewById(R.id.tvCardFirstValue)
        nextTypography = view.findViewById(R.id.tvCardSecondValue)
    }

    fun setValue(previousValue: String, nextValue: String) {
        if (previousValue != nextValue) {
            nextTypography?.alpha = 0.2f
            previousTypography?.alpha = 1f
            previousTypography?.text = previousValue
            nextTypography?.text = nextValue

            previousTypography
                    ?.animate()
                    ?.alpha(0f)
                    ?.duration = ANIM_DURATION
            nextTypography?.run {
                animate()?.alpha(1f)?.setDuration(ANIM_DURATION)?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {}

                    override fun onAnimationEnd(p0: Animator?) {
                        previousTypography?.alpha = 0f
                    }

                    override fun onAnimationCancel(p0: Animator?) {}

                    override fun onAnimationRepeat(p0: Animator?) {}
                })
            }
        }
    }

}