package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 14/07/21
 */
class InteractiveErrorView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val btnInteractiveRetry: UnifyButton

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_interactive_error, this)

        btnInteractiveRetry = view.findViewById(R.id.btn_interactive_retry)

        setupView(view)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupView(view: View) {
        btnInteractiveRetry.setOnClickListener {
            mListener?.onRetryButtonClicked(this)
        }
    }

    interface Listener {

        fun onRetryButtonClicked(view: InteractiveErrorView)
    }
}