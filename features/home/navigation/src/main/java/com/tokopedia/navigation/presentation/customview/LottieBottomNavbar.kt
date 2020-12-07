package com.tokopedia.navigation.presentation.customview

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.navigation.R

private const val DEFAULT_HEIGHT = 56f
private const val DEFAULT_ICON_PADDING = 2
private const val DEFAULT_TITLE_PADDING = 2
private const val DEFAULT_TITLE_PADDING_BOTTOM = 4
private const val DEFAULT_TEXT_SIZE = 10f

class LottieBottomNavbar : LinearLayout {
    private val badgeTextViewList: MutableList<TextView>? = mutableListOf()
    private var emptyBadgeLayoutParam: FrameLayout.LayoutParams? = null
    private var badgeLayoutParam: FrameLayout.LayoutParams? = null
    private var menu: MutableList<BottomMenu> = ArrayList()
    private var listener: IBottomClickListener? = null
    private var iconList: MutableList<Pair<LottieAnimationView, Boolean>> = ArrayList()
    private var iconPlaceholderList: MutableList<ImageView> = ArrayList()
    private var titleList: MutableList<TextView> = ArrayList()
    private var containerList: MutableList<LinearLayout> = ArrayList()
    private var itemCount: Int = 1
    private var buttonContainerBackgroundColor: Int = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
    private var buttonsHeight: Float = DEFAULT_HEIGHT
    private var selectedItem: Int? = null
    private var containerWidth: Int = 0
    private var navbarContainer: LinearLayout? = null
    private var buttonColor: Int = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N300)
    private var activeButtonColor: Int = Color.TRANSPARENT
    private var isThreeItemBottomNav: Boolean = false

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        getLayoutAtr(attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle) {
        getLayoutAtr(attrs)
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

    fun setBadge(badgeValue: Int = 0, iconPosition: Int, visibility: Int = View.VISIBLE) {
        val badge: View? = navbarContainer?.getChildAt(iconPosition)
        val badgeText = badge?.findViewById<TextView>(R.id.notification_badge)

        if (badgeValue == 0) {
            badgeText?.layoutParams = emptyBadgeLayoutParam
            badgeText?.setPadding(
                    resources.getDimensionPixelOffset(R.dimen.dp_5),
                    resources.getDimensionPixelOffset(R.dimen.dp_1),
                    resources.getDimensionPixelOffset(R.dimen.dp_2),
                    resources.getDimensionPixelOffset(R.dimen.dp_1)
            )
            badgeText?.text = ""
            badgeText?.background = ContextCompat.getDrawable(context, R.drawable.bg_badge_circle)
        } else {
            badgeText?.layoutParams = badgeLayoutParam
            badgeText?.setPadding(
                    resources.getDimensionPixelOffset(R.dimen.dp_5),
                    resources.getDimensionPixelOffset(R.dimen.dp_2),
                    resources.getDimensionPixelOffset(R.dimen.dp_5),
                    resources.getDimensionPixelOffset(R.dimen.dp_2)
            )

            badgeText?.background = ContextCompat.getDrawable(context, R.drawable.bg_badge_circular)
            badgeText?.text = badgeValue.toString()
        }

        badgeText?.bringToFront()
        badgeText?.visibility = visibility
    }

    private fun adjustBadgePosition() {
        if (menu.isEmpty()) return
        val itemWidthSize = containerWidth/menu.size
        val badgeRightMargin = if (isThreeItemBottomNav) (itemWidthSize/2.7).toInt() else itemWidthSize/4

        badgeLayoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        badgeLayoutParam?.gravity = Gravity.END
        badgeLayoutParam?.setMargins(
                resources.getDimensionPixelOffset(R.dimen.dp_0),
                resources.getDimensionPixelOffset(R.dimen.dp_1),
                badgeRightMargin,
                resources.getDimensionPixelOffset(R.dimen.dp_1)
        )

        emptyBadgeLayoutParam = FrameLayout.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_12),
                resources.getDimensionPixelOffset(R.dimen.dp_12))
        emptyBadgeLayoutParam?.gravity = Gravity.END
        emptyBadgeLayoutParam?.setMargins(
                resources.getDimensionPixelOffset(R.dimen.dp_0),
                resources.getDimensionPixelOffset(R.dimen.dp_1),
                badgeRightMargin,
                resources.getDimensionPixelOffset(R.dimen.dp_1)
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
            val llLayoutParam = LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.MATCH_PARENT)
            it.layoutParams = llLayoutParam
            it.invalidate()
        }
        invalidate()
    }

    private fun getLayoutAtr(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LottieBottomNavbar)
        val defaultButtonHeight = DEFAULT_HEIGHT * context.resources.displayMetrics.density

        buttonContainerBackgroundColor = a.getColor(R.styleable.LottieBottomNavbar_buttonContainerBackgroundColor, androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        buttonsHeight = a.getDimension(R.styleable.LottieBottomNavbar_buttonsHeight, defaultButtonHeight)

        buttonColor = a.getColor(R.styleable.LottieBottomNavbar_buttonColor, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
        activeButtonColor = a.getColor(R.styleable.LottieBottomNavbar_activeButtonColor, ContextCompat.getColor(context, R.color.transparent))
        a.recycle()

        weightSum = 1f
        orientation = VERTICAL
    }

    private fun setupMenuItems() {
        // menu item width is equal: container width / size of menu item
        val itemWidth = containerWidth / itemCount

        iconList.clear()
        titleList.clear()
        containerList.clear()

        val llLayoutParam = LinearLayout.LayoutParams(itemWidth, resources.getDimensionPixelOffset(R.dimen.dp_28))
        val imgLayoutParam = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelOffset(R.dimen.dp_28))

        badgeLayoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        badgeLayoutParam?.gravity = Gravity.END
        badgeLayoutParam?.setMargins(
                resources.getDimensionPixelOffset(R.dimen.dp_0),
                resources.getDimensionPixelOffset(R.dimen.dp_1),
                resources.getDimensionPixelOffset(R.dimen.dp_20),
                resources.getDimensionPixelOffset(R.dimen.dp_1)
        )

        emptyBadgeLayoutParam = FrameLayout.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_12),
                resources.getDimensionPixelOffset(R.dimen.dp_12))
        emptyBadgeLayoutParam?.gravity = Gravity.END
        emptyBadgeLayoutParam?.setMargins(
                resources.getDimensionPixelOffset(R.dimen.dp_0),
                resources.getDimensionPixelOffset(R.dimen.dp_1),
                resources.getDimensionPixelOffset(R.dimen.dp_25),
                resources.getDimensionPixelOffset(R.dimen.dp_1)
        )

        val txtLayoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        // create Button Container
        navbarContainer = LinearLayout(context)
        navbarContainer?.let {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, buttonsHeight.toInt())
            it.setBackgroundColor(buttonContainerBackgroundColor)
            it.orientation = HORIZONTAL
        }


        // for each menu:
        // create item container, draw image icon and title, add click listener if set
        menu.forEachIndexed { index, bottomMenu ->
            // add linear layout as container for menu item
            val buttonContainer = LinearLayout(context)

            buttonContainer.layoutParams = llLayoutParam
            buttonContainer.orientation = LinearLayout.VERTICAL
            buttonContainer.gravity = Gravity.CENTER
            buttonContainer.setBackgroundColor(Color.TRANSPARENT)
            containerList.add(index, buttonContainer)

            // add image view to display menu icon
            val icon = LottieAnimationView(context)
            icon.tag = context.getString(R.string.tag_lottie_animation_view)+bottomMenu.id
            icon.layoutParams = imgLayoutParam
            icon.setPadding(DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, 0)
            if (!isDeviceAnimationDisabled()) {
                bottomMenu.animName?.let {
                    icon.setAnimation(bottomMenu.animName)
                    icon.speed = bottomMenu.animSpeed
                }
            } else {
                bottomMenu.imageEnabledName?.let {
                    icon.setImageResource(it)
                }
            }
            if (bottomMenu.animName == null) {
                bottomMenu.imageName?.let {
                    icon.setImageResource(it)
                }
            }

            iconList.add(index, Pair(icon, false))

            val imageContainer = FrameLayout(context)
            val fLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT)
            imageContainer.layoutParams = fLayoutParams

            val iconPlaceholder = ImageView(context)
            iconPlaceholder.tag = "iconPlaceholder"+bottomMenu.id
            iconPlaceholder.setPadding(DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, DEFAULT_ICON_PADDING, 0)
            iconPlaceholder.layoutParams = imgLayoutParam
            iconPlaceholder.visibility = View.INVISIBLE
            iconPlaceholderList.add(index, iconPlaceholder)

            imageContainer.addView(icon)
            imageContainer.addView(iconPlaceholder)

            icon.addAnimatorListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (selectedItem != index) {
                        val bottomMenuSelected = bottomMenu
                        val iconSelected = icon

                        bottomMenuSelected.imageEnabledName?.let {
                            iconPlaceholder.setImageResource(it)
                        }
                        bottomMenuSelected.animName?.let {
                            iconSelected.setAnimation(it)
                            iconSelected.speed = bottomMenuSelected.animSpeed
                        }
                    } else {
                        val bottomMenuSelected = bottomMenu
                        val iconSelected = icon

                        bottomMenuSelected.imageName?.let {
                            iconPlaceholder.setImageResource(it)
                        }
                        bottomMenuSelected.animToEnabledName?.let {
                            iconSelected.setAnimation(it)
                            iconSelected.speed = bottomMenuSelected.animToEnabledSpeed
                        }
                    }

                    iconList[index] = Pair(icon, false)
                    icon.visibility = View.INVISIBLE
                    iconPlaceholder.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
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
                badgeTextView.tag = context.getString(R.string.tag_badge_textview)+bottomMenu.id.toString()
                badgeTextView.visibility = View.INVISIBLE
                imageContainer.addView(badge)
                badge.bringToFront()
            }

            buttonContainer.addView(imageContainer)

            // add text view to show title
            val title = TextView(context)
            title.layoutParams = txtLayoutParam
            title.setPadding(DEFAULT_TITLE_PADDING, 0, DEFAULT_TITLE_PADDING, DEFAULT_TITLE_PADDING_BOTTOM)
            title.text = bottomMenu.title
            title.tag = context.getString(R.string.tag_title_textview)+bottomMenu.id
            title.textSize = DEFAULT_TEXT_SIZE
            if (selectedItem != null && selectedItem == index) {
                title.setTextColor(menu[selectedItem!!].activeButtonColor)
            } else {
                title.setTextColor(buttonColor)
            }

            titleList.add(index, title)
            buttonContainer.addView(title)

            // add click listener
            buttonContainer.setOnClickListener {
                setSelected(index)
            }
            buttonContainer.id = bottomMenu.id

            // add view to container
            navbarContainer?.addView(buttonContainer)
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

    private fun changeColor(newPosition: Int) {
        if (selectedItem == newPosition) {
            listener?.menuReselected(newPosition, menu[newPosition].id)
            return
        }

        //when device animation disabled, only use image resource to prevent stackoverflow error
        //https://github.com/airbnb/lottie-android/issues/1534
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

        if (iconList[selectedItem?:0].second) {
            val pair = iconList[selectedItem?:0]
            pair.first.cancelAnimation()
            menu[selectedItem?:0].animToEnabledName?.let {
                pair.first.setAnimation(it)
                pair.first.speed = menu[selectedItem?:0].animToEnabledSpeed
            }

            iconList[selectedItem?:0] = Pair(pair.first, false)
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
        val activeSelectedItemColor = ContextCompat.getColor(context, menu[newPosition].activeButtonColor)
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

    fun setMenu(menu: List<BottomMenu>, isThreeItemBottomNav:Boolean = false) {
        this.menu.clear()
        this.menu.addAll(menu)
        this.isThreeItemBottomNav = isThreeItemBottomNav
        itemCount = this.menu.size
        resizeContainer()

        setupMenuItems()
        invalidate()
    }

    fun setMenuClickListener(listener: IBottomClickListener) {
        this.listener = listener
    }

    fun setNavbarPositionTop() {
        layoutContent()
        invalidate()
    }

    fun isDeviceAnimationDisabled() = getAnimationScale(context) == 0f

    fun getAnimationScale(context: Context): Float {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getFloat(context.contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f)
        } else {
            Settings.System.getFloat(context.contentResolver,
                    Settings.System.ANIMATOR_DURATION_SCALE, 1.0f)
        }
    }
}

data class BottomMenu(val id: Int,
                      val title: String,
                      val animName: Int? = null,
                      val animToEnabledName: Int? = null,
                      val imageName: Int? = null,
                      val imageEnabledName: Int? = null,
                      val activeButtonColor: Int,
                      val useBadge: Boolean = true,
                      val animSpeed: Float = 1f,
                      val animToEnabledSpeed: Float = 1f)
interface IBottomClickListener {
    fun menuClicked(position: Int, id: Int): Boolean
    fun menuReselected(position: Int, id: Int)
}

fun Float.toDp(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics).toInt()
}