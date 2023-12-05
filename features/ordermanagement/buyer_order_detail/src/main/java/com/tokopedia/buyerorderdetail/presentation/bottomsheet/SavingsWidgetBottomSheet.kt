package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.databinding.BuyerOrderDetailSavingWidgetBottomSheetBinding
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailSavingWidgetDetailBinding
import com.tokopedia.buyerorderdetail.domain.models.PlusComponent
import com.tokopedia.buyerorderdetail.domain.models.PlusTotal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.getHtmlTextDarkModeSupport

class SavingsWidgetBottomSheet : BottomSheetUnify() {

    companion object {
        private const val SAVINGS_WIDGET_BOTTOM_SHEET_TAG = "SAVINGS_WIDGET_BOTTOM_SHEET_TAG"
    }

    private var binding by autoClearedNullable<BuyerOrderDetailSavingWidgetBottomSheetBinding>()
    private var component: PlusComponent? = null

    fun show(manager: FragmentManager, component: PlusComponent) {
        this.component = component
        show(manager, SAVINGS_WIDGET_BOTTOM_SHEET_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        binding = BuyerOrderDetailSavingWidgetBottomSheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle("")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { ctx ->
            binding?.root?.removeAllViews()
            renderComponents(ctx)
            renderFooter(ctx)
        }
    }

    private fun renderComponents(ctx: Context) {
        component?.plusDetailComponents?.forEach { component ->
            val bindItem = ItemBuyerOrderDetailSavingWidgetDetailBinding.inflate(layoutInflater)

            bindItem.tvLabelSavingWidget.showIfWithBlock(component.label.isNotEmpty()) {
                text = HtmlLinkHelper(
                    ctx,
                    getHtmlTextDarkModeSupport(ctx, component.label)
                ).spannedString
            }

            bindItem.tvValueSavingWidget.showIfWithBlock(component.value.isNotEmpty()) {
                text = HtmlLinkHelper(
                    ctx,
                    getHtmlTextDarkModeSupport(ctx, component.value)
                ).spannedString
            }

            bindItem.imgSavingWidgetPlus.showIfWithBlock(component.imageUrl.isNotEmpty()) {
                this.loadImage(component.imageUrl)
            }

            binding?.root?.addView(bindItem.root)
        }
    }

    private fun renderFooter(ctx: Context) {
        if (component?.plusFooter?.plusFooterTotal?.footerLabel.orEmpty().isNotEmpty() &&
            component?.plusFooter?.plusFooterTotal?.footerValue.orEmpty().isNotEmpty()
        ) {
            val bindItem = ItemBuyerOrderDetailSavingWidgetDetailBinding.inflate(layoutInflater)
            val plusFooter = component?.plusFooter?.plusFooterTotal ?: PlusTotal()

            bindItem.dividerSavingWidgetBs.show()
            bindItem.imgSavingWidgetPlus.hide()
            bindItem.tvLabelSavingWidget.showIfWithBlock(plusFooter.footerLabel.isNotEmpty()) {
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                text = HtmlLinkHelper(
                    ctx, getHtmlTextDarkModeSupport(ctx, plusFooter.footerLabel)
                ).spannedString
            }

            bindItem.tvValueSavingWidget.showIfWithBlock(plusFooter.footerValue.isNotEmpty()) {
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                text = HtmlLinkHelper(
                    ctx,
                    getHtmlTextDarkModeSupport(ctx, plusFooter.footerValue)
                ).spannedString
            }

            binding?.root?.addView(bindItem.root)
        }
    }
}