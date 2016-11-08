package com.tokopedia.core.session.model;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 18/11/2015.
 */
@Parcel
public class ForgotPasswordViewModel {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
