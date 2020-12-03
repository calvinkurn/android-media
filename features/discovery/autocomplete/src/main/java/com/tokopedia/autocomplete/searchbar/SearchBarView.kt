package com.tokopedia.autocomplete.searchbar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.autocomplete.R
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import kotlinx.android.synthetic.main.autocomplete_search_bar_view.view.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class SearchBarView constructor(private val mContext: Context, attrs: AttributeSet) : ConstraintLayout(mContext, attrs) {

    companion object {
        const val REQUEST_VOICE = 9999
        private val TAG = SearchBarView::class.java.simpleName
        private const val LOCALE_INDONESIA = "in_ID"
    }

    private var mClearingFocus: Boolean = false

    private var mOldQueryText: CharSequence? = null
    private var mUserQuery: CharSequence? = null

    private lateinit var mOnQueryChangeListener: OnQueryTextListener
    private var activity: AppCompatActivity? = null
    private var mSavedState: SavedState? = null
    private var searchParameter = SearchParameter()

    private var allowVoiceSearch: Boolean = false
    private var copyText = false
    private var compositeSubscription: CompositeSubscription? = null
    private var queryListener: QueryListener? = null
    private lateinit var remoteConfig: RemoteConfig
    private var lastQuery: String? = null
    private var hint: String? = null
    private var isTyping = false
    private val isABTestNavigationRevamp = isABTestNavigationRevamp()

    private fun isABTestNavigationRevamp(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD) == AbTestPlatform.NAVIGATION_VARIANT_REVAMP
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private val mOnClickListener = OnClickListener { v ->
        if (v === actionUpBtn || v === actionCancelButton) {
            KeyboardHandler.DropKeyboard(activity, searchTextView)
            activity?.finish()
        } else if (v === actionVoiceButton) {
            onVoiceClicked()
        } else if (v === actionEmptyButton) {
            searchTextView?.text = null
        }
    }

    private val searchNavigationOnClickListener = OnClickListener { v ->
        when {
            v === autocompleteActionUpButton -> {
                KeyboardHandler.DropKeyboard(activity, searchTextView)
                activity?.finish()
            }
            v === autocompleteVoiceButton -> {
                onVoiceClicked()
            }
            v === autocompleteClearButton -> {
                searchTextView?.text?.clear()
            }
        }
    }

    private val isVoiceAvailable: Boolean
        get() {
            val pm = context.packageManager
            val activities = pm.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
            return activities.size != 0
        }

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

    private fun initiateView() {
        LayoutInflater.from(mContext).inflate(R.layout.autocomplete_search_bar_view, this, true)

        if (isABTestNavigationRevamp) {
            configureSearchNavigationLayout()
            setSearchNavigationListener()
        } else {
            setListener()
        }

        allowVoiceSearch = true

        remoteConfig = FirebaseRemoteConfigImpl(context)

        showVoiceButton(true)

        initSearchView()
    }

    private fun configureSearchNavigationLayout() {
        configureOldNavButton()
        configureSearchNavigationView()
    }

    private fun configureOldNavButton() {
        actionUpBtn?.visibility = View.GONE
        autocompleteIconSearch?.visibility = View.GONE
        actionEmptyButton?.visibility = View.GONE
        actionVoiceButton?.visibility = View.GONE
    }

    private fun configureSearchNavigationView() {
        configureSearchNavigationButtonVisibility()
        configureSearchNavigationSearchTextView()
    }

    private fun configureSearchNavigationButtonVisibility() {
        autocompleteActionUpButton?.visibility = View.VISIBLE
        autocompleteVoiceButton?.visibility = View.VISIBLE
        autocompleteClearButton?.visibility = View.VISIBLE
        autocompleteSearchIcon?.visibility = View.VISIBLE
    }

    private fun configureSearchNavigationSearchTextView() {
        searchTextView?.setHintTextColor(ContextCompat.getColor(mContext, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        searchTextView?.setPadding(
                28.dpToPx(mContext.resources.displayMetrics),
                12.dpToPx(mContext.resources.displayMetrics),
                32.dpToPx(mContext.resources.displayMetrics),
                12.dpToPx(mContext.resources.displayMetrics)
        )
    }

    private fun setSearchNavigationListener(){
        autocompleteActionUpButton?.setOnClickListener(searchNavigationOnClickListener)
        autocompleteVoiceButton?.setOnClickListener(searchNavigationOnClickListener)
        autocompleteClearButton?.setOnClickListener(searchNavigationOnClickListener)
    }

    private fun setListener(){
        actionUpBtn?.setOnClickListener(mOnClickListener)
        actionVoiceButton?.setOnClickListener(mOnClickListener)
        actionEmptyButton?.setOnClickListener(mOnClickListener)
        actionCancelButton?.setOnClickListener(mOnClickListener)
    }

    private fun showVoiceButton(show: Boolean) {
        if (show && isVoiceAvailable && allowVoiceSearch) {
            if (isABTestNavigationRevamp) autocompleteVoiceButton?.visibility = View.VISIBLE
            else actionVoiceButton?.visibility = View.VISIBLE
        } else {
            if (isABTestNavigationRevamp) autocompleteVoiceButton?.visibility = View.GONE
            else actionVoiceButton?.visibility = View.GONE

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

                    if (!lastQuery.equals(keyword) && !isTyping) {
                        isTyping = true
                        mOnQueryChangeListener.setIsTyping(isTyping)
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
            if (isABTestNavigationRevamp) autocompleteClearButton?.visibility = View.VISIBLE
            else {
                actionEmptyButton?.visibility = View.VISIBLE
                actionCancelButton?.visibility = View.VISIBLE
            }

            showVoiceButton(false)
        } else {
            if (isABTestNavigationRevamp) autocompleteClearButton?.visibility = View.GONE
            else {
                actionEmptyButton?.visibility = View.GONE
                actionCancelButton?.visibility = View.GONE
            }

            showVoiceButton(true)
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
            searchTopBar?.background = background
        } else {
            searchTopBar?.background = background
        }
    }

    override fun setBackgroundColor(color: Int) {
        searchTopBar?.setBackgroundColor(color)
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

        searchTopBar?.visibility = View.VISIBLE
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

        super.onRestoreInstanceState(mSavedState?.superState)
    }

    internal class SavedState : BaseSavedState {
        var query: String? = null
        private var isSearchOpen: Boolean = false
        var hint: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel): super(parcel) {
            query = parcel.readString()
            isSearchOpen = parcel.readInt() == 1
            hint = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(query)
            out.writeInt(if (isSearchOpen) 1 else 0)
            out.writeString(hint)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    private interface QueryListener {
        fun onQueryChanged(query: String)
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(searchParameter: SearchParameter): Boolean
        fun onQueryTextChange(searchParameter: SearchParameter)
        fun setIsTyping(isTyping: Boolean)
    }
}