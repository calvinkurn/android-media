package com.tokopedia.buyerorderdetail.presentation.coachmark

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCoachMarkData
import com.tokopedia.buyerorderdetail.presentation.model.BaseVisitableUiModel

class DriverTippingCoachMarkManager(
    override var viewHolderRootView: View? = null,
    override var uiModel: BaseVisitableUiModel? = null,
    override var key: String = BuyerOrderDetailCoachMarkData.DRIVER_TIPPING_INFO_COACHMARK_KEY,
    override var title: String = BuyerOrderDetailCoachMarkData.DRIVER_TIPPING_INFO_COACHMARK_TITLE,
    override var description: String = BuyerOrderDetailCoachMarkData.DRIVER_TIPPING_INFO_COACHMARK_DESCRIPTION
) : BuyerOrderDetailCoachMarkItemManager() {
    override fun getAnchorView(): View? {
        return viewHolderRootView?.findViewById(R.id.tvBuyerOrderDetailTippingDescription)
    }
}