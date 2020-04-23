package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.VoucherDisplayAdapter
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView
import com.tokopedia.vouchercreation.create.view.decorator.VoucherDisplayItemDecoration
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import kotlinx.android.synthetic.main.mvc_voucher_display_bottom_sheet_view.*
import kotlinx.android.synthetic.main.mvc_voucher_display_bottom_sheet_view.view.*

class VoucherDisplayBottomSheetFragment(private val getVoucherType: () -> VoucherTargetCardType) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context?,
                        getVoucherType: () -> VoucherTargetCardType) : VoucherDisplayBottomSheetFragment {
            return VoucherDisplayBottomSheetFragment(getVoucherType).apply {
                context?.run {
                    val view = View.inflate(context, R.layout.mvc_voucher_display_bottom_sheet_view, null)
                    view?.voucherDisplayRecyclerView?.run {
                        addItemDecoration(VoucherDisplayItemDecoration(context))
                        PagerSnapHelper().attachToRecyclerView(this)
                    }
                    setChild(view)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bottomSheetViewTitle = when(getVoucherType()) {
            VoucherTargetCardType.PUBLIC -> context.resources?.getString(R.string.mvc_create_public_voucher_display_title).toBlankOrString()
            VoucherTargetCardType.PRIVATE -> context.resources?.getString(R.string.mvc_create_private_voucher_display_title).toBlankOrString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override var bottomSheetViewTitle: String? = null

    private fun initView() {
        view?.setupBottomSheetCustomLayoutParams()
        voucherDisplayRecyclerView?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = VoucherDisplayAdapter(getVoucherType().displayPairList)
        }
    }

    private fun View.setupBottomSheetCustomLayoutParams() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }
}