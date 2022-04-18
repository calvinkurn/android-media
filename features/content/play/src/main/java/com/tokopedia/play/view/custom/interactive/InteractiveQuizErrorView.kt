package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.databinding.ViewInteractiveQuizErrorBinding
import com.tokopedia.play_common.view.game.GameHeaderView

/**
 * @author by astidhiyaa on 18/04/22
 */
class InteractiveQuizErrorView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var mListener: Listener? = null

    private val binding = ViewInteractiveQuizErrorBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        setupView()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setupView() {
        binding.btnInteractiveRetry.setOnClickListener {
            mListener?.onRetryButtonClicked(this)
        }
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    interface Listener {
        fun onRetryButtonClicked(view: InteractiveQuizErrorView)
    }
}