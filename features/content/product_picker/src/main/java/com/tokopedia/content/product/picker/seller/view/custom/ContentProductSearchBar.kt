package com.tokopedia.content.product.picker.seller.view.custom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.*
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.seller.util.transition.ScaleTransition

/**
 * Created by jegul on 26/05/20
 */
internal class ContentProductSearchBar : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var text: String
        get() = etSearch.text.toString()
        set(value) {
            etSearch.setText(value)
            etSearch.setSelection(etSearch.length())
        }

    private val view: View = View.inflate(context, R.layout.view_content_product_search_bar, this)
    private val clSearch: ConstraintLayout = view.findViewById(R.id.cl_search)
    private val etSearch: EditText = view.findViewById(R.id.et_search)
    private val ivClear: ImageView = view.findViewById(R.id.iv_clear)

    private var mListener: Listener? = null

    private lateinit var mTextWatcher: TextWatcher

    init {
        setupView(view)
    }

    override fun clearFocus() {
        super.clearFocus()
        etSearch.clearFocus()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun forceSearch() {
        etSearch.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
    }

    fun cancel() {
    }

    fun forceFocus() {
        etSearch.requestFocus()
        showKeyboard()
    }

    fun setHint(hint: String) {
        etSearch.hint = hint
    }

    fun clear() {
        clearFocus()
        etSearch.removeTextChangedListener(getTextWatcher())
        etSearch.setText("")
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val newState = SavedState(superState)
        newState.clearBtnVisibility = ivClear.visibility
        return newState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        ivClear.visibility = savedState.clearBtnVisibility
    }

    @Suppress("ClickableViewAccessibility")
    private fun setupView(view: View) {
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onChangeFocusTransition()
                etSearch.addTextChangedListener(getTextWatcher())
            } else {
                hideKeyboard()
                etSearch.removeTextChangedListener(getTextWatcher())
            }

            mListener?.onEditStateChanged(this@ContentProductSearchBar, hasFocus)
        }
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else {
                false
            }
        }

        etSearch.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onSearchBarClicked(this@ContentProductSearchBar)
            }
            false
        }

        ivClear.setOnClickListener {
            clearText()
            mListener?.onCleared(this@ContentProductSearchBar)
        }
    }

    private fun clearText() {
        etSearch.setText("")
        updateClearButton()
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun doSearch() {
        mListener?.onSearchButtonClicked(this@ContentProductSearchBar, etSearch.text.toString())
        etSearch.clearFocus()
    }

    private fun onChangeFocusTransition() {
        TransitionManager.beginDelayedTransition(
            view as ViewGroup,
            TransitionSet()
                .addTransition(getSearchBoxTransition())
                .addTransition(getCancelButtonTransition())
                .addTransition(getClearButtonTransition())
                .setStartDelay(200)
        )
    }

    private fun getCancelButtonTransition(): Transition {
        return Slide(Gravity.END)
            .setDuration(300)
    }

    private fun getSearchBoxTransition(): Transition {
        return ChangeBounds()
            .addTarget(clSearch)
            .setDuration(300)
    }

    private fun getClearButtonTransition(): Transition {
        return ScaleTransition()
            .addTarget(ivClear)
            .setDuration(300)
    }

    private fun showKeyboard() {
        val imm = etSearch.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getTextWatcher(): TextWatcher {
        if (!::mTextWatcher.isInitialized) {
            mTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateClearButton()
                    mListener?.onNewKeyword(this@ContentProductSearchBar, etSearch.text.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
        }

        return mTextWatcher
    }

    private fun updateClearButton() {
        ivClear.visibility = if (etSearch.text.isNotEmpty()) View.VISIBLE else View.GONE
    }

    class SavedState : BaseSavedState {

        var cancelBtnVisibility: Int = View.GONE
        var clearBtnVisibility: Int = View.GONE

        constructor(superState: Parcelable?) : super(superState)
        constructor(source: Parcel?) : super(source) {
            cancelBtnVisibility = source?.readInt() ?: View.GONE
            clearBtnVisibility = source?.readInt() ?: View.GONE
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(cancelBtnVisibility)
            out?.writeInt(clearBtnVisibility)
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

    interface Listener {

        fun onSearchBarClicked(view: ContentProductSearchBar) {}
        fun onEditStateChanged(view: ContentProductSearchBar, isEditing: Boolean) {}
        fun onCanceled(view: ContentProductSearchBar) {}
        fun onCleared(view: ContentProductSearchBar) {}
        fun onNewKeyword(view: ContentProductSearchBar, keyword: String)
        fun onSearchButtonClicked(view: ContentProductSearchBar, keyword: String) {}
    }
}
