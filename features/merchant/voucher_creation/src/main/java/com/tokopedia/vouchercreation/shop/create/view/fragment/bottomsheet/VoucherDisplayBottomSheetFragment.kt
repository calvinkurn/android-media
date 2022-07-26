package com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.decorator.VoucherDisplayItemDecoration
import com.tokopedia.vouchercreation.databinding.MvcVoucherDisplayBottomSheetViewBinding
import com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertarget.VoucherDisplayAdapter
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.shop.create.view.listener.VoucherDisplayScrollListener

class VoucherDisplayBottomSheetFragment : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context?,
                           getVoucherType: () -> VoucherTargetCardType,
                           userId: String) : VoucherDisplayBottomSheetFragment {
            return VoucherDisplayBottomSheetFragment().apply {
                context?.run {
                    this@apply.getVoucherType = getVoucherType
                    this@apply.userId = userId
                }
            }
        }

        const val TAG = "VoucherDisplayBottomSheetFragment"
    }

    private var binding by autoClearedNullable<MvcVoucherDisplayBottomSheetViewBinding>()

    private val pagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    private val voucherDisplayScrollListener by lazy {
        VoucherDisplayScrollListener(pagerSnapHelper, ::onSnapPositionChangeListener)
    }

    private var getVoucherType: () -> VoucherTargetCardType = {
        VoucherTargetCardType.PRIVATE
    }

    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    override var bottomSheetViewTitle: String? = null

    private fun initBottomSheet() {
        binding = MvcVoucherDisplayBottomSheetViewBinding.inflate(LayoutInflater.from(context))
        // Setup decoration at start of instantiating to avoid increase of padding per refresh layout
        binding?.root?.setupItemDecoration()
        setChild(binding?.root)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private fun initView() {
        view?.setupBottomSheetChildNoMargin()
        val displayList = getVoucherType().displayPairList
        val targetType = getVoucherType().targetType
        val voucherDisplayAdapter = VoucherDisplayAdapter(displayList, targetType, userId)
        binding?.voucherDisplayRecyclerView?.run {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            linearLayoutManager.smoothScrollToPosition(this, null, 0)
            adapter = voucherDisplayAdapter
            addOnScrollListener(voucherDisplayScrollListener)
        }
        binding?.voucherDisplayPageControl?.setIndicator(displayList.size)
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
        binding?.voucherDisplayRecyclerView?.run {
            addItemDecoration(VoucherDisplayItemDecoration(context))
            pagerSnapHelper.attachToRecyclerView(this)
        }
    }

    private fun onSnapPositionChangeListener(position: Int) {
        binding?.voucherDisplayPageControl?.setCurrentIndicator(position)
    }
}