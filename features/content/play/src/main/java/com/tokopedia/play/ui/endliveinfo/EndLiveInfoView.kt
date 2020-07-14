package com.tokopedia.play.ui.endliveinfo

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.play.view.uimodel.TotalViewUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

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

    private val txtLiveEndedTitle = view.findViewById<TextView>(R.id.txt_live_ended_title)
    private val txtLiveEndedBody = view.findViewById<TextView>(R.id.txt_live_ended_body)
    private val btnLiveEndedAction = view.findViewById<UnifyButton>(R.id.btn_live_ended_action)
    private val tvTotalViews = view.findViewById<Typography>(R.id.tv_total_views)
    private val tvTotalLikes = view.findViewById<Typography>(R.id.tv_total_likes)

    private val resources: Resources
        get() = view.resources

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

    internal fun setTotalViews(totalView: TotalViewUiModel) {
        tvTotalViews.text = totalView.totalView
    }

    internal fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        tvTotalLikes.text = view.context.resources.getQuantityString(R.plurals.play_likes,
                totalLikes.totalLike, totalLikes.totalLikeFormatted)
    }

    interface Listener {
        fun onButtonActionClicked(view: EndLiveInfoView, btnUrl: String)
    }
}