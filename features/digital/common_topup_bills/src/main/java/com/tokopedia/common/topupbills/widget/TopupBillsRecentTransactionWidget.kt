package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
class TopupBillsRecentTransactionWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                  defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val titleWidget: TextView
    private val topupBillsRecentNumbersAdapter: TopupBillsRecentNumbersAdapter
    private val recentNumbers = mutableListOf<TopupBillsRecommendation>()
    private lateinit var listener: ActionListener
    private val digitalTrackRecentPrev = mutableListOf<TopupBillsTrackRecentTransaction>()

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
        titleWidget = view.findViewById(R.id.title_component)

        topupBillsRecentNumbersAdapter = TopupBillsRecentNumbersAdapter(recentNumbers)
        recyclerView.adapter = topupBillsRecentNumbersAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setRecentNumbers(recentNumbers: List<TopupBillsRecommendation>) {
        titleWidget.text = context.getString(R.string.title_reccent_transaction_widget)
        topupBillsRecentNumbersAdapter.setListener(object : TopupBillsRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int) {
                listener.onClickRecentNumber(topupBillsRecommendation, topupBillsRecommendation.categoryId,
                        position)
            }
        })
        this.recentNumbers.addAll(recentNumbers)
        topupBillsRecentNumbersAdapter.notifyDataSetChanged()

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
        if (digitalTrackRecentList.size > 0 &&
                digitalTrackRecentList.size != digitalTrackRecentPrev.size &&
                digitalTrackRecentList != digitalTrackRecentPrev) {
            listener.onTrackImpressionRecentList(digitalTrackRecentList)

            digitalTrackRecentPrev.clear()
            digitalTrackRecentPrev.addAll(digitalTrackRecentList)
        }
    }

    fun hideTitle(value: Boolean) {
        if (value) titleWidget.show() else titleWidget.hide()
    }

    interface ActionListener {
        fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int, position: Int)
        fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>)
    }
}