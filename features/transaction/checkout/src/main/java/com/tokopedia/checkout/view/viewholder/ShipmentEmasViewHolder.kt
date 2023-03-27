package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.CheckoutHolderItemEmasBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.util.*

class ShipmentEmasViewHolder(private val binding: CheckoutHolderItemEmasBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("NewApi")
    fun bindViewHolder(egoldAttributeModel: EgoldAttributeModel) {
        binding.llContainer.setOnClickListener {
            if (egoldAttributeModel.isEnabled) {
                binding.cbEmas.isChecked = !binding.cbEmas.isChecked
            }
        }
        binding.cbEmas.isChecked = egoldAttributeModel.isChecked
        binding.tvEmasTitle.text = egoldAttributeModel.titleText
        binding.imgEmasInfo.setOnClickListener {
            if (egoldAttributeModel.isEnabled) {
                showBottomSheet(egoldAttributeModel)
            }
        }
        binding.tvEmasSubTitle.text = MethodChecker.fromHtml(
            String.format(
                binding.root.context.getString(R.string.emas_checkout_desc),
                egoldAttributeModel.subText,
                CurrencyFormatUtil.convertPriceValueToIdrFormat(egoldAttributeModel.buyEgoldValue, false).removeDecimalSuffix()
            )
        )
        binding.cbEmas.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (egoldAttributeModel.isEnabled) {
                shipmentAdapterActionListener.onEgoldChecked(isChecked)
            }
        }

        if (egoldAttributeModel.isShowHyperlink) {
            binding.tvEmasHyperlink.text = String.format(Locale.getDefault(), "(${egoldAttributeModel.hyperlinkText})")
            binding.tvEmasHyperlink.setOnClickListener {
                RouteManager.route(
                    itemView.context,
                    String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, egoldAttributeModel.hyperlinkUrl)
                )
            }
            binding.tvEmasHyperlink.visible()
        } else {
            binding.tvEmasHyperlink.gone()
        }

        if (egoldAttributeModel.isEnabled) {
            binding.cbEmas.isEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.llContainer.foreground = ContextCompat.getDrawable(binding.root.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
            }
        } else {
            binding.cbEmas.isEnabled = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.llContainer.foreground = ContextCompat.getDrawable(binding.root.context, com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item)
            }
        }
    }

    private fun showBottomSheet(egoldAttributeModel: EgoldAttributeModel) {
        GeneralBottomSheet().apply {
            setTitle(egoldAttributeModel.titleText ?: "")
            setDesc(egoldAttributeModel.tooltipText ?: "")
            setButtonText(binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(binding.root.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_EMAS = R.layout.checkout_holder_item_emas
    }
}
