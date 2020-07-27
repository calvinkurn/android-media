package com.tokopedia.manageaddress.data.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 01/03/19.
 */
public class AddressCornerAnalytics extends TransactionAnalytics {

    @Inject
    public AddressCornerAnalytics() {

    }

    public void sendChooseCornerAddress() {
        sendEventCategoryAction(
                EventName.CLICK_REGISTER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CHOOSE_LOCATION_CORNER
        );
    }
}
