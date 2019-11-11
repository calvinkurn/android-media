package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.vo.WalletDataModel

class WalletDelegateAdapter : ViewTypeDelegateAdapter {
    override fun isForViewType(item: ModelViewType): Boolean {
        return item is WalletDataModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return WalletViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as WalletViewHolder
        holder.bind(item as WalletDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is WalletViewHolder){
            holder.bind(item as WalletDataModel)
        }
    }

    inner class WalletViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.layout_wallet_login)){

        fun bind(item: WalletDataModel){

        }
    }
}