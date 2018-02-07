package com.tokopedia.core.router.digitalmodule.sellermodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/5/17.
 */

public interface TokoCashRouter {

    Intent goToDatePicker(Activity activity, List<PeriodRangeModelCore> periodRangeModels, long startDate, long endDate,
                          int datePickerSelection, int datePickerType);

    String getRangeDateFormatted(Context context, long startDate, long endDate);

    //TODO will continue using this method in next sprint
    Intent goToHistoryTokoCash(Context context);

    Intent goToQRScannerTokoCash(Context context);
}
