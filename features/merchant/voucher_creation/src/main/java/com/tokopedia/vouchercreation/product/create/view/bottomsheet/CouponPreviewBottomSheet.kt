package com.tokopedia.vouchercreation.product.create.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.databinding.BottomsheetCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CouponPreviewViewModel
import javax.inject.Inject


class CouponPreviewBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"

        @JvmStatic
        fun newInstance(coupon: Coupon): CouponPreviewBottomSheet {
            val args = Bundle()
            args.putSerializable(BUNDLE_KEY_COUPON, coupon)
            val fragment = CouponPreviewBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var nullableBinding: BottomsheetCouponPreviewBinding? = null
    private val binding: BottomsheetCouponPreviewBinding
        get() = requireNotNull(nullableBinding)

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CouponPreviewViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nullableBinding = BottomsheetCouponPreviewBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.coupon_preview))
        setChild(binding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCouponTypeChips()
        observeCouponImage()
    }

    private fun observeCouponImage() {
        viewModel.couponImage.observe(viewLifecycleOwner, { image ->
            //TODO render data to image
        })
    }

    private fun setupCouponTypeChips() {
        with(binding) {

            chipImageHorizontal.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_SELECTED
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_NORMAL
                chipImageVertical.chipType = ChipsUnify.TYPE_NORMAL
                previewCoupon(ImageRatio.HORIZONTAL)
            }

            chipImageRatioSquare.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_NORMAL
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_SELECTED
                chipImageVertical.chipType = ChipsUnify.TYPE_NORMAL
                previewCoupon(ImageRatio.SQUARE)
            }

            chipImageVertical.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_NORMAL
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_NORMAL
                chipImageVertical.chipType = ChipsUnify.TYPE_SELECTED
                previewCoupon(ImageRatio.VERTICAL)
            }
        }


    }

    override fun onDestroyView() {
        nullableBinding = null
        super.onDestroyView()
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

    private fun previewCoupon(imageRatio: ImageRatio) {
        val coupon = arguments?.getSerializable(BUNDLE_KEY_COUPON) as? Coupon
        viewModel.previewImage(imageRatio, coupon ?: return)
    }

}