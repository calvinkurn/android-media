package com.tokopedia.navigation.presentation.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.navigation.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.resources.isDarkMode

class LottieBottomNavbar : LinearLayout {

    private var mDarkModeContext = getDarkModeContext()
    private val isDarkMode: Boolean
        get() = if (mIsForceDarkMode) {
            true
        } else {
            context.isDarkMode()
        }
    private val modeAwareContext: Context
        get() = if (isDarkMode) mDarkModeContext else context
    private var mIsForceDarkMode = false

    private val badgeTextViewList: MutableList<TextView>? = mutableListOf()
    private var emptyBadgeLayoutParam: FrameLayout.LayoutParams? = null
    private var badgeLayoutParam: FrameLayout.LayoutParams? = null
    private var menu: MutableList<BottomMenu> = ArrayList()
    private var listener: IBottomClickListener? = null
    private var homeForYouListener: IBottomHomeForYouClickListener? = null
    private var iconList: MutableList<Pair<LottieAnimationView, Boolean>> = ArrayList()
    private var iconPlaceholderList: MutableList<ImageView> = ArrayList()
    private var titleList: MutableList<TextView> = ArrayList()
    private var containerList: MutableList<FrameLayout> = ArrayList()
    private var itemCount: Int = 1
    private var buttonsHeight: Float = DEFAULT_HEIGHT
    private var selectedItem: Int? = null
    private var containerWidth: Int = 0
    private var navbarContainer: LinearLayout? = null

    // this flags to switch between home or for you bottom nav menu
    private var isForYouToHomeSelected: Boolean = false

    private var buttonContainerBackgroundLightColor: Int =
        ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    private var buttonLightColor: Int =
        ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)

    private var buttonContainerBackgroundDarkColor: Int =
        ContextCompat.getColor(mDarkModeContext, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    private var buttonDarkColor: Int =
        ContextCompat.getColor(mDarkModeContext, com.tokopedia.unifyprinciples.R.color.Unify_NN500)

    private var currentRippleScale = 0f
    private val interpolatorEnter = UnifyMotion.EASE_OVERSHOOT
    private val interpolatorExit = UnifyMotion.EASE_IN_OUT
    private val durationEnter = UnifyMotion.T3
    private val durationExit = UnifyMotion.T2

    private val buttonContainerBackgroundColor: Int
        get() = if (mIsForceDarkMode) buttonContainerBackgroundDarkColor else buttonContainerBackgroundLightColor

    private val buttonColor: Int
        get() = if (mIsForceDarkMode) buttonDarkColor else buttonLightColor

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        getLayoutAtr(attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle) {
        getLayoutAtr(attrs)
    }

    companion object {
        private const val DEFAULT_HEIGHT = 56f
        private const val DEFAULT_ICON_PADDING = 2
        private const val DEFAULT_TITLE_PADDING = 2
        private const val DEFAULT_TITLE_PADDING_BOTTOM = 4
        private const val SCALE_MIN_IMAGE = 0.6f
        private const val SCALE_MAX_IMAGE = 1f
        private const val INITIAL_ALPHA_RIPPLE = 0f
        private const val MAX_ALPHA_RIPPLE = 0.6f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        resizeContainer()
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        this.setMeasuredDimension(parentWidth, buttonsHeight.toInt())

        super.onMeasure(widthMeasureSpec, buttonsHeight.toInt())

        containerWidth = parentWidth
        resizeContainer()
        adjustBadgePosition()
    }

    fun forceDarkMode(isDarkMode: Boolean) {
        if (context.isDarkMode()) return
        if (isDarkMode == mIsForceDarkMode) return

        mIsForceDarkMode = isDarkMode
        setupMenuItems(modeAwareContext)
        requestLayout()
    }

    fun setBadge(badgeValue: Int = 0, iconPosition: Int, visibility: Int = View.VISIBLE) {
        val badge: View? = navbarContainer?.getChildAt(iconPosition)
        val badgeText = badge?.findViewById<TextView>(R.id.notification_badge)

        if (badgeValue == 0) {
            badgeText?.layoutParams = emptyBadgeLayoutParam
            badgeText?.setPadding(
                5f.toDpInt(),
                1f.toDpInt(),
                2f.toDpInt(),
                1f.toDpInt()
            )
            badgeText?.text = ""
            badgeText?.background =
                ContextCompat.getDrawable(modeAwareContext, R.drawable.bg_badge_circle)
        } else {
            badgeText?.layoutParams = badgeLayoutParam
            badgeText?.setPadding(
                5f.toDpInt(),
                2f.toDpInt(),
                5f.toDpInt(),
                2f.toDpInt()
            )

            badgeText?.background =
                ContextCompat.getDrawable(modeAwareContext, R.drawable.bg_badge_circular)
            badgeText?.text = badgeValue.toString()
        }

        badgeText?.bringToFront()
        badgeText?.visibility = visibility
    }

    private fun getDarkModeContext(): Context {
        val newConfig = Configuration(resources.configuration).apply {
            this.uiMode =
                Configuration.UI_MODE_NIGHT_YES or (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        }
        return context.createConfigurationContext(newConfig)
    }

    private fun adjustBadgePosition() {
        if (menu.isEmpty()) return
        val itemWidthSize = containerWidth / menu.size
        val badgeRightMargin = itemWidthSize / menu.size

        badgeLayoutParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        badgeLayoutParam?.gravity = Gravity.END
        badgeLayoutParam?.setMargins(
            0f.toDpInt(),
            1f.toDpInt(),
            badgeRightMargin,
            1f.toDpInt()
        )

        emptyBadgeLayoutParam = FrameLayout.LayoutParams(
            12f.toDpInt(),
            12f.toDpInt()
        )
        emptyBadgeLayoutParam?.gravity = Gravity.END
        emptyBadgeLayoutParam?.setMargins(
            0f.toDpInt(),
            1f.toDpInt(),
            badgeRightMargin + 12f.toDpInt(),
            1f.toDpInt()
        )

        badgeTextViewList?.forEach {
            if (it.text == "") {
                it.layoutParams = emptyBadgeLayoutParam
            } else {
                it.layoutParams = badgeLayoutParam
            }
        }
    }

    private fun resizeContainer() {
        // menu item width is equal: container width / size of menu item
        val itemWidth = containerWidth / itemCount

        containerList.forEach {
            val llLayoutParam =
                LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.MATCH_PARENT)
            it.layoutParams = llLayoutParam
            it.invalidate()
        }
        invalidate()
    }

    private fun getLayoutAtr(attrs: AttributeSet) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.LottieBottomNavbar)
        val darkModeStyledAttributes =
            mDarkModeContext.obtainStyledAttributes(attrs, R.styleable.LottieBottomNavbar)
        val defaultButtonHeight = DEFAULT_HEIGHT * context.resources.displayMetrics.density

        buttonsHeight = styledAttributes.getDimension(
            R.styleable.LottieBottomNavbar_buttonsHeight,
            defaultButtonHeight
        )

        buttonContainerBackgroundLightColor = styledAttributes.getColor(
            R.styleable.LottieBottomNavbar_buttonContainerBackgroundColor,
            androidx.core.content.ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        buttonContainerBackgroundDarkColor = darkModeStyledAttributes.getColor(
            R.styleable.LottieBottomNavbar_buttonContainerBackgroundColor,
            androidx.core.content.ContextCompat.getColor(
                mDarkModeContext,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )

        buttonLightColor = styledAttributes.getColor(
            R.styleable.LottieBottomNavbar_buttonColor,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
        )
        buttonDarkColor = darkModeStyledAttributes.getColor(
            R.styleable.LottieBottomNavbar_buttonColor,
            ContextCompat.getColor(
                mDarkModeContext,
                com.tokopedia.unifyprinciples.R.color.Unify_NN500
            )
        )

        styledAttributes.recycle()
        darkModeStyledAttributes.recycle()

        weightSum = 1f
        orientation = VERTICAL
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupMenuItems(context: Context) {
        removeAllViews()
        // menu item width is equal: container width / size of menu item
        val itemWidth = containerWidth / itemCount

        iconList.clear()
        titleList.clear()
        containerList.clear()

        val rootLayoutParam = FrameLayout.LayoutParams(itemWidth, 28f.toDpInt())
        val containerLayoutParam =
            LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val imgLayoutParam = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            24f.toDpInt()
        )

        badgeLayoutParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        badgeLayoutParam?.gravity = Gravity.END
        badgeLayoutParam?.setMargins(
            0f.toDpInt(),
            1f.toDpInt(),
            20f.toDpInt(),
            1f.toDpInt()
        )

        emptyBadgeLayoutParam = FrameLayout.LayoutParams(
            12f.toDpInt(),
            12f.toDpInt()
        )
        emptyBadgeLayoutParam?.gravity = Gravity.END
        emptyBadgeLayoutParam?.setMargins(
            0f.toDpInt(),
            1f.toDpInt(),
            25f.toDpInt(),
            1f.toDpInt()
        )

        val txtLayoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // create Button Container
        navbarContainer = LinearLayout(context)
        navbarContainer?.let {
            it.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                buttonsHeight.toInt()
            )
            it.setBackgroundColor(buttonContainerBackgroundColor)
            it.orientation = HORIZONTAL
        }

        // for each menu:
        // create item container, draw image icon and title, add click listener if set
        menu.forEachIndexed { index, bottomMenu ->
            // add root layout
            val rootButtonContainer = FrameLayout(context)
            rootButtonContainer.layoutParams = rootLayoutParam
            rootButtonContainer.setBackgroundColor(Color.TRANSPARENT)
            containerList.add(index, rootButtonContainer)

            // add linear layout as container for image and text
            val buttonContainer = LinearLayout(context)
            buttonContainer.layoutParams = containerLayoutParam
            buttonContainer.orientation = LinearLayout.VERTICAL
            buttonContainer.gravity = Gravity.CENTER
            buttonContainer.setBackgroundColor(Color.TRANSPARENT)

            // add image view to display menu icon
            val icon = LottieAnimationView(context)
            icon.tag = context.getString(R.string.tag_lottie_animation_view) + bottomMenu.id
            icon.layoutParams = imgLayoutParam
            icon.setPadding(DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, 0)

            val animName = if (!isDarkMode) bottomMenu.animName else bottomMenu.animDarkName
            if (!isDeviceAnimationDisabled()) {
                animName?.let {
                    icon.setAnimation(animName)
                    icon.speed = bottomMenu.animSpeed
                }
            } else {
                bottomMenu.imageEnabledName?.let {
                    icon.setImageResource(it)
                }
            }
            if (animName == null) {
                bottomMenu.imageName?.let {
                    icon.setImageResource(it)
                }
            }

            iconList.add(index, Pair(icon, selectedItem == index))

            val imageContainer = FrameLayout(context)
            val fLayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            imageContainer.layoutParams = fLayoutParams

            val iconPlaceholder = ImageView(context)
            iconPlaceholder.tag = "iconPlaceholder" + bottomMenu.id
            iconPlaceholder.setPadding(
                DEFAULT_ICON_PADDING,
                DEFAULT_ICON_PADDING,
                DEFAULT_ICON_PADDING,
                0
            )
            iconPlaceholder.layoutParams = imgLayoutParam
            iconPlaceholder.visibility = View.INVISIBLE
            iconPlaceholderList.add(index, iconPlaceholder)

            imageContainer.addView(icon)
            imageContainer.addView(iconPlaceholder)

            icon.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    if (selectedItem != index) {
                        val bottomMenuSelected = bottomMenu
                        val iconSelected = icon
                        val animNameSelected =
                            if (!isDarkMode) bottomMenuSelected.animName else bottomMenuSelected.animDarkName

                        bottomMenuSelected.imageEnabledName?.let {
                            iconPlaceholder.setImageResource(it)
                        }
                        animNameSelected?.let {
                            iconSelected.setAnimation(it)
                            iconSelected.speed = bottomMenuSelected.animSpeed
                        }
                    } else {
                        val bottomMenuSelected = bottomMenu
                        val iconSelected = icon
                        val animToEnabledNameSelected =
                            if (!isDarkMode) bottomMenuSelected.animToEnabledName else bottomMenuSelected.animDarkToEnabledName

                        if (index == Int.ZERO) {
                            // isForYouToHomeSelected: true -> for you tab -> home header
                            // isForYouToHomeSelected: false -> home header -> for you tab
                            if (isForYouToHomeSelected) {
                                bottomMenuSelected.homeForYou?.homeImageName?.let {
                                    iconPlaceholder.setImageResource(it)
                                }
                                animToEnabledNameSelected?.let {
                                    iconSelected.setAnimation(it)
                                    iconSelected.speed = bottomMenuSelected.animToEnabledSpeed
                                }
                            } else {
                                bottomMenuSelected.homeForYou?.forYouImageName?.let {
                                    iconPlaceholder.setImageResource(it)
                                }

                                val animToIdleNameSelected =
                                    bottomMenuSelected.homeForYou?.animThumbIdleName

                                animToIdleNameSelected?.let {
                                    iconSelected.repeatCount = LottieDrawable.INFINITE
                                    iconSelected.setAnimation(it)
                                    iconSelected.playAnimation()
                                }
                            }
                        } else {
                            bottomMenuSelected.imageName?.let {
                                iconPlaceholder.setImageResource(it)
                            }

                            animToEnabledNameSelected?.let {
                                iconSelected.setAnimation(it)
                                iconSelected.speed = bottomMenuSelected.animToEnabledSpeed
                            }
                        }
                    }

                    iconList[index] = Pair(icon, false)
                    icon.visibility = View.INVISIBLE
                    iconPlaceholder.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationStart(p0: Animator) {
                    icon.visibility = View.VISIBLE
                    iconPlaceholder.visibility = View.INVISIBLE
                }
            })

            iconPlaceholder.bringToFront()

            if (bottomMenu.useBadge) {
                val badge: View = LayoutInflater.from(context)
                    .inflate(R.layout.badge_layout, imageContainer, false)
                badge.layoutParams = badgeLayoutParam
                val badgeTextView = badge.findViewById<TextView>(R.id.notification_badge)
                badgeTextViewList?.add(badgeTextView)
                badgeTextView.tag =
                    context.getString(R.string.tag_badge_textview) + bottomMenu.id.toString()
                badgeTextView.visibility = View.INVISIBLE
                imageContainer.addView(badge)
                badge.bringToFront()
            }

            buttonContainer.addView(imageContainer)

            // add text view to show title
            val title = Typography(context)
            title.maxLines = 1
            title.ellipsize = TextUtils.TruncateAt.END
            title.layoutParams = txtLayoutParam
            title.setPadding(
                DEFAULT_TITLE_PADDING,
                0,
                DEFAULT_TITLE_PADDING,
                DEFAULT_TITLE_PADDING_BOTTOM
            )
            title.text = bottomMenu.title
            title.tag = context.getString(R.string.tag_title_textview) + bottomMenu.id
            title.setType(Typography.SMALL)
            if (selectedItem != null && selectedItem == index) {
                title.setTextColor(menu[selectedItem!!].activeButtonColor)
            } else {
                title.setTextColor(buttonColor)
            }

            titleList.add(index, title)
            buttonContainer.addView(title)

            // add ripple layer
            val rippleView = View(context)
            val rippleLayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            rippleView.layoutParams = rippleLayoutParams
            rippleView.setBackgroundResource(R.drawable.bg_ripple_container)
            rippleView.alpha = INITIAL_ALPHA_RIPPLE

            // add button container and ripple to root layout
            rootButtonContainer.addView(rippleView)
            rootButtonContainer.addView(buttonContainer)
            buttonContainer.bringToFront()

            // handle ripple on touch
            val longPressHandler = Handler(Looper.getMainLooper())
            val onLongPress = Runnable {
                rootButtonContainer.performLongClick()
            }
            val rippleAnimator = ValueAnimator.ofFloat()
            rootButtonContainer.setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        longPressHandler.removeCallbacks(onLongPress)
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                rippleAnimator.addListener(
                                    onEnd = {
                                        // if user up before enter animation finished,
                                        // do exit animation after enter animation finished
                                        if (currentRippleScale == SCALE_MAX_IMAGE) {
                                            scalingRipple(
                                                end = SCALE_MIN_IMAGE,
                                                duration = durationExit,
                                                pathInterpolator = interpolatorExit,
                                                rippleView = rippleView,
                                                rippleAnimator = rippleAnimator
                                            )
                                        }
                                    }
                                )
                                // if user up/cancel after enter animation finished (ex: long press)
                                if (currentRippleScale == SCALE_MAX_IMAGE) {
                                    // do exit animation
                                    scalingRipple(
                                        SCALE_MAX_IMAGE,
                                        SCALE_MIN_IMAGE,
                                        durationExit,
                                        interpolatorExit,
                                        rippleView,
                                        rippleAnimator
                                    )
                                }
                            },
                            Int.ZERO.toLong()
                        )
                    }

                    MotionEvent.ACTION_DOWN -> {
                        longPressHandler.postDelayed(
                            onLongPress,
                            ViewConfiguration.getLongPressTimeout().toLong()
                        )
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                // start enter animation ripple
                                scalingRipple(
                                    SCALE_MIN_IMAGE,
                                    SCALE_MAX_IMAGE,
                                    durationEnter,
                                    interpolatorEnter,
                                    rippleView,
                                    rippleAnimator
                                )
                            },
                            Int.ZERO.toLong()
                        )
                    }
                }
                false
            }

            // add click listener
            rootButtonContainer.setOnClickListener {
                setSelected(index)
            }
            rootButtonContainer.id = bottomMenu.id

            // add view to container
            navbarContainer?.addView(rootButtonContainer)
        }
        layoutContent()
    }

    private fun layoutContent() {
        when {
            (indexOfChild(navbarContainer) >= 0) -> removeView(navbarContainer)
        }
        addView(navbarContainer)
    }

    private fun handleItemClicked(index: Int, bottomMenu: BottomMenu) {
        // invoke listener
        Handler().post {
            if (listener?.menuClicked(index, bottomMenu.id) == true) {
                changeColor(index)
                selectedItem = index
            }
        }
    }

    fun updateHomeBottomMenuWhenScrolling(isForYouToHomeMenu: Boolean) {
        this.isForYouToHomeSelected = isForYouToHomeMenu
        updateHomeMenuView()
    }

    fun getForYouToHomeSelected(): Boolean {
        return isForYouToHomeSelected
    }

    // changes: switch title, icon, animation based on home or for you bottom nav
    // isForYouToHomeSelected: true -> for you tab -> home header
    // isForYouToHomeSelected: false -> home header -> for you tab
    private fun updateHomeMenuView() {
        selectedItem?.let { position ->

            val selectedIconPair = iconList[position]
            val selectedIcon = selectedIconPair.first
            val iconPlaceHolder = iconPlaceholderList[position]

            // update title based on home header or for you section
            titleList[position].text = if (isForYouToHomeSelected) {
                menu[position].homeForYou?.homeTitle.orEmpty()
            } else {
                menu[position].homeForYou?.forYouTitle.orEmpty()
            }
            titleList[position].invalidate()

            selectedIcon.invalidate()

            if (isDeviceAnimationDisabled()) {
                val pairSelectedItem = iconList[selectedItem ?: 0]
                menu[selectedItem ?: 0].imageEnabledName?.let {
                    pairSelectedItem.first.setImageResource(it)
                }

                val pairNewItem = iconList[position]
                menu[position].imageName?.let {
                    pairNewItem.first.setImageResource(it)
                }
                return
            }

            // update image based on home header or for you section
            // todo will add for dark mode later

            selectedIcon.cancelAnimation()

            // update animation based on home header or for you section
            // todo will add for dark mode later
            val animTransitionName = if (isForYouToHomeSelected) {
                menu[position].homeForYou?.animThumbToHomeName
            } else {
                menu[position].homeForYou?.animHomeToThumbName
            }

            animTransitionName?.let {
                selectedIcon.setAnimation(it)
                selectedIcon.repeatCount = Int.ZERO
                iconPlaceHolder.visibility = View.INVISIBLE
                selectedIcon.visibility = View.VISIBLE
                selectedIcon.playAnimation()
            }

            iconList[position] = Pair(selectedIcon, selectedIconPair.second)

            selectedIcon.invalidate()
        }
    }

    private fun changeColor(newPosition: Int) {
        if (selectedItem == newPosition) {
            if (newPosition == Int.ZERO) {
                homeForYouListener?.homeForYouMenuReselected(newPosition, menu[newPosition].id)
            } else {
                listener?.menuReselected(newPosition, menu[newPosition].id)
            }
            return
        }

        // when device animation disabled, only use image resource to prevent stackoverflow error
        // https://github.com/airbnb/lottie-android/issues/1534
        if (isDeviceAnimationDisabled()) {
            val pairSelectedItem = iconList[selectedItem ?: 0]
            menu[selectedItem ?: 0].imageEnabledName?.let {
                pairSelectedItem.first.setImageResource(it)
            }

            val pairNewItem = iconList[newPosition ?: 0]
            menu[newPosition ?: 0].imageName?.let {
                pairNewItem.first.setImageResource(it)
            }
            return
        }

        if (selectedItem == Int.ZERO) {
            if (!isForYouToHomeSelected) {
                val oldIcon = iconList[selectedItem ?: 0].first
                oldIcon.cancelAnimation()
                oldIcon.visibility = View.INVISIBLE
                oldIcon.invalidate()
            }
        }

        if (iconList[selectedItem ?: 0].second) {
            val pair = iconList[selectedItem ?: 0]
            pair.first.cancelAnimation()
            val animToEnabledName = if (!isDarkMode) {
                menu[selectedItem ?: 0].animToEnabledName
            } else {
                menu[selectedItem ?: 0].animDarkToEnabledName
            }
            animToEnabledName?.let {
                pair.first.setAnimation(it)
                pair.first.speed = menu[selectedItem ?: 0].animToEnabledSpeed
            }

            iconList[selectedItem ?: 0] = Pair(pair.first, false)
        }

        selectedItem?.let {
            val selectedIconPair = iconList[it]
            val selectedIcon = selectedIconPair.first
            if (!selectedIconPair.second) {
                selectedIcon.visibility = View.VISIBLE
                selectedIcon.playAnimation()
            }
            iconList[it] = Pair(selectedIcon, true)
            titleList[it].setTextColor(buttonColor)
            selectedIcon.invalidate()
            titleList[it].invalidate()
        }

        // change currently selected item color
        val activeSelectedItemColor =
            ContextCompat.getColor(modeAwareContext, menu[newPosition].activeButtonColor)
        val newSelectedItemPair = iconList[newPosition]
        val newSelectedItem = newSelectedItemPair.first
        if (!newSelectedItemPair.second) {
            newSelectedItem.visibility = View.VISIBLE
            newSelectedItem.playAnimation()
        }

        iconList[newPosition] = Pair(newSelectedItem, true)

        titleList[newPosition].setTextColor(activeSelectedItemColor)
        newSelectedItem.invalidate()
        titleList[newPosition].invalidate()

        selectedItem = newPosition
    }

    fun setSelected(position: Int) {
        if (menu.size > position) {
            handleItemClicked(position, menu[position])
        }
    }

    fun setMenu(menu: List<BottomMenu>) {
        this.menu.clear()
        this.menu.addAll(menu)
        itemCount = this.menu.size
        resizeContainer()

        setupMenuItems(modeAwareContext)
        invalidate()
    }

    fun isForYouToHomeBottomNavSelected(): Boolean {
        return isForYouToHomeSelected
    }

    fun setMenuClickListener(listener: IBottomClickListener) {
        this.listener = listener
    }

    fun setHomeForYouMenuClickListener(listener: IBottomHomeForYouClickListener) {
        this.homeForYouListener = listener
    }

    fun setNavbarPositionTop() {
        layoutContent()
        invalidate()
    }

    private fun isDeviceAnimationDisabled() = getAnimationScale(context) == 0f

    private fun getAnimationScale(context: Context): Float {
        return Settings.System.getFloat(
            context.contentResolver,
            Settings.System.ANIMATOR_DURATION_SCALE,
            1.0f
        )
    }

    private fun scalingRipple(
        start: Float = currentRippleScale,
        end: Float,
        duration: Long,
        pathInterpolator: Interpolator,
        rippleView: View,
        rippleAnimator: ValueAnimator
    ) {
        rippleAnimator.setFloatValues(start, end)
        rippleAnimator.removeAllListeners()
        rippleAnimator.removeAllUpdateListeners()
        rippleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if (start < end) {
                rippleView.scaleX = value
                rippleView.scaleY = value
            }
            val alpha =
                ((value - SCALE_MIN_IMAGE) / (SCALE_MAX_IMAGE - SCALE_MIN_IMAGE)) * MAX_ALPHA_RIPPLE
            rippleView.alpha = alpha
            currentRippleScale = value
        }
        rippleAnimator.duration = duration
        rippleAnimator.interpolator = pathInterpolator
        rippleAnimator.start()
    }
}

data class BottomMenu(
    val id: Int,
    val title: String,
    val homeForYou: HomeForYouMenu? = null,
    val animName: Int? = null,
    val animToEnabledName: Int? = null,
    val animDarkName: Int? = null,
    val animDarkToEnabledName: Int? = null,
    val imageName: Int? = null,
    val imageEnabledName: Int? = null,
    val activeButtonColor: Int,
    val useBadge: Boolean = true,
    val animSpeed: Float = 1f,
    val animToEnabledSpeed: Float = 1f
)

data class HomeForYouMenu(
    val homeTitle: String,
    val forYouTitle: String,
    val homeImageName: Int? = null,
    val forYouImageName: Int? = null,
    val animThumbIdleName: Int? = null,
    val animHomeToThumbName: Int? = null,
    val animThumbToHomeName: Int? = null
)

interface IBottomClickListener {
    fun menuClicked(position: Int, id: Int): Boolean
    fun menuReselected(position: Int, id: Int)
}

interface IBottomHomeForYouClickListener {
    fun homeForYouMenuReselected(position: Int, id: Int)
}

fun Float.toDpInt(): Int = this.toPx().toInt()
