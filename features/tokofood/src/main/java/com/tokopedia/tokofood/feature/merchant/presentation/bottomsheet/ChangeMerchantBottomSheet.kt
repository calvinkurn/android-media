package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.common.constants.ImageUrl
import com.tokopedia.tokofood.databinding.BottomsheetChangeMerchantLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChangeMerchantBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetChangeMerchantLayoutBinding>()

    private var addToCartListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetChangeMerchantLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.ivChangeMerchant?.loadImage(ImageUrl.Merchant.IV_CHANGE_MERCHANT_URL)
        setBtnConfirmOrder()
        setBtnCancelOrder()
    }

    private fun setBtnCancelOrder() {
        binding?.btnCancelOrder?.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun setBtnConfirmOrder() {
        binding?.btnConfirmOrder?.setOnClickListener {
            addToCartListener?.invoke()
            dismissAllowingStateLoss()
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun addToCart(addToCartListener: () -> Unit) {
        this.addToCartListener = addToCartListener
    }

    companion object {
        fun newInstance(): ChangeMerchantBottomSheet {
            return ChangeMerchantBottomSheet()
        }

        val TAG: String = ChangeMerchantBottomSheet::class.java.simpleName
    }

}