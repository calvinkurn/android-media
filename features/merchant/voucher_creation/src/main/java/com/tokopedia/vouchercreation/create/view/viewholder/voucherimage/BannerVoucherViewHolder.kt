package com.tokopedia.vouchercreation.create.view.viewholder.voucherimage

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.typefactory.voucherimage.VoucherImageTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import kotlinx.android.synthetic.main.mvc_voucher_image_preview.view.*

class BannerVoucherViewHolder<T : VoucherImageTypeFactory>(itemView: View) : AbstractViewHolder<BannerVoucherUiModel<T>>(itemView) {

    private var painter: VoucherPreviewPainter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_image_preview

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    override fun bind(element: BannerVoucherUiModel<T>) {
        itemView.bannerImage?.run {
            if (painter == null) {
                Glide.with(context)
                        .asBitmap()
                        .load(BANNER_BASE_URL)
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                painter = VoucherPreviewPainter(context, resource)
                                setImageBitmap(painter?.drawInitial(element))
                                return false
                            }
                        })
                        .submit()
            }
            else {
                setImageBitmap(painter?.drawInitial(element))
            }
        }
    }
}