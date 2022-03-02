package com.tokopedia.vouchercreation.product.download

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.databinding.ItemCouponImageDownloadBinding

class DownloadCouponImageAdapter : RecyclerView.Adapter<DownloadCouponImageAdapter.ViewHolder>() {

    companion object {
        const val STRAIGHT_ANGLE = 180
    }

    private var items: MutableList<CouponImageUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<CouponImageUiModel>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemCouponImageDownloadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(element: CouponImageUiModel) {
            with(binding) {
                tvMvcRatio.text = element.ratioStr
                tvMvcRatioDescription.text = element.description
                cbxMvcDownloadVoucher.setOnCheckedChangeListener(null)
                cbxMvcDownloadVoucher.isChecked = element.isSelected
                cbxMvcDownloadVoucher.setOnCheckedChangeListener { _, isChecked ->
                    element.isSelected = isChecked
                    element.onCheckBoxClicked(element.imageType)
                }
                setViewExpansion(element.isExpanded)
                root.setOnClickListener {
                    handleExpandCollapseState(element)
                }

                icMvcChevron.setOnClickListener {
                    handleExpandCollapseState(element)
                    element.onChevronIconClicked(element.imageType)
                }
            }

            setupDownloadImage(element.imageType)
        }

        private fun handleExpandCollapseState(element: CouponImageUiModel) {
            val isExpanded = binding.imgMvcVoucher.isVisible
            if (!isExpanded) {
                element.onImageOpened(adapterPosition)
            }
            toggleViewExpansion(isExpanded)
            element.isExpanded = binding.imgMvcVoucher.isVisible
        }

        private fun setViewExpansion(isExpanded: Boolean) {
            binding.imgMvcVoucher.isVisible = isExpanded
            rotateChevronIcon(!isExpanded)
        }

        private fun toggleViewExpansion(voucherIsVisible: Boolean) {
            binding.imgMvcVoucher.isVisible = !voucherIsVisible
            rotateChevronIcon(voucherIsVisible)
        }

        private fun rotateChevronIcon(isExpanded: Boolean) = with(binding) {
            val angle =
                if (isExpanded) NumberConstant.ZERO else STRAIGHT_ANGLE
            icMvcChevron.rotation = angle.toFloat()
        }

        private fun setupDownloadImage(type: ImageType) {
            Glide.with(binding.imgMvcVoucher.context)
                .load(type.imageUrl)
                .into(binding.imgMvcVoucher)
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCouponImageDownloadBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun getSelection(): List<CouponImageUiModel> {
        return items
    }


}