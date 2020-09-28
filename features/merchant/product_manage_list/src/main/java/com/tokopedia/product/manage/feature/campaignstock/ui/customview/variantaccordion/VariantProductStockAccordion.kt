package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.decoration.CampaignStockVariantDividerDecoration
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockProductModel
import kotlinx.android.synthetic.main.item_campaign_stock_variant_action.view.*
import kotlinx.android.synthetic.main.layout_campaign_stock_variant_accordion.view.*

class VariantProductStockAccordion @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val CHEVRON_CLOSED_ROTATION = 0f
        private const val CHEVRON_OPENED_ROTATION = 180f
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_campaign_stock_variant_accordion, this, false)
        addView(view)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val itemDecorator by lazy {
        CampaignStockVariantDividerDecoration(context)
    }

    private var onActionClickListener: (isAccordionOpened: Boolean) -> Unit = {}

    fun setEventVariantInfo(variantsProductList: List<ReservedStockProductModel>,
                            isAccordionOpened: Boolean) {
        layout_campaign_stock_variant_action?.run {
            setOnClickListener {
                onActionClickListener(!isAccordionOpened)
            }
        }

        rv_campaign_stock_variant_list?.run {
            adapter = VariantProductStockAccordionAdapter(variantsProductList)
            layoutManager = linearLayoutManager
            addItemDecoration(itemDecorator)
        }

        setDisplay(isAccordionOpened)

        setBackground()
    }

    fun setOnActionClickListener(clickAction: (isAccordionOpened: Boolean) -> Unit) {
        onActionClickListener = clickAction
    }

    private fun setDisplay(isAccordionOpened: Boolean) {
        setChevronRotation(isAccordionOpened)
        if (isAccordionOpened) {
            rv_campaign_stock_variant_list?.show()
        } else {
            rv_campaign_stock_variant_list?.gone()
        }
    }

    private fun setChevronRotation(isOpened: Boolean) {
        layout_campaign_stock_variant_action?.iv_campaign_stock_chevron?.rotation =
                if (isOpened) {
                    CHEVRON_OPENED_ROTATION
                } else {
                    CHEVRON_CLOSED_ROTATION
                }
    }

    private fun setBackground() {
        setBackgroundResource(R.drawable.bg_campaign_stock_grey_rounded)
    }

}