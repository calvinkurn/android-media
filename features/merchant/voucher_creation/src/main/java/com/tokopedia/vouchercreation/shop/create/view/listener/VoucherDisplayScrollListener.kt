package com.tokopedia.vouchercreation.shop.create.view.listener

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class VoucherDisplayScrollListener(private val snapHelper: SnapHelper,
                                   private val onSnapPositionChangeListener: (Int) -> Unit = {}) : RecyclerView.OnScrollListener() {

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
        val snapPosition = snapView?.let {
            recyclerView.layoutManager?.getPosition(it)
        }
        val isSnapPositionChanged = this.snapPosition != snapPosition
        if (isSnapPositionChanged) {
            onSnapPositionChangeListener(snapPosition ?: RecyclerView.NO_POSITION)
            this.snapPosition = snapPosition ?: RecyclerView.NO_POSITION
        }

    }

}