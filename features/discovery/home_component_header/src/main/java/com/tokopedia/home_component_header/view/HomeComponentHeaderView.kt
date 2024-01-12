package com.tokopedia.home_component_header.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component_header.R as home_component_headerR
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.util.DateHelper
import com.tokopedia.home_component_header.util.ViewUtils.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class HomeComponentHeaderView : FrameLayout {
    private var headerContainer: ConstraintLayout? = null
    private var txtTitle: Typography? = null
    private var txtSubtitle: Typography? = null
    private var timerUnify: TimerUnifySingle? = null

    private var headerColorMode: Int = COLOR_MODE_NORMAL
    private var headerCtaMode: Int = CTA_MODE_SEE_ALL
    private var listener: HomeComponentHeaderListener? = null

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

    fun bind(
        channelHeader: ChannelHeader,
        listener: HomeComponentHeaderListener? = null,
        colorMode: Int? = null,
        ctaMode: Int? = null,
        maxLines: Int = ONE_MAX_LINE,
    ) {
        init(channelHeader.layoutStrategy)
        this.listener = listener
        colorMode?.let {
            this.headerColorMode = it
        }
        ctaMode?.let {
            this.headerCtaMode = it
        }
        handleHeaderComponent(channelHeader)
        applyRuleHeaderTitleLine(maxLines)
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

    private fun handleHeaderComponent(channelHeader: ChannelHeader) {
        handleTitle(channelHeader.name, channelHeader)
        channelHeader.layoutStrategy.renderIconSubtitle(this, channelHeader)
        handleSubtitle(channelHeader.subtitle, channelHeader)
        channelHeader.layoutStrategy.renderCta(
            this,
            channelHeader,
            listener,
            headerCtaMode,
            headerColorMode
        )
        handleHeaderExpiredTime(channelHeader)
        handleBackgroundColor(channelHeader)
        channelHeader.layoutStrategy.setConstraints(headerContainer, channelHeader)
    }

    private fun handleTitle(title: String?, channelHeader: ChannelHeader) {
        /**
         * Requirement:
         * Only show title when it is exist
         */
        if (title?.isNotEmpty() == true) {
            txtTitle?.text = title
            headerContainer?.show()
            txtTitle?.show()
            channelHeader.layoutStrategy.renderTitle(
                context,
                channelHeader,
                txtTitle,
                headerColorMode
            )
        } else {
            headerContainer?.hide()
        }
    }

    private fun applyRuleHeaderTitleLine(maxLines: Int) {
        when (maxLines) {
            TWO_MAX_LINE -> {
                txtTitle?.applyMaxLineHeaderTitle(maxLines, TWO_LINE_MAX_EMS, null)
            }
            else -> txtTitle?.applyMaxLineHeaderTitle(maxLines, ONE_LINE_MAX_EMS, TextUtils.TruncateAt.END)
        }
    }

    private fun Typography?.applyMaxLineHeaderTitle(maxLine: Int, maxEms: Int, ellipsize: TextUtils.TruncateAt?) {
        this?.maxLines = maxLine
        this?.maxEms = maxEms
        this?.ellipsize = ellipsize
    }

    private fun handleSubtitle(subtitle: String?, channelHeader: ChannelHeader) {
        /**
         * Requirement:
         * Only show subtitle when it is exist
         */
        if (subtitle?.isNotEmpty() == true) {
            txtSubtitle?.text = subtitle
            txtSubtitle?.show()
            channelHeader.layoutStrategy.renderSubtitle(
                context,
                channelHeader,
                txtSubtitle,
                headerColorMode
            )
        } else {
            txtSubtitle?.hide()
        }
    }

    private fun handleHeaderExpiredTime(channelHeader: ChannelHeader) {
        /**
         * Requirement:
         * Only show timer when expired time exist
         * Don't start timer when it is expired from backend
         */
        if (channelHeader.expiredTime.isNotEmpty()) {
            val expiredTime = DateHelper.getExpiredTime(channelHeader.expiredTime)
            if (!DateHelper.isExpired(channelHeader.serverTimeOffset, expiredTime)) {
                timerUnify?.run {
                    timerVariant = if (channelHeader.backColor.isNotEmpty() || headerColorMode == COLOR_MODE_INVERTED) {
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + channelHeader.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(channelHeader.channelId)
                    }
                }
            }
        } else {
            timerUnify?.let {
                it.visibility = View.GONE
            }
        }
    }

    private fun handleBackgroundColor(channelHeader: ChannelHeader) {
        if (channelHeader.backColor.isNotEmpty()) {
            headerContainer?.let {
                it.setBackgroundColor(Color.parseColor(channelHeader.backColor))

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

        private const val ONE_LINE_MAX_EMS = 12
        private const val ONE_MAX_LINE = 1
        private const val TWO_LINE_MAX_EMS = 32
        private const val TWO_MAX_LINE = 2
    }
}
