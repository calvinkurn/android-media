package com.tokopedia.buyerorder.detail.revamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerUtils
import com.tokopedia.buyerorder.databinding.LayoutScanQrCodeBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.RedeemVoucherModel
import com.tokopedia.buyerorder.detail.revamp.util.Utils
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.DELIMITERS
import com.tokopedia.buyerorder.detail.view.adapter.RedeemVoucherAdapter
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * created by @bayazidnasir on 6/9/2022
 */

class QrEventBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutScanQrCodeBinding>()
    private var actionButton: ActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {arguments ->
            actionButton = arguments.getParcelable(ACTION_BUTTON_EXTRA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutScanQrCodeBinding.inflate(LayoutInflater.from(context))
        setTitle(getString(R.string.text_redeem_voucher))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionButton?.let {actionButton ->
            setData(actionButton)
        }
    }

    private fun setData(actionButton: ActionButton) {
        binding?.let {
            var totalItem = 0
            val voucherList = mutableListOf<RedeemVoucherModel>()
            val indicatorItems = mutableListOf<ImageView>()
            if (actionButton.body.body.isNotEmpty()){
                val voucherCodes = actionButton.body.body.split(DELIMITERS)
                val seatingNumbers = actionButton.body.seatingNumbers.split(DELIMITERS)
                voucherCodes.zip(seatingNumbers).forEachIndexed { index, voucherDetail ->
                    voucherList.add(
                        RedeemVoucherModel(
                            actionButton.headerObject.powered_by,
                            actionButton.body.appURL,
                            voucherDetail.first,
                            voucherDetail.second,
                            actionButton.headerObject.statusLabel)
                    )

                    totalItem = voucherCodes.size

                    val indicator = ImageView(context).apply {
                        setPadding(
                            PADDING_5,
                            PADDING_0,
                            PADDING_5,
                            PADDING_0
                        )
                    }

                    if (index == FIRST_INDEX) {
                        indicator.setImageResource(R.drawable.ic_indicator_selected)
                    } else {
                        indicator.setImageResource(R.drawable.ic_indicator_unselected)
                    }

                    indicatorItems.add(indicator)
                    it.itemIndicator.addView(indicator)
                }

                val isVoucherCodesNotEmpty = voucherCodes.size > 1

                it.tvLabelCount.showWithCondition(isVoucherCodesNotEmpty)
                it.itemIndicator.showWithCondition(isVoucherCodesNotEmpty)
            }

            it.tvLabelCount.text = getString(R.string.deals_voucher_label_count, 1, totalItem)

            val redeemAdapter = RedeemVoucherAdapter(voucherList)
            with(it.rvVoucher){
                adapter = redeemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }

            if (it.rvVoucher.onFlingListener == null){
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(it.rvVoucher)
            }

            redeemAdapter.setOnCopiedListener { voucherCode ->
                context?.let { ctx ->
                    BuyerUtils.copyTextToClipBoard(Utils.Const.KEY_TEXT, voucherCode, ctx)
                    binding?.root?.let { v ->
                        Toaster.build(v, getString(R.string.deals_msg_copy)).show()
                    }
                }
            }

           setOnDismissListener {
                it.rvVoucher.onFlingListener = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        disableScreenCapture()
    }

    override fun onDestroy() {
        super.onDestroy()
        enableScreenCapture()
    }

    private fun enableScreenCapture() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun disableScreenCapture() {
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    companion object{
        private const val PADDING_5 = 5
        private const val PADDING_0 = 0
        private const val FIRST_INDEX = 0

        private const val ACTION_BUTTON_EXTRA = "ACTION_BUTTON_EXTRA"

        fun newInstance(actionButton: ActionButton):  QrEventBottomSheet {
            val bottomSheet = QrEventBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(ACTION_BUTTON_EXTRA, actionButton)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
