package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.WidgetBrcCsatBinding
import com.tokopedia.buyerorderdetail.databinding.WidgetBrcCsatContentBinding
import com.tokopedia.buyerorderdetail.presentation.model.WidgetBrcCsatUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.WidgetBrcCsatUiState
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster

class WidgetBrcCsat(
    context: Context,
    attrs: AttributeSet?
) : BaseCustomView(context, attrs) {

    private val binding = WidgetBrcCsatBinding.inflate(LayoutInflater.from(context), this, true)
    private val contentBinding by lazyThreadSafetyNone {
        WidgetBrcCsatContentBinding.inflate(LayoutInflater.from(context), null, false)
    }
    private val contentData by lazyThreadSafetyNone {
        AccordionDataUnify(
            title = context.getString(R.string.bom_brc_csat_title),
            expandableView = contentBinding.root,
            isExpanded = true
        ).apply { setContentPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO) }
    }

    private var _navigator: BuyerOrderDetailNavigator? = null

    fun setup(navigator: BuyerOrderDetailNavigator) {
        _navigator = navigator
    }

    fun setup(uiState: WidgetBrcCsatUiState) {
        when (uiState) {
            is WidgetBrcCsatUiState.HasData.Reloading -> onReloading(uiState.data)
            is WidgetBrcCsatUiState.HasData.Showing -> onShowing(uiState.data)
            is WidgetBrcCsatUiState.Hidden -> onHidden()
            is WidgetBrcCsatUiState.Loading -> onLoading()
        }
    }

    private fun onReloading(data: WidgetBrcCsatUiModel) {
        show()
        with(binding.accordionBomBrcCsat) {
            contentBinding.setup()
            if (!accordionData.contains(contentData)) addGroup(contentData)
            if (data.expanded) expandAllGroup() else collapseAllGroup()
        }
    }

    private fun onShowing(data: WidgetBrcCsatUiModel) {
        show()
        with(binding.accordionBomBrcCsat) {
            contentBinding.setup()
            if (!accordionData.contains(contentData)) addGroup(contentData)
            if (data.expanded) expandAllGroup() else collapseAllGroup()
        }
    }

    private fun onHidden() {
        gone()
    }

    private fun onLoading() {
        gone()
    }

    private fun WidgetBrcCsatContentBinding.setup() {
        setupSmileys()
        setupHelpText()
    }

    private fun WidgetBrcCsatContentBinding.setupSmileys() {
        ivBomBrcCsatSmiley1.setOnClickListener {
            onSmileyClicked(1)
        }
        ivBomBrcCsatSmiley2.setOnClickListener {
            onSmileyClicked(2)
        }
        ivBomBrcCsatSmiley3.setOnClickListener {
            onSmileyClicked(3)
        }
        ivBomBrcCsatSmiley4.setOnClickListener {
            onSmileyClicked(4)
        }
        ivBomBrcCsatSmiley5.setOnClickListener {
            onSmileyClicked(5)
        }
    }

    private fun WidgetBrcCsatContentBinding.onSmileyClicked(smileyPosition: Int) {
        Toaster.build(this@WidgetBrcCsat, "Clicked on smiley $smileyPosition").show()
        ivBomBrcCsatSmiley1.isEnabled = false
        ivBomBrcCsatSmiley2.isEnabled = false
        ivBomBrcCsatSmiley3.isEnabled = false
        ivBomBrcCsatSmiley4.isEnabled = false
        ivBomBrcCsatSmiley5.isEnabled = false
    }

    private fun WidgetBrcCsatContentBinding.setupHelpText() {
        tvBomBrcCsatHelp.movementMethod = LinkMovementMethod.getInstance()
        tvBomBrcCsatHelp.text = HtmlLinkHelper(
            context, context.getString(R.string.bom_brc_csat_help)
        ).apply {
            urlList.forEach { it.onClick = { _navigator?.openAppLink(it.linkUrl, false) } }
        }.spannedString ?: String.EMPTY
    }
}
