package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.view_vg_input_dropdown_bottom_sheet.view.*
import kotlinx.android.synthetic.main.view_vg_input_dropdown_bottom_sheet_item.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
open class TopupBillsInputDropdownBottomSheet @JvmOverloads constructor(@NotNull context: Context,
                                                                        attrs: AttributeSet? = null,
                                                                        defStyleAttr: Int = 0,
                                                                        listener: OnClickListener? = null)
    : BaseCustomView(context, attrs, defStyleAttr), SearchInputView.Listener {

    private var initialData: List<TopupBillsInputDropdownData> = listOf()
    private var displayData: List<TopupBillsInputDropdownData> = listOf()
    set(value) {
        field = value
        with (vg_input_dropdown_recycler_view.adapter as TopupBillsInputDropdownAdapter) {
            items = value
            notifyDataSetChanged()
        }
    }

    init {
        View.inflate(context, getLayout(), this)

        vg_input_dropdown_recycler_view.adapter = TopupBillsInputDropdownAdapter(displayData, listener)

        vg_input_dropdown_search_view.setListener(this)
        vg_input_dropdown_search_view.setResetListener {
            displayData = initialData
        }
        vg_input_dropdown_search_view.searchTextView.requestFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_vg_input_dropdown_bottom_sheet
    }

    fun setData(data: List<TopupBillsInputDropdownData>) {
        initialData = data
        displayData = data
    }

    override fun onSearchSubmitted(text: String?) {

    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            var filteredData = initialData.filter { item -> item.value.contains(it, true) }
            if (filteredData.isEmpty()) filteredData = listOf(TopupBillsInputDropdownData(it))
            displayData = filteredData
        }
    }

    class TopupBillsInputDropdownAdapter(var items: List<TopupBillsInputDropdownData>,
                                         private val listener: OnClickListener?): RecyclerView.Adapter<TopupBillsInputDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupBillsInputDropdownViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_vg_input_dropdown_bottom_sheet_item, parent, false)
            return TopupBillsInputDropdownViewHolder(view, listener)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TopupBillsInputDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }

    }

    class TopupBillsInputDropdownViewHolder(itemView: View, private val listener: OnClickListener?): RecyclerView.ViewHolder(itemView) {
        fun bind(element: TopupBillsInputDropdownData) {
            with(itemView) {
                vg_input_dropdown_label.text = element.value
                vg_input_dropdown_label.setOnClickListener { listener?.onItemClicked(element) }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: TopupBillsInputDropdownData)
    }
}
