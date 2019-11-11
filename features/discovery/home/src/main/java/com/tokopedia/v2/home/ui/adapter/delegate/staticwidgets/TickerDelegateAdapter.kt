package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.vo.TickerDataModel
import kotlinx.android.synthetic.main.layout_ticker_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
class TickerDelegateAdapter : ViewTypeDelegateAdapter{
    override fun isForViewType(item: ModelViewType): Boolean {
        return item is TickerDataModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TickerViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as TickerViewHolder
        holder.bind(item as TickerDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is TickerViewHolder) holder.bind(item as TickerDataModel)
    }

    inner class TickerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.layout_ticker_home)){

        private val textSwitch: TextSwitcher = itemView.text_switch
        private val tickerCoroutine = ticker(delayMillis = 5000, initialDelayMillis = 0)
        private var hasStarted = false
        private var i = 0

        fun bind(item: TickerDataModel){
            textSwitch.setInAnimation(itemView.context, R.anim.slide_in_right)
            textSwitch.setOutAnimation(itemView.context, R.anim.slide_out_left)
            if (!hasStarted){
                GlobalScope.launch(Dispatchers.Main){
                    hasStarted = true
                    for(event in tickerCoroutine){
                        textSwitch.setText(item.tickers[i].message)
                        if (i < item.tickers.size - 1)
                            i++
                        else
                            i = 0
                    }
                }
                itemView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener{
                    @SuppressLint("SyntheticAccessor")
                    override fun onViewDetachedFromWindow(v: View?) {
                        tickerCoroutine.cancel()
                    }

                    override fun onViewAttachedToWindow(v: View?) {}
                })
            }
        }
    }
}