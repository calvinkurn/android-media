package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.CustomViewExclusiveLaunchMoreVoucherBinding
import com.tokopedia.unifyprinciples.Typography

class ExclusiveLaunchMoreVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private val ROOT_BACKGROUND_DARK = R.drawable.bg_exclusive_launch_more_voucher_dark
        private val ROOT_PATTERN_DARK = R.drawable.bg_exclusive_launch_more_voucher_pattern_dark
        private val TEXT_COLOR_DARK = com.tokopedia.unifyprinciples.R.color.Unify_Static_White

        private val ROOT_BACKGROUND_LIGHT = R.drawable.bg_exclusive_launch_more_voucher_light
        private val ROOT_PATTERN_LIGHT = R.drawable.bg_exclusive_launch_more_voucher_pattern_light
        private val TEXT_COLOR_LIGHT = R.color.dms_static_Unify_NN950_light
    }

    private var binding: CustomViewExclusiveLaunchMoreVoucherBinding? = null
    private val rootLayout: View?
        get() = binding?.rootLayout
    private val viewPattern: View?
        get() = binding?.viewPattern
    private val textRemainingVoucher: Typography?
        get() = binding?.textRemainingVoucher

    init {
        binding = CustomViewExclusiveLaunchMoreVoucherBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setRemainingVoucher(remainingQuota: Int) {
        val textRemainingQuota = MethodChecker.fromHtml(context.getString(
            R.string.shop_page_placeholder_view_all,
            remainingQuota
        ))
        textRemainingVoucher?.text = textRemainingQuota
    }

    fun configColorMode(isDarkMode: Boolean) {
        configRootColorMode(isDarkMode)
        configPatternColorMode(isDarkMode)
        configTextRemainingVoucherColorMode(isDarkMode)
    }

    private fun configTextRemainingVoucherColorMode(isDarkMode: Boolean) {
        val textColorRes = if (isDarkMode) {
            TEXT_COLOR_DARK
        } else {
            TEXT_COLOR_LIGHT
        }
        textRemainingVoucher?.setTextColorCompat(textColorRes)
    }

    private fun configPatternColorMode(isDarkMode: Boolean) {
        val patternRes = if (isDarkMode) {
            ROOT_PATTERN_DARK
        } else {
            ROOT_PATTERN_LIGHT
        }
        viewPattern?.background = MethodChecker.getDrawable(context, patternRes)
    }

    private fun configRootColorMode(isDarkMode: Boolean) {
        val backgroundRes = if (isDarkMode) {
            ROOT_BACKGROUND_DARK
        } else {
            ROOT_BACKGROUND_LIGHT
        }
        rootLayout?.background = MethodChecker.getDrawable(context, backgroundRes)
    }

}
