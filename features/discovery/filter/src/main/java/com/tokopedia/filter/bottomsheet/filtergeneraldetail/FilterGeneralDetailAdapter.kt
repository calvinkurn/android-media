package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.helper.createColorSampleDrawable
import com.tokopedia.filter.common.helper.isTypeRadio
import com.tokopedia.filter.databinding.FilterGeneralDetailItemViewHolderBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterGeneralDetailAdapter(
        private val callback: Callback,
): RecyclerView.Adapter<FilterGeneralDetailAdapter.FilterGeneralDetailViewHolder>() {

    private val optionList = mutableListOf<IOption>()

    fun setOptionList(optionList: List<IOption>) {
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

    override fun onBindViewHolder(holder: FilterGeneralDetailViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(optionList[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class FilterGeneralDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var binding: FilterGeneralDetailItemViewHolderBinding? by viewBinding()

        fun bind(option: IOption, payload: List<Any>? = null) {
            if (!isBindInputStateOnly(payload)) {
                bindOnClickListener(option)
                bindColorSample(option)
                bindIconOption(option)
                bindTitle(option)
                bindNewIcon(option)
                bindDescription(option)
            }
            bindRadioState(option)
            bindCheckedState(option)
        }

        private fun bindOnClickListener(option: IOption) {
            binding?.filterDetailContainer?.setOnClickListener {
                callback.onOptionClick(option, !option.isSelected(), adapterPosition)
            }
        }

        private fun bindColorSample(option: IOption) {
            val colorSampleImageView = binding?.colorSampleImageView ?: return
            colorSampleImageView.shouldShowWithAction(option.hexColor.isNotEmpty()) {
                val gradientDrawable = createColorSampleDrawable(itemView.context, option.hexColor)
                colorSampleImageView.setImageDrawable(gradientDrawable)
            }
        }

        private fun bindIconOption(option: IOption) {
            val iconOptionImageView = binding?.optionIconImageView ?: return
            iconOptionImageView.shouldShowWithAction(option.iconUrl.isNotEmpty()) {
                iconOptionImageView.loadImage(option.iconUrl)
            }
        }


        private fun bindNewIcon(option: IOption) {
            binding?.newNotification?.showWithCondition(option.isNew)
        }

        private fun bindTitle(option: IOption) {
            binding?.filterDetailTitle?.text = option.name
        }

        private fun bindDescription(option: IOption) {
            val filterDetailDescription = binding?.filterDetailDescription ?: return
            filterDetailDescription.shouldShowWithAction(option.description.isNotEmpty()) {
                filterDetailDescription.text = option.description
            }
        }

        private fun bindRadioState(option: IOption) {
            val filterDetailRadio = binding?.filterDetailRadio ?: return
            filterDetailRadio.shouldShowWithAction(option.isTypeRadio) {
                filterDetailRadio.isChecked = option.isSelected()
                filterDetailRadio.setOnClickListener {
                    callback.onOptionClick(option, !option.isSelected(), bindingAdapterPosition)
                }
            }
        }

        private fun bindCheckedState(option: IOption) {
            val filterDetailCheckBox = binding?.filterDetailCheckBox ?: return
            filterDetailCheckBox.shouldShowWithAction(!option.isTypeRadio) {
                filterDetailCheckBox.setOnCheckedChangeListener(null)
                filterDetailCheckBox.isChecked = option.isSelected()
                filterDetailCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    callback.onOptionClick(option, isChecked, bindingAdapterPosition)
                }
            }
        }

        private fun isBindInputStateOnly(payload: List<Any>?): Boolean =
            payload?.contains(Payload.BIND_INPUT_STATE_ONLY) ?: false

        private fun IOption.isSelected() = inputState.toBoolean()
    }

    interface Callback {
        fun onOptionClick(option: IOption, isChecked: Boolean, position: Int)
    }

    enum class Payload {
        BIND_INPUT_STATE_ONLY
    }
}
