package com.tokopedia.digital_checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutDetailBinding
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutDetailSubtitleBinding

/**
 * @author by jessica on 11/01/21
 */

class DigitalCartDetailInfoAdapter(
    private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var infoItems: MutableList<Any> = mutableListOf()
    private var mainInfoItemCount: Int = 0

    var isExpanded = false
    set(value) {
        field = value
        if (isExpanded) actionListener.expandAdditionalList()
        else actionListener.collapseAdditionalList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DigitalCartDetailTitleViewHolder.LAYOUT) {
            val binding = ItemDigitalCheckoutDetailSubtitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            DigitalCartDetailTitleViewHolder(binding)
        } else {
            val binding = ItemDigitalCheckoutDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            DigitalCartDetailViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (!isExpanded) mainInfoItemCount else infoItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == DigitalCartDetailTitleViewHolder.LAYOUT) {
            (holder as DigitalCartDetailTitleViewHolder).bind(infoItems[position] as CartItemDigitalWithTitle)
        } else if (holder.itemViewType == DigitalCartDetailViewHolder.LAYOUT) {
            (holder as DigitalCartDetailViewHolder).bind(infoItems[position] as CartItemDigital)
        }
    }

    fun clearAllItems() {
        this.infoItems.clear()
        notifyDataSetChanged()
    }

    fun setInfoItems(infoItems: List<CartItemDigital>) {
        this.infoItems = this.infoItems.subList(mainInfoItemCount, this.infoItems.size)
        this.infoItems.addAll(0, infoItems)
        mainInfoItemCount = infoItems.size
        notifyDataSetChanged()
    }

    fun setAdditionalInfoItems(infoItems: List<CartItemDigitalWithTitle>) {
        this.infoItems = this.infoItems.subList(0, mainInfoItemCount)
        infoItems.forEach { infoItem ->
            this.infoItems.add(CartItemDigitalWithTitle(infoItem.title))
            infoItem.items.forEach { item ->
                this.infoItems.add(CartItemDigital(item.label, item.value))
            }
        }
        notifyDataSetChanged()
    }

    fun toggleIsExpanded() {
        this.isExpanded = !this.isExpanded
    }

    override fun getItemViewType(position: Int): Int {
        return if (infoItems[position] is CartItemDigital) {
            DigitalCartDetailViewHolder.LAYOUT
        } else {
            DigitalCartDetailTitleViewHolder.LAYOUT
        }
    }

    class DigitalCartDetailViewHolder(private val binding: ItemDigitalCheckoutDetailBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            val LAYOUT = R.layout.item_digital_checkout_detail
        }

        fun bind(item: CartItemDigital) {
            with(binding) {
                tvCheckoutDetailLabel.text = item.label
                tvCheckoutDetailValue.text = item.value
            }

        }
    }

    class DigitalCartDetailTitleViewHolder(private val binding: ItemDigitalCheckoutDetailSubtitleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            val LAYOUT = R.layout.item_digital_checkout_detail_subtitle
        }

        fun bind(item: CartItemDigitalWithTitle) {
            binding.tvCheckoutDetailSubtitle.text = item.title
        }
    }

    interface ActionListener {
        fun expandAdditionalList()
        fun collapseAdditionalList()
    }
}