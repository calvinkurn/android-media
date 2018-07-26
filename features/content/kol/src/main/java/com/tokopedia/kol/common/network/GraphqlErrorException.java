package com.tokopedia.kol.common.network;

/**
 * @author by milhamj on 19/04/18.
 */

public class GraphqlErrorException extends RuntimeException {
    public GraphqlErrorException(String errorMessage) {
        super(errorMessage);
    }
}
