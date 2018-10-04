package com.tokopedia.instantloan.ddcollector;

import java.util.Map;

public interface OnDeviceDataReady {
    void callback(Map<String, Object> data);
}