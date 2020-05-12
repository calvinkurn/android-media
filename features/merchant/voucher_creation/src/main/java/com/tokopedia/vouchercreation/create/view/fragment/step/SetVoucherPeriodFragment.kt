package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import kotlinx.android.synthetic.main.mvc_set_voucher_period_fragment.*

class SetVoucherPeriodFragment(private val onNext: () -> Unit,
                               private val getVoucherBitmap: () -> Bitmap?) : Fragment() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit,
                           getVoucherBitmap: () -> Bitmap?) = SetVoucherPeriodFragment(onNext, getVoucherBitmap)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    private val bannerVoucherUiModel: BannerVoucherUiModel =
            // todo: change dummy
            BannerVoucherUiModel(
                    VoucherImageType.FreeDelivery(10000),
                    "harusnyadaristep1",
                    "Ini Harusnya dari Backend",
                    "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg",
                    BANNER_BASE_URL
            )

    private var bannerBitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_set_voucher_period_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setDateNextButton?.setOnClickListener {
            onNext()
        }
        if (bannerBitmap == null) {
            context?.run {
                Glide.with(this)
                        .asDrawable()
                        .load(BANNER_BASE_URL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                val bitmap = resource.toBitmap()
                                val painter = VoucherPreviewPainter(this@run, bitmap, ::onSuccessGetBitmap)
                                painter.drawFull(bannerVoucherUiModel, bitmap)
                                return false
                            }
                        })
                        .submit()
            }
        } else {
            bannerBitmap?.run {
                onSuccessGetBitmap(this)
            }
        }

    }

    private fun onSuccessGetBitmap(bitmap: Bitmap) {
        periodBannerImage?.setImageBitmap(bitmap)
    }

}