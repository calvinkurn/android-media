package com.tokopedia.posapp.react.exception;

/**
 * Created by okasurya on 8/28/17.
 */

public class TableNotFoundException extends Exception {

    public static final String MESSAGE_TABLE_NOT_FOUND = "Table not found";

    public TableNotFoundException() {
        super(MESSAGE_TABLE_NOT_FOUND);
    }

    public TableNotFoundException(String message) {
        super(message);
    }
}
