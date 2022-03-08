package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.createColorSampleDrawable
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.filter_general_detail_item_view_holder.view.*

internal class FilterGeneralDetailAdapter(
        private val callback: Callback
): RecyclerView.Adapter<FilterGeneralDetailAdapter.FilterGeneralDetailViewHolder>() {

    private val optionList = mutableListOf<Option>()

    fun setOptionList(optionList: List<Option>) {
        this.optionList.clear()
        this.optionList.addAll(optionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterGeneralDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_general_detail_item_view_holder, parent, false)

        return FilterGeneralDetailViewHolder(view)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: FilterGeneralDetailViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    inner class FilterGeneralDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(option: Option) {
            bindOnClickListener(option)
            bindColorSample(option)
            bindTitle(option)
            bindNewIcon(option)
            bindDescription(option)
            bindRadioState(option)
            bindCheckedState(option)
        }

        private fun bindOnClickListener(option: Option) {
            itemView.filterDetailContainer?.setOnClickListener {
                callback.onOptionClick(option, !option.isSelected(), adapterPosition)
            }
        }

        private fun bindColorSample(option: Option) {
            itemView.colorSampleImageView?.shouldShowWithAction(option.hexColor.isNotEmpty()) {
                val gradientDrawable = createColorSampleDrawable(itemView.context, option.hexColor)
                itemView.colorSampleImageView?.setImageDrawable(gradientDrawable)
            }
        }

        private fun bindNewIcon(option: Option) {
            itemView.newNotification?.showWithCondition(option.isNew)
        }

        private fun bindTitle(option: Option) {
            itemView.filterDetailTitle?.text = option.name
        }

        private fun bindDescription(option: Option) {
            itemView.filterDetailDescription?.shouldShowWithAction(option.description.isNotEmpty()) {
                itemView.filterDetailDescription?.text = option.description
            }
        }

        private fun bindRadioState(option: Option) {
            itemView.filterDetailRadio?.shouldShowWithAction(option.isTypeRadio) {
                itemView.filterDetailRadio?.isChecked = option.isSelected()
                itemView.filterDetailRadio?.setOnClickListener {
                    callback.onOptionClick(option, !option.isSelected(), adapterPosition)
                }
            }
        }

        private fun bindCheckedState(option: Option) {
            itemView.filterDetailCheckBox?.shouldShowWithAction(!option.isTypeRadio) {
                itemView.filterDetailCheckBox?.setOnCheckedChangeListener(null)
                itemView.filterDetailCheckBox?.isChecked = option.isSelected()
                itemView.filterDetailCheckBox?.setOnCheckedChangeListener { _, isChecked ->
                    callback.onOptionClick(option, isChecked, adapterPosition)
                }
            }
        }

        private fun Option.isSelected() = inputState.toBoolean()
    }

    interface Callback {
        fun onOptionClick(option: Option, isChecked: Boolean, position: Int)
    }
}