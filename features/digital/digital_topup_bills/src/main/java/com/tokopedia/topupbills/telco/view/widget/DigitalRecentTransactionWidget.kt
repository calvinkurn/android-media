package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.digital.topupbillsproduct.adapter.DigitalRecentNumbersAdapter
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoRecommendation
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
    private val recentNumbers = mutableListOf<TelcoRecommendation>()
    private lateinit var listener: ActionListener

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

    fun setRecentNumbers(recentNumbers: List<TelcoRecommendation>) {
        titleWidget.visibility = View.VISIBLE
        titleWidget.text = context.getString(R.string.title_reccent_transaction_widget)
        digitalRecentNumbersAdapter.setListener(object : DigitalRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(telcoRecommendation: TelcoRecommendation, position: Int) {
                listener.onClickRecentNumber(telcoRecommendation)
            }
        })
        this.recentNumbers.addAll(recentNumbers)
        digitalRecentNumbersAdapter.notifyDataSetChanged()
    }

    interface ActionListener {
        fun onClickRecentNumber(telcoRecommendation: TelcoRecommendation)
    }
}