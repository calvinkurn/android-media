package com.tokopedia.network.ttnet;

import com.bytedance.frameworks.baselib.network.http.NetworkParams;
import com.bytedance.ttnet.http.HttpRequestInfo;

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
//                 try {
//                    String[] remoteIp = new String[1];
//                    int status = ConvertIOException.ConvertIOExceptionToStatus(e, remoteIp);
//                    JSONObject jsonObject = new JSONObject();
//                    if (e != null && !StringUtils.isEmpty(e.getClass().getName())) {
//                       jsonObject.put("ex_name", e.getClass().getName());
//                       if ((status == 1 && ApmDelegate.getInstance().getLogTypeSwitch("ex_message_open"))
//                               || ApmDelegate.getInstance().getLogTypeSwitch("debug_ex_message_open")) {
//                          String str = TtnetUtil.outputThrowableStackTrace(e);
//                          if (!StringUtils.isEmpty(str)) {
//                             jsonObject.put("ex_message", str);
//                          }
//                          String cronetExceptionMessage = com.bytedance.ttnet.HttpClient.getCronetExceptionMessage();
//                          if (!StringUtils.isEmpty(cronetExceptionMessage)) {
//                             jsonObject.put("cronet_init_ex_message", cronetExceptionMessage);
//                          }
//                       }
//                    }
//                    if (StringUtils.isEmpty(remoteIp[0])) {
//                       remoteIp[0] = info.remoteIp;
//                    }
//                    packageRequestParamters(info, jsonObject);
//                    ApmAgent.monitorApiError(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
//                    ApmAgent.monitorSLA(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
//                 } catch (Throwable throwable) {
//
//                 }
                }

                @Override
                public void monitorApiOk(long duration, long sendTime, String url, String traceCode, HttpRequestInfo info) {
                    //网络请求成功监控回调，关联监控库上传监控日志
//                 try {
//                    String[] remoteIp = new String[1];
//                    int status = HttpStatus.SC_OK;
//                    JSONObject jsonObject = new JSONObject();
//                    if (StringUtils.isEmpty(remoteIp[0])) {
//                       remoteIp[0] = info.remoteIp;
//                    }
//                    packageRequestParamters(info, jsonObject);
//                    ApmAgent.monitorSLA(duration, sendTime, url, remoteIp[0], traceCode, status, jsonObject);
//                 } catch (Throwable throwable) {
//
//                 }
                }
            };

    public static final NetworkParams.ApiProcessHook<HttpRequestInfo> sApiProcessHook = new IESCronetApiProcessHook();

}
