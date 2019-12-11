package com.tokopedia.digital.productV2.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.presentation.model.DigitalProductSelectDropdownData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.view_widget_product_select_dropdown_bottom_sheet.view.*
import kotlinx.android.synthetic.main.view_widget_product_select_dropdown_bottom_sheet_item.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
class DigitalProductSelectDropdownBottomSheet @JvmOverloads constructor(@NotNull context: Context,
                                                                        attrs: AttributeSet? = null,
                                                                        defStyleAttr: Int = 0,
                                                                        var listener: OnClickListener? = null)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var dropdownData: List<DigitalProductSelectDropdownData> = listOf()
    set(value) {
        field = value
        with (rv_product_select_dropdown.adapter as DigitalProductSelectDropdownAdapter) {
            items = value
            notifyDataSetChanged()
        }
    }

    init {
        View.inflate(context, getLayout(), this)

        rv_product_select_dropdown.adapter = DigitalProductSelectDropdownAdapter(dropdownData)
    }

    open fun getLayout(): Int {
        return R.layout.view_widget_product_select_dropdown_bottom_sheet
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class DigitalProductSelectDropdownAdapter(var items: List<DigitalProductSelectDropdownData>): RecyclerView.Adapter<DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalProductSelectDropdownViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_widget_product_select_dropdown_bottom_sheet_item, parent, false)
            return DigitalProductSelectDropdownViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: DigitalProductSelectDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class DigitalProductSelectDropdownViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            fun bind(element: DigitalProductSelectDropdownData) {
                with(itemView) {
                    product_select_item_title.text = element.title
                    product_select_item_price.text = element.price

                    toggleVisibility(product_select_item_label, element.label)
                    toggleVisibility(product_select_item_desc, MethodChecker.fromHtml(element.description))
                    toggleVisibility(product_select_item_original_price, element.slashedPrice)
                    product_select_item_original_price.paintFlags = product_select_item_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    product_select_item.setOnClickListener {
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
        fun onItemClicked(item: DigitalProductSelectDropdownData)
    }
}
