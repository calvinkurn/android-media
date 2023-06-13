package com.tokopedia.privacycenter.ui.searchhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(
    private val list: MutableList<ItemSearch>,
    private val onClick: (ItemSearch, Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int =
        list.size

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(
        private val binding: ItemSearchHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: ItemSearch? = null

        init {
            binding.apply {
                icDelete.setOnClickListener {
                    icDelete.hide()
                    loaderDelete.show()

                    currentItem?.let { itemSearch ->
                        onClick(itemSearch, absoluteAdapterPosition)
                    }
                }
            }
        }

        fun bind(item: ItemSearch) {
            this.currentItem = item

            val isShopType = item.type == KEY_TYPE_SHOP

            binding.apply {
                dividerIconShop.showWithCondition(item.iconTitle.isNotEmpty())
                imgIconShop.showWithCondition(item.iconTitle.isNotEmpty())
                textStoreName.showWithCondition(isShopType)
                textLocation.showWithCondition(isShopType)
                textKeyword.showWithCondition(!isShopType)

                icDelete.show()
                loaderDelete.hide()
            }

            if (isShopType) {
                binding.apply {
                    imgIconShop.loadImageWithoutPlaceholder(item.iconTitle)
                    textStoreName.text = item.title
                    textLocation.text = item.subtitle
                }
            } else {
                binding.textKeyword.text = item.title
            }

            binding.apply {
                imgIconSearch.loadImageWithoutPlaceholder(item.imageUrl)
            }
        }
    }

    companion object {
        private const val KEY_TYPE_SHOP = "shop"
    }
}
