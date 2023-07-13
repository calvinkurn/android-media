package com.tokopedia.promousage.view.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.promousage.databinding.PromoCheckoutItemVoucherSectionBinding

class VoucherSectionAdapter : RecyclerView.Adapter<VoucherSectionAdapter.ViewHolder>() {
    
    private val differCallback = object : DiffUtil.ItemCallback<VoucherSection>() {
        override fun areItemsTheSame(
            oldItem: VoucherSection,
            newItem: VoucherSection
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: VoucherSection,
            newItem: VoucherSection
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    private var onSectionClick: (Int) -> Unit = {}
    private var onVoucherClick: (Voucher) -> Unit = {}
    private var onViewAllVoucherClick: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PromoCheckoutItemVoucherSectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder(private val binding: PromoCheckoutItemVoucherSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
           binding.root.setOnClickListener { onSectionClick(bindingAdapterPosition) }
        }

        fun bind(section: VoucherSection) {
            bindChevron(section)
            bindVouchers(section, onVoucherClick, onViewAllVoucherClick)
        }

        private fun bindChevron(section: VoucherSection) {
            binding.tpgSectionTitle.text = section.title
            val iconDrawable = if (section.isExpanded) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            }

            binding.iconChevron.setImage(iconDrawable)

        }

        private fun bindVouchers(
            section: VoucherSection,
            onVoucherClick: (Voucher) -> Unit,
            onViewAllVoucherClick: (Int) -> Unit
        ) {
            val voucherAdapter = CompositeAdapter.Builder()
                .add(VoucherDelegateAdapter(onVoucherClick))
                .add(ViewAllVoucherDelegateAdapter(bindingAdapterPosition, onViewAllVoucherClick))
                .build()

            binding.childRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.childRecyclerView.context)
                adapter = voucherAdapter
            }

            binding.childRecyclerView.isVisible = section.isExpanded
            voucherAdapter.submit(section.vouchers)
        }

    }


    fun setOnSectionClick(onSectionClick: (Int) -> Unit) {
        this.onSectionClick = onSectionClick
    }

    fun setOnVoucherClick(onVoucherClick: (Voucher) -> Unit) {
        this.onVoucherClick = onVoucherClick
    }

    fun setOnViewAllVoucherClick(onViewAllVoucherClick: (Int) -> Unit) {
        this.onViewAllVoucherClick = onViewAllVoucherClick
    }

    fun submit(newVouchers: List<VoucherSection>) {
        differ.submitList(newVouchers)
    }


    fun getItemAtOrNull(position: Int): VoucherSection? {
        return try {
            snapshot()[position]
        } catch (e: Exception) {
            null
        }
    }

    fun snapshot(): List<VoucherSection> {
        return differ.currentList
    }
}
