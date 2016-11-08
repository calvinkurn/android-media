package com.tokopedia.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.network.v4.NetworkHandler;
import com.tokopedia.core.network.v4.NetworkHandlerBuilder;
import com.tokopedia.core.network.v4.OnNetworkResponseListener;
import com.tokopedia.core.network.v4.OnNetworkTimeout;
import com.tokopedia.core.service.model.GetVerificationNumberForm;

import org.json.JSONObject;

import java.util.ArrayList;

import com.tkpd.library.kirisame.network.entity.NetError;

/**
 * @author Nanda Julianda Akbar
 * @since 11/06/2015
 * modified by c6f8406	29/09/2015 17:12	hangnadi	import class
 * modified by m.normansyah 23-11-2015, change to V4
 */
@Deprecated
public class CheckVerification extends Service{
	public static final String VERIFICATION_STATUS = "VERIFICATION_STATUS";
	public static final String VERIFIED = "verified";
	public static final String ALLOW_SHOP = "allow_shop";
	private LocalCacheHandler cache;
	private final int showDialogIdealCondition = 0;
	private final int verificationIdealCondition = 1;
	String TAG = "MNORMANSYAH";
	String messageTAG = "CheckVerification : ";

	Gson gson;

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not Yet Implemented");
	}

	public CheckVerification() {

	}

	@Override
	public void onCreate() {
		cache = new LocalCacheHandler(this, VERIFICATION_STATUS);
		if (!cache.isExpired()) {
			CommonUtils.dumper("NOT EXPIRED");
		} else {
			CommonUtils.dumper("EXPIRED");
			CheckVerificationStatus();
			cache.setExpire(604800);
		}

	}

	private void CheckVerificationStatus() {
		gson = new GsonBuilder().create();
		boolean isNeedLogin = true;
		NetworkHandler networkHandler = new NetworkHandlerBuilder(NetworkConfig.GET, this,
				TkpdBaseURL.User.URL_MSISDN+ TkpdBaseURL.User.PATH_GET_VERIFICATION_NUMBER_FORM)
				.setNeedLogin(isNeedLogin)
				.setIdentity()
				.setAllParamSupply(true)
				.setNetworkResponse(new OnNetworkResponseListener() {
					@Override
					public void onResponse(JSONObject Response) {
						Log.d(TAG, messageTAG + Response.toString());
						JSONObject jsonObject = Response.optJSONObject(GetVerificationNumberForm.MSISDN_TAG);
						GetVerificationNumberForm form = null;
						if (jsonObject != null) {
							form = gson.fromJson(jsonObject.toString(), GetVerificationNumberForm.class);
							CommonUtils.dumper("MASUK GET VERIFICATION STATUS");
							CommonUtils.dumper("SHOW DIALOGNYA " + Integer.toString(form.getShowDialog()));
							putToCache(form.getShowDialog(), ALLOW_SHOP);
							putToCache(form.getIsVerified(), VERIFIED);
						}
						stopSelf();
					}

					@Override
					public void onMessageError(ArrayList<String> MessageError) {
						CommonUtils.dumper("FAILED GET VERIFICATION STATUS "+MessageError.toString());
						stopSelf();
					}

					@Override
					public void onNetworkError(NetError error, int errorCode) {
						CommonUtils.dumper("FAILED GET VERIFICATION STATUS "+error);
						stopSelf();
					}
				})
				.setNetworkTimeout(new OnNetworkTimeout() {
					@Override
					public void onNetworkTimeout(com.tokopedia.core.network.v4.NetworkHandler network) {
						CommonUtils.dumper("TIMEOUT GET VERIFICATION STATUS");
						stopSelf();
					}
				})
				.setRetryPolicy(DownloadService.TIMEOUT_TIME, DownloadService.NUMBER_OF_TRY)
				.finish();
		networkHandler.commit();
	}
	private void putToCache(int condition,  String cacheKey){
		cache.putBoolean(cacheKey, condition==1?true:false);
		cache.applyEditor();
	}
	private void putToCache(int condition, int idealCondition, String cacheKey){
		if(condition != idealCondition){
			cache.putBoolean(cacheKey, false);
			cache.applyEditor();
		}else{
			cache.putBoolean(cacheKey, true);
			cache.applyEditor();
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
