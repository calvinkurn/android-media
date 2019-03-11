package com.tokopedia.applink;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriBuilder {
    /**
     * Build pattern uri to uri String
     *
     * @param uriPattern      example: "tokopedia-android-internal://merchant/product/{id}/edit/"
     * @param parameter       example: "213"
     * @return "tokopedia-android-internal://merchant/product/213/edit/
     */
    public static String buildUri(@NonNull String uriPattern,
                                  String...parameter) {
        String result = uriPattern;
        if (parameter != null) {
            Pattern p = Pattern.compile("\\{(.*?)\\}");
            Matcher m = p.matcher(uriPattern);
            int i = 0;
            while (m.find()) {
                result = (result.replace(m.group(), parameter[i]));
                i++;
            }
        }
        return result;
    }

    /**
     * Build pattern uri to uri String
     *
     * @param uri      example: "tokopedia-android-internal://merchant/product/edit/"
     * @param queryParameters       example: mapOf ("userId" to "222")
     * @return "tokopedia-android-internal://merchant/product/edit/?userId=222"
     */
    public static String buildUriAppendParam(@NonNull String uri,
                                             @Nullable Map<String, String> queryParameters) {
        StringBuilder stringBuilder = new StringBuilder(uri);
        if (queryParameters != null && queryParameters.size() > 0) {
            stringBuilder.append("?");
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return stringBuilder.toString();
    }

}
