package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.mvc_set_voucher_period_fragment.*

class SetVoucherPeriodFragment(private val onNext: () -> Unit,
                               private val getVoucherBitmap: () -> Bitmap?) : Fragment() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit,
                           getVoucherBitmap: () -> Bitmap?) = SetVoucherPeriodFragment(onNext, getVoucherBitmap)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_set_voucher_period_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            loadBannerInformation()
        }
    }

    private fun setupView() {
        setDateNextButton?.setOnClickListener {
            onNext()
        }
    }

    private fun loadBannerInformation() {
        getVoucherBitmap()?.let { bitmap ->
            periodBannerImage?.setImageBitmap(bitmap)
        }
    }

}