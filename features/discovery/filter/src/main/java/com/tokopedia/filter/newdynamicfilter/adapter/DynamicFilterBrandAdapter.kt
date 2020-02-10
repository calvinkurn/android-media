package com.tokopedia.filter.newdynamicfilter.adapter

import android.view.View
import android.widget.CheckBox

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.OptionWrapper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

class DynamicFilterBrandAdapter(private val filterDetailView: DynamicFilterDetailView) : SectionDividedItemAdapter<OptionWrapper>() {

    private val sectionTitleDictionary = SectionTitleDictionary()

    override fun getItemLayout(): Int {
        return R.layout.filter_detail_item
    }

    fun setOptionList(optionList: List<Option>) {
        setItemList(wrapOptionList(optionList))
    }

    private fun wrapOptionList(optionList: List<Option>): List<OptionWrapper> {
        val optionWrapperList = arrayListOf<OptionWrapper>()
        for (option in optionList) {
            optionWrapperList.add(OptionWrapper(option))
        }
        return optionWrapperList
    }

    fun resetAllOptionsInputState() {
        for (item in itemList) {
            item.option.inputState = ""
        }
        notifyDataSetChanged()
    }

    override fun getViewHolder(itemView: View): ViewHolder<OptionWrapper> {
        return BrandViewHolder(itemView, filterDetailView, sectionTitleDictionary)
    }

    private class BrandViewHolder internal constructor(itemView: View, private val filterDetailView: DynamicFilterDetailView,
                                                       sectionTitleDictionary: SectionTitleDictionary) : SectionDividedItemAdapter.ViewHolder<OptionWrapper>(itemView, sectionTitleDictionary) {
        var checkBox: CheckBox? = null

        override fun initItem(itemView: View) {
            checkBox = itemView.findViewById(R.id.filter_detail_item_checkbox)
        }

        override fun bindItem(item: OptionWrapper) {
            itemView.setOnClickListener { checkBox?.isChecked = checkBox?.isChecked != true }
            checkBox?.let { OptionHelper.bindOptionWithCheckbox(item.option, it, filterDetailView) }
            checkBox?.text = item.option.name
        }
    }
}
