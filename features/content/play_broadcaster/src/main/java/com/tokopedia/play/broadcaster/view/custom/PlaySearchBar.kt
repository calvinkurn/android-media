package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 26/05/20
 */
class PlaySearchBar : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val ivSearch: ImageView
    private val etSearch: EditText
    private val ivClear: ImageView

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_play_search_bar, this)
        ivSearch = view.findViewById(R.id.iv_search)
        etSearch = view.findViewById(R.id.et_search)
        ivClear = view.findViewById(R.id.iv_clear)

        setupView(view)
    }

    override fun setEnabled(isEnabled: Boolean) {
        etSearch.isEnabled = isEnabled
        etSearch.isCursorVisible = isEnabled
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setText(text: String) {
        etSearch.setText(text)
    }

    private fun setupView(view: View) {
        etSearch.setOnFocusChangeListener { v, hasFocus ->
            mListener?.onEditStateChanged(this@PlaySearchBar, hasFocus)
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ivClear.visibility = if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        ivClear.setOnClickListener {
            etSearch.setText("")
        }
    }

    interface Listener {
        fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {}
    }
}