package com.tokopedia.purchase_platform.common.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.purchase_platform.common.data.model.response.cod.Data;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICheckoutModuleRouter {

    Intent getShopPageIntent(Context context, String shopId);

    Intent getGeolocationIntent(Context context, LocationPass locationPass);

    Intent getCodPageIntent(Context context, Data data);

}
