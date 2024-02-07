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
      // TTNet内部日志调试开关，线上不要开启，仅在local_test渠道或debug模式下返回true
      // 必须实现
      return false;
   }

   @Override
   public String getDeviceId() {
      // 默认从LogSDK内部获取did，宿主App需要保证LogSDK正常初始化
      // 必须实现
//      return TeaAgent.getServerDeviceId();
      return AppLog.getDid();
   }

   @Override
   public String getUserId() {
      // 默认从LogSDK内部获取uid，宿主App需要保证LogSDK正常初始化
      // 必须实现
//      return AppLog.getUserId();
      return "";
   }

   @Override
   public String getAppId() {
      // AppID直接hardcode即可，不要从LogSDK中获取（隐私弹窗未同意前无法从LogSDK拿到aid）
      // 必须实现
      return "573733";
   }

   @Override
   public String getAppName() {
      // App名称（必须实现）
      return "Tokopedia";
   }

   @Override
   public String getChannel() {
      // App发布渠道（必须实现）
      return "local_test";
   }

   @Override
   public String getVersionCode() {
      // App内部版本号，返回接入app版本号（必须实现）
      return "100";
   }

   @Override
   public String getVersionName() {
      // App内部版本名称，返回接入app版本名称（必须实现）
      return "1.0.0";
   }

   @Override
   public String getUpdateVersionCode() {
      // App灰度版本号，返回接入app灰度版本号
      return "10000";
   }

   @Override
   public String getManifestVersionCode() {
      // App对外发布版本号，返回接入app对外发布版本号
      return "1000";
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
