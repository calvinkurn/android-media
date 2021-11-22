package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 11/06/20.
 */
class PlayStatInfoView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var tvTotalView: Typography
    private var tvTotalLike: Typography
    private val tvCountDown: Typography

    init {
        val view = View.inflate(context, R.layout.view_play_stats_info, this)
        tvTotalView = view.findViewById(com.tokopedia.play_common.R.id.tv_total_views)
        tvTotalLike = view.findViewById(R.id.tv_total_likes)
        tvCountDown = view.findViewById(R.id.tv_bro_count_down)

        tvTotalView.text = context.getString(R.string.play_live_broadcast_stat_info_default)
        tvTotalLike.text = context.getString(R.string.play_live_broadcast_stat_info_default)
    }

    fun setTotalView(totalView: TotalViewUiModel) {
        tvTotalView.text = totalView.totalView
    }

    fun setTotalLike(totalLike: TotalLikeUiModel) {
        tvTotalLike.text = totalLike.totalLike
    }

    fun setCountDown(timeInMillis: Long) {
        tvCountDown.text = context.getString(
            R.string.play_live_broadcast_remaining_duration_format,
            timeInMillis.millisToMinutes(),
            timeInMillis.millisToRemainingSeconds()
        )
    }
}