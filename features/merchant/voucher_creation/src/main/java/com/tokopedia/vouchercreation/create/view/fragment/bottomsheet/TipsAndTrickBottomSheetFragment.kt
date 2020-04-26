package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.data.source.TipsAndTrickStaticDataSource
import com.tokopedia.vouchercreation.create.view.adapter.VoucherTipsAdapter
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView
import kotlinx.android.synthetic.main.mvc_voucher_tips_bottom_sheet_view.*

class TipsAndTrickBottomSheetFragment(bottomSheetContext: Context) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context) : TipsAndTrickBottomSheetFragment {
            return TipsAndTrickBottomSheetFragment(context).apply {
                context.run {
                    val view = View.inflate(this, R.layout.mvc_voucher_tips_bottom_sheet_view, null)
                    setChild(view)
                }
            }
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val voucherTipsAdapter by lazy {
        context?.run {
            VoucherTipsAdapter(
                    TipsAndTrickStaticDataSource.getTipsAndTrickUiModelList(),
                    ::onChevronAltered)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    private fun initView() {
        view?.setupBottomSheetChildNoMargin()
        voucherTipsRecyclerView?.run {
            layoutManager = linearLayoutManager
            adapter = voucherTipsAdapter
        }
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }


    private fun onChevronAltered(position: Int) {
    }

    override var bottomSheetViewTitle: String? = bottomSheetContext.resources?.getString(R.string.mvc_create_tips_bottomsheet_title).toBlankOrString()
}