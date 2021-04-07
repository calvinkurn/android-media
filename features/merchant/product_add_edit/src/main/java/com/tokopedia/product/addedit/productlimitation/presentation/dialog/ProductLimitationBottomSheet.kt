package com.tokopedia.product.addedit.productlimitation.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
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
    }

    private var onBottomSheetResult: (String) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        overlayClickDismiss = true
        setTitle(getString(R.string.title_product_limitation_cant_add_product))
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
        requireView().findViewById<UnifyButton>(R.id.btn_dismiss).setOnClickListener {
            dismiss()
        }
    }

    private fun initChildLayout() {
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_product_limitation_bottom_sheet_content, null)
        val rvItems = contentView?.findViewById<RecyclerView>(R.id.rv_product_limitation)
        val ticker = contentView?.findViewById<Ticker>(R.id.ticker_product_limitation)

        rvItems?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvItems?.addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        rvItems?.adapter = ProductLimitationItemAdapter().apply {
            setData(actionItems)
            setOnItemClick {
                onBottomSheetResult.invoke(it)
            }
        }

        ticker?.apply {
            if (isEligible) {
                tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerTitle = getString(R.string.title_product_limitation_bottomsheet_ticker, limitAmount)
            } else {
                tickerType = Ticker.TYPE_WARNING
                tickerTitle = ""
            }
            setHtmlDescription(getString(R.string.label_product_limitation_bottomsheet_ticker))
        }

        setChild(contentView)
    }

    fun setOnBottomSheetResult(onBottomSheetResult: (String) -> Unit) {
        this.onBottomSheetResult = onBottomSheetResult
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

}