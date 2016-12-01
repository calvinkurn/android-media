package com.tokopedia.core.network;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class NetworkConfig {
	
	private LocalCacheHandler config;
	private int Timeout = 10000;
	private int MaxRetries = 2;
	private float BackoffMulti = 1.0f;
	private Context context;
	private String configName;
	
	public NetworkConfig (Context context, String url) {
		this.context = context;
		String urlEnc = url.replace("/", "~");
		configName = "CONFIG_"+urlEnc;
		config = new LocalCacheHandler(context, TkpdCache.NETWORK_HANDLER_CONFIG_GENERAL);
	}
	
	public NetworkConfig (Context context) {
		this.context = context;
		configName = "";
		config = new LocalCacheHandler(context, TkpdCache.NETWORK_HANDLER_CONFIG_GENERAL);
	}
	
	public void UpdateConfigFromJSON(JSONObject Json) {
		if (!configName.equals("")) {
			try {
				config.putInt(TkpdCache.Key.TIMEOUT, Json.getInt("timeout"));
				config.putInt(TkpdCache.Key.MAX_RETRIES, Json.getInt("max_retries"));
				config.putFloat(TkpdCache.Key.MULTI, Float.parseFloat(Json.getString("backoff_multi")));
				config.applyEditor();
				//CommonUtils.UniversalToast(context, Json.toString(1));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getLatestConfig() {
/*		if (false) {
			NetworkHandler network = new NetworkHandler(context, TkpdUrl.RETRY_CONFIG);
			network.disableRetry();
			network.setRetryPolicy(10000, 2, 1.0f);
			network.Commit(new NetworkHandlerListener() {
				
				@Override
				public void onSuccess(Boolean status) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void getResponse(JSONObject Result) {
					try {
						config.putInt(TkpdCache.Key.TIMEOUT, Result.getInt("timeout"));
						config.putInt(TkpdCache.Key.MAX_RETRIES, Result.getInt("max_retries"));
						config.putFloat(TkpdCache.Key.MULTI, Float.parseFloat(Result.getString("backoff_multi")));
						config.applyEditor();
						config.setExpire(3600000);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void getMessageError(ArrayList<String> MessageError) {
					// TODO Auto-generated method stub
					
				}
			});
		}*/
	}
	
	public int getTimeout() {
		LocalCacheHandler config_spec = new LocalCacheHandler(context, configName);
		return config_spec.getInt(TkpdCache.Key.TIMEOUT, getConfigTimeout());
	}
	
	public int getMaxRetries() {
		LocalCacheHandler config_spec = new LocalCacheHandler(context, configName);
		return config_spec.getInt(TkpdCache.Key.MAX_RETRIES, getConfigMaxRetries());
	}
	
	public float getBackOffMulti() {
		LocalCacheHandler config_spec = new LocalCacheHandler(context, configName);
		return config_spec.getFloat(TkpdCache.Key.MULTI, getConfigBackOffMulti());
	}
	
	private int getConfigTimeout() {
		return config.getInt(TkpdCache.Key.TIMEOUT, Timeout);
	}
	
	private int getConfigMaxRetries() {
		return config.getInt(TkpdCache.Key.MAX_RETRIES, MaxRetries);
	}
	
	private float getConfigBackOffMulti() {
		return config.getFloat(TkpdCache.Key.MULTI, BackoffMulti);
	}

}
