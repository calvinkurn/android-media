package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.unifycomponents.ChipsUnify

class NotificationFilterView : LinearLayout {

    private var transaction: ChipsUnify? = null
    private var promo: ChipsUnify? = null
    private var info: ChipsUnify? = null
    private var selectedFilter: ChipsUnify? = null
    var filterListener: FilterListener? = null

    interface FilterListener {
        fun onFilterChanged(@NotificationFilterType filterType: Int)
    }

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context?) {
        initViewInflation(context)
    }

    private val clickListener = OnClickListener { chip ->
        if (chip is ChipsUnify) {
            modifyChipState(chip)
            notifyFilterListener()
        }
    }

    private fun modifyChipState(clickedChip: ChipsUnify) {
        selectedFilter?.chipType = ChipsUnify.TYPE_NORMAL
        if (clickedChip == selectedFilter) {
            clickedChip.chipType = ChipsUnify.TYPE_NORMAL
            selectedFilter = null
        } else {
            clickedChip.chipType = ChipsUnify.TYPE_SELECTED
            selectedFilter = clickedChip
        }
    }

    private fun notifyFilterListener() {
        val selectedFilterType = when (selectedFilter?.id) {
            R.id.filter_transaction -> NotificationFilterType.TRANSACTION
            R.id.filter_promo -> NotificationFilterType.PROMOTION
            R.id.filter_info -> NotificationFilterType.INFO
            else -> NotificationFilterType.NONE
        }
        filterListener?.onFilterChanged(selectedFilterType)
    }

    private fun initViewInflation(context: Context?) {
        View.inflate(context, R.layout.item_notifcenter_filter, this)?.apply {
            bindView(this)
            bindClick()
        }
    }

    private fun bindClick() {
        transaction?.setOnClickListener(clickListener)
        promo?.setOnClickListener(clickListener)
        info?.setOnClickListener(clickListener)
    }

    private fun bindView(view: View) {
        transaction = view.findViewById(R.id.filter_transaction)
        promo = view.findViewById(R.id.filter_promo)
        info = view.findViewById(R.id.filter_info)
    }

}