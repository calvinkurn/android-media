package com.tokopedia.core.gcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;

import java.util.Map;

/**
 * Created by Herdi_WORK on 13.12.16.
 */

class GCMUtils {

    private GCMUtils(){

    }

    static boolean isDeprecated(int tkpCode) {
        switch (tkpCode) {
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY:
                return true;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY:
                return true;
            default:
                return false;
        }
    }

    static boolean isExcludeFromSellerApp(int tkpCode) {
        switch (tkpCode) {
            case TkpdState.GCMServiceState.GCM_REVIEW:
                return true;
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                return true;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
                return true;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
                return true;
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
                return true;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                return true;
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
                return true;
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
                return true;
            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
                return true;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
                return true;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
                return true;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                return true;
            default:
                return false;
        }
    }

    static boolean isValidForSellerApp(int tkpCode, Application app) {
        if (GlobalConfig.isSellerApp()
                && GCMUtils.isExcludeFromSellerApp(tkpCode)){
            return false;
        }
        return true;
    }

    static int getCode(Bundle data) {
        int code;
        try {
            code = Integer.parseInt(data.getString("tkp_code"));
        } catch (Exception e) {
            code = 0;
        }
        return code;
    }

    static Bundle convertMap(Map<String, String> map) {
        Bundle bundle = new Bundle(map != null ? map.size() : 0);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }
}
