package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.data.source.TipsAndTrickStaticDataSource
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTipsItemAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTipsItemTypeFactory
import kotlinx.android.synthetic.main.mvc_voucher_bottom_sheet_view.*

class GeneralExpensesInfoBottomSheetFragment : BottomSheetUnify(), VoucherBottomView {

    companion object {
        @JvmStatic
        fun createInstance(context: Context?) : GeneralExpensesInfoBottomSheetFragment {
            return GeneralExpensesInfoBottomSheetFragment().apply {
                context.run {
                    val view = View.inflate(this, R.layout.mvc_voucher_bottom_sheet_view, null)
                    setChild(view)
                    setTitle(context?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                    bottomSheetViewTitle = context?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString()
                }
            }
        }

        val TAG = "GeneralExpensesInfoBottomSheet"
    }

    override var bottomSheetViewTitle: String? = null

    private val adapterTypeFactory by lazy {
        VoucherTipsItemAdapterTypeFactory()
    }

    private val bottomSheetAdapter by lazy {
        BaseListAdapter<Visitable<VoucherTipsItemTypeFactory>, VoucherTipsItemAdapterTypeFactory>(adapterTypeFactory)
    }

    private val adapterLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val uiModelList by lazy {
        TipsAndTrickStaticDataSource.getGeneralExpenseUiModelList()
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
            adapter = bottomSheetAdapter
            layoutManager = adapterLayoutManager
        }
        bottomSheetAdapter.setVisitables(uiModelList)
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }
}