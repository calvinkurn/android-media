package com.tokopedia.contactus.common.data.model;

import java.io.IOException;

/**
 * Created by sandeepgoyal.
 */

public class ContactUsException extends IOException {

    public ContactUsException(String message) {
        super(message);
    }
}
