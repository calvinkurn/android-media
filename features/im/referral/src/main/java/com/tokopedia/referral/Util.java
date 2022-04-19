package com.tokopedia.referral;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.util.List;

public class Util {
    public static RequestParams getPostRequestBody(UserSession userSession) {
        RequestParams params = RequestParams.create();
        if (userSession.isLoggedIn()) {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty(GetReferralDataUseCase.Companion.getUserId(), Integer.parseInt(userSession.getUserId()));
            requestBody.addProperty(GetReferralDataUseCase.Companion.getMsisdn(), userSession.getPhoneNumber());
            params.getParameters().put(GetReferralDataUseCase.Companion.getData(), requestBody);
        }
        return params;
    }

    public static void shareData(Activity context, String packageName, String targetType, String shareTxt, String productUri) {
        boolean resolved = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(targetType);
        share.putExtra(Intent.EXTRA_REFERRER, productUri);
        share.putExtra(Intent.EXTRA_TEXT, shareTxt);

        if (context != null) {
            if (context.getPackageManager() != null) {
                List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
                for (ResolveInfo info : resInfo) {
                    if (info.activityInfo.packageName.equals(packageName)) {
                        resolved = true;
                        share.setPackage(info.activityInfo.packageName);
                    }
                }
            }

            if (resolved)
                context.startActivity(share);
            else
                Toast.makeText(context, context.getString(R.string.error_apps_not_installed), Toast.LENGTH_SHORT).show();
        }
    }
}
