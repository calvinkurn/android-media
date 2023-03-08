package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVoucherViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.IntroVoucherUiModel
import com.tokopedia.mvc.util.constant.FIRST_INDEX
import com.tokopedia.mvc.util.constant.SECOND_INDEX
import com.tokopedia.mvc.util.constant.ZEROTH_INDEX
import com.tokopedia.utils.view.binding.viewBinding

class IntroVoucherViewHolder(itemView: View?) : AbstractViewHolder<IntroVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_voucher_viewholder
        private const val BACKGROUND_IMAGE_URL = "https://images.tokopedia.net/img/android/seller-mvc/ic_mvc_intro_background.png"
    }

    private var binding: SmvcIntroVoucherViewholderBinding? by viewBinding()

    override fun bind(element: IntroVoucherUiModel?) {
        binding?.apply {
            ivBackground.loadImage(BACKGROUND_IMAGE_URL)
            title.text = element?.title.toBlankOrString()
            subtitle.text = element?.subtitle.toBlankOrString()
            element?.list?.getOrNull(ZEROTH_INDEX)?.let {
                viewFlexibleForCoupons.setData(
                    it.benefitTitle,
                    it.benefitSubtitle,
                    it.benefitImageUrl
                )
            }
            element?.list?.getOrNull(FIRST_INDEX)?.let {
                viewSelectionCoupons.setData(
                    it.benefitTitle,
                    it.benefitSubtitle,
                    it.benefitImageUrl
                )
            }
            element?.list?.getOrNull(SECOND_INDEX)?.let {
                viewSetTarget.setData(it.benefitTitle, it.benefitSubtitle, it.benefitImageUrl)
            }
        }
    }
}
