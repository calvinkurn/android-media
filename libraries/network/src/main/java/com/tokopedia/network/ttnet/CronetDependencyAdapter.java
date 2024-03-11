package com.tokopedia.network.ttnet;

import com.bytedance.applog.AppLog;
import com.bytedance.ttnet.cronet.AbsCronetDependAdapter;

import org.chromium.CronetAppProviderManager;
import org.chromium.CronetDependManager;

public class CronetDependencyAdapter extends AbsCronetDependAdapter {
   public static CronetDependencyAdapter INSTANCE = new CronetDependencyAdapter();

   public static void inject() {
      CronetDependManager.inst().setAdapter(INSTANCE);
      CronetAppProviderManager.inst().setAdapter(INSTANCE);
   }

   @Override
   public boolean loggerDebug() {
      // always return false unless it's on debug mode
      return false;
   }

   @Override
   public String getDeviceId() {
      return AppLog.getDid();
   }

   @Override
   public String getUserId() {
      //todo, plz use Tokopedia Account service to return this value
      return "";
   }

   @Override
   public String getAppId() {
      return "573733";
   }

   @Override
   public String getAppName() {
      return "Tokopedia";
   }

   @Override
   public String getChannel() {
      // todo plz define your own channel enums: local_test, googleplay, xiaomi...
      return "local_test";
   }

   @Override
   public String getVersionCode() {
      // todo remind that every version needs to be read from manifest or other config file
      return "320309600";
   }

   @Override
   public String getVersionName() {
      // split by .
      // todo plz choose one of them
      return "3.96";
//      return "32.3.96.0.0";
   }

   @Override
   public String getUpdateVersionCode() {
      // same as getVersionCode (this update is for China domestic app only, so we use it same as version_code)
      return "320309600";
   }

   @Override
   public String getManifestVersionCode() {
      // App对外发布版本号，返回接入app对外发布版本号
      return "320309600";
   }

   @Override
   public void sendAppMonitorEvent(String logContent, String logType) {
      // 网络库cronet内核监控日志回调，必须实现
      // 宿主需调用monitor库接口回传TTNet内部日志
//      try {
//         ApmAgent.monitorCommonLog(logType, new JSONObject(logContent));
//      } catch (JSONException e) {
         // ignore
//      }
   }

   @Override
   public String getRegion() {
      // 海外业务region，用于TNC调度
      // 国内业务不需要实现
      return "ID";
   }

   @Override
   public String getSysRegion() {
      // SysRegion用于TNC调度
      // 国内业务不需要实现
      return "ID";
   }

   @Override
   public String getCarrierRegion() {
      // CarrierRegion用于TNC调度
      // 国内业务不需要实现
      return "ID";
   }

   @Override
   public String getStoreIdc() {
      // StoreIdc用于TNC调度
      // 国内业务不需要实现
      return "alisg";
   }
}
