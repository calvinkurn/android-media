package com.tokopedia.tkpd.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.ForceUpdate;
import com.tokopedia.tkpd.MaintenancePage;
import com.tokopedia.tkpd.network.NetworkConfig;
import com.tokopedia.tkpd.service.CheckVerification;
import com.tokopedia.tkpd.util.RequestManager;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.analytics.TrackingUtils;
import com.tokopedia.tkpd.var.TkpdCache;
import com.tokopedia.tkpd.var.TkpdState;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TkpdFragmentActivity extends FragmentActivity{

	private Boolean isPause = false;
	private Boolean VerificationOpened;

	public TkpdFragmentActivity() {
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if(MaintenancePage.isMaintenance(this)) {
			startActivity(MaintenancePage.createIntent(this));
		}
    }

	@Override
    protected void onStart() {
		super.onStart();
		isPause = false;
		VerificationOpened = false;
		MainApplication.setActivityState(TkpdState.Application.ACTIVITY);
		MainApplication.setActivityname(this.getClass().getSimpleName());
		if (!MainApplication.isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		Resources resources = this.getResources();
		AssetManager assetManager = resources.getAssets();
		InputStream inputStream;
		try {
			Log.i("adwords tag", this.getClass().getSimpleName());
			Properties properties = new Properties();
			inputStream = assetManager.open("adwords-build.properties");
			properties.load(inputStream);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	@Override
    protected void onPause() {
		super.onPause();
		isPause = true;
		MainApplication.setActivityState(0);
		MainApplication.setActivityname(null);
    }
	
	
	@Override
	protected void onResume() {
		super.onResume();
		isPause = false;
		VerificationService();
		MainApplication.setActivityState(TkpdState.Application.FRAGMENT_ACTIVITY);
		MainApplication.setActivityname(this.getClass().getSimpleName());
		//RequestManager.retryRequestList(this.getClass().getSimpleName());
		NetworkConfig networkconfig = new NetworkConfig(this);
		networkconfig.getLatestConfig();
		LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.STATUS_UPDATE);
		if (cache.getInt(TkpdCache.Key.STATUS) == TkpdState.UpdateState.MUST_UPDATE) {
			Intent intent = new Intent(this, ForceUpdate.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(new Intent(this, ForceUpdate.class));
			finish();
		}

        CommonUtils.dumper(this.getClass().toString());
        initGTM();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isPause = true;
		RequestManager.clearRequestList(this.getClass().getSimpleName());
        RequestManager.cancelAllRequest();
	}
	
	public Boolean isPausing() {
		return isPause;
	}

	public void VerificationService(){
		if(SessionHandler.isV4Login(getBaseContext())){
			startService(new Intent(getBaseContext(), CheckVerification.class));
		}		
	}

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void initGTM () {
		TrackingUtils.eventPushUserID();
		TrackingUtils.eventOnline();
    }

}
