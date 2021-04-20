package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class TopupBillsRecentTransactionWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                       defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    protected val recyclerView: RecyclerView
    protected val titleWidget: TextView

    private lateinit var listener: TopupBillsRecentNumberListener

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view_menu_component)
        titleWidget = view.findViewById(R.id.title_component)
    }

    fun setListener(listenerBills: TopupBillsRecentNumberListener) {
        this.listener = listenerBills
    }

    fun setRecentNumbers(recentNumbers: List<TopupBillsRecommendation>) {
        titleWidget.text = context.getString(R.string.common_topup_title_recent_transaction_widget)
        initAdapterWithData(recentNumbers)
    }

    open fun initAdapterWithData(recentNumbers: List<TopupBillsRecommendation>) {
        val adapter = TopupBillsRecentNumbersAdapter(recentNumbers)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setListener(object : TopupBillsRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int) {
                listener.onClickRecentNumber(topupBillsRecommendation, topupBillsRecommendation.categoryId,
                        position)
            }
        })

        getVisibleRecentItemsToUsersTracking(recentNumbers)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getVisibleRecentItemsToUsersTracking(recentNumbers)
                }
            }
        })
    }

    fun getVisibleRecentItemsToUsersTracking(recentNumbers: List<TopupBillsRecommendation>) {
        val firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()


        val digitalTrackRecentList = mutableListOf<TopupBillsTrackRecentTransaction>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= recentNumbers.size - 1) {
                digitalTrackRecentList.add(TopupBillsTrackRecentTransaction(recentNumbers[i],
                        recentNumbers[i].categoryId, i))
            }
        }
        if (digitalTrackRecentList.size > 0) {
            listener.onTrackImpressionRecentList(digitalTrackRecentList)
        }
    }

    fun toggleTitle(value: Boolean) {
        if (value) titleWidget.show() else titleWidget.hide()
    }
}