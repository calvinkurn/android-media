package com.tokopedia.tkpd.flashsale.presentation.avp.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantItemBinding
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.campaign.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.tkpd.flashsale.presentation.avp.adapter.item.ManageProductVariantItem
import com.tokopedia.unifycomponents.TextFieldUnify2
import timber.log.Timber
import java.lang.Math.floor
import java.text.DecimalFormat
import java.text.NumberFormat

class ManageProductVariantDelegateAdapter(
    private val onToggleSwitched: (Int, Boolean) -> Unit,
    private val onDiscountAmountChanged: (Int, Long) -> Unit
) :
    DelegateAdapter<ManageProductVariantItem, ManageProductVariantDelegateAdapter.ViewHolder>(
        ManageProductVariantItem::class.java
    ) {

    companion object {
        private const val NUMBER_PATTERN = "#,###,###"
    }

    var isForceTextChanged = false

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutCampaignManageProductDetailVariantItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: ManageProductVariantItem,
        viewHolder: ManageProductVariantDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: LayoutCampaignManageProductDetailVariantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ManageProductVariantItem) {
            binding.run {
                containerLayoutProductParent.run {
                    textParentTitle.text = item.name
                    textParentOriginalPrice.text = item.price.price.toLong().getCurrencyFormatted()
                    textParentErrorMessage.gone()
                    textParentTotalStock.text = "${item.stock} produk"
                    switcherToggleParent.run {
                        isChecked = item.isToggleOn
                        setOnCheckedChangeListener { _, isChecked ->
                            onToggleSwitched(adapterPosition, isChecked)
                        }
                    }
                }
                containerProductChild.isVisible = item.isToggleOn
                containerLayoutProductInformation.run {
                    periodSection.gone()
                    textFieldPriceDiscountNominal.apply {
                        setMessage(getPriceRange(item))
                        setDiscountAmountListener(textFieldPriceDiscountPercentage, item)
                    }
                    textFieldPriceDiscountPercentage.apply {
                        appendText("%")
                    }
                    quantityEditor.setValue(item.stock)
                    textQuantityEditorSubTitle.text = "stock wajib ${item.stock}"
                }
            }
        }

        private fun TextFieldUnify2.setDiscountAmountListener(
            discountPercentageView: TextFieldUnify2,
            item: ManageProductVariantItem
        ) {
            this.textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val number = s.toString().digitsOnly()
                    val discountedPrice =
                        kotlin.math.floor((number / item.price.price.toDouble()) * 100)
                    discountPercentageView.textInputLayout.editText?.setText(discountedPrice.toString())
                    onDiscountAmountChanged(adapterPosition, number)
                }
            })
        }

        private fun getPriceRange(item: ManageProductVariantItem): String {
            return "${
                item.price.lowerPrice.toLong().getCurrencyFormatted()
            } - ${item.price.upperPrice.toLong().getCurrencyFormatted()}"
        }
    }
}