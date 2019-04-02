package com.tokopedia.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.shop.open.view.listener.ShopOpenLogisticView;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public abstract class ShopOpenLogisticPresenter extends BaseDaggerPresenter<ShopOpenLogisticView> {

    public abstract void getCouriers(int districtCode);

    public abstract void saveCourier(CourierServiceIdWrapper courierServiceIdWrapper);

}
