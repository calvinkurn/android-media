package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtension
import com.tokopedia.buyerorderdetail.databinding.OrderExtensionSubmissionExtendsBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionDialog
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.BottomSheetUnify

class SubmissionOrderExtensionBottomSheet : BottomSheetUnify() {

    private var binding: OrderExtensionSubmissionExtendsBottomsheetBinding? = null

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
        setupTextView()
        setupCta()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupTextView() = binding?.run {
        val respondInfo = getRespondInfo().value
        tvSubmissionExtendsTitle.text = respondInfo?.confirmationTitle.orEmpty()
        tvSubmissionExtendsReason.text = respondInfo?.reasonExtension.orEmpty()
    }

    private fun setupCta() = binding?.run {
        btnOrderCancelled.setOnClickListener {
            showConfirmedCancelledOrderDialog()
        }
        btnSubmissionExtends.setOnClickListener {

        }
    }

    private fun showConfirmedCancelledOrderDialog() {
        val respondInfo = getRespondInfo().value
        val nn950Color = com.tokopedia.unifyprinciples.R.color.Unify_NN950.toString()
        val confirmedCancelledOrderDialog = context?.let {
            OrderExtensionDialog(
                it,
                DialogUnify.WITH_ILLUSTRATION
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
                setImageUrl(BuyerOrderDetailOrderExtension.Image.CONFIRMED_CANCELLED_ORDER_URL)
                setDialogSecondaryCta()
            }
        }
        confirmedCancelledOrderDialog?.getDialog()?.run {
            setPrimaryCTAClickListener {

            }
            setSecondaryCTAClickListener {

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
    }

    companion object {
        const val TAG = "SubmissionOrderExtensionBottomSheet"

        const val KEY_ITEM_RESPOND_INFO = "key_item_respond_info"
        private const val KEY_CACHE_MANAGER_ID = "extra_cache_manager_id"

        fun newInstance(cacheManagerId: String): SubmissionOrderExtensionBottomSheet {
            return SubmissionOrderExtensionBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
                arguments = bundle
            }
        }
    }
}