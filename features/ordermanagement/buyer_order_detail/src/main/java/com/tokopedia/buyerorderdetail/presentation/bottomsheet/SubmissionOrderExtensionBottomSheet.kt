package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderExtensionTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtensionConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderExtensionConstant
import com.tokopedia.buyerorderdetail.databinding.OrderExtensionSubmissionExtendsBottomsheetBinding
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionDialog
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.buyerorderdetail.presentation.partialview.OrderExtensionToaster
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.lang.ref.WeakReference
import javax.inject.Inject

class SubmissionOrderExtensionBottomSheet : BottomSheetUnify() {

    private val toasterComponent by lazy {
        OrderExtensionToaster(context, WeakReference(activity))
    }

    @Inject
    lateinit var buyerOrderDetailExtensionViewModel: BuyerOrderDetailExtensionViewModel

    private var binding: OrderExtensionSubmissionExtendsBottomsheetBinding? = null
    private var confirmedCancelledOrderDialog: OrderExtensionDialog? = null

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
            OrderExtensionSubmissionExtendsBottomsheetBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(getString(R.string.order_extension_title_submission_extends_bottom_sheet))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun observeRespond() {
        observe(buyerOrderDetailExtensionViewModel.orderExtensionRespond) {
            setButtonLoadingFalse()
            confirmedCancelledOrderDialog?.dismissDialog()
            when (it) {
                is Success -> {
                    showRespondOrderExtension(it.data)
                }
                is Fail -> {
                    toasterComponent.setToasterInternalError(it.throwable)
                }
            }
        }
    }

    private fun setButtonLoadingFalse() {
        if (binding?.btnSubmissionExtends?.isLoading == true) {
            binding?.btnSubmissionExtends?.isLoading = false
        }
    }

    private fun setupView() = binding?.run {
        val respondInfo = getRespondInfo().value
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
        }
    }

    private fun showRespondOrderExtension(orderExtensionRespondUiModel: OrderExtensionRespondUiModel) {
        when (orderExtensionRespondUiModel.messageCode) {
            BuyerOrderExtensionConstant.RespondMessageCode.SUCCESS,
            BuyerOrderExtensionConstant.RespondMessageCode.ERROR -> {
                toasterComponent.setToasterNormal(
                    orderExtensionRespondUiModel.messageCode,
                    orderExtensionRespondUiModel.message
                ) { isOrderExtended ->
                    if (isOrderExtended) {
                        BuyerOrderExtensionTracker.eventAcceptOrderExtension(
                            getOrderId()
                        )
                    } else {
                        BuyerOrderExtensionTracker.eventRejectOrderExtension(
                            getOrderId()
                        )
                    }
                }
            }
            BuyerOrderExtensionConstant.RespondMessageCode.STATUS_CHANGE -> {
                showConfirmedCancelledOrderDialog()
            }
        }
    }

    private fun getInstanceDialog(): Lazy<OrderExtensionDialog?> {
        val respondInfo = getRespondInfo().value
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

    private fun getOrderId(): String {
        return getRespondInfo().value?.orderId.orEmpty()
    }

    private fun showConfirmedCancelledOrderDialog() {
        confirmedCancelledOrderDialog = getInstanceDialog().value
        confirmedCancelledOrderDialog?.getDialog()?.apply {
            setPrimaryCTAText(getString(R.string.order_extension_order_cancelled))
            setSecondaryCTAText(getString(R.string.order_extension_btn_secondary))
            setPrimaryCTAClickListener {
                buyerOrderDetailExtensionViewModel.requestRespond(getOrderId(), CANCEL_ACTION)
            }
            setSecondaryCTAClickListener {
                buyerOrderDetailExtensionViewModel.requestRespond(getOrderId(), EXTENSION_ACTION)
            }
            show()
        }
    }

    private fun getRespondInfo(): Lazy<OrderExtensionRespondInfoUiModel?> {
        return lazy {
            val cacheManager = context?.let {
                SaveInstanceCacheManager(
                    it,
                    arguments?.getString(KEY_CACHE_MANAGER_ID)
                )
            }
            cacheManager?.get<OrderExtensionRespondInfoUiModel>(
                KEY_ITEM_RESPOND_INFO,
                OrderExtensionRespondInfoUiModel::class.java
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        toasterComponent.activity.clear()
    }

    companion object {
        const val TAG = "SubmissionOrderExtensionBottomSheet"

        const val KEY_ITEM_RESPOND_INFO = "key_item_respond_info"
        private const val KEY_CACHE_MANAGER_ID = "extra_cache_manager_id"

        private const val EXTENSION_ACTION = 1
        private const val CANCEL_ACTION = 0

        fun newInstance(cacheManagerId: String): SubmissionOrderExtensionBottomSheet {
            return SubmissionOrderExtensionBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
                arguments = bundle
            }
        }
    }
}