package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by jegul on 12/06/20
 */
class PlayStartStreamingButton : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvStreamingDurationInfo: TextView
    private val loaderStreaming: LoaderUnify
    private val groupStale: Group

    init {
        val view = View.inflate(context, R.layout.button_play_start_streaming, this)

        with (view) {
            tvStreamingDurationInfo = findViewById(R.id.tv_streaming_duration_info)
            loaderStreaming = findViewById(R.id.loader_streaming)
            groupStale = findViewById(R.id.group_stale)
        }

        setupView(view)
    }

    fun setMaxDurationDescription(desc: String) {
        tvStreamingDurationInfo.text = desc
    }

    private fun setupView(view: View) {

    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            loaderStreaming.visible()
            groupStale.gone()
        } else {
            loaderStreaming.gone()
            groupStale.visible()
        }
    }

}