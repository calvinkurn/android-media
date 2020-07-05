package com.tokopedia.applink;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtil {

    private final static String ENCODING = "UTF-8";
    private final static String QUERY_PARAM_SEPRATOR = "&";

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

    /**
     * Destructure uri to Bundle
     *
     * @param uriPatternString example: "tokopedia-android-internal://marketplace/shop/{id_1}/etalase/{id_2}/"
     * @param uri              example: "tokopedia-android-internal://marketplace/shop/123/etalase/345"
     * @param bundle            it can be nullable if not then it will add key value in this bundle
     * @return bundle of ("id_1":123,"id_2":345)
     */

    public static Bundle destructiveUriBundle(@NonNull String uriPatternString, Uri uri, Bundle bundle){
        try {
            if (bundle == null)
                bundle = new Bundle();
            Uri uriPattern = Uri.parse(uriPatternString);
            if (uriPattern == null) {
                return bundle;
            }

            int size = Math.min(uri.getPathSegments().size(), uriPattern.getPathSegments().size());
            int i = 0;
            while (i < size) {
                if (uriPattern.getPathSegments().get(i).startsWith("{") &&
                        uriPattern.getPathSegments().get(i).endsWith("}")) {
                    bundle.putString(uriPattern.getPathSegments().get(i).substring(1, uriPattern.getPathSegments().get(i).length() - 1), uri.getPathSegments().get(i));
                }
                i++;
            }
        } catch (Exception e){
            return bundle;
        }

        return bundle;

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
                        result.put(segmentName.substring(1, segmentName.length() - 1) ,
                                uri.getPathSegments().get(i));
                    }
                    i++;
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

    public static String buildUriAppendParams(@NonNull String uri,
                                              @Nullable Map<String, Object> queryParameters) {
        StringBuilder stringBuilder = new StringBuilder(uri);
        if (queryParameters != null && queryParameters.size() > 0) {
            stringBuilder.append("?");
            int i = 0;
            for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
                if (i > 0) stringBuilder.append("&");
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> uriQueryParamsToMap(@NonNull String url) {
        Map<String, String> map = new HashMap<>();
        try {
            Uri uri = Uri.parse(url);
            String query = uri.getQuery();
            if (!TextUtils.isEmpty(query)) {
                String[] pairs = query.split(QUERY_PARAM_SEPRATOR);
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    map.put(URLDecoder.decode(pair.substring(0, idx), ENCODING), URLDecoder.decode(pair.substring(idx + 1), ENCODING));
                }
            }
            return map;
        } catch (Exception e) {

        }
        return map;
    }

}
