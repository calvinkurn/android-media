package com.tokopedia.relic.track;

import com.newrelic.agent.android.NewRelic
import java.util.Map;

public class NewRelicUtil {
    public static void sendTrack(String event, Map<String, Object> map) {
        NewRelic.recordBreadcrumb(eventName, map);
    }
}
