package com.tokopedia.logisticaddaddress.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.logisticdata.data.entity.address.LocationPass;
import com.tokopedia.logisticdata.data.entity.address.Token;


/**
 * Created by Fajar Ulin Nuha on 10/10/18.
 */
public interface IAddressRouter {

    Intent getDistrictRecommendationIntent(Activity activity, Token token, boolean isFromMarketplaceCart);

    Intent getGeoLocationActivityIntent(Context context, LocationPass locationPass, boolean isFromMarketplaceCart);

}
