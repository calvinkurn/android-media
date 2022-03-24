package com.tokopedia.vouchercreation.product.preview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.databinding.BottomsheetCouponImagePreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import javax.inject.Inject


class CouponImagePreviewBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_COUPON_INFORMATION = "information"
        private const val BUNDLE_KEY_COUPON_SETTINGS = "settings"
        private const val BUNDLE_KEY_PARENT_PRODUCT_IDS = "parent_product_ids"
        private const val BUNDLE_KEY_IS_CREATE_MODE = "create-mode"
        private const val SCREEN_HEIGHT_FULL = 1f
        private const val SCREEN_HEIGHT_ONE_HALF = 1.5f
        private const val SCREEN_HEIGHT_MULTIPLIED = 2

        @JvmStatic
        fun newInstance(
            isCreateMode: Boolean,
            couponInformation: CouponInformation,
            couponSettings: CouponSettings,
            parentProductIds: List<Long>
        ): CouponImagePreviewBottomSheet {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_COUPON_INFORMATION, couponInformation)
            args.putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
            args.putLongArray(BUNDLE_KEY_PARENT_PRODUCT_IDS, parentProductIds.toLongArray())
            args.putBoolean(BUNDLE_KEY_IS_CREATE_MODE, isCreateMode)
            val fragment = CouponImagePreviewBottomSheet()
            fragment.arguments = args
            return fragment

        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var nullableBinding: BottomsheetCouponImagePreviewBinding? = null
    private val binding: BottomsheetCouponImagePreviewBinding
        get() = requireNotNull(nullableBinding)

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CouponImagePreviewViewModel::class.java) }

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
        nullableBinding = BottomsheetCouponImagePreviewBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.coupon_preview))
        setChild(binding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChipsClickListener()
        binding.chipImageHorizontal.chipType = ChipsUnify.TYPE_SELECTED
        observeCouponImage()
    }

    private fun observeCouponImage() {
        viewModel.couponImage.observe(viewLifecycleOwner, { result ->
            binding.loader.gone()
            binding.imgCoupon.visible()

            if (result is Success) {
                displayImage(result.data)
            } else {
                Toaster.build(binding.root, getString(R.string.error_message_failed_get_image), Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun setupChipsClickListener() {
        with(binding) {
            chipImageHorizontal.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    previewCoupon(ImageRatio.HORIZONTAL)
                    changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_FULL)
                }
            }
            chipImageRatioSquare.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    previewCoupon(ImageRatio.SQUARE)
                    changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_ONE_HALF)
                }
            }
            chipImageVertical.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    previewCoupon(ImageRatio.VERTICAL)
                    changeImageViewHeight(getDeviceHeight() * SCREEN_HEIGHT_MULTIPLIED)
                }
            }

            chipImageHorizontal.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_SELECTED
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_NORMAL
                chipImageVertical.chipType = ChipsUnify.TYPE_NORMAL
            }

            chipImageRatioSquare.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_NORMAL
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_SELECTED
                chipImageVertical.chipType = ChipsUnify.TYPE_NORMAL
            }

            chipImageVertical.chip_container.setOnClickListener {
                chipImageHorizontal.chipType = ChipsUnify.TYPE_NORMAL
                chipImageRatioSquare.chipType = ChipsUnify.TYPE_NORMAL
                chipImageVertical.chipType = ChipsUnify.TYPE_SELECTED
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
        binding.imgCoupon.gone()
        binding.loader.type = LoaderUnify.TYPE_CIRCULAR
        binding.loader.visible()

        val isCreateMode = arguments?.getBoolean(BUNDLE_KEY_IS_CREATE_MODE, false).orFalse()
        val couponInformation =
            arguments?.getParcelable(BUNDLE_KEY_COUPON_INFORMATION) as? CouponInformation
                ?: return
        val couponSettings =
            arguments?.getParcelable(BUNDLE_KEY_COUPON_SETTINGS) as? CouponSettings ?: return
        val parentProductIds = arguments?.getLongArray(BUNDLE_KEY_PARENT_PRODUCT_IDS)?.toList().orEmpty()

        viewModel.previewImage(
            isCreateMode,
            couponInformation,
            couponSettings,
            parentProductIds,
            imageRatio
        )
    }

    /**
     * Display image in safely manner to prevent out of memory exception error when image size is
     * too big
     */
    private fun displayImage(image : ByteArray) {
        try {
            Glide.with(binding.imgCoupon.context).asBitmap().load(image).into(binding.imgCoupon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeImageViewHeight(height : Float) {
        binding.imgCoupon.layoutParams.height = height.toInt()
        binding.imgCoupon.requestLayout()
    }

    private fun getDeviceHeight(): Float {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels / displayMetrics.density
    }


}