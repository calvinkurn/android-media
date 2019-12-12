package com.tokopedia.play.ui.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 09/12/19
 */
class ToolbarView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_toolbar, container, true)
                    .findViewById(R.id.cl_toolbar)

    private val flLiveBadge = view.findViewById<FrameLayout>(R.id.fl_live_badge)

    init {
        view.findViewById<ImageView>(R.id.iv_back)
                .setOnClickListener {
                    listener.onBackButtonClicked(this)
                }

        view.findViewById<ImageView>(R.id.iv_more)
                .setOnClickListener {
                    listener.onMoreButtonClicked(this)
                }

        view.findViewById<TextView>(R.id.tv_follow)
                .setOnClickListener {
                    listener.onFollowButtonClicked(this)
                }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.show()
    }

    fun setLiveBadgeVisibility(isLive: Boolean) {
        if (isLive) flLiveBadge.visible() else flLiveBadge.gone()
    }

    interface Listener {

        fun onBackButtonClicked(view: ToolbarView)
        fun onMoreButtonClicked(view: ToolbarView)
        fun onFollowButtonClicked(view: ToolbarView)
    }
}