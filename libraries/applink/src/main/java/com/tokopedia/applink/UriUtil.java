package com.tokopedia.applink;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtil {
    /**
     * Build pattern uri to uri String
     *
     * @param uriPattern example: "tokopedia-android-internal://merchant/product/{id}/edit/"
     * @param parameter  example: "213"
     * @return "tokopedia-android-internal://merchant/product/213/edit/
     */
    public static String buildUri(@NonNull String uriPattern,
                                  String... parameter) {
        String result = uriPattern;
        if (parameter != null && parameter.length > 0) {
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
     * Destructure uri to list of String
     *
     * @param uriPatternString example: "tokopedia-android-internal://marketplace/shop/{id}/etalase/{id}/"
     * @param uri              example: "tokopedia-android-internal://marketplace/shop/123/etalase/345"
     * @param checkScheme      if true, will check pattern and uri scheme. If not same, will return.
     * @return listOf (123, 345)
     */
    public static List<String> destructureUri(@NonNull String uriPatternString, @NonNull Uri uri, boolean checkScheme) {
        List<String> result = new ArrayList<>();
        try {
            Uri uriPattern = Uri.parse(uriPatternString);
            if (uriPattern == null) {
                return result;
            }
            if (checkScheme && uriPattern.getScheme() != null && !uriPattern.getScheme().equals(uri.getScheme())) {
                return result;
            }
            int i = 0;
            int patternSegmentSize = uriPattern.getPathSegments().size();
            int uriSegmentSize = uri.getPathSegments().size();
            if (patternSegmentSize != uriSegmentSize) {
                return result;
            }
            while (i < patternSegmentSize) {
                if (uriPattern.getPathSegments().get(i).startsWith("{") &&
                        uriPattern.getPathSegments().get(i).endsWith("}")) {
                    result.add(uri.getPathSegments().get(i));
                }
                i++;
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static List<String> destructureUri(@NonNull String uriPatternString, @NonNull Uri uri) {
        return destructureUri(uriPatternString, uri, true);
    }

    public static Map<String, Object> destructureUriToMap(@NonNull String uriPatternString, @NonNull Uri uri, boolean checkScheme){
        Map<String, Object> result = new HashMap<>();
        try {
            Uri uriPattern = Uri.parse(uriPatternString);
            if (uriPattern == null) {
                return result;
            }
            if (checkScheme && uriPattern.getScheme() != null && !uriPattern.getScheme().equals(uri.getScheme())) {
                return result;
            }
            int uriSegmentSize = uri.getPathSegments().size();

            if (uriSegmentSize == 0) {
                uriSegmentSize = uriPattern.getQueryParameterNames().size();
                if(uriSegmentSize > 0){
                        Iterator itr = uriPattern.getQueryParameterNames().iterator();
                        while (itr.hasNext()){
                            String paramName = itr.next().toString();
                            Object paramValue = uri.getQueryParameter(paramName);
                            if(paramValue != null) {
                                result.put(paramName, paramValue);
                            }
                        }
                }
                else {
                    return result;
                }
            }
            else {
                Iterator itr = uriPattern.getPathSegments().iterator();
                int i = 0;
                while (itr.hasNext()) {
                    String segmentName = itr.next().toString();
                    if (segmentName.startsWith("{") &&
                            segmentName.endsWith("}")) {
                        result.put(segmentName ,
                                uri.getPathSegments().get(i));
                    }
                }
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    /**
     * Build pattern uri to uri String
     *
     * @param uri             example: "tokopedia-android-internal://merchant/product/edit/"
     * @param queryParameters example: mapOf ("userId" to "222")
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
