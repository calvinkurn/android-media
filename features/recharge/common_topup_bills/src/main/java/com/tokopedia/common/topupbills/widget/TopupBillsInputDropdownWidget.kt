package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_topup_bills_input_dropdown_bottom_sheet.view.*
import kotlinx.android.synthetic.main.view_topup_bills_input_dropdown_bottom_sheet_item.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
class TopupBillsInputDropdownWidget @JvmOverloads constructor(@NotNull context: Context,
                                                              attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0,
                                                              var listener: OnClickListener? = null,
                                                              val selected: String = "")
    : FrameLayout(context, attrs, defStyleAttr) {

    private var initialData: List<TopupBillsInputDropdownData> = listOf()
    private var displayData: List<TopupBillsInputDropdownData> = listOf()
        set(value) {
            field = value
            with(vg_input_dropdown_recycler_view.adapter as TopupBillsInputDropdownAdapter) {
                items = value
                notifyDataSetChanged()
            }
        }

    init {
        View.inflate(context, getLayout(), this)

        vg_input_dropdown_recycler_view.adapter = TopupBillsInputDropdownAdapter(displayData)

        vg_input_dropdown_search_view.searchBarTextField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    displayData = initialData.filter { item -> item.label.contains(it, true) }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        vg_input_dropdown_search_view.clearListener = {
            displayData = initialData
        }
        vg_input_dropdown_search_view.searchBarTextField.requestFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_topup_bills_input_dropdown_bottom_sheet
    }

    fun setData(data: List<TopupBillsInputDropdownData>) {
        initialData = data
        displayData = data
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class TopupBillsInputDropdownAdapter(var items: List<TopupBillsInputDropdownData>) : RecyclerView.Adapter<TopupBillsInputDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupBillsInputDropdownViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_topup_bills_input_dropdown_bottom_sheet_item, parent, false)
            return TopupBillsInputDropdownViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TopupBillsInputDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }

    }

    inner class TopupBillsInputDropdownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: TopupBillsInputDropdownData) {
            with(itemView) {
                vg_input_dropdown_label.text = element.label

                if (element.icon.isNotEmpty()) {
                    ImageHandler.LoadImage(vg_input_dropdown_icon, element.icon)
                    vg_input_dropdown_icon.show()
                } else {
                    vg_input_dropdown_icon.hide()
                }

                if (selected.isNotEmpty() && element.label == selected) {
                    vg_input_dropdown_selected.show()
                } else {
                    vg_input_dropdown_selected.hide()
                }
                vg_input_dropdown_item.setOnClickListener { listener?.onItemClicked(element) }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: TopupBillsInputDropdownData)
    }

    companion object {
        const val SHOW_KEYBOARD_DELAY: Long = 200
    }
}
