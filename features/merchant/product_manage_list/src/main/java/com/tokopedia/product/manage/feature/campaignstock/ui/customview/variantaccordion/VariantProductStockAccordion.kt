package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedStockProductModel

class VariantProductStockAccordion @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): RecyclerView(context, attrs, defStyleAttr) {

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val mAdapter by lazy {
        VariantProductStockAccordionAdapter(mAdapterTypeFactory)
    }

    private val mAdapterTypeFactory: VariantProductStockAdapterTypeFactory by lazy {
        VariantProductStockAdapterTypeFactory { isAccordionOpened ->
            if (isAccordionOpened) {
                mAdapter.addMoreData(mVariantList)
                mAdapter.data.addAll(1, mVariantList)
                mAdapter.notifyDataSetChanged()
            } else {
                with(mAdapter) {
                    data.filterIsInstance<VariantProductStockNameUiModel>().forEach {
                        clearElement(it)
                    }
                }
            }
        }
    }

    private var mActionWording: String = ""

    private var mVariantList = listOf<VariantProductStockNameUiModel>()

    fun setEventVariantInfo(actionWording: String,
                            variantsProductList: List<ReservedStockProductModel>) {
        mActionWording = actionWording
        mVariantList = variantsProductList.map {
            VariantProductStockNameUiModel(it.productName, it.stock)
        }

        layoutManager = linearLayoutManager
        adapter = mAdapter.apply {
            addElement(VariantProductStockActionUiModel(actionWording))
        }
        setBackground()
    }

    private fun setBackground() {
        setBackgroundResource(R.drawable.bg_campaign_stock_grey_rounded)
    }

}