package com.tokopedia.play.ui.endliveinfo

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.stats.StatsView
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 14/01/20
 */
class EndLiveInfoView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_end_live_info, container, true)
                    .findViewById(R.id.cl_play_live_ended)

    private val clPlayLiveEnded = view as ConstraintLayout
    private val txtLiveEndedTitle = view.findViewById<TextView>(R.id.txt_live_ended_title)
    private val txtLiveEndedBody = view.findViewById<TextView>(R.id.txt_live_ended_body)
    private val btnLiveEndedAction = view.findViewById<UnifyButton>(R.id.btn_live_ended_action)

    internal val statsView = StatsView(clPlayLiveEnded)

    private val resources: Resources
        get() = view.resources

    init {
        layoutStats(statsView.containerId)
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setInfo(title: String, message: String, btnTitle: String, btnUrl: String) {
        txtLiveEndedTitle.text = title
        txtLiveEndedBody.text = message
        btnLiveEndedAction.text = btnTitle
        btnLiveEndedAction.setOnClickListener {
            listener.onButtonActionClicked(this, btnUrl)
        }
    }

    private fun layoutStats(@IdRes statsComponentId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(clPlayLiveEnded)

        constraintSet.apply {
            connect(statsComponentId, ConstraintSet.TOP, txtLiveEndedBody.id, ConstraintSet.BOTTOM, resources.getDimensionPixelOffset(R.dimen.dp_16))
            connect(statsComponentId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(statsComponentId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(statsComponentId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

            connect(txtLiveEndedBody.id, ConstraintSet.BOTTOM, statsComponentId, ConstraintSet.BOTTOM)
        }
        constraintSet.applyTo(clPlayLiveEnded)
    }

    interface Listener {
        fun onButtonActionClicked(view: EndLiveInfoView, btnUrl: String)
    }
}