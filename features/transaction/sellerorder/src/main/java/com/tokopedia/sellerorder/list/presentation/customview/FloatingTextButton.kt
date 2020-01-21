package com.tokopedia.sellerorder.list.presentation.customview

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerorder.R

/**
 * Created by fwidjaja on 2019-09-10.
 */
class FloatingTextButton : FrameLayout {

    private var container: CardView? = null
    private var leftIconView: ImageView? = null
    private var rightIconView: ImageView? = null
    private var titleView: TextView? = null

    private var title: String? = null
    private var titleColor: Int = 0
    private var leftIcon: Drawable? = null
    private var rightIcon: Drawable? = null
    private var background: Int = 0
    private var titleAllCaps: Boolean = false
    private val animation: ViewPropertyAnimatorCompat? = null
    private var forceHide = false
    var isAnimationStart: Boolean = false
        private set
    private var floatingIsShown: Boolean = false
    private lateinit var displayMetric : DisplayMetrics

    var leftIconDrawable: Drawable?
        get() = leftIcon
        set(drawable) {
            leftIcon = drawable
            if (drawable != null) {
                leftIconView?.visibility = View.VISIBLE
                leftIconView?.setImageDrawable(drawable)
            } else {
                leftIconView?.visibility = View.GONE
            }
        }

    var rightIconDrawable: Drawable?
        get() = rightIcon
        set(drawable) {
            rightIcon = drawable
            if (drawable != null) {
                rightIconView?.visibility = View.VISIBLE
                rightIconView?.setImageDrawable(drawable)
            } else {
                rightIconView?.visibility = View.GONE
            }
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateLayout(context)
        initAttributes(attrs)
        initView()
        initDisplayMetrics(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflateLayout(context)
        initAttributes(attrs)
        initView()
        initDisplayMetrics(context)
    }

    constructor(context: Context) : super(context) {
        inflateLayout(context)
        initView()
        initDisplayMetrics(context)
    }

    fun setTitle(newTitle: String?) {
        title = newTitle

        if (newTitle == null || newTitle.isEmpty()) {
            titleView?.visibility = View.GONE
        } else {
            titleView?.visibility = View.VISIBLE
        }

        titleView?.text = newTitle
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitleColor(@ColorInt color: Int) {
        titleColor = color
        titleView?.setTextColor(color)
    }

    @ColorInt
    fun getTitleColor(): Int {
        return titleColor
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        background = color
        container?.setCardBackgroundColor(color)
    }

    @ColorInt
    fun getBackgroundColor(): Int {
        return background
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        container?.setOnClickListener(listener)
    }

    override fun hasOnClickListeners(): Boolean {
        return container?.hasOnClickListeners()!!
    }

    override fun setOnLongClickListener(listener: View.OnLongClickListener?) {
        container?.setOnLongClickListener(listener)
    }

    private fun inflateLayout(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.widget_floating_text_button, this, true)

        container = view.findViewById(R.id.layout_button_container)

        leftIconView = view.findViewById(R.id.layout_button_image_left)
        rightIconView = view.findViewById(R.id.layout_button_image_right)

        titleView = view.findViewById(R.id.layout_button_text)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val styleable = context.obtainStyledAttributes(
                attrs,
                R.styleable.FloatingTextButton,
                0,
                0
        )

        title = styleable.getString(R.styleable.FloatingTextButton_floating_title)
        titleColor = styleable.getColor(R.styleable.FloatingTextButton_floating_title_color, Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftIcon = styleable.getDrawable(R.styleable.FloatingTextButton_floating_left_icon)
            rightIcon = styleable.getDrawable(R.styleable.FloatingTextButton_floating_right_icon)
        } else {
            val drawableLeftId = styleable.getResourceId(R.styleable.FloatingTextButton_floating_left_icon, -1)
            val drawableRightId = styleable.getResourceId(R.styleable.FloatingTextButton_floating_right_icon, -1)
            if (drawableLeftId != -1)
                leftIcon = AppCompatResources.getDrawable(context, drawableLeftId)
            if (drawableRightId != -1)
                rightIcon = AppCompatResources.getDrawable(context, drawableRightId)
        }
        background = styleable.getColor(R.styleable.FloatingTextButton_floating_background_color, Color.WHITE)
        titleAllCaps = styleable.getBoolean(R.styleable.FloatingTextButton_floating_title_allcaps, false)
        titleView?.isAllCaps = titleAllCaps
        styleable.recycle()
    }

    private fun initView() {
        setTitle(title)
        setTitleColor(titleColor)
        leftIconDrawable = leftIcon
        rightIconDrawable = rightIcon
        setBackgroundColor(background)
    }

    private fun initDisplayMetrics(context: Context) {
        displayMetric = context.resources.displayMetrics
    }

    private fun getVerticalPaddingValue(dp: Int): Int {
        return dp.dpToPx(displayMetric)
    }

    private fun getHorizontalPaddingValue(dp: Int): Int {
        return dp.dpToPx(displayMetric)
    }

    fun show() {
        show(-1)
    }

    private fun show(visibility: Int) {
        if (!floatingIsShown) {
            floatingIsShown = true
            if (forceHide)
                return
            animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f))
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            isAnimationStart = true
                            if (visibility >= 0) {
                                super@FloatingTextButton.setVisibility(visibility)
                            }
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            isAnimationStart = false
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            isAnimationStart = false
                        }

                        override fun onAnimationRepeat(animator: Animator) {

                        }
                    })
                    .setDuration(DURATION)
                    .start()
        }
    }

    fun hide() {
        hide(-1)
    }

    private fun hide(visibility: Int) {
        if (floatingIsShown) {
            floatingIsShown = false
            val layoutParams = layoutParams as FrameLayout.LayoutParams
            val fab_bottomMargin = layoutParams.bottomMargin
            animate().translationY((height + fab_bottomMargin).toFloat()).setInterpolator(AccelerateInterpolator(2f))
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            isAnimationStart = true
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            isAnimationStart = false
                            if (visibility >= 0) {
                                super@FloatingTextButton.setVisibility(visibility)
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            isAnimationStart = false
                        }

                        override fun onAnimationRepeat(animator: Animator) {

                        }
                    })
                    .setDuration(DURATION)
                    .start()
        }
    }

    override fun setVisibility(visibility: Int) {
        if (getVisibility() != visibility) {
            if (visibility == View.VISIBLE) {
                show(visibility)
            } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
                hide(visibility)
            }
        }
    }

    fun forceHide() {
        forceHide = true
        hide()
    }

    fun resetState() {
        this.forceHide = false
    }

    companion object {
        private val TAG = FloatingTextButton::class.java.simpleName
        private val INTERPOLATOR = FastOutSlowInInterpolator()
        private val DURATION = 250L
    }
}