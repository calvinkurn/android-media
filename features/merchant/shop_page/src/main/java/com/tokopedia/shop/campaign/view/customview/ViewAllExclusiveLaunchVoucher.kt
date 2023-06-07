package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.CustomViewViewAllVoucherBinding

class ViewAllExclusiveLaunchVoucher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: CustomViewViewAllVoucherBinding? = null

    init {
        binding = CustomViewViewAllVoucherBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setDescription(text: String) {
        binding?.tpgText?.text = MethodChecker.fromHtml(text)
    }


    fun useDarkBackground() {
        binding?.run {
            imgBackground.setBackgroundResource(R.drawable.bg_view_all_voucher_dark)
            tpgText.setTextColorCompat(R.color.Unify_Static_White)
        }
    }

    fun useLightBackground() {
        binding?.run {
            imgBackground.setBackgroundResource(R.drawable.bg_view_all_voucher_light)
            tpgText.setTextColorCompat(R.color.clr_dms_voucher_title)
        }
    }
}
