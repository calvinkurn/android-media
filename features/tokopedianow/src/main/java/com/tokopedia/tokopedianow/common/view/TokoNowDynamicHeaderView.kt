package com.tokopedia.tokopedianow.common.view

import android.view.LayoutInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.Calendar
import java.util.Date

class TokoNowDynamicHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var listener: TokoNowDynamicHeaderListener? = null
    private var itemView: View?
    private var headerContainer: ConstraintLayout? = null
    private var tpSeeAll: Typography? = null
    private var tpTitle: Typography? = null
    private var tpSubtitle: Typography? = null
    private var tusCountDown: TimerUnifySingle? = null
    private var sivCircleSeeAll: ShapeableImageView? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_tokopedianow_dynamic_header_custom_view, this)
        this.itemView = view
    }

    private fun handleHeaderComponent(
        model: TokoNowDynamicHeaderUiModel
    ) {
        setupUi()
        handleTitle(model.title)
        handleSubtitle(model.subTitle, model.expiredTime)
        handleSeeAllAppLink(model.title, model.ctaText, model.ctaTextLink, model.circleSeeAll)
        handleHeaderExpiredTime(model.expiredTime, model.serverTimeOffset, model.backColor)
    }

    private fun setupUi() {
        headerContainer = itemView?.findViewById(R.id.header_container)
        tpTitle = itemView?.findViewById(R.id.tp_title)
        tpSubtitle = itemView?.findViewById(R.id.tp_subtitle)
        tpSeeAll =  itemView?.findViewById(R.id.tp_see_all)
        tusCountDown = itemView?.findViewById(R.id.tus_count_down)
        sivCircleSeeAll = itemView?.findViewById(R.id.siv_circle_see_all)
    }

    private fun handleTitle(
        title: String
    ) {
        if (title.isNotBlank()) {
            headerContainer?.show()
            tpTitle?.text = title
            tpTitle?.show()
        } else {
            headerContainer?.gone()
        }
    }

    private fun handleSubtitle(subtitle: String, expiredTime: String) {
        tpSubtitle?.showIfWithBlock(subtitle.isNotBlank() || expiredTime.isNotBlank()) {
            if (subtitle.isNotBlank()) {
                tpSubtitle?.text = subtitle
            } else {
                tpSubtitle?.text = context.getString(R.string.tokopedianow_dynamic_header_subtitle_default_value)
            }
        }
    }

    private fun handleSeeAllAppLink(title: String, ctaText: String, ctaTextLink: String, circleSeeAll: Boolean) {
        if (ctaTextLink.isNotBlank()) {
            if (circleSeeAll) {
                sivCircleSeeAll?.apply {
                    shapeAppearanceModel = shapeAppearanceModel
                        .toBuilder()
                        .setAllCornerSizes(10f)
                        .build()
                    show()
                }

                tpSeeAll?.hide()
            } else {
                tpSeeAll?.text = if (ctaText.isNotBlank()) {
                    ctaText
                } else {
                    itemView?.context?.getString(R.string.tokopedianow_mix_left_carousel_widget_see_all)
                }
                tpSeeAll?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                tpSeeAll?.setOnClickListener {
                    listener?.onSeeAllClicked(
                        headerName = title,
                        appLink =  ctaTextLink
                    )
                }
                tpSeeAll?.show()
            }
        } else {
            tpSeeAll?.hide()
        }
    }

    private fun handleHeaderExpiredTime(expiredTime: String, serverTimeOffset: Long, backColor: String) {
        /**
         * Requirement:
         * Only show countDownView when expired time exist
         * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
         *  since onCountDownFinished would getting called and refresh home
         */
        if (expiredTime.isNotBlank()) {
            val expiredTimeFormat = DateHelper.getExpiredTime(expiredTime)
            if (!DateHelper.isExpired(serverTimeOffset, expiredTimeFormat)) {
                tusCountDown?.run {
                    timerVariant = if(backColor.isNotEmpty()){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + serverTimeOffset
                        val timeDiff = expiredTimeFormat.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired()
                    }
                }
            }
        } else {
            tusCountDown?.visibility = View.GONE
        }
    }

    fun setModel(
        model: TokoNowDynamicHeaderUiModel
    ) {
        handleHeaderComponent(model)
    }

    fun setListener(
        headerListener: TokoNowDynamicHeaderListener? = null
    ) {
        listener = headerListener
    }

    interface TokoNowDynamicHeaderListener {
        fun onSeeAllClicked(headerName: String, appLink: String)
        fun onChannelExpired()
    }
}
