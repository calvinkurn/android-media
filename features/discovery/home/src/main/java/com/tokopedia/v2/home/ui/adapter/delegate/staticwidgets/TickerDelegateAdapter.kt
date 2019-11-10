package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.pojo.Ticker

class TickerDelegateAdapter : ViewTypeDelegateAdapter{
    override fun isForViewType(item: ModelViewType): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class TickerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.home_banner)){

        fun bind(item: Ticker){

        }
    }
}