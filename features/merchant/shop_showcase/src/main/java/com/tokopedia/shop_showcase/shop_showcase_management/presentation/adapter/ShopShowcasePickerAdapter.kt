package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
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
    private var showcaseList: List<ShowcaseItem> = listOf()
    private var preSelectedShowcaseList: List<ShowcaseItemPicker> = listOf()

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
    }

    fun updateDataSet(newList: List<ShowcaseItem> = listOf(), preSelectedShowcase: List<ShowcaseItemPicker>? = listOf()) {
        if(preSelectedShowcase?.size.isMoreThanZero()) {
            preSelectedShowcase?.let {
                preSelectedShowcaseList = it
            }
        } else {
            showcaseList = newList
            showcaseList.forEach { list ->
                preSelectedShowcaseList.forEach { preList ->
                    if(preList.showcaseId == list.id) {
                        list.isChecked = true
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun getCheckedItem(): Int {
        showcaseList.filter {
            it.isChecked
        }.let { filteredList ->
            return filteredList.size
        }
    }

    inner class ShopShowcasePickerViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val btnRadioPicker: RadioButtonUnify? by lazy {
            itemView.btn_radio_showcase_picker
        }
        private val btnCheckboxPicker: CheckboxUnify? by lazy {
            itemView.btn_checkbox_showcase_picker
        }
        private val tvShowcaseName: TextView? by lazy {
            itemView.tv_showcase_name
        }
        private val enableTextOpacity = 1f
        private val disableTextOpacity = 0.3f
        private val maxSelectedShowcase = 10

        fun bind(item: ShowcaseItem) {
            tvShowcaseName?.text = item.name

            if(pickerType == ShowcasePickerType.RADIO) {
                btnRadioPicker?.visible()
                itemView.setOnClickListener {
                    lastSelectedRadioPosition = adapterPosition
                    notifyDataSetChanged()
                    listener.onPickerItemClicked(item)
                }
                btnRadioPicker?.skipAnimation()
            } else {
                btnCheckboxPicker?.visible()
                if(getCheckedItem() >= maxSelectedShowcase) {
                    if(item.isChecked) {
                        setDisableState(itemView.context, false)
                    } else {
                        setDisableState(itemView.context, true)
                    }
                } else {
                    setDisableState(itemView.context, false)
                }
                btnCheckboxPicker?.isChecked = item.isChecked
                itemView.setOnClickListener {
                    if(!item.isChecked && getCheckedItem() >= maxSelectedShowcase) {
                        listener.onPickerMaxSelectedShowcase()
                    } else {
                        item.isChecked = !item.isChecked
                        btnCheckboxPicker?.isChecked = item.isChecked
                        listener.onPickerItemClicked(item)
                        notifyDataSetChanged()
                    }
                }
                btnCheckboxPicker?.run {
                    if(isEnabled)
                        skipAnimation()
                }
            }
        }

        private fun setDisableState(ctx: Context, state: Boolean) {
            if(state) {
                tvShowcaseName?.setTextColor(ContextCompat.getColor(ctx, R.color.showcase_picker_disable_color))
                tvShowcaseName?.alpha = disableTextOpacity
                btnCheckboxPicker?.isEnabled = false
            } else {
                tvShowcaseName?.setTextColor(Color.BLACK)
                tvShowcaseName?.alpha = enableTextOpacity
                btnCheckboxPicker?.isEnabled = true
            }
        }
    }

    interface PickerClickListener {
        fun onPickerItemClicked(item: ShowcaseItem)
        fun onPickerMaxSelectedShowcase()
    }
}