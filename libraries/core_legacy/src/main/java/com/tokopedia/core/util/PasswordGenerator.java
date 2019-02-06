package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.tkpd.library.utils.legacy.CommonUtils;

import java.util.UUID;

@Deprecated
public class PasswordGenerator {
	
	private Context context;
	private PGListener listener;
	private static final String PG_STORAGE = "PG_STORAGE";
	private static final String VERSION_STORAGE = "VERSION_STORAGE";
	private String uniqueID = null;
	byte[] raw = new byte[] {'r','r','r','r','g','g','g','g','t','t','t','t','t','u','j','k'};   
	byte[] rawPassword = new byte[] {'t','t','t','t','t','r','r','r','r','g','g','g','g','u','j','k'};   
	
	public PasswordGenerator(Context context) {
		this.context = context;
	}
	
	public interface PGListener {
		public void onSuccess(int status);
	}

	public void generateAPPID(PGListener pglistener) {
		this.listener = pglistener;
	    if (uniqueID == null) {
	        final SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
	        final SharedPreferences sharedPrefsVer = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
	        uniqueID = sharedPrefs.getString("APP_ID", null);
	        if (uniqueID == null) {
	            uniqueID = UUID.randomUUID().toString();
        		final String signature = EncoderDecoder.Encrypt(getUniquePsuedoID(), uniqueID.substring(0, 16), raw);
                Editor editor = sharedPrefs.edit();
                editor.putString("APP_ID", uniqueID);
                editor.putString("SIGNATURE", signature);
                editor.commit();

                Editor editorVer = sharedPrefsVer.edit();
                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    int versionCode = pInfo.versionCode;
                    editorVer.putInt("VERSION_CODE", versionCode);
                    editorVer.commit();
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                listener.onSuccess(1);

	        }
	    }
	}



	public String getUniquePsuedoID() {
	    // If all else fails, if the user does have lower than API 9 (lower
	    // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
	    // returns 'null', then simply the ID returned will be solely based
	    // off their Android device information. This is where the collisions
	    // can happen.
	    // Thanks http://www.pocketmagic.net/?p=1662!
	    // Try not to use DISPLAY, HOST or ID - these items could change.
	    // If there are collisions, there will be overlapping data
	    String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

	    // Thanks to @Roman SL!
	    // http://stackoverflow.com/a/4789483/950427
	    // Only devices with API >= 9 have android.os.Build.SERIAL
	    // http://developer.android.com/reference/android/os/Build.html#SERIAL
	    // If a user upgrades software or roots their phone, there will be a duplicate entry
	    String serial = null;
	    try
	    {
	        serial = Build.class.getField("SERIAL").get(null).toString();

	        // Go ahead and return the serial for api => 9
	        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
	    }
	    catch (Exception e)
	    {
	        // String needs to be initialized
	        serial = "serial"; // some value
	    }

	    // Thanks @Joe!
	    // http://stackoverflow.com/a/2853253/950427
	    // Finally, combine the values we have found by using the UUID class to create a unique identifier
	    return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
	}
	
	public synchronized String getAppId() {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
		 uniqueID = sharedPrefs.getString("APP_ID", null);
		 return uniqueID;
	}
	
	public static String getAppId(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
		 return sharedPrefs.getString("APP_ID", null);
	}
	
	public String getSignature() {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
		 String signature = sharedPrefs.getString("SIGNATURE", null);
		 signature = EncoderDecoder.Decrypt(signature, getAppId().substring(0,16), raw);
		 return signature;
	}
	
	public String generatePassword(String sk) {
		String password = getAppId()+"~"+sk+"~"+getSignature();
		String iv = getAppId().substring(0,8)+getSignature().substring(0,8);
		CommonUtils.dumper("IV: " + iv);
		String enc_password = EncoderDecoder.Encrypt(password, iv, rawPassword);
		return enc_password.replaceAll("\n", "");
	}

    public static void clearTokenStorage(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();
    }



}
