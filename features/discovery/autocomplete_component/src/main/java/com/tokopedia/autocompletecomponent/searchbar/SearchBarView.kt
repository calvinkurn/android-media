package com.tokopedia.autocompletecomponent.searchbar

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
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
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.AutocompleteSearchBarViewBinding
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchBarView constructor(
    private val mContext: Context,
    attrs: AttributeSet,
) : ConstraintLayout(mContext, attrs) {

    companion object {
        const val REQUEST_VOICE = 9999
        private val TAG = SearchBarView::class.java.simpleName
        private const val LOCALE_INDONESIA = "in_ID"
        const val SEARCH_BAR_DELAY_MS: Long = 200
        private const val AUTO_COMPLETE_ERROR = "AUTO_COMPLETE_ERROR"
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
    private var placeholder: String? = null
    private var isTyping = false
    private var binding: AutocompleteSearchBarViewBinding? = null

    private var isMpsEnabled: Boolean = false
    private var isMpsAnimationEnabled: Boolean = false

    private var viewListener: SearchBarViewListener? = null

    val addButton : ImageUnify?
        get() = binding?.autocompleteAddButton

    private val searchNavigationOnClickListener = OnClickListener { v ->
        val binding = binding ?: return@OnClickListener
        when {
            v === binding.autocompleteActionUpButton -> {
                viewListener?.onUpButtonClicked(binding.searchTextView)
            }
            v === binding.autocompleteVoiceButton -> {
                onVoiceClicked()
            }
            v === binding.autocompleteClearButton -> {
                binding.searchTextView.text?.clear()
            }
            v === binding.autocompleteAddButton -> {
                viewListener?.onAddButtonClicked(binding.searchTextView.text)
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

        initiateView(attrs)

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

    private fun initiateView(attrs: AttributeSet) {
        initAttribute(attrs)
        val view = LayoutInflater.from(mContext).inflate(R.layout.autocomplete_search_bar_view, this, true)
        binding = AutocompleteSearchBarViewBinding.bind(view)

        configureSearchNavigationLayout()
        setSearchNavigationListener()

        allowVoiceSearch = true

        remoteConfig = FirebaseRemoteConfigImpl(context)

        showVoiceButton(true)

        initSearchView()
    }

    private fun initAttribute(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SearchBarView,
            0, 0
        ).apply {
            try {
                isMpsEnabled = getBoolean(R.styleable.SearchBarView_enable_mps, false)
                isMpsAnimationEnabled = getBoolean(R.styleable.SearchBarView_enable_mps_animation, false)
            } finally {
                recycle()
            }
        }
    }

    private fun configureSearchNavigationLayout() {
        configureSearchNavigationView()
    }

    private fun configureSearchNavigationView() {
        configureSearchNavigationButtonVisibility()
        configureSearchNavigationSearchTextView()
    }

    private fun configureSearchNavigationButtonVisibility() {
        val binding = binding ?: return
        binding.autocompleteActionUpButton.visibility = View.VISIBLE
        binding.autocompleteVoiceButton.visibility = View.VISIBLE
        binding.autocompleteClearButton.visibility = View.VISIBLE
        binding.autocompleteSearchIcon.visibility = View.VISIBLE
        showAddButton()
    }

    private fun configureSearchNavigationSearchTextView() {
        val searchTextView = binding?.searchTextView ?: return
        searchTextView.setHintTextColor(ContextCompat.getColor(mContext, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        searchTextView.setPadding(
            28.dpToPx(mContext.resources.displayMetrics),
            12.dpToPx(mContext.resources.displayMetrics),
            32.dpToPx(mContext.resources.displayMetrics),
            12.dpToPx(mContext.resources.displayMetrics),
        )
    }

    private fun setSearchNavigationListener() {
        val binding = binding ?: return
        binding.autocompleteActionUpButton.setOnClickListener(searchNavigationOnClickListener)
        binding.autocompleteVoiceButton.setOnClickListener(searchNavigationOnClickListener)
        binding.autocompleteClearButton.setOnClickListener(searchNavigationOnClickListener)
        binding.autocompleteAddButton.setOnClickListener(searchNavigationOnClickListener)
    }

    private fun showVoiceButton(show: Boolean) {
        val binding = binding ?: return
        if (show && isVoiceAvailable && allowVoiceSearch) {
            binding.autocompleteVoiceButton.visibility = View.VISIBLE
        } else {
            binding.autocompleteVoiceButton.visibility = View.GONE

            if (!isVoiceAvailable) {
                @Suppress("MagicNumber")
                setMargin(binding.searchTextView, convertDpToPx(8), 0, convertDpToPx(12), 0)
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
        val searchTextView = binding?.searchTextView ?: return
        searchTextView.typeface = Typography.getFontType(context, false, Typography.DISPLAY_2)
        searchTextView.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            true
        }

        searchTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    var keyword = s.toString()

                    if (copyText) {
                        keyword = keyword.trim { it <= ' ' }
                        copyText = false
                    }
                    mUserQuery = keyword

                    mOnQueryChangeListener.onQueryTextChanged(keyword)

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

        searchTextView.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard(searchTextView)
            }
        }
    }

    private fun onSubmitQuery() {
        val searchTextView = binding?.searchTextView ?: return
        searchTextView.text?.let { modifyQueryInSearchParameter(it) }

        if (!mOnQueryChangeListener.onQueryTextSubmit()) {
            searchTextView.text = null
        }
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun setTextViewHint(hint: CharSequence?) {
        binding?.searchTextView?.hint = hint
    }

    private fun initCompositeSubscriber() {
        compositeSubscription = getNewCompositeSubIfUnsubscribed(compositeSubscription)
        compositeSubscription?.add(
            Observable.unsafeCreate(
                Observable.OnSubscribe<String> { subscriber ->
                    queryListener = object : QueryListener {
                        override fun onQueryChanged(query: String) {
                            subscriber.onNext(query)
                        }
                    }
                }
            )
            .debounce(SEARCH_BAR_DELAY_MS, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<String>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

                override fun onNext(s: String?) {
                    if (s != null) {
                        onTextChanged(s)
                    }
                }
            })
        )
    }

    private fun getNewCompositeSubIfUnsubscribed(subscription: CompositeSubscription?): CompositeSubscription {
        return if (subscription == null || subscription.isUnsubscribed) {
            CompositeSubscription()
        } else subscription

    }

    private fun onTextChanged(newText: CharSequence?) {
        val binding = binding ?: return
        val text = binding.searchTextView.text
        mUserQuery = text
        val hasText = !TextUtils.isEmpty(text)
        if (hasText) {
            binding.autocompleteClearButton.visible()
            showVoiceButton(false)
        } else {
            binding.autocompleteClearButton.hide()
            showVoiceButton(true)
        }
    }

    private fun ImageUnify.startAnimationDrawable() {
        if(!isMpsAnimationEnabled) return
        when(val drawable = drawable) {
            is AnimatedVectorDrawableCompat -> drawable.startLoopAnimation()
            is AnimatedVectorDrawable -> drawable.startLoopAnimation()
        }
    }

    private fun ImageUnify.stopAnimationDrawable() {
        when(val drawable = drawable) {
            is AnimatedVectorDrawableCompat -> drawable.stopLoopAnimation()
            is AnimatedVectorDrawable -> drawable.stopLoopAnimation()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private val animatableAnimationCallback = object: Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            if(drawable is AnimatedVectorDrawable) {
                drawable.start()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun AnimatedVectorDrawable.startLoopAnimation() {
        registerAnimationCallback(animatableAnimationCallback)
        start()
    }
    @TargetApi(Build.VERSION_CODES.M)
    private fun AnimatedVectorDrawable.stopLoopAnimation() {
        unregisterAnimationCallback(animatableAnimationCallback)
        stop()
    }


    private val animatableCompatAnimationCallback = object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            if (drawable is AnimatedVectorDrawableCompat) {
                drawable.start()
            }
        }
    }

    private fun AnimatedVectorDrawableCompat.startLoopAnimation() {
        registerAnimationCallback(animatableCompatAnimationCallback)
        start()
    }
    private fun AnimatedVectorDrawableCompat.stopLoopAnimation() {
        unregisterAnimationCallback(animatableCompatAnimationCallback)
        stop()
    }

    fun setMPSEnabled(isMPSEnabled: Boolean) {
        this.isMpsEnabled = isMPSEnabled
    }

    fun showAddButton() {
        if (isMpsEnabled) showAddButtonGroup()
    }

    private fun showAddButtonGroup() {
        binding?.autocompleteAddButtonGroup?.visibility = View.VISIBLE
    }

    fun hideAddButton() {
        binding?.autocompleteAddButtonGroup?.visibility = View.GONE
    }

    fun setActiveKeyword(searchBarKeyword: SearchBarKeyword) {
        mOldQueryText = searchBarKeyword.keyword
        binding?.searchTextView?.apply {
            setText(searchBarKeyword.keyword)
            setSelection(searchBarKeyword.keyword.length)
        }
    }

    fun setMPSAnimationEnabled(isMPSAnimationEnabled: Boolean) {
        this.isMpsAnimationEnabled = isMPSAnimationEnabled
    }

    fun startMpsAnimation() {
        val binding = binding ?: return
        if(isMpsEnabled) {
            binding.autocompleteAddButton.startAnimationDrawable()
        }
    }

    fun stopMpsAnimation() {
        val binding = binding ?: return
        binding.autocompleteAddButton.stopAnimationDrawable()
    }

    fun setViewListener(listener: SearchBarViewListener?) {
        this.viewListener = listener
    }

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    private fun modifyQueryInSearchParameter(query: CharSequence) {
        searchParameter.setSearchQuery(query.toString().trim())
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun setBackground(background: Drawable?) {
        binding?.searchTopBar?.background = background
    }

    override fun setBackgroundColor(color: Int) {
        binding?.searchTopBar?.setBackgroundColor(color)
    }

    fun setQuery(query: CharSequence, submit: Boolean, copyText: Boolean) {
        this.copyText = copyText
        setQuery(query, submit)
    }

    fun setQuery(query: CharSequence?, submit: Boolean) {
        val searchTextView = binding?.searchTextView ?: return
        searchTextView.setText(query)
        if (query != null) {
            searchTextView.setSelection(searchTextView.length())
            mUserQuery = query
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    fun showSearch(searchParameter: SearchParameter): SearchParameter {
        val param = this.searchParameter
        this.searchParameter = searchParameter

        val hint = searchParameter.get(SearchApiConst.HINT)
        val placeholder = searchParameter.get(SearchApiConst.PLACEHOLDER)

        setHintIfExists(hint, placeholder)
        lastQuery = searchParameter.getSearchQuery()
        showSearch()
        return param
    }

    private fun showSearch() {
        textViewRequestFocus()

        binding?.searchTopBar?.visibility = View.VISIBLE
    }

    private fun setHintIfExists(hint: String?, placeholder: String?) {
        if (!TextUtils.isEmpty(hint)) {
            setHint(searchParameter.get(SearchApiConst.HINT))
        } else if (!TextUtils.isEmpty(placeholder)) {
            setPlaceholder(searchParameter.get(SearchApiConst.PLACEHOLDER))
        }
    }

    private fun setHint(hint: CharSequence) {
        this.hint = hint.toString()

        setTextViewHint(hint)
    }

    private fun setPlaceholder(placeholder: CharSequence) {
        this.placeholder = placeholder.toString()

        setTextViewHint(placeholder)
    }

    private fun textViewRequestFocus() {
        try {
            val lastQuery = lastQuery ?: return
            binding?.searchTextView?.setText(lastQuery)
            onTextChanged(lastQuery)

            searchTextViewShowKeyboard()
        } catch (throwable: Throwable) {
            ServerLogger.log(
                Priority.P2,
                AUTO_COMPLETE_ERROR,
                mapOf("type" to throwable.stackTraceToString()),
            )
        }
    }

    private fun searchTextViewShowKeyboard() {
        val searchTextView = binding?.searchTextView ?: return
        searchTextView.postDelayed(
            {
                showKeyboard(searchTextView)
                searchTextView.text?.length?.let { searchTextView.setSelection(it) }
            },
            SEARCH_BAR_DELAY_MS
        )
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        mOnQueryChangeListener = listener
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) false else binding?.searchTextView?.requestFocus(direction, previouslyFocusedRect) == true
    }

    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        binding?.searchTextView?.clearFocus()
        mClearingFocus = false
    }

    fun preventKeyboardDismiss() {
        val binding = binding ?: return
        binding.searchTextView.setPreventDismissKeyboard(true)
    }

    fun allowKeyboardDismiss() {
        val binding = binding ?: return
        binding.searchTextView.setPreventDismissKeyboard(false)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        mSavedState = SavedState(superState)
        mSavedState?.query = if (mUserQuery != null) mUserQuery?.toString() else null
        mSavedState?.hint = this.hint
        mSavedState?.placeholder = this.placeholder

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
        setHintIfExists(mSavedState?.hint, mSavedState?.placeholder)

        super.onRestoreInstanceState(mSavedState?.superState)
    }

    internal class SavedState : BaseSavedState {
        var query: String? = null
        private var isSearchOpen: Boolean = false
        var hint: String? = null
        var placeholder: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            query = parcel.readString()
            isSearchOpen = parcel.readInt() == 1
            hint = parcel.readString()
            placeholder = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(query)
            out.writeInt(if (isSearchOpen) 1 else 0)
            out.writeString(hint)
            out.writeString(placeholder)
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
        fun onQueryTextSubmit(): Boolean
        fun onQueryTextChanged(query: String)
        fun setIsTyping(isTyping: Boolean)
    }

    interface SearchBarViewListener {
        fun onUpButtonClicked(view: View)

        fun onAddButtonClicked(editable: Editable?)
    }
}
