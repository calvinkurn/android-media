package com.tokopedia.vouchercreation.create.view.viewholder.voucherimage

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.typefactory.voucherimage.VoucherImageTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import kotlinx.android.synthetic.main.mvc_voucher_banner.view.*

class BannerVoucherViewHolder<T : VoucherImageTypeFactory>(itemView: View) : AbstractViewHolder<BannerVoucherUiModel<T>>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_banner

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    override fun bind(element: BannerVoucherUiModel<T>) {
        itemView.bannerImage?.run {
            Glide.with(context)
                    .load(BANNER_BASE_URL)
                    .into(this)
        }
    }

}