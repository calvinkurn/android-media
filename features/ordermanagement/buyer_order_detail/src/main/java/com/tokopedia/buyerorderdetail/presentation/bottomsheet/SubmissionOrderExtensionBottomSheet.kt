package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTrackerConstant
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderExtensionTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtensionConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderExtensionConstant
import com.tokopedia.buyerorderdetail.databinding.OrderExtensionSubmissionExtendsBottomsheetBinding
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderExtensionActivity
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionDialog
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionHasBeenSentDialog
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.buyerorderdetail.presentation.partialview.OrderExtensionToaster
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.ref.WeakReference
import javax.inject.Inject

class SubmissionOrderExtensionBottomSheet : BottomSheetUnify() {

    private val orderExtensionHasBeenSentDialog by lazy {
        context?.let { OrderExtensionHasBeenSentDialog(it, DialogUnify.SINGLE_ACTION) }
    }

    private val toasterComponent by lazy {
        context?.let {
            OrderExtensionToaster(
                it,
                WeakReference(activity)
            )
        }
    }

    @Inject
    lateinit var buyerOrderDetailExtensionViewModel: BuyerOrderDetailExtensionViewModel

    private var binding by autoClearedNullable<OrderExtensionSubmissionExtendsBottomsheetBinding>()
    private var confirmedCancelledOrderDialog: OrderExtensionDialog? = null
    private var respondInfo: OrderExtensionRespondInfoUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            OrderExtensionSubmissionExtendsBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRespondInfo()
        setupView()
        setupCta()
        observeRespond()
    }

    private fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun getRootView(): View? {
        return binding?.root?.rootView
    }

    private fun observeRespond() {
        observe(buyerOrderDetailExtensionViewModel.orderExtensionRespond) {
            setButtonLoadingFalse()
            dismissConfirmedCancelledOrderDialog()
            when (it) {
                is Success -> {
                    showRespondOrderExtension(it.data)
                }
                is Fail -> {
                    toasterComponent?.setToasterInternalError(getRootView(), it.throwable)
                }
            }
        }
    }

    private fun dismissConfirmedCancelledOrderDialog() {
        confirmedCancelledOrderDialog?.dismissDialog()
    }

    private fun setButtonLoadingFalse() {
        if (binding?.btnSubmissionExtends?.isLoading == true) {
            binding?.btnSubmissionExtends?.isLoading = false
        }

        confirmedCancelledOrderDialog?.getDialog()?.run {
            if (dialogPrimaryCTA.isLoading) {
                dialogPrimaryCTA.isLoading = false
            }
            if (dialogSecondaryLongCTA.isLoading) {
                dialogSecondaryLongCTA.isLoading = false
            }
        }
    }

    private fun setupView() = binding?.run {
        tvSubmissionExtendsTitle.text = respondInfo?.confirmationTitle.orEmpty()
        tvSubmissionExtendsReason.text = respondInfo?.reasonExtension.orEmpty()
        dividerSubmissionExtends.setBackgroundResource(R.drawable.ic_divider_submission_extends)
        submissionExtendsIndicator.setBackgroundResource(R.drawable.ic_reason_submission_extends)
    }

    private fun setupCta() = binding?.run {
        btnOrderCancelled.setOnClickListener {
            showConfirmedCancelledOrderDialog()
        }
        btnSubmissionExtends.setOnClickListener {
            btnSubmissionExtends.isLoading = true
            buyerOrderDetailExtensionViewModel.requestRespond(
                orderId = getOrderId(),
                action = EXTENSION_ACTION
            )
        }
    }

    private fun showRespondOrderExtension(
        orderExtensionRespondUiModel: OrderExtensionRespondUiModel
    ) {
        val isOrderExtended =
            orderExtensionRespondUiModel.messageCode ==
                    BuyerOrderExtensionConstant.RespondMessageCode.SUCCESS
        val isFromUOH = arguments?.getBoolean(
            ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_UOH, false
        ).orFalse()
        val isUoH = if (isFromUOH) {
            BuyerOrderDetailTrackerConstant.UOH_SOURCE
        } else {
            BuyerOrderDetailTrackerConstant.BOM_SOURCE
        }
        when (orderExtensionRespondUiModel.messageCode) {
            BuyerOrderExtensionConstant.RespondMessageCode.SUCCESS,
            BuyerOrderExtensionConstant.RespondMessageCode.ERROR -> {
                toasterComponent?.showToaster(
                    isOrderExtended,
                    orderExtensionRespondUiModel.message,
                    getRootView()
                ) {
                    if (orderExtensionRespondUiModel.actionType == EXTENSION_ACTION) {
                        BuyerOrderExtensionTracker.eventAcceptOrderExtension(
                            getOrderId(),
                            isUoH
                        )
                    } else {
                        BuyerOrderExtensionTracker.eventRejectOrderExtension(
                            getOrderId(),
                            isUoH
                        )
                    }
                }
            }
            BuyerOrderExtensionConstant.RespondMessageCode.STATUS_CHANGE -> {
                showOrderHasBeenSentDialog()
            }
        }
    }

    private fun showOrderHasBeenSentDialog() {
        orderExtensionHasBeenSentDialog?.run {
            showOrderHasBeenSentDialog()
            getDialog()?.setOnDismissListener {
                (activity as? BuyerOrderExtensionActivity)?.setResultFinish(
                    Activity.RESULT_OK,
                    true
                )
            }
        }
    }

    private fun getOrderId(): String {
        return respondInfo?.orderId.orEmpty()
    }

    private fun getInstanceDialog(): Lazy<OrderExtensionDialog?> {
        val nn950Color = com.tokopedia.unifyprinciples.R.color.Unify_NN950.toString()
        return lazy {
            context?.let {
                OrderExtensionDialog(
                    it,
                    DialogUnify.VERTICAL_ACTION
                ).apply {
                    setTitle(getString(R.string.order_extension_title_confirmed_order_cancelled))
                    setDescription(
                        getString(
                            R.string.order_extension_desc_confirmed_order_cancelled,
                            respondInfo?.rejectText.orEmpty(),
                            nn950Color,
                            respondInfo?.newDeadline.orEmpty(),
                        )
                    )
                    setDialogSecondaryCta()
                    setImageUrl(BuyerOrderDetailOrderExtensionConstant.Image.CONFIRMED_CANCELLED_ORDER_URL)
                }
            }
        }
    }

    private fun showConfirmedCancelledOrderDialog() {
        confirmedCancelledOrderDialog = getInstanceDialog().value
        confirmedCancelledOrderDialog?.getDialog()?.apply {
            setPrimaryCTAText(getString(R.string.order_extension_order_cancelled))
            setSecondaryCTAText(getString(R.string.order_extension_btn_secondary))
            setPrimaryCTAClickListener {
                dialogPrimaryCTA.isLoading = true
                buyerOrderDetailExtensionViewModel.requestRespond(
                    getOrderId(),
                    CANCEL_ACTION
                )
            }
            setSecondaryCTAClickListener {
                dialogSecondaryLongCTA.isLoading = true
                buyerOrderDetailExtensionViewModel.requestRespond(
                    getOrderId(),
                    EXTENSION_ACTION
                )
            }
            show()
        }
    }

    private fun setupRespondInfo() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        respondInfo = cacheManager?.get<OrderExtensionRespondInfoUiModel>(
            KEY_ITEM_RESPOND_INFO,
            OrderExtensionRespondInfoUiModel::class.java
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        toasterComponent?.activity?.clear()
    }

    companion object {
        const val TAG = "SubmissionOrderExtensionBottomSheet"

        const val KEY_ITEM_RESPOND_INFO = "key_item_respond_info"
        private const val KEY_CACHE_MANAGER_ID = "extra_cache_manager_id"

        private const val EXTENSION_ACTION = 1
        private const val CANCEL_ACTION = 2

        fun newInstance(
            cacheManagerId: String,
            isFromUoh: Boolean
        ): SubmissionOrderExtensionBottomSheet {
            return SubmissionOrderExtensionBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
                bundle.putBoolean(
                    ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_UOH,
                    isFromUoh
                )
                arguments = bundle
            }
        }
    }
}