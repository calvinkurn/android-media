package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.decorator.VoucherDisplayItemDecoration
import com.tokopedia.vouchercreation.create.view.adapter.vouchertarget.VoucherDisplayAdapter
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.listener.VoucherDisplayScrollListener
import kotlinx.android.synthetic.main.mvc_voucher_display_bottom_sheet_view.*
import kotlinx.android.synthetic.main.mvc_voucher_display_bottom_sheet_view.view.*

class VoucherDisplayBottomSheetFragment : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context?,
                           getVoucherType: () -> VoucherTargetCardType,
                           userId: String) : VoucherDisplayBottomSheetFragment {
            return VoucherDisplayBottomSheetFragment().apply {
                context?.run {
                    val view = View.inflate(context, R.layout.mvc_voucher_display_bottom_sheet_view, null)
                    // Setup decoration at start of instantiating to avoid increase of padding per refresh layout
                    view?.setupItemDecoration()
                    setChild(view)
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                    this@apply.getVoucherType = getVoucherType
                    this@apply.userId = userId
                }
            }
        }

        const val TAG = "VoucherDisplayBottomSheetFragment"
    }

    private val pagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    private val voucherDisplayScrollListener by lazy {
        VoucherDisplayScrollListener(pagerSnapHelper, ::onSnapPositionChangeListener)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private var getVoucherType: () -> VoucherTargetCardType = {
        VoucherTargetCardType.PRIVATE
    }

    private var userId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    override var bottomSheetViewTitle: String? = null

    private fun initView() {
        view?.setupBottomSheetChildNoMargin()
        val displayList = getVoucherType().displayPairList
        val targetType = getVoucherType().targetType
        val voucherDisplayAdapter = VoucherDisplayAdapter(displayList, targetType, userId)
        voucherDisplayRecyclerView?.run {
            layoutManager = linearLayoutManager
            linearLayoutManager.smoothScrollToPosition(this, null, 0)
            adapter = voucherDisplayAdapter
            addOnScrollListener(voucherDisplayScrollListener)
        }
        voucherDisplayPageControl?.setIndicator(displayList.size)
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }

    private fun View.setupItemDecoration() {
        voucherDisplayRecyclerView?.run {
            addItemDecoration(VoucherDisplayItemDecoration(context))
            pagerSnapHelper.attachToRecyclerView(this)
        }
    }

    private fun onSnapPositionChangeListener(position: Int) {
        view?.voucherDisplayPageControl?.setCurrentIndicator(position)
    }
}