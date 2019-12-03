package com.tokopedia.vouchergame.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.data.VoucherGameEnquiryFields
import kotlinx.android.synthetic.main.view_vg_input_dropdown_bottom_sheet.view.*
import kotlinx.android.synthetic.main.view_vg_input_dropdown_bottom_sheet_item.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
open class VoucherGameInputDropdownBottomSheet @JvmOverloads constructor(@NotNull context: Context,
                                                                         attrs: AttributeSet? = null,
                                                                         defStyleAttr: Int = 0,
                                                                         listener: OnClickListener? = null)
    : BaseCustomView(context, attrs, defStyleAttr), SearchInputView.Listener {

    private var initialData: List<VoucherGameEnquiryFields.DataCollection> = listOf()
    private var displayData: List<VoucherGameEnquiryFields.DataCollection> = listOf()
    set(value) {
        field = value
        with (vg_input_dropdown_recycler_view.adapter as VoucherGameInputDropdownAdapter) {
            items = value
            notifyDataSetChanged()
        }
    }

    init {
        View.inflate(context, getLayout(), this)

        vg_input_dropdown_recycler_view.adapter = VoucherGameInputDropdownAdapter(displayData, listener)

        vg_input_dropdown_search_view.setListener(this)
        vg_input_dropdown_search_view.setResetListener {
            displayData = initialData
        }
        vg_input_dropdown_search_view.searchTextView.requestFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_vg_input_dropdown_bottom_sheet
    }

    fun setData(data: List<VoucherGameEnquiryFields.DataCollection>) {
        initialData = data
        displayData = data
    }

    override fun onSearchSubmitted(text: String?) {

    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            var filteredData = initialData.filter { item -> item.value.contains(it, true) }
            if (filteredData.isEmpty()) filteredData = listOf(VoucherGameEnquiryFields.DataCollection(it))
            displayData = filteredData
        }
    }

    class VoucherGameInputDropdownAdapter(var items: List<VoucherGameEnquiryFields.DataCollection>,
                                          private val listener: OnClickListener?): RecyclerView.Adapter<VoucherGameInputDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherGameInputDropdownViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_vg_input_dropdown_bottom_sheet_item, parent, false)
            return VoucherGameInputDropdownViewHolder(view, listener)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: VoucherGameInputDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }

    }

    class VoucherGameInputDropdownViewHolder(itemView: View, private val listener: OnClickListener?): RecyclerView.ViewHolder(itemView) {
        fun bind(element: VoucherGameEnquiryFields.DataCollection) {
            with(itemView) {
                vg_input_dropdown_label.text = element.value
                vg_input_dropdown_label.setOnClickListener { listener?.onItemClicked(element) }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: VoucherGameEnquiryFields.DataCollection)
    }
}
