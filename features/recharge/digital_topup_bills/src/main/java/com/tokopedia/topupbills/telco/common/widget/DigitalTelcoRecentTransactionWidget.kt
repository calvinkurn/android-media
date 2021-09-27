package com.tokopedia.topupbills.telco.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.common.topupbills.widget.TopupBillsRecentTransactionWidget
import com.tokopedia.topupbills.telco.common.adapter.TelcoRecentNumbersAdapter
import org.jetbrains.annotations.NotNull

/**
 * override layout manager recycler view with vertical version of recent transaction
 */

class DigitalTelcoRecentTransactionWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                    defStyleAttr: Int = 0)
    : TopupBillsRecentTransactionWidget(context, attrs, defStyleAttr) {

    private lateinit var listenerTelco: TopupBillsRecentNumberListener

    fun setListenerRecentTelco(listenerBills: TopupBillsRecentNumberListener) {
        this.listenerTelco = listenerBills
    }

    override fun initAdapterWithData(recentNumbers: List<TopupBillsRecommendation>) {
        val adapter = TelcoRecentNumbersAdapter(recentNumbers)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setListener(object : TelcoRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int) {
                listenerTelco.onClickRecentNumber(topupBillsRecommendation, topupBillsRecommendation.categoryId,
                        position)
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getVisibleRecentItemsToUsersTracking(recentNumbers)
                }
            }
        })
    }
}