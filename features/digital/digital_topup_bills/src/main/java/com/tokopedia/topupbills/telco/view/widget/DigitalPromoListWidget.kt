package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.digital.topupbillsproduct.adapter.DigitalPromoListAdapter
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoPromo
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 07/05/19.
 */
class DigitalPromoListWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val titleWidget: TextView
    private val promoList = mutableListOf<TelcoPromo>()
    private val digitalPromoListAdapter: DigitalPromoListAdapter
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
        titleWidget = view.findViewById(R.id.title_component)

        digitalPromoListAdapter = DigitalPromoListAdapter(promoList)
        recyclerView.adapter = digitalPromoListAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setPromoList(promoList: List<TelcoPromo>) {
        titleWidget.visibility = View.VISIBLE
        titleWidget.text = context.getString(R.string.title_promo)
        digitalPromoListAdapter.setListener(object : DigitalPromoListAdapter.ActionListener {
            override fun onClickPromoCode(voucherCode: String) {
                listener.onCopiedPromoCode(voucherCode)
            }

            override fun onClickPromoItem(telcoPromo: TelcoPromo) {
                listener.onClickItemPromo(telcoPromo)
            }
        })
        this.promoList.addAll(promoList)
        digitalPromoListAdapter.notifyDataSetChanged()
    }

    interface ActionListener {
        fun onCopiedPromoCode(voucherCode: String)

        fun onClickItemPromo(telcoPromo: TelcoPromo)
    }

}
