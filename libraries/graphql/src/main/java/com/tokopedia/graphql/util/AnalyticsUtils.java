package com.tokopedia.graphql.util;

import android.content.Context;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

public class AnalyticsUtils {

    public interface GtmKeys {
        String EVENT_NAME = "graphql";
        String EVENT_CATEGORY = "graphql_caching";
        String EVENT_ACTION = "graphql_serve_from_caching";
    }

    public static void sendEvent(String event, String category,
                                 String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label));
    }

    /**
     * To get label for gtm which include queryname along with size of response in Kbs
     *
     * @param queryName
     * @param rawResponse
     * @return
     */
    public static String getLabel(String queryName, String rawResponse) {
        if (queryName == null || rawResponse == null) {
            return "";
        }

        StringBuilder labelBuilder = new StringBuilder();

        labelBuilder.append(CacheHelper.getQueryName(queryName)).
                append(rawResponse.getBytes().length / 1024)
                .append(" Kb");

        return labelBuilder.toString();
    }
}
