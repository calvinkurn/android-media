package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel.CampaignStatus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class BroadcastCampaignLabelView : LinearLayout {

    private var desc: Typography? = null
    private var countdown: TimerUnifySingle? = null
    private var startDateIcon: ImageView? = null
    private var startDateText: Typography? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewInflation()
        initViewBinding()
    }

    private fun initViewInflation() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        desc = findViewById(R.id.tp_broadcast_campaign_status)
        countdown = findViewById(R.id.tu_bc_countdown)
        startDateIcon = findViewById(R.id.iu_broadcast_start_date)
        startDateText = findViewById(R.id.tp_broadcast_start_date)
    }

    fun renderState(banner: ImageAnnouncementUiModel) {
        if (banner.eligibleToRenderCampaignLabel()) {
            show()
            bindDescText(banner)
            bindDescWeight(banner)
            bindDescColor(banner)
            bindLabelBackgroundColor(banner)
            bindStartDate(banner)
            bindCountDown(banner)
        } else {
            hide()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        countdown?.onFinish = null
    }

    private fun bindDescText(banner: ImageAnnouncementUiModel) {
        val description = when (banner.statusCampaign) {
            CampaignStatus.ENDED -> banner.finishedDescription
            else -> banner.campaignLabel
        }
        desc?.text = description
    }

    private fun bindDescWeight(banner: ImageAnnouncementUiModel) {
        val weightType = when (banner.statusCampaign) {
            CampaignStatus.STARTED,
            CampaignStatus.ON_GOING -> Typography.BOLD
            CampaignStatus.ENDED -> Typography.REGULAR
            else -> null
        } ?: return
        desc?.setWeight(weightType)
    }

    private fun bindDescColor(banner: ImageAnnouncementUiModel) {
        val colorRes = when (banner.statusCampaign) {
            CampaignStatus.ENDED -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN0
        }
        val color = MethodChecker.getColor(context, colorRes)
        desc?.setTextColor(color)
    }

    private fun bindLabelBackgroundColor(banner: ImageAnnouncementUiModel) {
        val colorRes = when {
            banner.hasEndedCampaign() -> com.tokopedia.unifyprinciples.R.color.Unify_TN100
            banner.isHideBanner -> R.drawable.bg_chat_broadcast_campaign_label_without_banner
            else -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
        }
        setBackgroundResource(colorRes)
    }

    private fun bindStartDate(banner: ImageAnnouncementUiModel) {
        if (banner.hasStartedCampaign()) {
            showStartDate()
            startDateText?.text = banner.startDateFormatted
        } else {
            hideStartDate()
        }
    }

    private fun bindCountDown(banner: ImageAnnouncementUiModel) {
        if (banner.hasOngoingCampaign()) {
            countdown?.show()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = banner.endDateMillis
            }
            countdown?.targetDate = calendar
            countdown?.onFinish = {
                banner.endCampaign()
                renderState(banner)
            }
        } else {
            countdown?.hide()
        }
    }

    private fun showStartDate() {
        startDateIcon?.show()
        startDateText?.show()
    }

    private fun hideStartDate() {
        startDateIcon?.hide()
        startDateText?.hide()
    }

    companion object {
        val LAYOUT = R.layout.partial_chat_broadcast_campaign_label
    }
}
