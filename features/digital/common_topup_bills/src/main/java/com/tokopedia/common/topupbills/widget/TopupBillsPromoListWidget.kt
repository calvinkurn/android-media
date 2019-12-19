package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 07/05/19.
 */
class TopupBillsPromoListWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val titleWidget: TextView
    private val promoList = mutableListOf<TopupBillsPromo>()
    private lateinit var topupBillsPromoListAdapter: TopupBillsPromoListAdapter
    private lateinit var listener: ActionListener
    private val digitalTrackRecentPrev = mutableListOf<TopupBillsTrackPromo>()

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
        titleWidget = view.findViewById(R.id.title_component)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setPromoList(promoList: List<TopupBillsPromo>, showTitle: Boolean = true) {
        if (showTitle) {
            titleWidget.show()
            titleWidget.text = context.getString(R.string.title_promo)
        } else {
            titleWidget.hide()
        }

        topupBillsPromoListAdapter = TopupBillsPromoListAdapter(promoList)
        recyclerView.adapter = topupBillsPromoListAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        topupBillsPromoListAdapter.setListener(object : TopupBillsPromoListAdapter.ActionListener {
            override fun onClickPromoCode(promoId: Int, voucherCode: String) {
                listener.onCopiedPromoCode(promoId, voucherCode)
            }

            override fun onClickPromoItem(topupBillsPromo: TopupBillsPromo, position: Int) {
                listener.onClickItemPromo(topupBillsPromo, position)
            }
        })
        this.promoList.addAll(promoList)
        topupBillsPromoListAdapter.notifyDataSetChanged()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getVisibleRecentItemsToUsersTracking(promoList)
                }
            }
        })
    }

    fun getVisibleRecentItemsToUsersTracking(promoList: List<TopupBillsPromo>) {
        val firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()


        val digitalTrackPromoList = mutableListOf<TopupBillsTrackPromo>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= promoList.size - 1) {
                digitalTrackPromoList.add(TopupBillsTrackPromo(promoList[i], i))
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
        if (::topupBillsPromoListAdapter.isInitialized) {
            topupBillsPromoListAdapter.resetPromoListSelected(promoId)
        }
    }

    fun toggleTitleVisibility(value: Boolean) {
        if (value) titleWidget.show() else titleWidget.hide()
    }

    interface ActionListener {
        fun onCopiedPromoCode(promoId: Int, voucherCode: String)

        fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>)

        fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int)
    }

}
