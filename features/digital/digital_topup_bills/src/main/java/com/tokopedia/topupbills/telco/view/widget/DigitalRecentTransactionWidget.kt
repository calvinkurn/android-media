package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.telco.view.adapter.DigitalRecentNumbersAdapter
import com.tokopedia.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.topupbills.telco.view.model.DigitalTrackRecentTransactionTelco
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
class DigitalRecentTransactionWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val titleWidget: TextView
    private val digitalRecentNumbersAdapter: DigitalRecentNumbersAdapter
    private val recentNumbers = mutableListOf<TopupBillsRecommendation>()
    private lateinit var listener: ActionListener
    private val digitalTrackRecentPrev = mutableListOf<DigitalTrackRecentTransactionTelco>()

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
        titleWidget = view.findViewById(R.id.title_component)

        digitalRecentNumbersAdapter = DigitalRecentNumbersAdapter(recentNumbers)
        recyclerView.adapter = digitalRecentNumbersAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setRecentNumbers(recentNumbers: List<TopupBillsRecommendation>) {
        titleWidget.visibility = View.VISIBLE
        titleWidget.text = context.getString(R.string.title_reccent_transaction_widget)
        digitalRecentNumbersAdapter.setListener(object : DigitalRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int) {
                listener.onClickRecentNumber(topupBillsRecommendation, topupBillsRecommendation.categoryId,
                        position)
            }
        })
        this.recentNumbers.addAll(recentNumbers)
        digitalRecentNumbersAdapter.notifyDataSetChanged()

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


        val digitalTrackRecentList = mutableListOf<DigitalTrackRecentTransactionTelco>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= recentNumbers.size - 1) {
                digitalTrackRecentList.add(DigitalTrackRecentTransactionTelco(recentNumbers[i],
                        recentNumbers[i].categoryId, i))
            }
        }
        if (digitalTrackRecentList.size > 0 &&
                digitalTrackRecentList.size != digitalTrackRecentPrev.size &&
                digitalTrackRecentList != digitalTrackRecentPrev) {
            listener.onTrackImpressionRecentList(digitalTrackRecentList)

            digitalTrackRecentPrev.clear()
            digitalTrackRecentPrev.addAll(digitalTrackRecentList)
        }
    }

    interface ActionListener {
        fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int, position: Int)
        fun onTrackImpressionRecentList(digitalTrackRecentList: List<DigitalTrackRecentTransactionTelco>)
    }
}