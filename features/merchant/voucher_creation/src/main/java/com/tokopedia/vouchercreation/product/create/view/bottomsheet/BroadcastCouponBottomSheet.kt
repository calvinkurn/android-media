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
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.BottomsheetBroadcastCouponBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.view.viewmodel.BroadcastCouponViewModel
import com.tokopedia.vouchercreation.product.share.LinkerDataGenerator
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import javax.inject.Inject
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.vouchercreation.common.consts.ShareComponentConstant
import com.tokopedia.vouchercreation.common.tracker.CouponCreationSuccessNoticeTracker

class BroadcastCouponBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var linkerDataGenerator: LinkerDataGenerator

    @Inject
    lateinit var tracker : CouponCreationSuccessNoticeTracker

    private var nullableBinding: BottomsheetBroadcastCouponBinding? = null
    private val binding: BottomsheetBroadcastCouponBinding
        get() = requireNotNull(nullableBinding)

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(BroadcastCouponViewModel::class.java) }
    private val coupon by lazy { arguments?.getParcelable(BUNDLE_KEY_COUPON) as? Coupon }

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"
        private const val BUNDLE_KEY_COUPON_ID = "couponId"
        private const val TPD_VOUCHER_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val ZERO: Long = 0

        fun newInstance(couponId: Long, coupon: Coupon): BroadcastCouponBottomSheet {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_COUPON, coupon)
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
        viewModel.setCoupon(coupon)
        tracker.sendCouponCreationSuccessImpression()
        setCloseClickListener {
            tracker.sendDismissBottomSheetImpression()
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeBroadcastMetaDataResult()
        observeGenerateImage()
        viewModel.getBroadcastMetaData()
    }

    private fun setupView() {
        displayCouponPeriod(viewModel.getCoupon()?.information ?: return)
        binding.imgVoucher.loadImage(TPD_VOUCHER_IMAGE_URL)
        binding.layoutBroadcastCoupon.setOnClickListener {
            val couponId = arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero()
            broadcastCoupon(couponId)
        }
        binding.layoutShareToSocialMedia.setOnClickListener {
            viewModel.generateImage(viewModel.getCoupon() ?: return@setOnClickListener)
        }
        handleShareToSocialMediaCardVisibility()
    }

    private fun handleShareToSocialMediaCardVisibility() {
        if (coupon?.information?.target == CouponInformation.Target.PRIVATE) {
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


    private fun observeGenerateImage() {
        viewModel.couponImageWithShop.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(
                        viewModel.getCoupon() ?: return@observe,
                        result.data.imageUrl,
                        result.data.shop
                    )
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
        com.tokopedia.vouchercreation.common.utils.SharingUtil.shareToBroadCastChat(requireActivity(), couponId.toInt())
    }

    private fun displayShareBottomSheet(coupon: Coupon, imageUrl: String, shop: ShopBasicDataResult) {
        val title = String.format(getString(R.string.placeholder_share_component_outgoing_title), shop.shopName)
        val endDate = coupon.information.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.information.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val description = String.format(getString(R.string.placeholder_share_component_text_description), shop.shopName, endDate, endHour)


        val bottomSheet = buildShareComponentInstance(
            imageUrl,
            title,
            coupon.id,
            onShareOptionsClicked = { shareModel ->
                handleShareOptionSelection(coupon.id, shareModel, title, description, shop.shopDomain)
            }, onCloseOptionClicked = {}
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun buildShareComponentInstance(
        imageUrl: String,
        title: String,
        couponId: Long,
        onShareOptionsClicked : (ShareModel) -> Unit,
        onCloseOptionClicked : () -> Unit
    ): UniversalShareBottomSheet {
        return UniversalShareBottomSheet.createInstance().apply {
            val listener = object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    onShareOptionsClicked(shareModel)
                }

                override fun onCloseOptionClicked() {
                    onCloseOptionClicked()
                }
            }

            init(listener)
            setMetaData(tnTitle = title, tnImage = ShareComponentConstant.VOUCHER_PRODUCT_THUMBNAIL_ICON_IMAGE_URL)
            setOgImageUrl(imageUrl)
            setUtmCampaignData(
                pageName = ShareComponentConstant.VOUCHER_PRODUCT_PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = listOf(userSession.shopId, couponId.toString()),
                feature = ShareComponentConstant.VOUCHER_PRODUCT_FEATURE
            )
        }
    }

    private fun handleShareOptionSelection(
        couponId: Long,
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
            couponId,
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