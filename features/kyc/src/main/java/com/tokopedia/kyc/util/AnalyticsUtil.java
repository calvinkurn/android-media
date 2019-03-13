package com.tokopedia.kyc.util;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsUtil {
    public interface EventName{
        String CLICK_OVO = "clickOVO";
    }
    public interface EventCategory{
        String OVO_KYC = "ovo kyc";
    }
    public interface EventAction{
        String CLK_MUL_STP1 = "click mulai step 1";
        String CLK_CPTR_PIC_STP2 = "click capture foto step 2";
        String CLK_CONF_STP2 = "click konfirmasi step 2";
        String CLK_ULN_PIC_STP2 = "click ulangi foto step 2";
        String CLK_OK_STP3 = "click ok step 3";
        String CLK_CPTR_PIC_STP3 = "click capture foto step 3";
        String CLK_GNKN_STP3 = "click gunakan step 3";
        String CLK_ULN_STP3 = "click ulangi step 3";
        String CLK_OK_ULN_STP3 = "click ok ulangi step 3";
        String CLK_ULN_KTP = "click ulangi ktp";
        String CLK_ULN_SLFE = "click ulangi selfie";
        String CLK_PRCS = "click proses";
        String CLK_LNJTN_STP4 = "click lanjuktan step 4";
        String CLK_BTLKN_STP4 = "click batalkan step 4";
        String CLK_CBLG_STLGGL = "click coba lagi setelah gagal";
        String CLK_KMBL_TKPD = "click kembali ke tokopedia";
        String CLK_PHN = "click phone";
        String CLK_EML = "click email";
        String CLK_LNJKTN = "click lanjuktan";
        String CLK_NNT_SJA = "click nanti saja";
    }

    public interface Keys{
        String USR_ID = "user_id";
        String ENT = "event";
        String ENT_CAT = "eventCategory";
        String ENT_ACT = "eventAction";
        String ENT_LBL = "eventLable";
    }

    public static void sendEvent(Context context, String event, String category,
                                 String label, String userId, String action) {
        if (context == null) {
            return;
        }

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker == null) {
            return;
        }
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(Keys.USR_ID, userId);
        eventMap.put(Keys.ENT, event);
        eventMap.put(Keys.ENT_ACT, action);
        eventMap.put(Keys.ENT_CAT, category);
        eventMap.put(Keys.ENT_LBL, label);
        tracker.sendEventTracking(eventMap);
    }
}
