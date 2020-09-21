package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import kotlinx.android.synthetic.main.shop_showcase_item_picker.view.*

class ShopShowcasePickerAdapter(
        private val listener: PickerClickListener,
        private val pickerType: String
): RecyclerView.Adapter<ShopShowcasePickerAdapter.ShopShowcasePickerViewHolder>() {

    private var lastSelectedRadioPosition = 0
    private var lastSelectedCheckboxPosition = -1
    private var showcaseList: List<ShowcaseItem> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShowcasePickerViewHolder {
        return ShopShowcasePickerViewHolder(
                parent.inflateLayout(R.layout.shop_showcase_item_picker)
        )
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ShopShowcasePickerViewHolder, position: Int) {
        holder.bind(showcaseList[position])
        if(pickerType == ShowcasePickerType.RADIO)
            holder.btnRadioPicker?.isChecked = (lastSelectedRadioPosition == position)
        else
            holder.btnCheckboxPicker?.isChecked = (lastSelectedCheckboxPosition == position)
    }

    fun updateDataSet(newList: List<ShowcaseItem>) {
        showcaseList = newList
        notifyDataSetChanged()
    }

    inner class ShopShowcasePickerViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val btnRadioPicker: RadioButtonUnify? by lazy {
            itemView.btn_radio_showcase_picker
        }
        val btnCheckboxPicker: CheckboxUnify? by lazy {
            itemView.btn_checkbox_showcase_picker
        }
        private val tvShowcaseName: TextView? by lazy {
            itemView.tv_showcase_name
        }

        fun bind(item: ShowcaseItem) {
            tvShowcaseName?.text = item.name

            if(pickerType == ShowcasePickerType.RADIO) {
                btnRadioPicker?.visible()
                itemView.setOnClickListener {
                    lastSelectedRadioPosition = adapterPosition
                    notifyDataSetChanged()
                    listener.onPickerItemClicked(item)
                }
            } else {
                btnCheckboxPicker?.visible()
                itemView.setOnClickListener {
                    item.isHighlight = !item.isHighlight
                    btnCheckboxPicker?.isChecked = item.isHighlight
                    listener.onPickerItemClicked(item)
                }
            }
        }
    }

    interface PickerClickListener {
        fun onPickerItemClicked(item: ShowcaseItem)
    }
}