package com.tokopedia.searchbar.navigation_component

import android.text.TextUtils
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import com.tokopedia.searchbar.navigation_component.analytics.NavToolbarTracking
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.TopNavComponentListener
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NavSearchbarController(val view: View,
                             val applink: String = "",
                             val searchbarClickCallback: ((hint: String)-> Unit)?,
                             val searchbarImpressionCallback: ((hint: String)-> Unit)?,
                             val topNavComponentListener: TopNavComponentListener,
                             val disableDefaultGtmTracker: Boolean = false
) : CoroutineScope {
    init {
        view.layout_search.visibility = VISIBLE
    }
    private lateinit var animationJob: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val context = view.context
    val etSearch = view.et_search

    fun setHint(
            hints: List<HintData>,
            isShowTransition: Boolean,
            durationAutoTransition: Long
    ) {
        if (::animationJob.isInitialized) {
            animationJob.cancel()
        }
        if (hints.size > 1 && isShowTransition) {
            setHintAnimation(hints, durationAutoTransition)
        } else {
            setHintSingle(hints[0])
        }
        etSearch.setSingleLine()
        etSearch.ellipsize = TextUtils.TruncateAt.END
    }

    private fun setHintSingle(hint: HintData) {
        etSearch.hint = if (hint.placeholder.isEmpty()) context.getString(R.string.search_tokopedia) else hint.placeholder
        etSearch.setOnClickListener {
            if (!disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = topNavComponentListener.getPageName(),
                        componentName = IconList.NAME_SEARCH_BAR,
                        userId = topNavComponentListener.getUserId(),
                        keyword = hint.keyword
                )
            }
            if (searchbarClickCallback == null) {
                onClickHint()
            } else {
                searchbarClickCallback.invoke(hint.keyword)
            }
        }
    }

    private fun setHintAnimation(
            hints: List<HintData>,
            durationAutoTransition: Long
    ) {
        var iterator = hints.iterator()

        animationJob = launch {
            while (true) {
                var hint = context.getString(R.string.search_tokopedia)
                var keyword = ""
                val slideUpIn = AnimationUtils.loadAnimation(context, R.anim.search_bar_slide_up_in)
                slideUpIn.interpolator = EasingInterpolator(Ease.QUART_OUT)
                val slideOutUp = AnimationUtils.loadAnimation(context, R.anim.slide_out_up)
                slideOutUp.interpolator = EasingInterpolator(Ease.QUART_IN)
                slideOutUp.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        if (iterator.hasNext()) {
                            val placeholder = iterator.next()
                            hint = placeholder.placeholder
                            keyword = placeholder.keyword
                        } else {
                            iterator = hints.iterator()
                            val placeholder = iterator.next()
                            hint = placeholder.placeholder
                            keyword = placeholder.keyword
                        }
                        etSearch.hint = hint
                        etSearch.startAnimation(slideUpIn)
                        searchbarImpressionCallback?.invoke(hint)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                etSearch.startAnimation(slideOutUp)
                etSearch.setOnClickListener {
                    if (!disableDefaultGtmTracker) {
                        NavToolbarTracking.clickNavToolbarComponent(
                                pageName = topNavComponentListener.getPageName(),
                                componentName = IconList.NAME_SEARCH_BAR,
                                userId = topNavComponentListener.getUserId()
                        )
                    }
                    if (searchbarClickCallback == null) {
                        onClickHint()
                    } else {
                        searchbarClickCallback.invoke(keyword)
                    }
                }
                delay(durationAutoTransition)
            }
        }
    }

    private fun onClickHint() {
        RouteManager.route(context, applink)
    }

    fun startHintAnimation() {
        if (::animationJob.isInitialized) {
            animationJob.start()
        }
    }

    fun stopHintAnimation() {
        if (::animationJob.isInitialized) {
            animationJob.cancel()
        }
    }
}