package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.broadcaster.R

class PlayBottomSheetHeader : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val ivBack: AppCompatImageView
    private val tvTitle: TextView

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_play_bottom_sheet_header, this)

        with (view) {
            ivBack = findViewById(R.id.iv_back)
            tvTitle = findViewById(R.id.tv_title)
        }

        setupView(view)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setHeader(title: String, isRoot: Boolean) {
        tvTitle.text = title

        ivBack.setImageResource(
                if (isRoot) com.tokopedia.unifycomponents.R.drawable.unify_bottomsheet_close
                else com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24
        )
    }

    private fun setupView(view: View) {
        ivBack.setOnClickListener { mListener?.onBackButtonClicked(this) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mListener = null
    }

    interface Listener {

        fun onBackButtonClicked(view: PlayBottomSheetHeader)
    }
}