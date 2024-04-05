package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.loginregister.databinding.BottomSheetOsLandingBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OfficialShopLandingBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetOsLandingBinding>()

    private var onWebviewClick: (() -> Unit)? = null
    private var onStayClick: (() -> Unit)? = null

    companion object {
        private val TAG: String = OfficialShopLandingBottomSheet::class.java.simpleName
        private const val IMG_BANNER = "https://images.tokopedia.net/img/android/user/loginregister/img_os_web_onboard.png"

        fun createInstance(): OfficialShopLandingBottomSheet {
            return OfficialShopLandingBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetOsLandingBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(
        fm: FragmentManager,
    ) {
        show(fm, TAG)
    }

    private fun setupView() {
        binding?.buttonWebview?.setOnClickListener {
            onWebviewClick?.invoke()
            dismiss()
        }

        binding?.buttonStay?.setOnClickListener {
            onStayClick?.invoke()
            dismiss()
        }

        binding?.imgBanner?.loadImage(IMG_BANNER)
    }

    fun setOnWebviewClick(onClickReset: () -> Unit) {
        this.onWebviewClick = onClickReset
    }

    fun setOnStayClick(onClickRemoveAll: () -> Unit) {
        this.onStayClick = onClickRemoveAll
    }

}
