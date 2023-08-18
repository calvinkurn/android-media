package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.helper.createColorSampleDrawable
import com.tokopedia.filter.databinding.FilterGeneralDetailItemViewHolderBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterGeneralDetailAdapter(
    private val callback: Callback? = null,
): RecyclerView.Adapter<FilterGeneralDetailAdapter.FilterGeneralDetailViewHolder>() {


    private val optionList = mutableListOf<OptionFilterShort>()
    private var selectedItem: OptionFilterShort? = null

    fun setOptionList(optionList: List<OptionFilterShort>) {
        this.optionList.clear()
        this.optionList.addAll(optionList)
        notifyDataSetChanged()
    }

    fun setOptionList(optionList: List<OptionFilterShort>, selectedOption: OptionFilterShort?) {
        this.optionList.clear()
        this.optionList.addAll(optionList)
        this.selectedItem = selectedOption
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterGeneralDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_general_detail_item_view_holder, parent, false)

        return FilterGeneralDetailViewHolder(view)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: FilterGeneralDetailViewHolder, position: Int) {
        holder.bind(optionList[position], position = position)
    }

    override fun onBindViewHolder(holder: FilterGeneralDetailViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(optionList[position], payloads, position)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class FilterGeneralDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var binding: FilterGeneralDetailItemViewHolderBinding? by viewBinding()

        fun bind(option: OptionFilterShort, payload: List<Any>? = null, position: Int) {
            when (option) {
                is Option -> {
                    bindBehaviourOfFilter(option, payload)
                }
                is Sort -> {
                    bindBehaviourOfSort(option, payload, position)
                }
            }
        }

        private fun bindBehaviourOfFilter(optionOfFilter : OptionFilterShort, payload: List<Any>? = null){
            val option = optionOfFilter as Option
            if (!isBindInputStateOnly(payload)) {
                bindOnClickListener(option)
                bindColorSample(option)
                bindIconOption(option)
                bindTitle(option.name)
                bindNewIcon(option)
                bindDescription(option)
            }
            bindRadioState(option)
            bindCheckedState(option)
        }

        private fun bindBehaviourOfSort(sortItem: Sort, payload: List<Any>? = null, position: Int) {
            if (!isBindInputStateOnly(payload)) {
                bindOnClickListener(sortItem, position)
                bindTitle(sortItem.name)
                bindRadioButtonState(sortItem)
            }
            bindRadioState(sortItem)
        }

        private fun bindOnClickListener(option: Option) {
            binding?.filterDetailContainer?.setOnClickListener {
                callback?.onOptionClick(option, !option.isSelected(), adapterPosition)
            }
        }

        private fun bindOnClickListener(option: Sort, position: Int) {
            val filterDetailRadio = binding?.filterDetailRadio ?: return
            binding?.run {
                root.setOnClickListener { selectItem(option, position) }
                filterDetailRadio.setOnClickListener { selectItem(option, position) }
            }
        }

        private fun selectItem(option: Sort, position: Int) {
            callback?.onOptionClick(option, true, position)
            selectedItem = option
            notifyDataSetChanged()
        }

        private fun bindColorSample(option: Option) {
            val colorSampleImageView = binding?.colorSampleImageView ?: return
            colorSampleImageView.shouldShowWithAction(option.hexColor.isNotEmpty()) {
                val gradientDrawable = createColorSampleDrawable(itemView.context, option.hexColor)
                colorSampleImageView.setImageDrawable(gradientDrawable)
            }
        }

        private fun bindIconOption(option: Option) {
            val iconOptionImageView = binding?.optionIconImageView ?: return
            iconOptionImageView.shouldShowWithAction(option.iconUrl.isNotEmpty()) {
                iconOptionImageView.loadImage(option.iconUrl)
            }
        }

        private fun bindNewIcon(option: Option) {
            binding?.newNotification?.showWithCondition(option.isNew)
        }

        private fun bindTitle(title: String) {
            binding?.filterDetailTitle?.text = title
        }

        private fun bindDescription(option: Option) {
            val filterDetailDescription = binding?.filterDetailDescription ?: return
            filterDetailDescription.shouldShowWithAction(option.description.isNotEmpty()) {
                filterDetailDescription.text = option.description
            }
        }

        private fun bindRadioState(option: Option) {
            val filterDetailRadio = binding?.filterDetailRadio ?: return
            filterDetailRadio.shouldShowWithAction(option.isTypeRadio) {
                filterDetailRadio.isChecked = option.isSelected()
                filterDetailRadio.setOnClickListener {
                    callback?.onOptionClick(option, !option.isSelected(), adapterPosition)
                }
            }
        }

        private fun bindRadioButtonState(sortItem: OptionFilterShort) {
            val filterDetailRadio = binding?.filterDetailRadio ?: return
            filterDetailRadio.isChecked = sortItem.getValueOptions() == selectedItem?.getValueOptions()
        }

        private fun bindRadioState(sortItem: OptionFilterShort) {
            val filterDetailRadio = binding?.filterDetailRadio ?: return
            val filterDetailCheckBox = binding?.filterDetailCheckBox ?: return
            filterDetailRadio.show()
            filterDetailCheckBox.hide()
            filterDetailRadio.isChecked = sortItem.getValueOptions() == selectedItem?.getValueOptions()
        }

        private fun bindCheckedState(option: Option) {
            val filterDetailCheckBox = binding?.filterDetailCheckBox ?: return
            filterDetailCheckBox.shouldShowWithAction(!option.isTypeRadio) {
                filterDetailCheckBox.setOnCheckedChangeListener(null)
                filterDetailCheckBox.isChecked = option.isSelected()
                filterDetailCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    callback?.onOptionClick(option, isChecked, adapterPosition)
                }
            }
        }

        private fun isBindInputStateOnly(payload: List<Any>?): Boolean =
            payload?.contains(Payload.BIND_INPUT_STATE_ONLY) ?: false

        private fun Option.isSelected() = inputState.toBoolean()
    }

    interface Callback {
        fun onOptionClick(option: OptionFilterShort, isChecked: Boolean, position: Int)
    }

    enum class Payload {
        BIND_INPUT_STATE_ONLY
    }
}
