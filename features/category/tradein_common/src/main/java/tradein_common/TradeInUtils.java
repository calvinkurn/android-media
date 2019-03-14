package tradein_common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.tokopedia.user.session.UserSession;

import static android.content.Context.TELEPHONY_SERVICE;

public class TradeInUtils {

    public static String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (!(ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0)) {
                Bundle tradeInData = ((Activity) context).getIntent().getExtras();
                if (tradeInData != null) {
                    String did = tradeInData.getString("DEVICE ID");
                    if (did != null && did.length() > 0) {
                        return did;
                    } else {
                        UserSession userSession = new UserSession(context);
                        return userSession.getDeviceId();
                    }
                } else {
                    UserSession userSession = new UserSession(context);
                    return userSession.getDeviceId();
                }
            } else {
                String imei = "";
                if (Build.VERSION.SDK_INT >= 26) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                return imei != null && !imei.isEmpty() ? imei : null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
