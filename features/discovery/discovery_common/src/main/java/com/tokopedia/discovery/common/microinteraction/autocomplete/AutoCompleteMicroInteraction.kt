package com.tokopedia.discovery.common.microinteraction.autocomplete

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.discovery.common.microinteraction.SearchBarMicroInteractionAttributes
import com.tokopedia.discovery.common.microinteraction.onAnimationEndListener
import com.tokopedia.discovery.common.microinteraction.searchBarMicroInteractionAnimator
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.toPx
import java.lang.ref.WeakReference

class AutoCompleteMicroInteraction internal constructor(
    context: Context
) {
    private val contextRef = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    private var containerViewGroupRef: WeakReference<ConstraintLayout?>? = null
    private val containerViewGroup: ConstraintLayout?
        get() = containerViewGroupRef?.get()

    private var backButtonViewRef: WeakReference<View?>? = null
    private val backButtonView: View?
        get() = backButtonViewRef?.get()

    private var searchBarViewRef: WeakReference<View?>? = null
    private val searchBarView: View?
        get() = searchBarViewRef?.get()

    private var sideIconViewRef: WeakReference<View?>? = null
    private val sideIconView: View?
        get() = sideIconViewRef?.get()

    private var searchBarMicroInteractionAttributes = SearchBarMicroInteractionAttributes()
    private var searchBarConstraintSet = ConstraintSet()
    private var homeIconList: List<IconUnify> = listOf()
    private var animateContentDone = false

    fun setSearchBarMicroInteractionAttributes(
        searchBarMicroInteractionAttributes: SearchBarMicroInteractionAttributes
    ) {
        this.searchBarMicroInteractionAttributes = searchBarMicroInteractionAttributes
    }

    fun setSearchBarComponents(
        containerViewGroup: ConstraintLayout?,
        backButtonView: View?,
        searchBarView: View?,
        sideIconView: View?,
    ) {
        this.containerViewGroupRef = WeakReference(containerViewGroup)
        this.backButtonViewRef = WeakReference(backButtonView)
        this.searchBarViewRef = WeakReference(searchBarView)
        this.sideIconViewRef = WeakReference(sideIconView)
    }

    fun animateSearchBar() {
        preAnimate()

        val searchBarAnimator = createSearchBarAnimator()
        val iconListAnimator = createHomeIconAnimator()
        val (backButtonAnimator, bouncingBackButtonAnimator) = createBackButtonAnimator()
        val (sideButtonAnimator, bouncingSideButtonAnimator) = createSideButtonAnimator()

        val animatorList = iconListAnimator +
            listOf(
                backButtonAnimator,
                searchBarAnimator,
                sideButtonAnimator,
            )

        startAnimations {
            playTogether(animatorList)

            play(sideButtonAnimator).before(bouncingSideButtonAnimator)
            play(backButtonAnimator).before(bouncingBackButtonAnimator)

            addListener(onAnimationEndListener {
                postAnimate()
            })
        }
    }

    private fun preAnimate() {
        saveConstraintSet()

        hideNonSearchBarView()

        createHomeIconList()
        addHomeIconToContainer()

        configurePreAnimateConstraint()
    }

    private fun saveConstraintSet() {
        val containerViewGroup = containerViewGroup ?: return
        searchBarConstraintSet = ConstraintSet().also { it.clone(containerViewGroup) }
    }

    private fun hideNonSearchBarView() {
        backButtonView?.scaleXY(HIDE_BUTTON_SCALE_DP)
        sideIconView?.scaleXY(HIDE_BUTTON_SCALE_DP)
    }

    private fun View?.scaleXY(scale: Float) {
        scaleXY(scale, scale)
    }

    private fun View?.scaleXY(scaleX: Float, scaleY: Float) {
        this?.scaleX = scaleX
        this?.scaleY = scaleY
    }

    private fun createHomeIconList() {
        val context = context ?: return

        homeIconList = iconIdList.map {
            createHomeIcon(context, it)
        }
    }

    private fun addHomeIconToContainer() {
        homeIconList.forEach {
            containerViewGroup?.addView(it)
        }
    }

    private fun createHomeIcon(context: Context, iconId: Int): IconUnify =
        IconUnify(context, iconId).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                HOME_ICON_SIZE_DP.toPx(),
                HOME_ICON_SIZE_DP.toPx()
            )
        }

    private fun configurePreAnimateConstraint() {
        val searchBarView = searchBarView ?: return
        val searchBarViewId = searchBarView.id

        containerViewGroup?.applyConstraintSet {
            constrainWidth(searchBarView.id, searchBarMicroInteractionAttributes.width.toInt())

            connect(
                searchBarViewId,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                searchBarMicroInteractionAttributes.x.toInt(),
            )

            clear(
                searchBarViewId,
                ConstraintSet.END,
            )

            var previousViewStartId = searchBarViewId

            homeIconList.forEach { iconUnify ->
                connect(
                    iconUnify.id,
                    ConstraintSet.START,
                    previousViewStartId,
                    ConstraintSet.END,
                    HOME_ICON_MARGIN_START_DP.toPx()
                )

                connect(
                    iconUnify.id,
                    ConstraintSet.TOP,
                    searchBarViewId,
                    ConstraintSet.TOP,
                )

                connect(
                    iconUnify.id,
                    ConstraintSet.BOTTOM,
                    searchBarViewId,
                    ConstraintSet.BOTTOM,
                )

                previousViewStartId = iconUnify.id
            }
        }
    }

    private fun ConstraintLayout.applyConstraintSet(apply: ConstraintSet.() -> Unit) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.apply()
        constraintSet.applyTo(this)
    }

    private fun createSearchBarAnimator(): ValueAnimator? {
        val searchBarView = searchBarView ?: return null
        val searchBarLayoutParams = searchBarView.layoutParams as? ViewGroup.MarginLayoutParams ?: return null
        val initialMarginStart = searchBarMicroInteractionAttributes.x.toInt()
        val initialWidth = searchBarMicroInteractionAttributes.width.toInt()

        val fullWidthIncrement = getSearchBarFinalWidth() - initialWidth
        val fullMarginStartIncrement = SEARCH_BAR_MARGIN_START_PARENT_DP.toPx() - initialMarginStart

        return searchBarMicroInteractionAnimator(
            SEARCH_BAR_ANIMATOR_START_VALUE,
            SEARCH_BAR_ANIMATOR_END_VALUE,
            SEARCH_BAR_ANIMATOR_DURATION
        ) { valuePercentage ->
            val widthIncrement = valuePercentage * fullWidthIncrement
            val marginIncrement = valuePercentage * fullMarginStartIncrement

            searchBarLayoutParams.width = initialWidth + widthIncrement.toInt()
            searchBarLayoutParams.marginStart = initialMarginStart + marginIncrement.toInt()
            searchBarView.layoutParams = searchBarLayoutParams
        }
    }

    private fun getSearchBarFinalWidth(): Int {
        val screenWidth = context?.resources?.displayMetrics?.widthPixels ?: return 0

        return screenWidth - SEARCH_BAR_HORIZONTAL_MARGIN_DP.toPx()
    }

    private fun createHomeIconAnimator(): List<ValueAnimator> =
        homeIconList.map { iconView ->
            searchBarMicroInteractionAnimator(
                start = HOME_ICON_ANIMATOR_START_VALUE,
                end = HOME_ICON_ANIMATOR_END_VALUE,
                duration = HOME_ICON_ANIMATOR_DURATION,
            ) {
                iconView.scaleX = it
                iconView.scaleY = it
            }
        }

    private fun View.bouncingViewAnimator(
        scaleFactor: Float
    ): Pair<ValueAnimator?, ValueAnimator?> {
        val bounceStartAnimator = scaleChangeAnimator(
            start = HIDE_BUTTON_SCALE_DP,
            end = scaleFactor,
            duration = START_BOUNCING_DURATION,
        )

        val bounceEndAnimator = scaleChangeAnimator(
            start = scaleFactor,
            end = SHOW_BUTTON_SCALE_DP,
            duration = END_BOUNCING_DURATION,
        )

        return Pair(bounceStartAnimator, bounceEndAnimator)
    }

    private fun View.scaleChangeAnimator(start: Float, end: Float, duration: Long) =
        searchBarMicroInteractionAnimator(start, end, duration) {
            scaleXY(it)
        }

    private fun createBackButtonAnimator(): Pair<ValueAnimator?, ValueAnimator?> {
        val scaleFactor = BACK_BUTTON_BOUNCING_START_SIZE_DP / BACK_BUTTON_END_SIZE_DP

        return backButtonView?.bouncingViewAnimator(scaleFactor) ?: pairOfNull()
    }

    private fun createSideButtonAnimator(): Pair<ValueAnimator?, ValueAnimator?> {
        val scaleFactor = SIDE_BUTTON_BOUNCING_START_SIZE_DP / SIDE_BUTTON_END_SIZE_DP

        return sideIconView?.bouncingViewAnimator(scaleFactor) ?: pairOfNull()
    }

    private fun startAnimations(animationSettings: AnimatorSet.() -> Unit) {
        AnimatorSet().run {
            animationSettings()

            start()
        }
    }

    private fun postAnimate() {
        removeHomeIcons()
        reconfigureSearchBarView()
        clearReferences()
    }

    private fun removeHomeIcons() {
        val containerViewGroup = containerViewGroup ?: return

        homeIconList.forEach {
            val iconUnify = containerViewGroup.findViewById<View?>(it.id) ?: return@forEach
            containerViewGroup.removeView(iconUnify)
        }
    }

    private fun reconfigureSearchBarView() {
        val containerViewGroup = containerViewGroup ?: return
        searchBarConstraintSet.applyTo(containerViewGroup)
    }

    private fun clearReferences() {
        homeIconList = listOf()

        containerViewGroupRef?.clear()
        containerViewGroupRef = null

        backButtonViewRef?.clear()
        backButtonViewRef = null

        searchBarViewRef?.clear()
        searchBarViewRef = null

        sideIconViewRef?.clear()
        sideIconViewRef = null
    }

    fun animateContent(contentView: View?) {
        if (animateContentDone) return

        createContentAnimator(contentView)?.start()

        animateContentDone = true
    }

    private fun createContentAnimator(contentView: View?): ValueAnimator? {
        contentView ?: return null

        return searchBarMicroInteractionAnimator(
            CONTENT_ANIMATION_START,
            CONTENT_ANIMATION_END,
            CONTENT_ANIMATION_DURATION
        ) {
            contentView.visibility = View.VISIBLE
            contentView.scaleX = contentScaleAnimatedValue(it) + CONTENT_ANIMATION_START_SIZE_DP
            contentView.scaleY = contentScaleAnimatedValue(it) + CONTENT_ANIMATION_START_SIZE_DP
            contentView.alpha = it
        }
    }

    private fun contentScaleAnimatedValue(animatedValue: Float) =
        animatedValue * (CONTENT_ANIMATION_END_SIZE_DP - CONTENT_ANIMATION_START_SIZE_DP)

    companion object {
        private const val SEARCH_BAR_MARGIN_START_PARENT_DP = 44
        private const val SEARCH_BAR_ANIMATOR_START_VALUE = 0f
        private const val SEARCH_BAR_ANIMATOR_END_VALUE = 1f
        private const val SEARCH_BAR_ANIMATOR_DURATION: Long = 250
        private const val SEARCH_BAR_HORIZONTAL_MARGIN_DP = 60

        private const val HOME_ICON_MARGIN_START_DP = 11
        private const val HOME_ICON_ANIMATOR_DURATION: Long = 120
        private const val HOME_ICON_ANIMATOR_START_VALUE = 1f
        private const val HOME_ICON_ANIMATOR_END_VALUE = 0f
        private const val HOME_ICON_SIZE_DP = 25

        private const val HIDE_BUTTON_SCALE_DP = 0f
        private const val SHOW_BUTTON_SCALE_DP = 1f
        private const val START_BOUNCING_DURATION: Long = 200
        private const val END_BOUNCING_DURATION: Long = 100

        private const val BACK_BUTTON_BOUNCING_START_SIZE_DP = 26f
        private const val BACK_BUTTON_END_SIZE_DP = 24f

        private const val SIDE_BUTTON_BOUNCING_START_SIZE_DP = 18f
        private const val SIDE_BUTTON_END_SIZE_DP = 16f

        private const val CONTENT_ANIMATION_START = 0f
        private const val CONTENT_ANIMATION_END = 1f
        private const val CONTENT_ANIMATION_START_SIZE_DP = 0.95f
        private const val CONTENT_ANIMATION_END_SIZE_DP = 1f
        private const val CONTENT_ANIMATION_DURATION: Long = 300

        private fun pairOfNull() = Pair(null, null)

        private val iconIdList = listOf(
            IconUnify.MESSAGE,
            IconUnify.BELL,
            IconUnify.CART,
            IconUnify.MENU_HAMBURGER,
        )
    }
}
