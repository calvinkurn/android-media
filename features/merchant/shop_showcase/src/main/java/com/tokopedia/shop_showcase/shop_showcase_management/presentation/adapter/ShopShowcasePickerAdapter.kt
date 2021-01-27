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
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import kotlinx.android.synthetic.main.shop_showcase_item_picker.view.*

class ShopShowcasePickerAdapter(
        private val listener: PickerClickListener,
        private val pickerType: String
): RecyclerView.Adapter<ShopShowcasePickerAdapter.ShopShowcasePickerViewHolder>() {

    private var totalCheckedItem = 0
    private var lastSelectedRadioPosition = -1
    private var showcaseList: List<ShopEtalaseModel> = listOf()
    private var preSelectedShowcaseList: MutableList<ShowcaseItemPicker> = mutableListOf()

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

    fun updateDataSet(newList: List<ShopEtalaseModel> = listOf(), preSelectedShowcase: List<ShowcaseItemPicker>? = listOf(), totalChecked: Int = 0) {
        if(preSelectedShowcase?.size.isMoreThanZero()) {
            preSelectedShowcase?.let {
                preSelectedShowcaseList = it.toMutableList()
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
        totalCheckedItem = totalChecked
        notifyDataSetChanged()
    }

    companion object {
        const val MAX_SELECTED_SHOWCASE = 10
        private const val ENABLE_TEXT_OPACITY = 1f
        private const val DISABLE_TEXT_OPACITY = 0.3f
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

        fun bind(item: ShopEtalaseModel) {
            tvShowcaseName?.text = item.name

            if(pickerType == ShowcasePickerType.RADIO) {
                btnRadioPicker?.visible()
                itemView.setOnClickListener {
                    lastSelectedRadioPosition = adapterPosition
                    notifyDataSetChanged()
                    listener.onPickerItemClicked(item, totalCheckedItem)
                }
                btnRadioPicker?.skipAnimation()
            } else {
                btnCheckboxPicker?.visible()
                if(totalCheckedItem >= MAX_SELECTED_SHOWCASE) {
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
                    if(!item.isChecked && totalCheckedItem >= MAX_SELECTED_SHOWCASE) {
                        listener.onPickerMaxSelectedShowcase()
                    } else {
                        item.isChecked = !item.isChecked
                        btnCheckboxPicker?.isChecked = item.isChecked
                        if(item.isChecked) totalCheckedItem += 1
                        else {
                            preSelectedShowcaseList.remove(ShowcaseItemPicker(showcaseId = item.id, showcaseName = item.name))
                            totalCheckedItem -= 1
                        }
                        listener.onPickerItemClicked(item, totalCheckedItem)
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
                tvShowcaseName?.setTextColor(ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                tvShowcaseName?.alpha = DISABLE_TEXT_OPACITY
                btnCheckboxPicker?.isEnabled = false
            } else {
                tvShowcaseName?.setTextColor(androidx.core.content.ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_G900))
                tvShowcaseName?.alpha = ENABLE_TEXT_OPACITY
                btnCheckboxPicker?.isEnabled = true
            }
        }
    }

    interface PickerClickListener {
        fun onPickerItemClicked(item: ShopEtalaseModel, totalCheckedItem: Int)
        fun onPickerMaxSelectedShowcase()
    }
}