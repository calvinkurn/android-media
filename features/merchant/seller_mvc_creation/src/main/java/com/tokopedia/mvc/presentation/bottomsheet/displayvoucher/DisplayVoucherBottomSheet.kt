package com.tokopedia.mvc.presentation.bottomsheet.displayvoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetCouponDisplayBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.DisplayVoucherViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DisplayVoucherBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetCouponDisplayBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DisplayVoucherViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DisplayVoucherViewModel::class.java)
    }


    private var voucher: Voucher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetCouponDisplayBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.resources?.getString(R.string.voucher_bs_coupon_display_title).toBlankOrString())
        initInjector()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.squareChip?.chipType = ChipsUnify.TYPE_NORMAL
    }


    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(voucher: Voucher): DisplayVoucherBottomSheet {
            return DisplayVoucherBottomSheet().apply {
                this.voucher = voucher
            }
        }
    }

}
