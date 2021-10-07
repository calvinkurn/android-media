package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.LayoutCampaignStockVariantAccordionBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.decoration.CampaignStockVariantDividerDecoration
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockProductModel

class VariantProductStockAccordion @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val CHEVRON_CLOSED_ROTATION = 0f
        private const val CHEVRON_OPENED_ROTATION = 180f
    }

    private var binding: LayoutCampaignStockVariantAccordionBinding? = null

    init {
        binding = LayoutCampaignStockVariantAccordionBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
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
        binding?.layoutCampaignStockVariantAction?.run {
            setOnClickListener {
                onActionClickListener(!isAccordionOpened)
            }
        }

        binding?.rvCampaignStockVariantList?.run {
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
            binding?.rvCampaignStockVariantList?.show()
        } else {
            binding?.rvCampaignStockVariantList?.gone()
        }
    }

    private fun setChevronRotation(isOpened: Boolean) {
        binding?.layoutCampaignStockVariantAction?.ivCampaignStockChevron?.rotation =
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