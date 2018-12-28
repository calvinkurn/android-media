package com.tokopedia.linker;

import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;
import com.tokopedia.linker.requests.LinkerDeeplinkRequest;
import com.tokopedia.linker.requests.LinkerGenericRequest;
import com.tokopedia.linker.requests.LinkerShareRequest;

import io.branch.referral.BranchError;

public class LinkerUtils {

    public static String getOgTitle(LinkerData data) {
        if (TextUtils.isEmpty(data.getOgTitle())) {
            return data.getName();
        } else {
            return data.getOgTitle();
        }
    }

    public static String getOgDesc(LinkerData data) {
        if (TextUtils.isEmpty(data.getOgDescription())) {
            return data.getDescription();
        } else {
            return data.getOgDescription();
        }
    }

    public static String getOgImage(LinkerData data) {
        if (TextUtils.isEmpty(data.getOgImageUrl())) {
            return data.getImgUri();
        } else {
            return data.getOgImageUrl();
        }
    }

    public static double convertStringToDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String normalizePhoneNumber(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum))
            return phoneNum.replaceFirst("^0(?!$)", "62");
        else
            return "";
    }

    public static double convertToDouble(String value) {
        double result = 0;
        try {
            result = Double.valueOf(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static LinkerShareResult createShareResult(String shareContents, String shareUri, String url) {
        LinkerShareResult linkerShareData = new LinkerShareResult();
        linkerShareData.setShareContents(shareContents);
        linkerShareData.setShareUri(shareUri);
        linkerShareData.setUrl(url);
        return linkerShareData;
    }

    public static LinkerError createLinkerError(int errorCode, String errorMessage) {
        LinkerError linkerError = new LinkerError();
        linkerError.setErrorCode(getLinkerErrorCode(errorCode));
        linkerError.setErrorMessage(errorMessage);
        return linkerError;
    }

    private static int getLinkerErrorCode(int errorCode){
        int error = LinkerConstants.ERROR_SOMETHING_WENT_WRONG;
        switch (errorCode){
            case BranchError.ERR_BRANCH_NO_SHARE_OPTION:
                error = LinkerConstants.ERROR_REQUEST_NOT_SUCCESSFUL;
                break;
            case BranchError.ERR_BRANCH_INIT_FAILED:
                error = LinkerConstants.ERROR_INIT_FAILED;
                break;
        }
        return error;
    }

    public static LinkerDeeplinkResult createDeeplinkData(String deeplink, String promocode) {
        LinkerDeeplinkResult linkerDeeplinkData = new LinkerDeeplinkResult();
        linkerDeeplinkData.setDeeplink(deeplink);
        linkerDeeplinkData.setPromoCode(promocode);
        return linkerDeeplinkData;
    }

    public static <T> LinkerGenericRequest createGenericRequest(int eventId, T dataObject){
        LinkerGenericRequest<T> linkerGenericRequest = new LinkerGenericRequest<T>(eventId, dataObject);
        return linkerGenericRequest;
    }

    public static <T> LinkerDeeplinkRequest createDeeplinkRequest(
            int eventId, T dataObj, DefferedDeeplinkCallback defferedDeeplinkCallback, Activity activity){
        LinkerDeeplinkRequest<T> linkerDeeplinkRequest = new LinkerDeeplinkRequest<>(
                eventId, dataObj, defferedDeeplinkCallback);
        return linkerDeeplinkRequest;
    }

    public static <T> LinkerShareRequest createShareRequest(
            int eventId, T dataObject, ShareCallback shareCallback){
        LinkerShareRequest<T> linkerShareRequest = new LinkerShareRequest<>(eventId, dataObject, shareCallback);
        return linkerShareRequest;
    }

    public static Spanned fromHtml(String text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableStringBuilder("");
        }
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }
}
