package com.tokopedia.buyerorderdetail.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderExtensionConstant
import com.tokopedia.buyerorderdetail.databinding.FragmentBuyerOrderExtensionBinding
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderExtensionActivity
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SubmissionOrderExtensionBottomSheet
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionHasBeenSentDialog
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
        OrderExtensionToaster(
            context,
            WeakReference(activity)
        )
    }

    private val orderExtensionHasBeenSentDialog: OrderExtensionHasBeenSentDialog? by lazy {
        context?.let { OrderExtensionHasBeenSentDialog(it, DialogUnify.SINGLE_ACTION) }
    }

    @Inject
    lateinit var buyerOrderDetailExtensionViewModel: BuyerOrderDetailExtensionViewModel

    private var binding by autoClearedNullable<FragmentBuyerOrderExtensionBinding>()

    private var isFromUoh: Boolean = false

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
        observeRespondInfo()
        loadRespondInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        toasterComponent.activity.clear()
    }

    private fun setDataFromArguments() {
        isFromUoh = arguments?.getBoolean(ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_UOH)
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
                    toasterComponent.setToasterInternalError(binding?.root?.rootView, it.throwable)
                }
            }
        }
    }

    private fun showRespondOrderExtension(
        data:
        OrderExtensionRespondInfoUiModel
    ) {
        val isOrderExtended =
            data.messageCode == BuyerOrderExtensionConstant.RespondInfoMessageCode.SUCCESS
        when (data.messageCode) {
            BuyerOrderExtensionConstant.RespondInfoMessageCode.SUCCESS -> {
                showSubmissionOrderExtension(data)
            }
            BuyerOrderExtensionConstant.RespondInfoMessageCode.ERROR -> {
                toasterComponent.showToaster(
                    isOrderExtended,
                    data.message
                )
            }
            BuyerOrderExtensionConstant.RespondInfoMessageCode.STATUS_CHANGE -> {
                showOrderHasBeenSentDialog()
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
            SubmissionOrderExtensionBottomSheet.newInstance(cacheManager?.id.orEmpty(), isFromUoh)
        bottomSheet.setOnDismissListener {
            (activity as? BuyerOrderExtensionActivity)?.setResultFinish(Activity.RESULT_CANCELED)
        }
        binding?.loaderBuyerOrderExtension?.hide()
        bottomSheet.show(childFragmentManager)
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

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerOrderExtensionFragment {
            return BuyerOrderExtensionFragment().apply {
                this.arguments = bundle
            }
        }
    }
}