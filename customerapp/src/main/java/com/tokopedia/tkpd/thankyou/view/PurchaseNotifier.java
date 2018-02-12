package com.tokopedia.tkpd.thankyou.view;

import android.content.Context;
import android.os.Bundle;

import com.bca.xco.widget.XCOEnum;
import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.fcm.applink.ApplinkBuildAndShowNotification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author okasurya on 2/7/18.
 */

public class PurchaseNotifier {
    private static final String TOKOPEDIA = "Tokopedia";
    private static final String PLATFORM = "platform";
    private static final String TEMPLATE = "template";
    private static final String MARKETPLACE = "marketplace";
    private static final String TRANSFER = "transfer";
    private static final String DATE_FORMAT = "dd MMMM yyyy HH:mm";
    private static final String TOTAL_AMOUNT = "total_amount";
    private static final String BANK_NAME = "bank_name";
    private static final String BANK_NUM = "bank_num";
    private static final String DEADLINE_DELTA = "deadline_delta";

    public static void notify(Context context, Bundle extras) {
        try {
            if (showNotification(extras)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ARG_NOTIFICATION_TITLE, TOKOPEDIA);
                bundle.putString(Constants.ARG_NOTIFICATION_DESCRIPTION, getTransferNotificationMessage(context, extras));
                bundle.putString(Constants.ARG_NOTIFICATION_APPLINK, Constants.Applinks.PURCHASE_VERIFICATION);

                ApplinkBuildAndShowNotification.showApplinkNotification(context, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean showNotification(Bundle bundle) {
        return bundle != null
                && bundle.getString(PLATFORM, "").equals(MARKETPLACE)
                && bundle.getString(TEMPLATE, "").equals(TRANSFER);
    }

    private static String getTransferNotificationMessage(Context context, Bundle bundle) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, Integer.parseInt(bundle.getString(DEADLINE_DELTA)));
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, new Locale("id"));

        return String.format(
                context.getString(R.string.payment_description_transfer_procedure),
                bundle.getString(TOTAL_AMOUNT),
                bundle.getString(BANK_NAME),
                bundle.getString(BANK_NUM),
                dateFormat.format(new Date(calendar.getTimeInMillis()))
        );
    }
}
