package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.common.topupbills.widget.TopupBillsRecentTransactionWidget
import com.tokopedia.recharge_credit_card.adapter.RechargeCCRecentNumberAdapter
import org.jetbrains.annotations.NotNull

class RechargeCCRecentTransactionWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                  defStyleAttr: Int = 0)
    : TopupBillsRecentTransactionWidget(context, attrs, defStyleAttr) {

    private var listenerCC: TopupBillsRecentNumberListener? = null

    fun setListenerRecentNumber(listenerBills: TopupBillsRecentNumberListener) {
        this.listenerCC = listenerBills
    }

    override fun initAdapterWithData(recentNumbers: List<TopupBillsRecommendation>) {
        val adapter = RechargeCCRecentNumberAdapter(recentNumbers)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setListener(object : RechargeCCRecentNumberAdapter.ActionListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int) {
                listenerCC?.onClickRecentNumber(topupBillsRecommendation, topupBillsRecommendation.categoryId,
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
