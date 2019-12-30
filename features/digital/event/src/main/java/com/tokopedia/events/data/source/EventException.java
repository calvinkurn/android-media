package com.tokopedia.events.data.source;

import java.io.IOException;

/**
 * Created by sandeepgoyal.
 */

public class EventException extends IOException {

    public EventException(String message) {
        super(message);
    }
}
