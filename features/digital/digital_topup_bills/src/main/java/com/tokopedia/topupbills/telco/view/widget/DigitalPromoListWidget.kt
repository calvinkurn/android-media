package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoPromo
import com.tokopedia.topupbills.telco.view.adapter.DigitalPromoListAdapter
import com.tokopedia.topupbills.telco.view.model.DigitalTrackPromoTelco
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
    private lateinit var digitalPromoListAdapter: DigitalPromoListAdapter
    private lateinit var listener: ActionListener
    private val digitalTrackRecentPrev = mutableListOf<DigitalTrackPromoTelco>()

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
        titleWidget = view.findViewById(R.id.title_component)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setPromoList(promoList: List<TelcoPromo>) {
        titleWidget.visibility = View.VISIBLE
        titleWidget.text = context.getString(R.string.title_promo)

        digitalPromoListAdapter = DigitalPromoListAdapter(promoList)
        recyclerView.adapter = digitalPromoListAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        digitalPromoListAdapter.setListener(object : DigitalPromoListAdapter.ActionListener {
            override fun onClickPromoCode(promoId: Int, voucherCode: String) {
                listener.onCopiedPromoCode(promoId, voucherCode)
            }

            override fun onClickPromoItem(telcoPromo: TelcoPromo) {
                listener.onClickItemPromo(telcoPromo)
            }
        })
        this.promoList.addAll(promoList)
        digitalPromoListAdapter.notifyDataSetChanged()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getVisibleRecentItemsToUsersTracking(promoList)
                }
            }
        })
    }

    fun getVisibleRecentItemsToUsersTracking(promoList: List<TelcoPromo>) {
        val firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()


        val digitalTrackPromoList = mutableListOf<DigitalTrackPromoTelco>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= promoList.size - 1) {
                digitalTrackPromoList.add(DigitalTrackPromoTelco(promoList[i], i))
            }
        }
        if (digitalTrackPromoList.size > 0 &&
                digitalTrackPromoList.size != digitalTrackRecentPrev.size &&
                digitalTrackPromoList != digitalTrackRecentPrev) {
            listener.onTrackImpressionPromoList(digitalTrackPromoList)

            digitalTrackRecentPrev.clear()
            digitalTrackRecentPrev.addAll(digitalTrackPromoList)
        }
    }

    fun notifyPromoItemChanges(promoId: Int) {
        if (::digitalPromoListAdapter.isInitialized) {
            digitalPromoListAdapter.resetPromoListSelected(promoId)
        }
    }

    interface ActionListener {
        fun onCopiedPromoCode(promoId: Int, voucherCode: String)

        fun onTrackImpressionPromoList(digitalTrackPromoList: List<DigitalTrackPromoTelco>)

        fun onClickItemPromo(telcoPromo: TelcoPromo)
    }

}
