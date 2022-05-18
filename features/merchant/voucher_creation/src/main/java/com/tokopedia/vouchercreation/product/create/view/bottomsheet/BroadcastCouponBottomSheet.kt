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
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.consts.ShareComponentConstant
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.tracker.CouponCreationSuccessNoticeTracker
import com.tokopedia.vouchercreation.common.tracker.SharingComponentTracker
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.BottomsheetBroadcastCouponBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.view.viewmodel.BroadcastCouponViewModel
import com.tokopedia.vouchercreation.product.share.LinkerDataGenerator
import com.tokopedia.vouchercreation.product.share.SharingComponentInstanceBuilder
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import javax.inject.Inject

class BroadcastCouponBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var linkerDataGenerator: LinkerDataGenerator

    @Inject
    lateinit var tracker : CouponCreationSuccessNoticeTracker

    @Inject
    lateinit var sharingComponentTracker : SharingComponentTracker

    @Inject
    lateinit var shareComponentInstanceBuilder: SharingComponentInstanceBuilder

    private var nullableBinding: BottomsheetBroadcastCouponBinding? = null
    private val binding: BottomsheetBroadcastCouponBinding
        get() = requireNotNull(nullableBinding)

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(BroadcastCouponViewModel::class.java) }
    private val couponId by lazy { arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }

    companion object {
        private const val BUNDLE_KEY_COUPON_ID = "couponId"
        private const val TPD_VOUCHER_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val ZERO: Long = 0

        fun newInstance(couponId: Long): BroadcastCouponBottomSheet {
            val args = Bundle()
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
        tracker.sendCouponCreationSuccessImpression()
        setCloseClickListener {
            tracker.sendDismissBottomSheetClickEvent()
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeBroadcastMetaDataResult()
        observeShopAndTopProducts()
        observeCouponDetail()
        viewModel.getCouponDetail(couponId)
        viewModel.getBroadcastMetaData()
    }

    private fun setupView() {
        binding.imgVoucher.loadImage(TPD_VOUCHER_IMAGE_URL)
        binding.layoutBroadcastCoupon.setOnClickListener {
            broadcastCoupon(couponId)
        }
        binding.layoutShareToSocialMedia.setOnClickListener {
            sharingComponentTracker.sendShareClickEvent(
                ShareComponentConstant.ENTRY_POINT_COUPON_CREATION_SUCCESS,
                couponId.toString()
            )
            viewModel.getShopAndTopProducts(viewModel.getCoupon() ?: return@setOnClickListener)
        }
    }

    private fun handleShareToSocialMediaCardVisibility(coupon: CouponUiModel) {
        if (coupon.isPublic) {
            binding.layoutShareToSocialMedia.gone()
        } else {
            binding.layoutShareToSocialMedia.visible()
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

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            when(result) {
                is Success -> {
                    viewModel.setCoupon(result.data)
                    displayCouponPeriod(result.data)
                    handleShareToSocialMediaCardVisibility(result.data)
                }
                is Fail -> {
                    showError(result.throwable)
                }
            }

        })
    }

    private fun observeShopAndTopProducts() {
        viewModel.shopWithTopProducts.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(
                        viewModel.getCoupon() ?: return@observe,
                        result.data.topProductsImageUrl,
                        result.data.shop
                    )
                }
                is Fail -> {
                    showError(result.throwable)
                }
            }
        })
    }

    private fun displayCouponPeriod(coupon: CouponUiModel) {
        val startDate = coupon.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT).parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT).parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT).parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT).parseTo(DateTimeUtils.HOUR_FORMAT)

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
        com.tokopedia.vouchercreation.common.utils.SharingUtil.shareToBroadCastChat(requireActivity(), couponId.toInt())
    }

    private fun displayShareBottomSheet(coupon: CouponUiModel, productImageUrls : List<String>, shop: ShopBasicDataResult) {
        val title = String.format(getString(R.string.placeholder_share_component_outgoing_title), shop.shopName)
        val endDate = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.HOUR_FORMAT)
        val description = String.format(getString(R.string.placeholder_share_component_text_description), shop.shopName, endDate, endHour)


        val shareComponentBottomSheet = shareComponentInstanceBuilder.build(
            coupon,
            shop.logo,
            shop.shopName,
            title,
            productImageUrls,
            onShareOptionsClicked = { shareModel ->
                sharingComponentTracker.sendSelectShareChannelClickEvent(
                    shareModel.channel.orEmpty(),
                    coupon.id.toString()
                )
                handleShareOptionSelection(
                    coupon.galadrielVoucherId,
                    shareModel,
                    title,
                    description,
                    shop.shopDomain
                )
            },
            onCloseOptionClicked = {
                sharingComponentTracker.sendShareBottomSheetDismissClickEvent(coupon.id.toString())
            })

        sharingComponentTracker.sendShareBottomSheetDisplayedEvent(coupon.id.toString())

        shareComponentBottomSheet.show(childFragmentManager, shareComponentBottomSheet.tag)
    }

    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun handleShareOptionSelection(
        galadrielVoucherId: Long,
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    description
                )
                dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val linkerShareData = linkerDataGenerator.generate(
            galadrielVoucherId,
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            description
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                NumberConstant.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

}