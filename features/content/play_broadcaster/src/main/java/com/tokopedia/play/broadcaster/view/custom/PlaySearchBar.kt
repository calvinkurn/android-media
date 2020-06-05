package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.*
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 26/05/20
 */
class PlaySearchBar : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var text: String
        get() = etSearch.text.toString()
        set(value) {
            etSearch.setText(value)
        }

    private val view: View = View.inflate(context, R.layout.view_play_search_bar, this)
    private val clSearch: ConstraintLayout
    private val ivSearch: ImageView
    private val etSearch: EditText
    private val ivClear: ImageView
    private val tvCancel: TextView

    private var mListener: Listener? = null

    init {
        clSearch = view.findViewById(R.id.cl_search)
        ivSearch = view.findViewById(R.id.iv_search)
        etSearch = view.findViewById(R.id.et_search)
        ivClear = view.findViewById(R.id.iv_clear)
        tvCancel = view.findViewById(R.id.tv_cancel)

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

    private fun setupView(view: View) {
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onChangeFocusTransition()
                tvCancel.visible()
            }
            else {
                if (etSearch.text.isEmpty()) tvCancel.performClick()
                hideKeyboard()
            }

            mListener?.onEditStateChanged(this@PlaySearchBar, hasFocus)
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ivClear.visibility = if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                mListener?.onNewKeyword(this@PlaySearchBar, etSearch.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else false
        }

        ivClear.setOnClickListener {
            clearText()
            etSearch.requestFocus()
        }

        tvCancel.setOnClickListener {
            onChangeFocusTransition()

            clearText()
            tvCancel.gone()
            etSearch.clearFocus()
            mListener?.onCanceled(this@PlaySearchBar)
        }
    }

    private fun clearText() = etSearch.setText("")

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun doSearch() {
        mListener?.onSearchButtonClicked(this@PlaySearchBar, etSearch.text.toString())
        etSearch.clearFocus()
    }

    private fun onChangeFocusTransition() {
        TransitionManager.beginDelayedTransition(
                view as ViewGroup,
                TransitionSet()
                        .addTransition(getSearchBoxTransition())
                        .addTransition(getCancelButtonTransition())
                        .setStartDelay(200)
        )
    }

    private fun getCancelButtonTransition(): Transition {
        return Slide(Gravity.END)
                .addTarget(tvCancel)
                .setDuration(300)
    }

    private fun getSearchBoxTransition(): Transition {
        return ChangeBounds()
                .addTarget(clSearch)
                .setDuration(300)
    }

    interface Listener {

        fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {}
        fun onCanceled(view: PlaySearchBar) {}
        fun onNewKeyword(view: PlaySearchBar, keyword: String)
        fun onSearchButtonClicked(view: PlaySearchBar, keyword: String)
    }
}