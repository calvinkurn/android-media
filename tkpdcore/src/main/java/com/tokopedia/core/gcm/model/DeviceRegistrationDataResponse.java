package com.tokopedia.core.gcm.model;

/**
 * @author  by alvarisi on 1/5/17.
 */

public class DeviceRegistrationDataResponse {
    private int statusCode;
    private String statusMessage;
    private String deviceRegistration;

    public DeviceRegistrationDataResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDeviceRegistration() {
        return deviceRegistration;
    }

    public void setDeviceRegistration(String deviceRegistration) {
        this.deviceRegistration = deviceRegistration;
    }
}
