package com.tokopedia.core.router.digitalmodule.sellermodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by normansyahputa on 8/22/17.
 */

public interface SellerModuleRouter {

    void goToHome(Context context);

    void goToProductDetail(Context context, String productUrl);

    Intent goToDatePicker(Activity activity, List<PeriodRangeModelCore> periodRangeModels, long startDate, long endDate,
                          int datePickerSelection, int datePickerType);

    String getRangeDateFormatted(Context context, long startDate, long endDate);
}
