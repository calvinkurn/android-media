package com.tokopedia.buyerorder.detail.revamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerUtils
import com.tokopedia.buyerorder.databinding.LayoutScanQrCodeBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.RedeemVoucherModel
import com.tokopedia.buyerorder.detail.view.adapter.RedeemVoucherAdapter
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * created by @bayazidnasir on 25/8/2022
 */

class EventsQRBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutScanQrCodeBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutScanQrCodeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(getString(R.string.text_redeem_voucher))
        setChild(binding?.root)

        setCloseClickListener {
            dismiss()
        }
    }

    fun renderView(actionButton: ActionButton){
        binding?.let {
            var totalItem = 0
            val voucherList = mutableListOf<RedeemVoucherModel>()
            val indicatorItems = mutableListOf<ImageView>()

            if (actionButton.body.body.isNotEmpty()){
                val voucherCodes = actionButton.body.body.split(DELIMITERS)
                voucherCodes.forEachIndexed { index, code ->
                    voucherList.add(RedeemVoucherModel(
                        actionButton.headerObject.powered_by,
                        actionButton.body.appURL,
                        code,
                        actionButton.headerObject.statusLabel))

                    totalItem = voucherCodes.size

                    val indicator = ImageView(context).apply {
                        setPadding(PADDING_5, PADDING_0, PADDING_5, PADDING_0)
                    }

                    if (index == 0) {
                        indicator.setImageResource(R.drawable.ic_indicator_selected)
                    } else {
                        indicator.setImageResource(R.drawable.ic_indicator_unselected)
                    }

                    indicatorItems.add(indicator)
                    it.itemIndicator.addView(indicator)
                }

                it.tvLabelCount.showWithCondition(voucherCodes.size > 1)
                it.itemIndicator.showWithCondition(voucherCodes.size > 1)
            }

            it.tvLabelCount.text = getString(R.string.deals_voucher_label_count, 1, totalItem)

            val redeemAdapter = RedeemVoucherAdapter(voucherList)
            with(it.rvVoucher){
                adapter = redeemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                addOnScrollListener(object : CustomScrollListener(){
                    override fun onCurrentPosition(currentPosition: Int) {
                        indicatorItems.forEachIndexed { index, indicator ->
                            if (currentPosition != index) {
                                indicator.setImageResource(R.drawable.ic_indicator_unselected)
                            } else {
                                indicator.setImageResource(R.drawable.ic_indicator_selected)
                            }
                        }
                    }

                    override fun onFirstVisible(position: Int) {
                        it.tvLabelCount.text = getString(R.string.deals_voucher_label_count, position, totalItem)
                    }
                })
            }

            if (it.rvVoucher.onFlingListener == null){
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(it.rvVoucher)
            }

            redeemAdapter.setOnCopiedListener { voucherCode ->
                context?.let { ctx ->
                    BuyerUtils.copyTextToClipBoard(KEY_TEXT, voucherCode, ctx)
                }
                view?.let { v ->
                    Toaster.build(v, getString(R.string.deals_msg_copy)).show()
                }
            }

            setOnDismissListener {
                it.rvVoucher.onFlingListener = null
            }
        }
    }

    fun show(fragmentManager: FragmentManager){
        show(fragmentManager, TAG)
    }

    abstract inner class CustomScrollListener: RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val currentPos = layoutManager.findFirstCompletelyVisibleItemPosition()

            if (currentPos != NO_POSITION) recyclerView.smoothScrollToPosition(currentPos)

            onCurrentPosition(currentPos)
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition() + 1
            onFirstVisible(firstVisiblePosition)
        }

        abstract fun onCurrentPosition(currentPosition: Int)

        abstract fun onFirstVisible(position: Int)
    }

    private companion object{
        const val DELIMITERS = ","
        const val PADDING_5 = 5
        const val PADDING_0 = 0
        const val NO_POSITION = -1
        const val KEY_TEXT = "text"
        const val TAG = "events_qr_bottom_sheet"
    }
}