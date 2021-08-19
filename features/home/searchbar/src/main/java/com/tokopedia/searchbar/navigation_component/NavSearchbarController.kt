package com.tokopedia.searchbar.navigation_component

import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.View.FOCUSABLE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import com.tokopedia.searchbar.navigation_component.analytics.NavToolbarTracking
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.TopNavComponentListener
import com.tokopedia.searchbar.util.NavUtil
import com.tokopedia.unifycomponents.SearchBarUnify
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NavSearchbarController(val view: View,
                             val applink: String = "",
                             val searchbarClickCallback: ((hint: String)-> Unit)?,
                             val searchbarImpressionCallback: ((hint: String)-> Unit)?,
                             val topNavComponentListener: TopNavComponentListener,
                             val disableDefaultGtmTracker: Boolean = false,
                             val navSearchbarInterface: ((text: CharSequence?,
                                                      start: Int,
                                                      count: Int,
                                                      after: Int) -> Unit)? = null,
                             val editorActionCallback: ((hint: String)-> Unit)?
) : CoroutineScope {
    var etSearch: EditText? = null
    var etSearchbarUnify: SearchBarUnify? = null

    init {
        view.layout_search.visibility = VISIBLE
        etSearch = view.et_search
        etSearchbarUnify = view.findViewById(R.id.et_searchbar_unify)
    }
    private lateinit var animationJob: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun setHint(
            hints: List<HintData>,
            isShowTransition: Boolean,
            durationAutoTransition: Long
    ) {
        etSearchbarUnify?.gone()
        etSearch?.visible()
        if (::animationJob.isInitialized) {
            animationJob.cancel()
        }
        if (hints.size > 1 && isShowTransition) {
            setHintAnimation(hints, durationAutoTransition)
        } else {
            setHintSingle(hints[0])
        }
        etSearch?.setSingleLine()
        etSearch?.ellipsize = TextUtils.TruncateAt.END
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

    fun setEditableSearchbar(hint: String) {
        setEditorActionListener()
        etSearchbarUnify?.visible()
        etSearch?.gone()
        etSearchbarUnify?.searchBarPlaceholder = hint
        etSearchbarUnify?.searchBarTextField?.addTextChangedListener(
            onTextChanged = { text, start, count, after ->
                navSearchbarInterface?.invoke(
                    text, start, count, after
                )
            }
        )
    }

    private fun setEditorActionListener() {
        etSearchbarUnify?.isFocusableInTouchMode = true;
        etSearchbarUnify?.isFocusable = true;
        etSearchbarUnify?.searchBarTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        etSearchbarUnify?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                NavToolbarTracking.clickNavToolbarComponent(
                    pageName = topNavComponentListener.getPageName(),
                    componentName = IconList.NAME_SEARCH_BAR,
                    userId = topNavComponentListener.getUserId(),
                    keyword = etSearchbarUnify?.searchBarTextField?.text.toString()
                )
            }
        }
        etSearchbarUnify?.searchBarIcon?.setOnClickListener {
            etSearchbarUnify?.searchBarTextField?.text?.clear()
            editorActionCallback?.invoke("")
        }
        etSearchbarUnify?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editorActionCallback?.invoke(etSearchbarUnify?.searchBarTextField?.text.toString())
                true
            } else false
        }
    }

    private fun setHintSingle(hint: HintData) {
        etSearch?.hint = if (hint.placeholder.isEmpty()) view.context.getString(R.string.search_tokopedia) else hint.placeholder
        etSearch?.setOnClickListener {
            if (!disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = topNavComponentListener.getPageName(),
                        componentName = IconList.NAME_SEARCH_BAR,
                        userId = topNavComponentListener.getUserId(),
                        keyword = hint.keyword
                )
            }
            if (searchbarClickCallback == null && editorActionCallback == null) {
                onClickHint()
            } else {
                searchbarClickCallback?.invoke(hint.keyword)
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
                var hint = view.context.getString(R.string.search_tokopedia)
                var keyword = ""
                val slideUpIn = AnimationUtils.loadAnimation(view.context, R.anim.search_bar_slide_up_in)
                slideUpIn.interpolator = EasingInterpolator(Ease.QUART_OUT)
                val slideOutUp = AnimationUtils.loadAnimation(view.context, R.anim.slide_out_up)
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
                        etSearch?.hint = hint
                        etSearch?.startAnimation(slideUpIn)
                        searchbarImpressionCallback?.invoke(hint)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                etSearch?.startAnimation(slideOutUp)
                etSearch?.setOnClickListener {
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
        RouteManager.route(view.context, applink)
    }
}