package com.tokopedia.play.ui.view.comment

import android.view.View
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R

/**
 * @author by astidhiyaa on 07/02/23
 */
class CommentIconUiView(private val group: Group, listener: Listener) {

    private val view = group.rootView
    init {
        group.post {
            group.referencedIds.forEach {
                view.findViewById<View>(it)?.setOnClickListener {
                    listener.onCommentClicked(this)
                }
            }
        }
    }

    fun show(isShown: Boolean) {
        group.showWithCondition(isShown)
    }

    fun setCounter(value: String) {
        if (value.isBlank()) return
        view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.view_vod_comment_counter)?.text = value
    }

    interface Listener {
        fun onCommentClicked(view: CommentIconUiView)
    }
}
