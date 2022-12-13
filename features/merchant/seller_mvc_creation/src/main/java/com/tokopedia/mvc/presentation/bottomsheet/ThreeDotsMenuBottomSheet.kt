package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheet3DotsMenuBinding
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ThreeDotsMenuBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun newInstance(voucherName: String, type: Int): ThreeDotsMenuBottomSheet {
            return ThreeDotsMenuBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BundleConstant.BUNDLE_VOUCHER_NAME, voucherName)
                    putInt(BundleConstant.BUNDLE_BOTTOMSHEET_TYPE, type)
                }
            }
        }

        const val TYPE_CANCEL = 1
        const val TYPE_STOP = 2
        const val TYPE_DEFAULT = 3
        private const val TNC_URL =
            "https://www.tokopedia.com/help/seller/article/syarat-ketentuan-kupon-toko-saya"
    }

    private var binding by autoClearedNullable<SmvcBottomsheet3DotsMenuBinding>()
    private val bottomSheetType by lazy {
        arguments?.getInt(BundleConstant.BUNDLE_BOTTOMSHEET_TYPE).orZero()
    }
    private val voucherName by lazy {
        arguments?.getString(BundleConstant.BUNDLE_VOUCHER_NAME).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = SmvcBottomsheet3DotsMenuBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        setLayout()
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setLayout() {
        setTitle(voucherName)
        binding?.run {
            when (bottomSheetType) {
                TYPE_CANCEL -> {
                    tpgCancel.text = getString(R.string.smvc_cancel_label)
                }
                TYPE_STOP -> {
                    tpgCancel.text = getString(R.string.smvc_stop_label)
                }
                else -> {
                    iconCancel.gone()
                    tpgCancel.gone()
                }
            }
        }
        setAction()
    }

    private fun setAction() {
        binding?.run {
            tpgTnc.setOnClickListener {
                routeToUrl(TNC_URL)
            }
            tpgCancel.setOnClickListener {
                when (bottomSheetType) {
                    TYPE_CANCEL -> {
                        TODO("cancel voucher, use case is in progress")
                    }
                    TYPE_STOP -> {
                        TODO("stop voucher, use case is in progress")
                    }
                }
            }
        }
    }
}
