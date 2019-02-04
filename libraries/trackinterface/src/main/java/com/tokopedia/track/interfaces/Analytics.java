package com.tokopedia.track.interfaces;

import java.util.Map;

public interface Analytics {
    void sendGeneralEvent(Map<String, Object> value);
}
