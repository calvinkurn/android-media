package com.tokopedia.network.ttnet;

import android.content.Context;

import com.bytedance.ttnet.ITTNetDepend;
import com.bytedance.ttnet.TTNetInit;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TTNetDependencyAdapter implements ITTNetDepend {
   private Context mContext;

   public TTNetDependencyAdapter(Context context) {
      mContext = context;
   }

   @Override
   public Context getContext() {
      return mContext;
   }

   /*
    * 非插件下发cronet的情况下直接返回true
    * 插件化接入的情况下，需要在插件成功加载后返回true
    */
   @Override
   public boolean isCronetPluginInstalled() {
      return true;
   }

   @Override
   public boolean isPrivateApiAccessEnabled() {
      return false;
   }

   @Override
   public void mobOnEvent(Context context, String eventName, String labelName, JSONObject extraJson) {

   }

   @Override
   @Deprecated
   public void onNetConfigUpdate(JSONObject config, boolean localData) {

   }

   @Override
   @Deprecated
   public void onAppConfigUpdated(Context context, JSONObject ext_json) {

   }

   @Override
   public void onShareCookieConfigUpdated(String shareCookieHosts) {

   }

   @Override
   @Deprecated
   public String executeGet(int maxLength, String url) throws Exception {
      return null;
   }

   @Override
   @Deprecated
   public int checkHttpRequestException(Throwable tr, String[] remoteIp) {
      return 0;
   }

   @Override
   public void monitorLogSend(String logType, JSONObject json) {

   }

   @Override
   public String getProviderString(Context context, String key, String defaultValue) {
      // todo for cross-process
      return null;
   }

   @Override
   public int getProviderInt(Context context, String key, int defaultValue) {
      // todo for cross-process
      return 0;
   }

   @Override
   public void saveMapToProvider(Context context, Map<String, ?> map) {
      // todo for cross-process
   }

   /*
    * 必须实现，否则会抛出异常
    * 默认返回API请求二级域名后缀
    * 待废弃，老业务有依赖
    */
   @Override
   public String getHostSuffix() {
      return ".tiktokv.com";
   }

   @Override
   public String getApiIHostPrefix() {
      return "ib";
   }

   @Override
   public String getCdnHostSuffix() {
      return null;
   }

   @Override
   public Map<String, String> getHostReverseMap() {
      return new HashMap<>();
   }

   @Override
   public String getShareCookieMainDomain() {
      return "";
   }

   @Override
   public void onColdStartFinish() {

   }

   @Override
   public ArrayList<String> getCookieFlushPathList() {
      return null;
   }

   @Override
   public int getAppId() {
      return 573733;
   }

   /*
    * 注入TNC服务域名，必须实现
    * 以下注入的是主业务在亚洲地区的推荐域名
    */
   @Override
   public String[] getConfigServers() {
      return new String[]{"tnc16-platform-alisg.tiktokv.com",
              "tnc16-platform-useast1a.tiktokv.com"};
   }

   @Override
   public Map<String, String> getTTNetServiceDomainMap() {
      Map<String, String> domainMap = new HashMap<String, String>();
      domainMap.put(TTNetInit.DOMAIN_HTTPDNS_KEY, "34.102.215.99");
      domainMap.put(TTNetInit.DOMAIN_NETLOG_KEY, "ttnet.tiktokv.com");
//      if (DebugConfig.boeCanUse()) {
//         domainMap.put(TTNetInit.DOMAIN_BOE_KEY, ".boe-gateway.byted.org");
//      } else {
         domainMap.put(TTNetInit.DOMAIN_BOE_KEY, "xxx");
//      }
      return domainMap;
   }
}