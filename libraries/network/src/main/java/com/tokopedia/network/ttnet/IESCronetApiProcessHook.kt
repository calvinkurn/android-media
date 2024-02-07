package com.tokopedia.network.ttnet

import com.bytedance.applog.AppLog
import com.bytedance.bdinstall.Level
import com.bytedance.frameworks.baselib.network.http.NetworkParams
import com.bytedance.ttnet.http.HttpRequestInfo

/**
 * Created by longlong on 2017/9/28.
 */
class IESCronetApiProcessHook : NetworkParams.ApiProcessHook<HttpRequestInfo?> {
    override fun handleApiError(url: String, e: Throwable, time: Long, info: HttpRequestInfo?) {
        //don't need to implements
    }

    override fun handleApiOk(url: String, time: Long, info: HttpRequestInfo?) {
        //don't need to implements
    }

    override fun addCommonParams(url: String, isApi: Boolean): String {
        //only when get_domains and upload ttnet native crash
        var url = url
        if (url != null && (url.contains("/get_domains/") || url.contains("/ttnet_crash/"))) {
            url = AppLog.addNetCommonParams(AppLog.getContext(), url, isApi, Level.L0)
            return url
        }
        return url
    }

    override fun addRequestVertifyParams(url: String, isAddCommonParam: Boolean, vararg extra: Any): String {
        return url
    }

    override fun putCommonParams(params: Map<String, String>, isApi: Boolean) {
        //don't need to implements
//        com.ss.android.common.applog.NetUtil.putCommonParams(params, isApi);
    }

    override fun onTryInit() {
        //don't need to implements
    }

    override fun getCommonParamsByLevel(level: Int): Map<String, String>? {
        return null
    }
}
