package com.tokopedia.autocomplete.searchbar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.autocomplete.R
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.showcase.*
import kotlinx.android.synthetic.main.autocomplete_search_bar_view.view.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit

class SearchBarView constructor(private val mContext: Context, attrs: AttributeSet) : ConstraintLayout(mContext, attrs) {

    companion object {
        const val REQUEST_VOICE = 9999
        private val TAG = SearchBarView::class.java.simpleName
        private const val LOCALE_INDONESIA = "in_ID"
        private const val IMAGE_SEARCH_SHOW_CASE_DIALOG_DELAY: Long = 600
    }

    private var mClearingFocus: Boolean = false

    private var mOldQueryText: CharSequence? = null
    private var mUserQuery: CharSequence? = null

    private lateinit var mOnQueryChangeListener: OnQueryTextListener
    private var mImageSearchClickListener: ImageSearchClickListener? = null
    private var activity: AppCompatActivity? = null
    private var mSavedState: SavedState? = null
    private var searchParameter = SearchParameter()

    private var allowVoiceSearch: Boolean = false
    private var isAllowImageSearch: Boolean = false
    private var copyText = false
    private var compositeSubscription: CompositeSubscription? = null
    private var queryListener: QueryListener? = null
    private var showCaseDialog: ShowCaseDialog? = null
    private lateinit var remoteConfig: RemoteConfig
    private var isShowShowCase = false
    private var lastQuery: String? = null
    private var hint: String? = null

    private val mOnClickListener = OnClickListener { v ->
        if (v === action_up_btn || v === action_cancel_button) {
            KeyboardHandler.DropKeyboard(activity, searchTextView)
            activity?.finish()
        } else if (v === action_voice_btn) {
            onVoiceClicked()
        } else if (v === action_image_search_btn) {
            onImageSearchClicked()
        } else if (v === action_empty_btn) {
            searchTextView?.text = null
        }
    }

    private val isVoiceAvailable: Boolean
        get() {
            val pm = context.packageManager
            val activities = pm.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
            return activities.size != 0
        }

    val isOfficial: Boolean
        get() = searchParameter.getBoolean(SearchApiConst.OFFICIAL)

    init {

        initiateView()

        initCompositeSubscriber()

    }

    private fun onVoiceClicked() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LOCALE_INDONESIA)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        if (mContext is Activity) {
            mContext.startActivityForResult(intent, REQUEST_VOICE)
        }
    }

    private fun onImageSearchClicked() {
        clearFocus()
        mImageSearchClickListener?.onImageSearchClicked()
    }

    private fun initiateView() {
        LayoutInflater.from(mContext).inflate(R.layout.autocomplete_search_bar_view, this, true)
        setListener()
        allowVoiceSearch = true

        remoteConfig = FirebaseRemoteConfigImpl(context)
        setImageSearch(remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH,
                false))

        showVoiceButton(true)
        showImageSearch(true)

        initSearchView()
    }

    private fun setListener(){
        action_up_btn?.setOnClickListener(mOnClickListener)
        action_voice_btn?.setOnClickListener(mOnClickListener)
        action_empty_btn?.setOnClickListener(mOnClickListener)
        action_cancel_button?.setOnClickListener(mOnClickListener)
        action_image_search_btn?.setOnClickListener(mOnClickListener)
    }

    private fun showVoiceButton(show: Boolean) {
        if (show && isVoiceAvailable && allowVoiceSearch) {
            action_voice_btn?.visibility = View.VISIBLE
        } else {
            action_voice_btn?.visibility = View.GONE
            if (!isVoiceAvailable) {
                setMargin(searchTextView, convertDpToPx(8), 0, convertDpToPx(12), 0)
            }
        }
    }

    private fun setMargin(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        val r = mContext.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).toInt()
    }

    private fun showImageSearch(show: Boolean) {
        if (show && isAllowImageSearch) {
            action_image_search_btn?.visibility = View.VISIBLE
        } else {
            action_image_search_btn?.visibility = View.GONE
        }
    }

    private fun initSearchView() {
        searchTextView?.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            true
        }

        searchTextView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    var keyword = s.toString()

                    if (copyText) {
                        keyword = keyword.trim { it <= ' ' }
                        copyText = false
                    }
                    mUserQuery = keyword

                    searchParameter.setSearchQuery(keyword)

                    if (queryListener != null) {
                        queryListener?.onQueryChanged(keyword)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                for (span in s.getSpans(0, s.length, UnderlineSpan::class.java)) {
                    s.removeSpan(span)
                }
            }
        })

        searchTextView?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard(searchTextView)
            }
        }
    }

    private fun onSubmitQuery() {
        searchTextView?.text?.let { modifyQueryInSearchParameter(it) }

        if (TextUtils.getTrimmedLength(searchParameter.getSearchQuery()) > 0) {
            if (!mOnQueryChangeListener.onQueryTextSubmit(searchParameter)) {
                searchTextView?.text = null
            }
        }
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun setTextViewHint(hint: CharSequence?) {
        searchTextView?.hint = hint
    }

    private fun initCompositeSubscriber() {
        compositeSubscription = getNewCompositeSubIfUnsubscribed(compositeSubscription)
        compositeSubscription?.add(Observable.unsafeCreate(Observable.OnSubscribe<String> { subscriber ->
            queryListener = object : QueryListener {
                override fun onQueryChanged(query: String) {
                    subscriber.onNext(query)
                }
            }
        })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onCompleted() { }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.localizedMessage)
                    }

                    override fun onNext(s: String?) {
                        if (s != null) {
                            Log.d(TAG, "Sending the text $s")
                            this@SearchBarView.onTextChanged(s)
                        }
                    }
                }))
    }

    private fun getNewCompositeSubIfUnsubscribed(subscription: CompositeSubscription?): CompositeSubscription {
        return if (subscription == null || subscription.isUnsubscribed) {
            CompositeSubscription()
        } else subscription

    }

    private fun onTextChanged(newText: CharSequence?) {
        val text = searchTextView?.text
        mUserQuery = text
        val hasText = !TextUtils.isEmpty(text)
        if (hasText) {
            action_empty_btn?.visibility = View.VISIBLE
            action_cancel_button?.visibility = View.VISIBLE
            showVoiceButton(false)
            showImageSearch(false)
            setConstraint(search_top_bar, R.id.searchTextView, ConstraintSet.RIGHT, R.id.action_cancel_button, ConstraintSet.LEFT, 0)
        } else {
            action_empty_btn?.visibility = View.GONE
            action_cancel_button?.visibility = View.GONE
            showVoiceButton(true)
            showImageSearch(true)
            setConstraint(search_top_bar, R.id.searchTextView, ConstraintSet.RIGHT, R.id.action_voice_btn, ConstraintSet.LEFT, 0)
        }

        if (!TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(searchParameter)
        }

        mOldQueryText = newText.toString()
    }

    private fun setConstraint(layout: ConstraintLayout?, startId: Int, startSide: Int, endId: Int, endSide: Int, margin: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.connect(startId, startSide, endId, endSide, margin)
        constraintSet.applyTo(layout)
    }

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    private fun startShowCase() {
        if (shouldShowImageSearchShowCase()) {
            val showCaseTag = "Image Search ShowCase"
            if (ShowCasePreference.hasShown(mContext, showCaseTag)) {
                return
            }
            if (showCaseDialog != null) {
                return
            }
            showCaseDialog = createShowCase()
            showCaseDialog?.setShowCaseStepListener { _, _, showCaseObject -> false }

            val showCaseObjectList = ArrayList<ShowCaseObject>()
            showCaseObjectList.add(ShowCaseObject(
                    action_image_search_btn,
                    mContext.resources.getString(R.string.on_board_title),
                    remoteConfig.getString(RemoteConfigKey.IMAGE_SEARCH_ONBOARD_DESC,
                            mContext.resources.getString(R.string.on_board_desc)),
                    ShowCaseContentPosition.UNDEFINED,
                    com.tokopedia.design.R.color.tkpd_main_green))
            if (activity != null) {
                showCaseDialog?.show(activity, showCaseTag, showCaseObjectList)
            }

        }
    }

    private fun shouldShowImageSearchShowCase(): Boolean {
        return (isAllowImageSearch
                && !isShowShowCase
                && action_image_search_btn != null
                && action_image_search_btn?.visibility == View.VISIBLE)
    }

    private fun createShowCase(): ShowCaseDialog {
        return ShowCaseBuilder()
                .titleTextColorRes(com.tokopedia.design.R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.autocomplete_selector_circle_green)
                .prevStringRes(R.string.navigate_back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build()
    }

    private fun modifyQueryInSearchParameter(query: CharSequence) {
        var finalQuery: String? = query.toString()

        if (isQueryEmptyAndHintExists(query)) {
            finalQuery = hint
        }

        finalQuery?.let { searchParameter.setSearchQuery(it) }
    }

    private fun isQueryEmptyAndHintExists(query: CharSequence): Boolean {
        return TextUtils.isEmpty(query) && !TextUtils.isEmpty(hint)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun setBackground(background: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            search_top_bar?.background = background
        } else {
            search_top_bar?.background = background
        }
    }

    override fun setBackgroundColor(color: Int) {
        search_top_bar?.setBackgroundColor(color)
    }

    private fun setImageSearch(imageSearch: Boolean) {
        isAllowImageSearch = imageSearch
    }

    fun setQuery(query: CharSequence, submit: Boolean, copyText: Boolean) {
        this.copyText = copyText
        setQuery(query, submit)
    }

    fun setQuery(query: CharSequence?, submit: Boolean) {
        searchTextView?.setText(query)
        if (query != null) {
            searchTextView?.setSelection(searchTextView.length())
            mUserQuery = query
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    fun showSearch(searchParameter: SearchParameter) : SearchParameter{
        val param = this.searchParameter
        this.searchParameter = searchParameter

        setHintIfExists(searchParameter.get(SearchApiConst.HINT))
        lastQuery = searchParameter.getSearchQuery()
        showSearch()
        return param
    }

    private fun showSearch() {
        textViewRequestFocus()

        search_top_bar?.visibility = View.VISIBLE
        initShowCase()
    }

    private fun initShowCase() {
        Handler().postDelayed({ startShowCase() }, IMAGE_SEARCH_SHOW_CASE_DIALOG_DELAY)
    }

    private fun setHintIfExists(hint: String?) {
        if (!TextUtils.isEmpty(hint)) {
            setHint(searchParameter.get(SearchApiConst.HINT))
        }
    }

    private fun setHint(hint: CharSequence) {
        this.hint = hint.toString()

        setTextViewHint(hint)
    }

    private fun textViewRequestFocus() {
        searchTextView?.setText(lastQuery)
        onTextChanged(lastQuery)

        searchTextViewShowKeyboard()
    }

    private fun searchTextViewShowKeyboard() {
        searchTextView?.postDelayed({
            showKeyboard(searchTextView)
            searchTextView?.text?.length?.let { searchTextView?.setSelection(it) }
        }, 200)
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        mOnQueryChangeListener = listener
    }

    fun setOnImageSearchClickListener(imageSearchClickListener: ImageSearchClickListener) {
        mImageSearchClickListener = imageSearchClickListener
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) false else searchTextView.requestFocus(direction, previouslyFocusedRect)
    }

    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        searchTextView.clearFocus()
        mClearingFocus = false
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        mSavedState = SavedState(superState)
        mSavedState?.query = if (mUserQuery != null) mUserQuery?.toString() else null
        mSavedState?.allowImageSearch = this.isAllowImageSearch
        mSavedState?.hint = this.hint

        return mSavedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        mSavedState = state

        showSearch()
        setQuery(mSavedState?.query, false)
        setHintIfExists(mSavedState?.hint)

        mSavedState?.allowImageSearch?.let { setImageSearch(it) }

        super.onRestoreInstanceState(mSavedState?.superState)
    }

    internal class SavedState : BaseSavedState {
        var query: String? = null
        private var isSearchOpen: Boolean = false
        var allowImageSearch: Boolean = false
        var hint: String? = null

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(query)
            out.writeInt(if (isSearchOpen) 1 else 0)
            out.writeInt(if (allowImageSearch) 1 else 0)
            out.writeString(hint)
        }
    }

    private interface QueryListener {
        fun onQueryChanged(query: String)
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(searchParameter: SearchParameter): Boolean
        fun onQueryTextChange(searchParameter: SearchParameter)
    }

    interface ImageSearchClickListener {
        fun onImageSearchClicked()
    }

}