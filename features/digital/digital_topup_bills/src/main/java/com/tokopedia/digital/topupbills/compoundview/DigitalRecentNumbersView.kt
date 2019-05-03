package com.tokopedia.digital.topupbillsproduct.compoundview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.digital.topupbillsproduct.R
import com.tokopedia.digital.topupbillsproduct.adapter.DigitalRecentNumbersAdapter
import com.tokopedia.digital.topupbillsproduct.model.DigitalRecentNumber
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
class DigitalRecentNumbersView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val digitalRecentNumbersAdapter : DigitalRecentNumbersAdapter
    private val recentNumbers = mutableListOf<DigitalRecentNumber>()
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, R.layout.view_digital_product_recent_numbers, this)
        recyclerView = view.findViewById(R.id.recycler_view_recent_numbers)

        digitalRecentNumbersAdapter = DigitalRecentNumbersAdapter(recentNumbers)
        recyclerView.adapter = digitalRecentNumbersAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setRecentNumbers(recentNumbers: List<DigitalRecentNumber>) {
        digitalRecentNumbersAdapter.setListener(object : DigitalRecentNumbersAdapter.ActionListener {
            override fun onClickRecentNumber(digitalRecentNumber : DigitalRecentNumber, position: Int) {
                listener.onClickRecentNumber(digitalRecentNumber)
            }
        })
        this.recentNumbers.addAll(recentNumbers)
        digitalRecentNumbersAdapter.notifyDataSetChanged()
    }

    interface ActionListener {
        fun onClickRecentNumber(digitalRecentNumber : DigitalRecentNumber)
    }
}