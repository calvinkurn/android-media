package com.tokopedia.searchbar.navigation_component

import android.text.TextUtils
import android.view.View
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
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.LazyThreadSafetyMode.NONE
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
                             val editorActionCallback: ((hint: String)-> Unit)?,
                             val hintImpressionCallback: ((hint: HintData, index: Int) -> Unit)? = null,
                             val hintClickCallback: ((hint: HintData, index: Int) -> Unit)? = null,
                             val searchBtnClickCallback: ((hint: HintData, index: Int, isUsingDefaultHint: Boolean) -> Unit)? = null
) : CoroutineScope {

    companion object {
        private const val HIDE_SCALE = 0f
        private const val SHOW_SCALE = 1f

        private const val INITIAL_ANIMATION_DURATION = 0L
        private const val SCALE_X_PROPERTY = "scaleX"
        private const val SCALE_Y_PROPERTY = "scaleY"
        private const val DEFAULT_SCALE_DURATION = 150L
        private const val REPEAT_COUNT = 1
        private const val CLEAR_ICON_ANIM_DURATION = 200L
        private const val SEARCH_BTN_WORD_SOURCE = "search_bar_button"
    }

    private lateinit var animationJob: Job
    val etSearch : EditText? by lazy(NONE) {
        view.findViewById(R.id.et_search)
    }
    private val iconClear: IconUnify? by lazy {
        view.findViewById(R.id.icon_clear)
    }
    private val layoutSearch: View? by lazy(NONE) {
        view.findViewById(R.id.layout_search)
    }
    private val searchCta: Typography? by lazy {
        view.findViewById(R.id.tv_search_cta)
    }

    init {
        layoutSearch?.visibility = VISIBLE
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun setHint(
            hints: List<HintData>,
            isShowTransition: Boolean,
            durationAutoTransition: Long
    ) {
        iconClear?.gone()
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

    private fun setOnSearchCtaClicked(hintData: HintData, index: Int) {
        val hint = hintData.copy(wordsSource = SEARCH_BTN_WORD_SOURCE)
        val placeholder = hint.placeholder
        val defaultHint = view.context.getString(R.string.search_tokopedia)
        val isUsingDefaultHint = placeholder.isBlank() || placeholder.equals(defaultHint, true)
        searchCta?.setOnClickListener {
            searchBtnClickCallback?.invoke(hint, index, isUsingDefaultHint)
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

    fun setEditableSearchbar(hint: String) {
        setEditorActionListener()
        etSearch?.visible()
        etSearch?.hint = hint
        iconClear?.clearAnimation()
        iconClear?.animate()?.scaleX(HIDE_SCALE)?.scaleY(HIDE_SCALE)?.setDuration(INITIAL_ANIMATION_DURATION)?.start()

        etSearch?.addTextChangedListener(
            onTextChanged = { text, start, count, after ->
                navSearchbarInterface?.invoke(
                    text, start, count, after
                )

                iconClear?.visible()
                if(TextUtils.isEmpty(text)) {
                    iconClear?.clearAnimation()
                    iconClear?.animate()?.scaleX(HIDE_SCALE)?.scaleY(HIDE_SCALE)?.setDuration(CLEAR_ICON_ANIM_DURATION)?.start()
                } else {
                    iconClear?.clearAnimation()
                    iconClear?.animate()?.scaleX(SHOW_SCALE)?.scaleY(SHOW_SCALE)?.setDuration(CLEAR_ICON_ANIM_DURATION)?.start()
                }
            }
        )
        setupSearchByKeyword()
    }

    private fun setupSearchByKeyword() {
        searchCta?.setOnClickListener {
            val keyword = etSearch?.text?.toString().orEmpty()
            if (keyword.isNotBlank()) {
                editorActionCallback?.invoke(keyword)
            }
        }
    }

    private fun setEditorActionListener() {
        etSearch?.isFocusableInTouchMode = true;
        etSearch?.isFocusable = true;
        etSearch?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        etSearch?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                NavToolbarTracking.clickNavToolbarComponent(
                    pageName = topNavComponentListener.getPageName(),
                    componentName = IconList.NAME_SEARCH_BAR,
                    userId = topNavComponentListener.getUserId(),
                    keyword = etSearch?.text.toString()
                )
            }
        }
        iconClear?.setOnClickListener {
            etSearch?.text?.clear()
            editorActionCallback?.invoke("")
        }
        etSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editorActionCallback?.invoke(etSearch?.text.toString())
                true
            } else false
        }
    }

    private fun setHintSingle(hint: HintData) {
        val (placeholder, index) =
            if (hint.placeholder.isEmpty()) view.context.getString(R.string.search_tokopedia) to -1
            else hint.placeholder to 0
        etSearch?.hint = placeholder
        hintImpressionCallback?.invoke(hint, index)

        etSearch?.setOnClickListener {
            if (!disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = topNavComponentListener.getPageName(),
                        componentName = IconList.NAME_SEARCH_BAR,
                        userId = topNavComponentListener.getUserId(),
                        keyword = hint.keyword
                )
            }

            hintClickCallback?.invoke(hint, index)

            if (searchbarClickCallback == null && editorActionCallback == null) {
                onClickHint()
            } else {
                searchbarClickCallback?.invoke(hint.keyword)
            }
        }
        setOnSearchCtaClicked(hint, index)
    }

    private fun setHintAnimation(
            hints: List<HintData>,
            durationAutoTransition: Long
    ) {
        var iterator = hints.iterator().withIndex()

        animationJob = launch {
            while (true) {
                var hint = view.context.getString(R.string.search_tokopedia)
                var keyword = ""
                var index = -1
                var hintData = HintData(hint, keyword)
                val slideUpIn = AnimationUtils.loadAnimation(view.context, R.anim.search_bar_slide_up_in)
                slideUpIn.interpolator = EasingInterpolator(Ease.QUART_OUT)
                val slideOutUp = AnimationUtils.loadAnimation(view.context, R.anim.slide_out_up)
                slideOutUp.interpolator = EasingInterpolator(Ease.QUART_IN)
                slideOutUp.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        if (iterator.hasNext()) {
                            val nextIndexedIterator = iterator.next()
                            index = nextIndexedIterator.index
                            hintData = nextIndexedIterator.value
                            hint = hintData.placeholder
                            keyword = hintData.keyword
                        } else {
                            iterator = hints.iterator().withIndex()
                            val nextIndexedIterator = iterator.next()
                            index = nextIndexedIterator.index
                            hintData = nextIndexedIterator.value
                            hint = hintData.placeholder
                            keyword = hintData.keyword
                        }
                        etSearch?.hint = hint
                        etSearch?.startAnimation(slideUpIn)
                        searchbarImpressionCallback?.invoke(hint)
                        hintImpressionCallback?.invoke(hintData, index)
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
                    hintClickCallback?.invoke(hintData, index)
                    if (searchbarClickCallback == null) {
                        onClickHint()
                    } else {
                        searchbarClickCallback.invoke(keyword)
                    }
                }
                setOnSearchCtaClicked(hintData, index)
                delay(durationAutoTransition)
            }
        }
    }

    private fun onClickHint() {
        RouteManager.route(view.context, applink)
    }
}
