package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EMPTY_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.RECOMMENDATION_TYPE
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohEmptyStateViewHolder
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohRecommendationItemViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohItemAdapter : RecyclerView.Adapter<UohItemAdapter.BaseViewHolder<*>>() {
    var listTypeData = mutableListOf<UohTypeData>()
    var listRecommendationItem = mutableListOf<RecommendationItem>()
    private var actionListener: ActionListener? = null
    private var isEmptyState: Boolean = false
    companion object {
        private const val LAYOUT_ORDER_LIST = 0
        private const val LAYOUT_EMPTY_STATE = 1
        private const val LAYOUT_RECOMMENDATION_LIST = 2
    }

    interface ActionListener {
        fun onShowBottomSheetInfo(title: String, resIdDesc: Int)
        fun onTextCopied(label: String, str: String)
        fun onInvalidResiUpload(awbUploadUrl: String)
        fun onDialPhone(strPhoneNo: String)
        fun onShowBookingCode(bookingCode: String, bookingType: String)
        fun onShowBuyerRequestCancelReasonBottomSheet()
        fun onSeeInvoice(invoiceUrl: String)
        fun onCopiedInvoice(invoice: String, str: String)
        fun onClickProduct(productId: Int)
        fun onCopiedAddress(address: String, str: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LAYOUT_EMPTY_STATE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_empty_state, parent, false)
                UohEmptyStateViewHolder(view)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_recommendation_item, parent, false)
                UohRecommendationItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            EMPTY_TYPE -> LAYOUT_EMPTY_STATE
            RECOMMENDATION_TYPE -> LAYOUT_RECOMMENDATION_LIST
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = listTypeData[position]
        val recommendationItem = listRecommendationItem[position]
        when (holder) {
            is UohEmptyStateViewHolder-> {
                holder.bind(element, position)
            }
            is UohRecommendationItemViewHolder-> {
                holder.bind(recommendationItem, position)
            }
        }
    }
}