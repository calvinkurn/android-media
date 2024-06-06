package com.tokopedia.network.ttnet;

import com.bytedance.apm.ApmAgent;
import com.bytedance.apm.internal.ApmDelegate;
import com.bytedance.common.utility.Logger;
import com.bytedance.common.utility.StringUtils;
import com.bytedance.frameworks.baselib.network.http.NetworkParams;
import com.bytedance.frameworks.baselib.network.http.util.HttpStatus;
import com.bytedance.ttnet.TTNetInit;
import com.bytedance.ttnet.http.HttpRequestInfo;
import com.bytedance.ttnet.utils.TtnetUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class InitTTNetHelper {
    private static final String TAG = InitTTNetHelper.class.getSimpleName();

    private static final int
            ALOG_MAX_DIR_SIZE = 20 * 1024 * 1024;
    private static final int ALOG_PER_FILE_SZIE = 2 * 1024 * 1024;

    public static NetworkParams.MonitorProcessHook<HttpRequestInfo> sMonitorProcessHook =
            new NetworkParams.MonitorProcessHook<HttpRequestInfo>() {
                @Override
                public void monitorApiError(long duration, long sendTime, String url,
                                            String traceCode, HttpRequestInfo info, Throwable e) {
                    //网络请求错误监控回调，关联监控库上传监控日志
                 try {
                    String[] remoteIp = new String[1];
                    int status = 38;
                    JSONObject jsonObject = new JSONObject();
                    if (e != null && !StringUtils.isEmpty(e.getClass().getName())) {
                       jsonObject.put("ex_name", e.getClass().getName());
                       if ((status == 1 && ApmDelegate.getInstance().getLogTypeSwitch("ex_message_open"))
                               || ApmDelegate.getInstance().getLogTypeSwitch("debug_ex_message_open")) {
                          String str = TtnetUtil.outputThrowableStackTrace(e);
                          if (!StringUtils.isEmpty(str)) {
                             jsonObject.put("ex_message", str);
                          }
                          String cronetExceptionMessage = com.bytedance.ttnet.HttpClient.getCronetExceptionMessage();
                          if (!StringUtils.isEmpty(cronetExceptionMessage)) {
                             jsonObject.put("cronet_init_ex_message", cronetExceptionMessage);
                          }
                       }
                    }
                    if (StringUtils.isEmpty(remoteIp[0])) {
                       remoteIp[0] = info.remoteIp;
                    }
                    packageRequestParamters(info, jsonObject);
                    ApmAgent.monitorApiError(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
                    ApmAgent.monitorSLA(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
                 } catch (Throwable throwable) {

                 }
                }

                @Override
                public void monitorApiOk(long duration, long sendTime, String url, String traceCode, HttpRequestInfo info) {
                    //网络请求成功监控回调，关联监控库上传监控日志
                 try {
                    String[] remoteIp = new String[1];
                    int status = HttpStatus.SC_OK;
                    JSONObject jsonObject = new JSONObject();
                    if (StringUtils.isEmpty(remoteIp[0])) {
                       remoteIp[0] = info.remoteIp;
                    }
                    packageRequestParamters(info, jsonObject);
                    ApmAgent.monitorSLA(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
                 } catch (Throwable throwable) {

                 }
                }
            };

    public static final NetworkParams.ApiProcessHook<HttpRequestInfo> sApiProcessHook = new IESCronetApiProcessHook();

    private static void packageRequestParamters(HttpRequestInfo info, JSONObject jsonObject) {
        if (info == null || jsonObject == null) {
            return;
        }
        try {
            jsonObject.put("cronet_plugin_install", TTNetInit.getTTNetDepend().isCronetPluginInstalled());
            jsonObject.put("appLevelRequestStart", info.appLevelRequestStart);
            jsonObject.put("beforeAllInterceptors", info.beforeAllInterceptors);
            jsonObject.put("requestStart", info.requestStart);
            jsonObject.put("responseBack", info.responseBack);
            jsonObject.put("completeReadResponse", info.completeReadResponse);
            jsonObject.put("requestEnd", info.requestEnd);
            jsonObject.put("recycleCount", info.recycleCount);
            jsonObject.put("timing_dns", info.dnsTime);
            jsonObject.put("timing_connect", info.connectTime);
            jsonObject.put("timing_ssl", info.sslTime);
            jsonObject.put("timing_send", info.sendTime);
            jsonObject.put("timing_waiting", info.ttfbMs);
            jsonObject.put("timing_receive", info.receiveTime);
            jsonObject.put("timing_total", info.totalTime);
            jsonObject.put("timing_isSocketReused", info.isSocketReused);
            jsonObject.put("timing_totalSendBytes", info.sentByteCount);
            jsonObject.put("timing_totalReceivedBytes", info.receivedByteCount);
            jsonObject.put("timing_remoteIP", info.remoteIp);
            jsonObject.put("native_post_task_start", info.nativePostTaskStartTime);
            jsonObject.put("native_request_start", info.nativeRequestStartTime);
            jsonObject.put("native_wait_ctx", info.nativeWaitContext);
            jsonObject.put("request_log", info.requestLog);
            jsonObject.put("content_type", info.contentType);
            if (info.extraInfo != null) {
                jsonObject.put("req_info", info.extraInfo);
            }
            jsonObject.put("streaming", info.downloadFile);
        } catch (JSONException e) {
            Logger.w(TAG, "[packageRequestParamters] json op error. ", e);
        }
    }

}
