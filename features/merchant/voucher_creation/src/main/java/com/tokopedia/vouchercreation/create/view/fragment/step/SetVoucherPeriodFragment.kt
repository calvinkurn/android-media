package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import kotlinx.android.synthetic.main.mvc_set_voucher_period_fragment.*

class SetVoucherPeriodFragment(private val onNext: () -> Unit,
                               private val getVoucherBanner: () -> BannerVoucherUiModel) : Fragment() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit,
                           getVoucherBanner: () -> BannerVoucherUiModel) = SetVoucherPeriodFragment(onNext, getVoucherBanner)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    private var bannerVoucherUiModel: BannerVoucherUiModel = getVoucherBanner()

    private var bannerBitmap: Bitmap? = null

    override fun onResume() {
        super.onResume()
        bannerVoucherUiModel = getVoucherBanner()
        drawBanner()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_set_voucher_period_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        disableTextFieldEdit()
        setDateNextButton?.setOnClickListener {
            onNext()
        }

    }

    private fun onSuccessGetBitmap(bitmap: Bitmap) {
        activity?.runOnUiThread {
            periodBannerImage?.setImageBitmap(bitmap)
        }
    }

    private fun disableTextFieldEdit() {
        startDateTextField?.textFieldInput?.run {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
        endDateTextField?.textFieldInput?.run {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
    }

    private fun drawBanner() {
        if (bannerBitmap == null) {
            context?.run {
                Glide.with(this)
                        .asDrawable()
                        .load(BANNER_BASE_URL)
                        .signature(ObjectKey(System.currentTimeMillis().toString()))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                activity?.runOnUiThread {
                                    val bitmap = resource.toBitmap()
                                    val painter = VoucherPreviewPainter(this@run, bitmap, ::onSuccessGetBitmap)
                                    painter.drawFull(bannerVoucherUiModel, bitmap)
                                }
                                return false
                            }
                        })
                        .submit()
            }
        }
    }

}