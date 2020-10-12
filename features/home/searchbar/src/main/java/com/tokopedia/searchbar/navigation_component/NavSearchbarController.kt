package com.tokopedia.searchbar.navigation_component

import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.SearchBarAnalytics
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import kotlinx.coroutines.*
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext

class NavSearchbarController(val view: View,
                             val searchBarTrackingParam: SearchBarTrackingParam): CoroutineScope {
    private lateinit var animationJob: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val context = view.context
    val etSearch = view.et_search
    val searchBarAnalytics = SearchBarAnalytics(view.context)

    fun setHint(
            hints: ArrayList<HintData>,
            isFirstInstall: Boolean,
            isShowTransition: Boolean,
            durationAutoTransition: Long
    ) {
        if (::animationJob.isInitialized) {
            animationJob.cancel()
        }
        if (hints.size > 1 && isShowTransition) {
            setHintAnimation(hints, isFirstInstall, durationAutoTransition)
        } else {
            setHintSingle(hints[0], isFirstInstall)
        }
        etSearch.setSingleLine()
        etSearch.ellipsize = TextUtils.TruncateAt.END
    }

    private fun setHintSingle(hint: HintData, isFirstInstall: Boolean) {
        etSearch.hint = if (hint.placeholder.isEmpty()) context.getString(R.string.search_tokopedia) else hint.placeholder
        etSearch.setOnClickListener {
            onClickHint(hint.keyword, isFirstInstall)
        }
    }

    private fun setHintAnimation(
            hints: ArrayList<HintData>,
            isFirstInstall: Boolean,
            durationAutoTransition: Long
    ) {
        var iterator = hints.iterator()

        animationJob = launch {
            while (true) {
                var hint = context.getString(R.string.search_tokopedia)
                var keyword = ""
                val slideUpIn = AnimationUtils.loadAnimation(context, R.anim.slide_up_in)
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
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                etSearch.startAnimation(slideOutUp)
                etSearch.setOnClickListener {
                    onClickHint(keyword, isFirstInstall)
                }
                delay(durationAutoTransition)
            }
        }
    }

    private fun onClickHint(keyword: String, isFirstInstall: Boolean) {
        searchBarAnalytics.eventTrackingSearchBar(searchBarTrackingParam.screenName, keyword)
        if (keyword.isEmpty()) {
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        } else {
            RouteManager.route(context,
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE + searchBarTrackingParam.source,
                    searchBarTrackingParam.applinkParam,
                    safeEncodeUTF8(keyword),
                    isFirstInstall.toString())
        }
    }

    private fun safeEncodeUTF8(value: String): String {
        return try {
            URLEncoder.encode(value, Charsets.UTF_8.toString())
        }
        catch (e: Throwable) {
            value
        }
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

data class SearchBarTrackingParam(
        val screenName: String,
        val source: String,
        val applinkParam: String
)