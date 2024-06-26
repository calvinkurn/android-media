package com.tokopedia.mvc.presentation.bottomsheet.displayvoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.media.loader.loadResource
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetCouponDisplayBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.DisplayVoucherViewModel
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

    private var voucherConfiguration: VoucherConfiguration? = null
    private var currentVoucherSize = ImageRatio.HORIZONTAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetCouponDisplayBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(
            context?.resources?.getString(R.string.voucher_bs_coupon_display_title).toBlankOrString()
        )
        initInjector()
        initObservers()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSelectedVoucherChip(ImageRatio.HORIZONTAL)
        setOnClickListeners()
    }

    private fun initObservers() {
        viewModel.selectedDisplayVoucherType.observe(viewLifecycleOwner) { voucherType ->
            currentVoucherSize = voucherType
            when (voucherType) {
                ImageRatio.HORIZONTAL -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_SELECTED
                        squareChip.chipType = ChipsUnify.TYPE_NORMAL
                        verticalChip.chipType = ChipsUnify.TYPE_NORMAL
                        setUpImage(ImageRatio.HORIZONTAL)
                    }
                }
                ImageRatio.SQUARE -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_NORMAL
                        squareChip.chipType = ChipsUnify.TYPE_SELECTED
                        verticalChip.chipType = ChipsUnify.TYPE_NORMAL
                        setUpImage(ImageRatio.SQUARE)
                    }
                }
                ImageRatio.VERTICAL -> {
                    binding?.apply {
                        horizontalChip.chipType = ChipsUnify.TYPE_NORMAL
                        squareChip.chipType = ChipsUnify.TYPE_NORMAL
                        verticalChip.chipType = ChipsUnify.TYPE_SELECTED
                        setUpImage(ImageRatio.VERTICAL)
                    }
                }
            }
            changeImageViewHeight()
        }

        viewModel.couponImage.observe(viewLifecycleOwner) {
            binding?.apply {
                it.loadResource(voucherImage.context, {
                    setPlaceHolder(-1)
                }, target = MediaBitmapEmptyTarget(
                    onReady = {
                        voucherImage.setImageBitmap(it)
                    }
                ))
                voucherLoader.hide()
            }
            when (currentVoucherSize) {
                ImageRatio.HORIZONTAL -> changeImageViewHeight(
                    getDeviceHeight() * SCREEN_HEIGHT_HALF
                )
                ImageRatio.SQUARE -> changeImageViewHeight(
                    getDeviceHeight() * SCREEN_HEIGHT_FULL
                )
                ImageRatio.VERTICAL -> changeImageViewHeight(
                    getDeviceHeight() * SCREEN_HEIGHT_MULTIPLIED
                )
            }
        }
    }

    private fun getDeviceHeight(): Float {
        val displayMetrics = context?.resources?.displayMetrics
        return displayMetrics?.heightPixels.orZero() / displayMetrics?.density.orZero()
    }

    private fun setUpImage(imageRatio: ImageRatio) {
        binding?.voucherImage?.setImageBitmap(null)
        binding?.voucherLoader?.show()
        voucherConfiguration?.let {
            viewModel.previewImage(
                voucherConfiguration = it,
                parentProductIds = it.productIds,
                imageRatio = imageRatio
            )
        }
    }

    private fun setOnClickListeners() {
        binding?.horizontalChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(ImageRatio.HORIZONTAL)
        }
        binding?.verticalChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(ImageRatio.VERTICAL)
        }
        binding?.squareChip?.setOnClickListener {
            viewModel.setSelectedVoucherChip(ImageRatio.SQUARE)
        }
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    private fun changeImageViewHeight(height: Float = getDeviceHeight() * SCREEN_HEIGHT_HALF) {
        binding?.voucherImage?.layoutParams?.height = height.toInt()
        binding?.voucherImage?.requestLayout()
    }

    companion object {
        @JvmStatic
        fun newInstance(voucher: Voucher): DisplayVoucherBottomSheet {
            return newInstance(voucher.toVoucherConfiguration())
        }

        @JvmStatic
        fun newInstance(voucherConfiguration: VoucherConfiguration): DisplayVoucherBottomSheet {
            return DisplayVoucherBottomSheet().apply {
                this.voucherConfiguration = voucherConfiguration
            }
        }

        private const val SCREEN_HEIGHT_HALF = 0.5f
        private const val SCREEN_HEIGHT_FULL = 1.2f
        private const val SCREEN_HEIGHT_MULTIPLIED = 2f
    }
}
