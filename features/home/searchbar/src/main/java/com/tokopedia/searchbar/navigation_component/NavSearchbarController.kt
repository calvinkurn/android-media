package com.tokopedia.searchbar.navigation_component

import android.text.TextUtils
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import kotlinx.coroutines.*
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext

class NavSearchbarController(val view: View,
                             val applinkParam: String = "",
                             val searchbarClickCallback: ((hint: String)-> Unit)?,
                             val searchbarImpressionCallback: ((hint: String)-> Unit)?) : CoroutineScope {
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
            onClickHint(hint.keyword)
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
                        searchbarImpressionCallback?.invoke(hint)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                etSearch.startAnimation(slideOutUp)
                etSearch.setOnClickListener {
                    onClickHint(keyword)
                }
                delay(durationAutoTransition)
            }
        }
    }

    private fun onClickHint(keyword: String) {
        searchbarClickCallback?.invoke(keyword)
        if (keyword.isEmpty()) {
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        } else {
            RouteManager.route(
                    context,
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE + applinkParam,
                    applinkParam,
                    safeEncodeUTF8(keyword))
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