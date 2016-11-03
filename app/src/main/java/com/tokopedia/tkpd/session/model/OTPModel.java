package com.tokopedia.tkpd.session.model;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 05/11/2015.
 */
@Parcel
public class OTPModel {
    int success;

    public OTPModel(int success) {
        this.success = success;
    }

    public OTPModel(){}

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public static final int SUCCESS = 1;
    public static final int DEFAULT = 0;
}
