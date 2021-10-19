package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtensionConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderExtensionConstant
import com.tokopedia.buyerorderdetail.databinding.FragmentBuyerOrderExtensionBinding
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderExtensionActivity
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SubmissionOrderExtensionBottomSheet
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionDialog
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.partialview.OrderExtensionToaster
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.ref.WeakReference
import javax.inject.Inject

class BuyerOrderExtensionFragment : BaseDaggerFragment() {

    private val toasterComponent by lazy {
        OrderExtensionToaster(context, WeakReference(activity))
    }

    @Inject
    lateinit var buyerOrderDetailExtensionViewModel: BuyerOrderDetailExtensionViewModel

    private var binding by autoClearedNullable<FragmentBuyerOrderExtensionBinding>()

    private var isFromOrder: Boolean = false

    override fun getScreenName(): String = BuyerOrderExtensionFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyerOrderExtensionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataFromArguments()
//        observeRespondInfo()
//        loadRespondInfo()
        showSubmissionOrderExtension(OrderExtensionRespondInfoUiModel(messageCode = 1))
//        showOrderHasBeenDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toasterComponent.activity.clear()
    }

    private fun setDataFromArguments() {
        isFromOrder =
            arguments?.getBoolean(ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_ORDER)
                ?: false
    }

    private fun loadRespondInfo() {
        val orderId = arguments?.getString(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
        binding?.loaderBuyerOrderExtension?.show()
        buyerOrderDetailExtensionViewModel.requestRespondInfo(orderId)
    }

    private fun observeRespondInfo() {
        observe(buyerOrderDetailExtensionViewModel.orderExtensionRespondInfo) {
            binding?.loaderBuyerOrderExtension?.hide()
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

    private fun showRespondOrderExtension(
        orderExtensionRespondInfoUiModel:
        OrderExtensionRespondInfoUiModel
    ) {
        when (orderExtensionRespondInfoUiModel.messageCode) {
            BuyerOrderExtensionConstant.RespondInfoMessageCode.SUCCESS -> {
                showSubmissionOrderExtension(orderExtensionRespondInfoUiModel)
            }
            BuyerOrderExtensionConstant.RespondInfoMessageCode.ERROR -> {
                toasterComponent.setToasterNormal(
                    orderExtensionRespondInfoUiModel.messageCode,
                    orderExtensionRespondInfoUiModel.message
                ) {

                }
            }
            BuyerOrderExtensionConstant.RespondInfoMessageCode.STATUS_CHANGE -> {
                showOrderHasBeenDialog()
            }
        }
    }

    private fun showSubmissionOrderExtension(
        orderExtensionRespondInfoUiModel:
        OrderExtensionRespondInfoUiModel
    ) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(
            SubmissionOrderExtensionBottomSheet.KEY_ITEM_RESPOND_INFO,
            orderExtensionRespondInfoUiModel
        )
        val bottomSheet =
            SubmissionOrderExtensionBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheet.setOnDismissListener {
            (activity as? BuyerOrderExtensionActivity)?.setResultFinish()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showOrderHasBeenDialog() {
        val confirmedCancelledOrderDialog = context?.let {
            OrderExtensionDialog(
                it,
                DialogUnify.SINGLE_ACTION
            ).apply {
                setTitle(getString(R.string.order_extension_title_order_has_been_sent))
                setDescription(getString(R.string.order_extension_desc_order_has_been_sent))
                setImageUrl(BuyerOrderDetailOrderExtensionConstant.Image.ORDER_HAS_BEEN_SENT_URL)
            }
        }
        confirmedCancelledOrderDialog?.getDialog()?.run {
            setPrimaryCTAText(getString(R.string.label_understand))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setOnDismissListener {
                (activity as? BuyerOrderExtensionActivity)?.setResultFinish()
            }
            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerOrderExtensionFragment {
            return BuyerOrderExtensionFragment().apply {
                this.arguments = bundle
            }
        }
    }
}