package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.utils.StripedUnderlineUtil
import com.tokopedia.home.R
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.pojo.Tickers
import com.tokopedia.v2.home.model.vo.TickerDataModel
import kotlinx.android.synthetic.main.layout_ticker_home.view.*
import java.util.*

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

        val textMessage = itemView.ticker_message
        val closeButton = itemView.btn_close

        private val SLIDE_DELAY: Long = 5000
        internal var hasStarted = false
        private val timer: Timer = Timer()
        internal var tickerId = ""

        fun bind(item: TickerDataModel){
            val ticker = item.tickers.first()
            textMessage.text = ticker.message
            textMessage.movementMethod = TickerLinkMovementMethod(
                    ticker.id
            )
            StripedUnderlineUtil.stripUnderlines(textMessage)
            ViewCompat.setBackgroundTintList(closeButton, ColorStateList.valueOf(Color.parseColor(ticker.color)))
            if (!hasStarted)
                timer.scheduleAtFixedRate(SwitchTicker(item.tickers), 0, SLIDE_DELAY)
        }

        inner class TickerLinkMovementMethod(var tickerId: String) : LinkMovementMethod() {

            override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
//                HomePageTracking.eventClickTickerHomePage(
//                        context,
//                        tickerId
//                )
                return super.onTouchEvent(widget, buffer, event)
            }
        }

        private inner class SwitchTicker(private val tickers: List<Tickers>) : TimerTask() {
            private var i = 0

            override fun run() {
                itemView.post {
                    hasStarted = true
                    if (i < tickers.size - 1)
                        i++
                    else
                        i = 0
                    val ticker = tickers[i]
                    tickerId = ticker.id
                    textMessage.text = ticker.message
                    StripedUnderlineUtil.stripUnderlines(textMessage)
                    ViewCompat.setBackgroundTintList(closeButton, ColorStateList.valueOf(Color.parseColor(ticker.color)))
                }
            }
        }
    }
}