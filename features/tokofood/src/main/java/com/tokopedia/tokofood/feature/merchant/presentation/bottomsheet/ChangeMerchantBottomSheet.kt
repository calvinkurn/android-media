package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.common.constants.ImageUrl
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.BottomsheetChangeMerchantLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChangeMerchantBottomSheet : BottomSheetUnify() {

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding by autoClearedNullable<BottomsheetChangeMerchantLayoutBinding>()

    private var addToCartListener: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

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
        binding?.btnConfirmOrder?.run {
            this.setOnClickListener {
                isLoading = true
                addToCartListener?.invoke()
            }
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