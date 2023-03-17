package com.tokopedia.rechargegeneral.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.databinding.ViewRechargeGeneralProductSelectDropdownBottomSheetItemBinding
import com.tokopedia.rechargegeneral.databinding.ViewRechargeGeneralWidgetProductSelectDropdownBottomSheetBinding
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
class RechargeGeneralProductSelectBottomSheet @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var listener: OnClickListener? = null
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: ViewRechargeGeneralWidgetProductSelectDropdownBottomSheetBinding

    var dropdownData: List<RechargeGeneralProductSelectData> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            with(binding.rvProductSelectDropdown.adapter as DigitalProductSelectDropdownAdapter) {
                items = value
                notifyDataSetChanged()
            }
        }

    init {
        binding = ViewRechargeGeneralWidgetProductSelectDropdownBottomSheetBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )
        binding.rvProductSelectDropdown.adapter = DigitalProductSelectDropdownAdapter(dropdownData)
        addView(binding.root)
    }

    open fun getLayout(): Int {
        return R.layout.view_recharge_general_widget_product_select_dropdown_bottom_sheet
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class DigitalProductSelectDropdownAdapter(var items: List<RechargeGeneralProductSelectData>) : RecyclerView.Adapter<DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalProductSelectDropdownViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recharge_general_product_select_dropdown_bottom_sheet_item, parent, false)
            return DigitalProductSelectDropdownViewHolder(
                ViewRechargeGeneralProductSelectDropdownBottomSheetItemBinding.bind(view)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: DigitalProductSelectDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class DigitalProductSelectDropdownViewHolder(
            private val binding: ViewRechargeGeneralProductSelectDropdownBottomSheetItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(element: RechargeGeneralProductSelectData) {
                with(this@DigitalProductSelectDropdownViewHolder.binding) {
                    productSelectItemTitle.text = element.title

                    toggleVisibility(productSelectItemPrice, element.price)
                    toggleVisibility(productSelectItemLabel, element.label)
                    toggleVisibility(productSelectItemDesc, MethodChecker.fromHtml(element.description))
                    toggleVisibility(productSelectItemOriginalPrice, element.slashedPrice)
                    productSelectItemOriginalPrice.paintFlags = productSelectItemOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    productSelectItem.setOnClickListener {
                        listener?.onItemClicked(element)
                    }
                }
            }

            private fun toggleVisibility(view: TextView, text: CharSequence) {
                if (text.isNotEmpty()) {
                    view.text = text
                    view.show()
                } else {
                    view.hide()
                }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: RechargeGeneralProductSelectData)
    }
}
