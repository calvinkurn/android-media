package com.tokopedia.core.rescenter.inbox.facade;

import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.inbox.model.ResCenterInboxDataPass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 4/7/16.
 */
public class NetworkParam {

    private static final String PARAM_AS = "as";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SORT_TYPE = "sort_type";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_UNREAD = "unread";

    public static Map<String, String> paramGetInbox(@NonNull ResCenterInboxDataPass dataPass) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_AS, String.valueOf(dataPass.getRequestAs()));
        params.put(PARAM_PAGE, String.valueOf(dataPass.getRequestPage()));
        params.put(PARAM_SORT_TYPE, String.valueOf(dataPass.getSortType()));
        params.put(PARAM_STATUS, String.valueOf(dataPass.getFilterStatus()));
        params.put(PARAM_UNREAD, String.valueOf(dataPass.getReadUnreadStatus()));
        return params;
    }
}
