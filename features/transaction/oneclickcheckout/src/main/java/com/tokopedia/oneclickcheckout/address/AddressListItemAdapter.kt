package com.tokopedia.oneclickcheckout.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.oneclickcheckout.databinding.CardAddressListBinding
import com.tokopedia.oneclickcheckout.databinding.ItemLoadingAddressListBinding

class AddressListItemAdapter(private val listener: OnSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var addressList = mutableListOf<RecipientAddressModel>()
    private var hasNext = false

    fun setData(data: List<RecipientAddressModel>, hasNext: Boolean) {
        addressList.clear()
        addressList.addAll(data)
        this.hasNext = hasNext
        notifyDataSetChanged()
    }

    interface OnSelectedListener {
        fun onSelect(addressModel: RecipientAddressModel)
    }

    override fun getItemViewType(position: Int): Int {
        if (position + 1 > addressList.size) {
            return AddressLoadingViewHolder.LOADING_LAYOUT
        }
        return AddressListItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == AddressLoadingViewHolder.LOADING_LAYOUT) {
            return AddressLoadingViewHolder(ItemLoadingAddressListBinding.inflate(inflater, parent, false))
        }
        return AddressListItemViewHolder(CardAddressListBinding.inflate(inflater, parent, false), listener)
    }

    override fun getItemCount(): Int {
        return addressList.size + (if (hasNext) 1 else 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddressListItemViewHolder) {
            holder.bind(addressList[position])
        }
        if (holder is AddressLoadingViewHolder) {
            holder.bind()
        }
    }
}