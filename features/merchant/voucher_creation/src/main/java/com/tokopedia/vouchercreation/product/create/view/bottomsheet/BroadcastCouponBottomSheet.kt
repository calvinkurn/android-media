package com.tokopedia.vouchercreation.product.create.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.databinding.BottomsheetBroadcastCouponBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.view.viewmodel.BroadcastCouponViewModel
import javax.inject.Inject

class BroadcastCouponBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var nullableBinding: BottomsheetBroadcastCouponBinding? = null
    private val binding: BottomsheetBroadcastCouponBinding
        get() = requireNotNull(nullableBinding)

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(BroadcastCouponViewModel::class.java) }
    private val coupon by lazy { arguments?.getParcelable(BUNDLE_KEY_COUPON_INFORMATION) as? CouponInformation }

    companion object {
        private const val BUNDLE_KEY_COUPON_INFORMATION = "coupon-information"
        private const val BUNDLE_KEY_COUPON_ID = "coupon"
        private const val TPD_VOUCHER_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val ZERO: Long = 0

        fun newInstance(couponId: Long, couponInformation: CouponInformation): BroadcastCouponBottomSheet {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_COUPON_INFORMATION, couponInformation)
            args.putLong(BUNDLE_KEY_COUPON_ID, couponId)

            val fragment = BroadcastCouponBottomSheet()
            fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            fragment.arguments = args

            return fragment
        }
    }

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
        nullableBinding = BottomsheetBroadcastCouponBinding.inflate(inflater, container, false)
        setChild(binding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeBroadcastMetaDataResult()
        observeShopDetail()
        viewModel.getBroadcastMetaData()
    }

    private fun setupView() {
        displayCouponPeriod(coupon ?: return)
        binding.imgVoucher.loadImage(TPD_VOUCHER_IMAGE_URL)
        binding.layoutBroadcastCoupon.setOnClickListener {
            val couponId = arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero()
            broadcastCoupon(couponId)
        }
        binding.layoutShareToSocialMedia.setOnClickListener { viewModel.getShopDetail() }
        handleShareToSocialMediaCardVisibility()
    }

    private fun handleShareToSocialMediaCardVisibility() {
        if (coupon?.target== CouponInformation.Target.SPECIAL) {
            binding.layoutShareToSocialMedia.visible()
        } else {
            binding.layoutShareToSocialMedia.gone()
        }
    }

    private fun observeBroadcastMetaDataResult() {
        viewModel.broadcastMetadata.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    if (result.data.promo > ZERO) {
                        binding.tpgFree.visible()
                    } else {
                        binding.tpgFree.gone()
                    }
                }
                is Fail -> {
                    binding.tpgFree.gone()
                }
            }
        })
    }

    private fun observeShopDetail() {
        viewModel.shop.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(result.data.shopName, coupon ?: return@observe)
                }
                is Fail -> {
                   showError(result.throwable)
                }
            }
        })
    }

    private fun displayCouponPeriod(coupon: CouponInformation) {
        val startDate = coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val period = String.format(
            getString(R.string.placeholder_scheduled_coupon_period),
            coupon.name,
            startDate,
            startHour,
            endDate,
            endHour
        )

        binding.tgpVoucherPeriodInfo.text = period
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

    private fun broadcastCoupon(couponId: Long) {
        VoucherCreationTracking.sendBroadCastChatClickTracking(
            category = VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PAGE,
            shopId = userSession.shopId
        )
        SharingUtil.shareToBroadCastChat(requireActivity(), couponId.toInt())
    }

    private fun displayShareBottomSheet(shopName: String, coupon: CouponInformation) {
        //TODO implement share component
        val startDate =
            coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT_DAY_MONTH)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT_DAY_MONTH)

        val template = getString(R.string.placeholder_share_coupon_product_wording)
        val wording = String.format(template, shopName, startDate, endDate, "")
    }

    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }
}