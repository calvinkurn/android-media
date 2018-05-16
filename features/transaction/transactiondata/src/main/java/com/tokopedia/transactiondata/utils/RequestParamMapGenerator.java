package com.tokopedia.transactiondata.utils;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

/**
 * @author anggaprasetiyo on 16/05/18.
 */
public class RequestParamMapGenerator {
    Context context;
    UserSession userSession;

    protected TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ? AuthUtil.generateParamsNetwork(context, userSession.getUserId(), userSession.getDeviceId())
                : AuthUtil.generateParamsNetwork(context, originParams, userSession.getUserId(), userSession.getDeviceId());
    }
}
