package com.tokopedia.ovo.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

public class OvoPayByQrTrackerUtil {
    public interface EVENT {
        String viewOvoPayEvent = "viewOVOPay";
        String clickOvoPayEvent = "clickOVOPay";
    }

    public interface CATEGORY {
        String ovoPayByQr = "ovo pay by qr";
    }

    public interface ACTION {
        String viewPagePaymentSummary = "view page payment summary";
        String clickBayar = "click bayar";
        String viewPageTransaksiBerhasil = "view page transaksi berhasil";
        String clickKembaliBerhasil = "click kembali ke tokopedia berhasil";
        String viewPageTransaksi = "view page transaksi tidak berhasil";
        String cobaLagi = "coba lagi";
        String clickKembaliTidakBerhasil = "click kembali ke tokopedia tidak berhasil";
        String clickButuhBantuan = "click butuh bantuan";

    }

    public interface LABEL {
        String defaultLabel = "";
    }
    public static void sendEvent(Context context, String event, String category, String action, String label) {
        if (context == null) {
            return;
        }

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker == null) {
            return;
        }

        tracker.sendEventTracking(event, category, action, label);
    }
}
