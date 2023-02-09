package com.tokopedia.play.ui.view.comment

import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.databinding.ViewVodCommentBinding

/**
 * @author by astidhiyaa on 07/02/23
 */
class CommentIconUiView(private val binding: ViewVodCommentBinding, listener: Listener) {

    init {
        binding.root.setOnClickListener {
            listener.onCommentClicked(this)
        }
    }

    fun show(isShown: Boolean) {
        binding.root.showWithCondition(isShown)
    }

    fun setCounter(value: String) {
        binding.viewCommentCount.text = value
    }

    interface Listener {
        fun onCommentClicked(view: CommentIconUiView)
    }
}
