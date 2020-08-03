package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.play.view.uimodel.TotalViewUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class EndLiveInfoViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val txtLiveEndedTitle = findViewById<TextView>(R.id.txt_live_ended_title)
    private val txtLiveEndedBody = findViewById<TextView>(R.id.txt_live_ended_body)
    private val btnLiveEndedAction = findViewById<UnifyButton>(R.id.btn_live_ended_action)
    private val tvTotalViews = findViewById<Typography>(R.id.tv_total_views)
    private val tvTotalLikes = findViewById<Typography>(R.id.tv_total_likes)

    fun setInfo(title: String, message: String, btnTitle: String, btnUrl: String) {
        txtLiveEndedTitle.text = title
        txtLiveEndedBody.text = message
        btnLiveEndedAction.text = btnTitle
        btnLiveEndedAction.setOnClickListener {
            listener.onButtonActionClicked(this, btnUrl)
        }
    }

    fun setTotalViews(totalView: TotalViewUiModel) {
        tvTotalViews.text = totalView.totalView
    }

    fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        tvTotalLikes.text = resources.getQuantityString(R.plurals.play_likes,
                totalLikes.totalLike, totalLikes.totalLikeFormatted)
    }

    interface Listener {
        fun onButtonActionClicked(view: EndLiveInfoViewComponent, btnUrl: String)
    }
}