package com.tokopedia.home_wishlist.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import com.tokopedia.home_wishlist.R
import java.util.*
import java.util.concurrent.TimeUnit

class CustomSearchView : FrameLayout{
    private fun getLayout(): Int {
        return R.layout.custom_search_view
    }

    protected var view: View? = null

    interface Listener {
        fun onSearchSubmitted(text: String?)
        fun onSearchTextChanged(text: String?)
    }

    interface FocusChangeListener {
        fun onFocusChanged(hasFocus: Boolean)
    }

    interface ResetListener {
        fun onSearchReset()
    }

    var searchImageView: ImageView? = null
    var searchTextView: EditText? = null
    var closeImageButton: ImageButton? = null
    private var searchDrawable: Drawable? = null
    private var searchText: String? = null
    private var searchHint: String? = null
    private var delayTextChanged: Long = 0
    private var listener: Listener? = null
    private var reset: ResetListener? = null
    private var focusChangeListener: FocusChangeListener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setResetListener(listener: ResetListener?) {
        reset = listener
    }

    fun setFocusChangeListener(focusChangeListener: FocusChangeListener?) {
        this.focusChangeListener = focusChangeListener
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val styledAttributes: TypedArray = context.obtainStyledAttributes(attrs, com.tokopedia.design.R.styleable.SearchInputView)
        try {
            searchDrawable = styledAttributes.getDrawable(com.tokopedia.design.R.styleable.SearchInputView_siv_search_icon)
            searchText = styledAttributes.getString(com.tokopedia.design.R.styleable.SearchInputView_siv_search_text)
            searchHint = styledAttributes.getString(com.tokopedia.design.R.styleable.SearchInputView_siv_search_hint)
        } finally {
            styledAttributes.recycle()
        }
        init()
    }

    private fun init() {
        view = View.inflate(context, getLayout(), this)
        searchImageView = view?.findViewById<View>(searchImageViewResourceId) as ImageView
        searchTextView = view?.findViewById<View>(searchTextViewResourceId) as EditText
        closeImageButton = view?.findViewById<View>(closeImageButtonResourceId) as ImageButton
        delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED
        if (searchDrawable != null) {
            searchImageView?.setImageDrawable(searchDrawable)
        }
        if (!TextUtils.isEmpty(searchText)) {
            searchTextView?.setText(searchText)
        }
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView?.hint = searchHint
        }
        searchTextView?.setOnEditorActionListener(OnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && listener != null) {
                hideKeyboard()
                listener?.onSearchSubmitted(textView.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        searchTextView?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (focusChangeListener != null) {
                focusChangeListener?.onFocusChanged(hasFocus)
            }
        }
        searchTextView?.addTextChangedListener(searchTextWatcher)
        closeImageButton?.setOnClickListener {
            searchTextView?.setText("")
            hideKeyboard()
            if (reset != null) {
                reset?.onSearchReset()
            }
        }
    }

    private val searchImageViewResourceId: Int
        get() = com.tokopedia.design.R.id.image_view_search

    private val searchTextViewResourceId: Int
        get() = com.tokopedia.design.R.id.edit_text_search

    private val closeImageButtonResourceId: Int
        get() = com.tokopedia.design.R.id.image_button_close

    fun hideKeyboard() {
        searchTextView?.clearFocus()
        val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(searchTextView?.windowToken, 0)
    }

    fun setSearchText(searchText: String?) {
        this.searchText = searchText
        searchTextView?.setText(searchText)
    }

    fun setSearchHint(searchHint: String?) {
        this.searchHint = searchHint
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView?.hint = searchHint
        }
    }

    fun setDelayTextChanged(delayTextChanged: Long) {
        this.delayTextChanged = delayTextChanged
    }

    override fun setEnabled(enabled: Boolean) {
        searchTextView?.isEnabled = enabled
        closeImageButton?.isEnabled = enabled
    }

    fun getSearchText(): String {
        return searchTextView?.text?.toString() ?: ""
    }

    private val searchTextWatcher: TextWatcher
        get() = object : TextWatcher {
            private var timer: Timer? = Timer()
            override fun afterTextChanged(s: Editable) {
                runTimer(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
                if (TextUtils.isEmpty(searchTextView?.text.toString())) {
                    closeImageButton?.visibility = View.GONE
                } else {
                    closeImageButton?.visibility = View.VISIBLE
                }
            }

            private fun runTimer(text: String) {
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        updateListener(text)
                    }
                }, delayTextChanged)
            }

            private fun updateListener(text: String) {
                if (listener == null) {
                    return
                }
                val mainHandler = Handler(searchTextView?.context?.mainLooper)
                val myRunnable = Runnable { listener?.onSearchTextChanged(text) }
                mainHandler.post(myRunnable)
            }
        }

    companion object {
        private val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(0)
    }
}