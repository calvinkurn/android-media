package com.tokopedia.product.addedit.productlimitation.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.URL_PRODUCT_LIMITATION_EDU
import com.tokopedia.product.addedit.productlimitation.presentation.adapter.ProductLimitationItemAdapter
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationActionItemModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker

class ProductLimitationBottomSheet(
        private val actionItems: List<ProductLimitationActionItemModel> = emptyList(),
        private val isEligible: Boolean = false,
        private val limitAmount: Int = 0
) : BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Product Limitation Bottom Sheet"
        const val RESULT_FINISH_ACTIVITY = "result finish activity"
        const val RESULT_SAVING_DRAFT = "result saving draft"
    }

    private var onBottomSheetResult: (String) -> Unit = {}
    private var btnSubmitText: String = ""
    private var savingToDraft: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        overlayClickDismiss = true

        if (isEligible) {
            setTitle(getString(R.string.title_product_limitation_add_product_rules))
        } else {
            setTitle(getString(R.string.title_product_limitation_cant_add_product))
        }

        setCloseClickListener {
            dismiss()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDismissButton()
    }

    private fun setupDismissButton() {
        val btnSubmit = requireView().findViewById<UnifyButton>(R.id.btn_dismiss)
        btnSubmit.text = btnSubmitText
        btnSubmit.isVisible = !isEligible
        btnSubmit.setOnClickListener {
            val result = if (savingToDraft) RESULT_SAVING_DRAFT else RESULT_FINISH_ACTIVITY
            onBottomSheetResult.invoke(result)
            dismiss()
        }
    }

    private fun initChildLayout() {
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_product_limitation_bottom_sheet_content, null)
        val rvItems = contentView?.findViewById<RecyclerView>(R.id.rv_product_limitation)
        val ticker = contentView?.findViewById<Ticker>(R.id.ticker_product_limitation)
        val iconTitle = contentView?.findViewById<IconUnify>(R.id.icon_product_limitation)
        val tvTitle = contentView?.findViewById<TextView>(R.id.tv_title_product_limitation)

        rvItems?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvItems?.addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        rvItems?.adapter = ProductLimitationItemAdapter().apply {
            setData(actionItems)
            setOnItemClick {
                onBottomSheetResult.invoke(it)
            }
        }

        ticker?.apply {
            val tickerDesc: String
            if (isEligible) {
                tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerTitle = getString(R.string.title_product_limitation_bottomsheet_ticker, limitAmount)
                tickerDesc = getString(R.string.label_product_limitation_bottomsheet_ticker)
                closeButtonVisibility = GONE
            } else {
                tickerType = Ticker.TYPE_WARNING
                tickerTitle = ""
                tickerDesc = getString(R.string.label_product_limitation_bottomsheet_ticker_exceed, limitAmount)
                closeButtonVisibility = VISIBLE
            }
            setHtmlDescription(tickerDesc)
            setOnClickListener {
                onBottomSheetResult.invoke(URL_PRODUCT_LIMITATION_EDU)
            }
        }

        iconTitle?.isVisible = actionItems.isNotEmpty()
        tvTitle?.isVisible = actionItems.isNotEmpty()

        setChild(contentView)
    }

    fun setOnBottomSheetResult(onBottomSheetResult: (String) -> Unit) {
        this.onBottomSheetResult = onBottomSheetResult
    }

    fun setSubmitButtonText(text: String) {
        btnSubmitText = text
    }

    fun setIsSavingToDraft(savingToDraft: Boolean) {
        this.savingToDraft = savingToDraft
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

}