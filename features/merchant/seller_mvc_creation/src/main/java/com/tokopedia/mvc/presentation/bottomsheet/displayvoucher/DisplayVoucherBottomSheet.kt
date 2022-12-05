package com.tokopedia.mvc.presentation.bottomsheet.displayvoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetCouponDisplayBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.DisplayVoucherViewModel
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.VoucherType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DisplayVoucherBottomSheet : BottomSheetUnify() {

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
        initObservers()
        viewModel.setSelectedVoucherChip(VoucherType.Horizontal)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun initObservers() {
        viewModel.selectedDisplayVoucherType.observe(viewLifecycleOwner) { voucherType ->
            when (voucherType) {
                is VoucherType.Horizontal -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_SELECTED
                        squareChip.chipType = ChipsUnify.TYPE_NORMAL
                        verticalChip.chipType = ChipsUnify.TYPE_NORMAL
                        setUpImage(voucher?.image)
                        changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_FULL)
                    }
                }
                is VoucherType.Square -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_NORMAL
                        squareChip.chipType = ChipsUnify.TYPE_SELECTED
                        verticalChip.chipType = ChipsUnify.TYPE_NORMAL
                        setUpImage(voucher?.imageSquare)
                        changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_ONE_HALF)
                    }
                }
                is VoucherType.Vertical -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_NORMAL
                        squareChip.chipType = ChipsUnify.TYPE_NORMAL
                        verticalChip.chipType = ChipsUnify.TYPE_SELECTED
                        setUpImage(voucher?.imagePortrait)
                        changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_MULTIPLIED)
                    }
                }
            }
        }
    }

    private fun getDeviceHeight(): Float {
        val displayMetrics = context?.resources?.displayMetrics
        return displayMetrics?.heightPixels.orZero() / displayMetrics?.density.orZero()
    }

    private fun setUpImage(url : String?) {
        if (url == null)
            return
        binding?.voucherImage?.loadImage(url){
            setCacheStrategy(MediaCacheStrategy.RESOURCE)
        }
    }

    private fun setOnClickListeners() {
        binding?.horizontalChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(VoucherType.Horizontal)
        }
        binding?.verticalChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(VoucherType.Vertical)
        }
        binding?.squareChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(VoucherType.Square)
        }
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun changeImageViewHeight(height : Float) {
        binding?.voucherImage?.layoutParams?.height = height.toInt()
        binding?.voucherImage?.requestLayout()
    }

    companion object {
        @JvmStatic
        fun newInstance(voucher: Voucher): DisplayVoucherBottomSheet {
            return DisplayVoucherBottomSheet().apply {
                this.voucher = voucher
            }
        }

        private const val SCREEN_HEIGHT_FULL = 0.5f
        private const val SCREEN_HEIGHT_ONE_HALF = 1f
        private const val SCREEN_HEIGHT_MULTIPLIED = 1.5f
    }
}
