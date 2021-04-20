package com.tokopedia.digital.home.widget

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
import com.tokopedia.digital.home.R
import java.util.*
import java.util.concurrent.TimeUnit

@Deprecated("please use SearchBarUnify instead")
class RechargeSearchBarWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(0)

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

    private lateinit var searchImageView: ImageView
    private lateinit var searchTextView: EditText
    private lateinit var closeImageButton: ImageButton

    var searchDrawable: Drawable? = null
    private var searchText: String? = null
    private var searchHint: String? = null

    private var delayTextChanged: Long = 0
    private var listener: Listener? = null
    private var reset: ResetListener? = null
    private var focusChangeListener: FocusChangeListener? = null

    fun getSearchTextView(): EditText? {
        return searchTextView
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setResetListener(listener: ResetListener?) {
        this.reset = listener
    }

    fun setFocusChangeListener(focusChangeListener: FocusChangeListener?) {
        this.focusChangeListener = focusChangeListener
    }

    open fun init(attrs: AttributeSet) {
        val styledAttributes: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchInputViewSubhome)
        try {
            searchDrawable = styledAttributes.getDrawable(R.styleable.SearchInputViewSubhome_siv_search_icon_subhome)
            searchText = styledAttributes.getString(R.styleable.SearchInputViewSubhome_siv_search_text_subhome)
            searchHint = styledAttributes.getString(R.styleable.SearchInputViewSubhome_siv_search_hint_subhome)
        } finally {
            styledAttributes.recycle()
        }
    }

    init {
        attrs?.let { init(it) }

        val view = View.inflate(getContext(), getLayout(), this)
        searchImageView = view.findViewById<View>(getSearchImageViewResourceId()) as ImageView
        searchTextView = view.findViewById<View>(getSearchTextViewResourceId()) as EditText
        closeImageButton = view.findViewById<View>(getCloseImageButtonResourceId()) as ImageButton
        delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED
        if (searchDrawable != null) {
            searchImageView.setImageDrawable(searchDrawable)
        }
        if (!TextUtils.isEmpty(searchText)) {
            searchTextView.setText(searchText)
        }
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView.hint = searchHint
        }
        searchTextView.setOnEditorActionListener(getOnEditorActionListener())
        searchTextView.onFocusChangeListener = OnFocusChangeListener { v, hasFocus -> focusChangeListener?.onFocusChanged(hasFocus) }
        searchTextView.addTextChangedListener(getSearchTextWatcher())
        closeImageButton.setOnClickListener {
            searchTextView.setText("")
            hideKeyboard()
            reset?.onSearchReset()
        }
    }

    fun getOnEditorActionListener(): OnEditorActionListener? {
        return OnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && listener != null) {
                hideKeyboard()
                listener?.let { it.onSearchSubmitted(textView.text.toString()) }
                return@OnEditorActionListener true
            }
            false
        }
    }

    fun getSearchImageViewResourceId(): Int {
        return R.id.image_view_search
    }

    fun getSearchTextViewResourceId(): Int {
        return R.id.edit_text_search
    }

    fun getCloseImageButtonResourceId(): Int {
        return R.id.image_button_close
    }

    fun hideKeyboard() {
        searchTextView!!.clearFocus()
        val `in` = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(searchTextView!!.windowToken, 0)
    }

    fun setSearchText(searchText: String?) {
        this.searchText = searchText
        searchTextView.setText(searchText)
    }

    fun getSearchImageView(): ImageView? {
        return searchImageView
    }

    fun setSearchImageView(searchImageView: ImageView) {
        this.searchImageView = searchImageView
    }

    fun setSearchTextView(searchTextView: EditText) {
        this.searchTextView = searchTextView
    }

    fun getCloseImageButton(): ImageButton? {
        return closeImageButton
    }

    fun setCloseImageButton(closeImageButton: ImageButton) {
        this.closeImageButton = closeImageButton
    }

    fun setSearchHint(searchHint: String?) {
        this.searchHint = searchHint
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView!!.hint = searchHint
        }
    }

    fun setDelayTextChanged(delayTextChanged: Long) {
        this.delayTextChanged = delayTextChanged
    }

    override fun setEnabled(enabled: Boolean) {
        searchTextView.isEnabled = enabled
        closeImageButton.isEnabled = enabled
    }

    fun getSearchText(): String? {
        return searchTextView.text.toString()
    }

    fun getLayout(): Int {
        return R.layout.view_subhome_search_bar
    }

    fun getSearchTextWatcher(): TextWatcher? {
        return object : TextWatcher {
            private var timer: Timer? = Timer()
            override fun afterTextChanged(s: Editable) {
                runTimer(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (timer != null) {
                    timer!!.cancel()
                }
                if (TextUtils.isEmpty(searchTextView!!.text.toString())) {
                    closeImageButton!!.visibility = View.GONE
                } else {
                    closeImageButton!!.visibility = View.VISIBLE
                }
            }

            private fun runTimer(text: String) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        updateListener(text)
                    }
                }, delayTextChanged)
            }

            private fun updateListener(text: String) {
                if (listener == null) {
                    return
                }
                val mainHandler = Handler(searchTextView!!.context.mainLooper)
                val myRunnable = Runnable { listener?.let { it.onSearchTextChanged(text) } }
                mainHandler.post(myRunnable)
            }
        }
    }
}