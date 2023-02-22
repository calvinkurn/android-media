package com.tokopedia.mvc.presentation.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.databinding.SmvcItemVoucherImageBinding
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiModel

class DownloadVoucherImageAdapter : RecyclerView.Adapter<DownloadVoucherImageAdapter.ViewHolder>() {

    private var onCheckboxClick: (Int, Boolean) -> Unit = { _, _ -> }
    private var onDropdownClick: (Int) -> Unit = { _ -> }

    private val differCallback = object : DiffUtil.ItemCallback<VoucherImageUiModel>() {
        override fun areItemsTheSame(oldItem: VoucherImageUiModel, newItem: VoucherImageUiModel): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: VoucherImageUiModel, newItem: VoucherImageUiModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemVoucherImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun setOnCheckboxClick(onCheckboxClick: (Int, Boolean) -> Unit) {
        this.onCheckboxClick = onCheckboxClick
    }
    fun setOnDropdownClick(onDropdownClick: (Int) -> Unit) {
        this.onDropdownClick = onDropdownClick
    }


    inner class ViewHolder(
        private val binding: SmvcItemVoucherImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconDropdown.setOnClickListener { onDropdownClick(bindingAdapterPosition) }
            binding.root.setOnClickListener { onDropdownClick(bindingAdapterPosition) }
        }

        fun bind(voucherImage: VoucherImageUiModel) {
            with(binding) {
                checkbox.setOnCheckedChangeListener(null)

                imgVoucher.loadImage(voucherImage.imageUrl)
                imgVoucher.isVisible = voucherImage.isExpanded

                val iconResourceId = if (voucherImage.isExpanded) IconUnify.CHEVRON_UP else IconUnify.CHEVRON_DOWN
                iconDropdown.setImage(iconResourceId)

                tpgImageRatio.text = voucherImage.ratio
                tpgDescription.text = voucherImage.description

                checkbox.isChecked = voucherImage.isSelected
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckboxClick(bindingAdapterPosition, isChecked)
                }

            }
        }

    }

    fun submit(newVariants: List<VoucherImageUiModel>) {
        differ.submitList(newVariants)
    }

    fun snapshot(): List<VoucherImageUiModel> {
        return differ.currentList
    }


}
