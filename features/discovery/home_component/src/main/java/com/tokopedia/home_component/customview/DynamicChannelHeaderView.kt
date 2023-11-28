package com.tokopedia.home_component.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component.customview.header.HeaderLayoutStrategy
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import com.tokopedia.home_component_header.R as home_component_headerR

@Deprecated("Please use com.tokopedia.home_component_header.view.HomeComponentHeaderView")
class DynamicChannelHeaderView : FrameLayout {
    private var headerContainer: ConstraintLayout? = null
    private var txtTitle: Typography? = null
    private var txtSubtitle: Typography? = null
    private var timerUnify: TimerUnifySingle? = null

    private var headerColorMode: Int = COLOR_MODE_NORMAL
    private var headerCtaMode: Int = CTA_MODE_SEE_ALL
    private var listener: HeaderListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
            val attributes: TypedArray = context.obtainStyledAttributes(attrs, home_component_headerR.styleable.HomeComponentHeaderView)
        try {
            headerColorMode = attributes.getInt(home_component_headerR.styleable.HomeComponentHeaderView_color_mode, COLOR_MODE_NORMAL)
            headerCtaMode = attributes.getInt(home_component_headerR.styleable.HomeComponentHeaderView_cta_mode, CTA_MODE_SEE_ALL)
        } finally {
            attributes.recycle()
        }
    }

    @Deprecated("Please use com.tokopedia.home_component_header.view.HomeComponentHeaderView.bind()")
    fun setChannel(
        channelModel: ChannelModel,
        listener: HeaderListener,
        colorMode: Int? = null,
        ctaMode: Int? = null
    ) {
        init(channelModel.channelHeader.layoutStrategy)
        this.listener = listener
        colorMode?.let {
            this.headerColorMode = it
        }
        ctaMode?.let {
            this.headerCtaMode = it
        }
        handleHeaderComponent(channelModel)
    }

    private fun init(layoutStrategy: HeaderLayoutStrategy) {
        if(headerContainer == null) {
            inflate(context, layoutStrategy.getLayout(), this).also {
                headerContainer = findViewById(home_component_headerR.id.header_container)
                txtTitle = findViewById(home_component_headerR.id.header_title)
                txtSubtitle = findViewById(home_component_headerR.id.header_subtitle)
                timerUnify = findViewById(home_component_headerR.id.header_timer)
            }
        }
    }

    private fun handleHeaderComponent(channel: ChannelModel) {
        handleTitle(channel.channelHeader.name, channel)
        channel.channelHeader.layoutStrategy.renderIconSubtitle(this, channel.channelHeader)
        handleSubtitle(channel.channelHeader.subtitle, channel)
        channel.channelHeader.layoutStrategy.renderCta(
            this,
            channel,
            listener,
            headerCtaMode,
            headerColorMode
        )
        handleHeaderExpiredTime(channel)
        handleBackgroundColor(channel)
        channel.channelHeader.layoutStrategy.setConstraints(headerContainer, channel.channelHeader)
    }

    private fun handleTitle(title: String?, channel: ChannelModel) {
        /**
         * Requirement:
         * Only show channel header name when it is exist
         */
        if (title?.isNotEmpty() == true) {
            txtTitle?.text = title
            headerContainer?.show()
            txtTitle?.show()
            channel.channelHeader.layoutStrategy.renderTitle(
                context,
                channel.channelHeader,
                txtTitle,
                headerColorMode
            )
        } else {
            headerContainer?.hide()
        }
    }

    private fun handleSubtitle(subtitle: String?, channel: ChannelModel) {
        /**
         * Requirement:
         * Only show channel subtitle when it is exist
         */
        if (subtitle?.isNotEmpty() == true) {
            txtSubtitle?.text = subtitle
            txtSubtitle?.show()
            channel.channelHeader.layoutStrategy.renderSubtitle(
                context,
                channel.channelHeader,
                txtSubtitle,
                headerColorMode
            )
        } else {
            txtSubtitle?.hide()
        }
    }

    private fun handleHeaderExpiredTime(channel: ChannelModel) {
        /**
         * Requirement:
         * Only show countDownView when expired time exist
         * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
         *  since onCountDownFinished would getting called and refresh home
         */
        if (channel.channelHeader.expiredTime.isNotEmpty()) {
            val expiredTime = DateHelper.getExpiredTime(channel.channelHeader.expiredTime)
            if (!DateHelper.isExpired(channel.channelConfig.serverTimeOffset, expiredTime)) {
                timerUnify?.run {
                    timerVariant = if (channel.channelHeader.backColor.isNotEmpty() || headerColorMode == COLOR_MODE_INVERTED) {
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + channel.channelConfig.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(channel)
                    }
                }
            }
        } else {
            timerUnify?.let {
                it.visibility = View.GONE
            }
        }
    }

    private fun handleBackgroundColor(channel: ChannelModel) {
        if (channel.channelHeader.backColor.isNotEmpty()) {
            headerContainer?.let {
                it.setBackgroundColor(Color.parseColor(channel.channelHeader.backColor))

                it.setPadding(
                    it.paddingLeft,
                    convertDpToPixel(TITLE_TOP_PADDING, it.context),
                    it.paddingRight,
                    it.paddingBottom
                )
            }
        }
    }

    companion object {
        private const val TITLE_TOP_PADDING = 15f

        const val COLOR_MODE_NORMAL = 0
        const val COLOR_MODE_INVERTED = 1

        const val CTA_MODE_SEE_ALL = 0
        const val CTA_MODE_RELOAD = 1
        const val CTA_MODE_CLOSE = 2
    }
}
