package com.tokopedia.statistic.view.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.databinding.ViewStcExclusiveIdentifierBinding

/**
 * Created By @ilhamsuaib on 25/10/21
 */

class ExclusiveIdentifierView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var onCtaClick: (() -> Unit)? = null
    private var binding: ViewStcExclusiveIdentifierBinding? = null

    fun setDescription(text: String) {
        binding?.tvStcExclusiveIdentifierDesc?.text = text.parseAsHtml()
    }

    private fun init(context: Context) {
        binding = ViewStcExclusiveIdentifierBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        binding?.run {
            root.setBackgroundColor(Color.TRANSPARENT)
            imgStcExclusiveIdentifier.loadImage(Const.Image.IMG_EXCLUSIVE_IDENTIFIER)
            btnStcUpgradeShopCta.setOnClickListener {
                openPowerMerchantPage()
                onCtaClick?.invoke()
            }
        }
    }

    private fun openPowerMerchantPage() {
        RouteManager.route(context, ApplinkConst.POWER_MERCHANT_SUBSCRIBE)
        StatisticTracker.sendClickEventOnCtaExclusiveIdentifier()
    }

    fun setOnCtaClickListener(function: () -> Unit) {
        onCtaClick = function
    }
}