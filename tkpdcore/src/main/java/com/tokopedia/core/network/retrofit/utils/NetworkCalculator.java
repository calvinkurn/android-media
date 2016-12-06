
package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.network.v4.NetworkConfigUtil;
import com.tokopedia.core.util.SessionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by m.normansyah on 27/11/2015.
 * This is isn't contain any context
 */
public class NetworkCalculator {
    public static final String messageTAG = NetworkCalculator.class.getSimpleName();

    // instance for some data processing
    private final static Pattern p = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
    public static final String CONTENT_MD5 = "Content-MD5";
    public static final String DATE = "Date";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_METHOD = "X-Method";
    public static final String USER_ID = "user_id";
    public static final String DEVICE_ID = "device_id";
    public static final String HASH = "hash";
    public static final String DEVICE_TIME = "device_time";

    // flag to determine if api need login or not
    int isNeedLogin;
    // flag that indicate if all things needed to process already gathered.
    int isAllParamSupply;
    // flag that indicate that listener for returned value from server
    boolean isListenerSet;

    // value for GET or POST
    // reduce amount of memory using integer for Network Type flag
    private int NetworkType;

    // full url need to save in order to get last segment
    private String url;

    // variable that need context initiate at constructor
    private String userId;
    private String deviceId;

    // md5 from param content stored here
    String Content_MD5;

    Map<String, String> header;
    Map<String, String> content;

    boolean withoutBuilder;

    int step;

    /**
     * This is for before login
     * @param NetworkType
     * @param deviceId
     * @param url
     */
    public NetworkCalculator(int NetworkType, String deviceId, String url){
        this(NetworkType, null, deviceId, url, false);
    }

    /**
     * This is replacement for Old NetworkHandlerBuilder, use this for after login only
     * @param NetworkType
     * @param context
     * @param url
     */
    public NetworkCalculator(int NetworkType, Context context, String url){
        this(NetworkType, getUserId(context), getDeviceId(context), url, false);
    }

    public NetworkCalculator(int NetworkType, String userId, String deviceId, String url){
        this(NetworkType, userId, deviceId, url, false);
    }

    /**
     * This is for after login
     * @param NetworkType
     * @param userId
     * @param deviceId
     * @param url
     */
    public NetworkCalculator(int NetworkType, String userId, String deviceId, String url, boolean withoutBuilder){
        Matcher matcher = p.matcher(url);
        if(!matcher.find()){
            throw new RuntimeException(messageTAG+" need to supply valid url!!!");
        }
        this.NetworkType = NetworkType;
        this.url = url;
        this.userId = userId==null?"":userId;
        this.deviceId = deviceId;

        header = new HashMap<>();
        content = new HashMap<>();

        this.withoutBuilder = withoutBuilder;
        step++;// increase step
    }

    public NetworkCalculator setIdentity(){
        if(!withoutBuilder && step!=1)
            throw new RuntimeException("please call provided constructor !!!");
        step++;// increase step

        content.put(USER_ID, userId);
        content.put(DEVICE_ID, deviceId);
        content.put(HASH, NetworkConfigUtil.md5(userId + "~" + deviceId));
        content.put(DEVICE_TIME,(System.currentTimeMillis() / 1000L) + "");

        return this;
    }

    public NetworkCalculator compileAllParam(){
        if(!withoutBuilder &&step!=2)
            throw new RuntimeException("please call setIdentity !!!");
        step++;// increase step

        Content_MD5 = NetworkConfigUtil.md5(content.toString());

        Date currentDate = new Date();
        String act = "";
        String contentType = "";
        String tkpdUrl = NetworkConfigUtil.generateTkpdPath(url);
        if(NetworkType== NetworkConfig.GET) {
            act += "GET";
            contentType = "";// to indicate that it just blank
        }else {
            act += "POST";
            contentType += "application/x-www-form-urlencoded";
        }

        String signToString = NetworkConfigUtil.generateTkpdAuthString(act, Content_MD5, contentType, dateFormat.format(currentDate), tkpdUrl);
        String signature = NetworkConfigUtil.generateHMACSignature(signToString);

        header.put(CONTENT_MD5, Content_MD5);
        header.put(DATE, dateFormat.format(currentDate));
        header.put(AUTHORIZATION, "TKPD Tokopedia:" + signature);
        header.put(X_METHOD, act);

        return this;
    }

    @Override
    public String toString() {
        return "NetworkCalculator{" +
                "header=" + header +
                ", content=" + content +
                '}';
    }

    public NetworkCalculator addParam(String key, String value){
        if(key==null||value==null){
            throw new RuntimeException(messageTAG+" need to supply non null key and value !!!");
        }
        content.put(key, value);
        return this;
    }

    public NetworkCalculator finish(){
        if(step!=3)
            throw new RuntimeException("please call compileAllParam !!!");

        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public static String getUserId(Context context){
        return SessionHandler.getLoginID(context);
    }

    public static String getDeviceId(Context context){
        return GCMHandler.getRegistrationId(context);
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public static String getHash(NetworkCalculator networkCalculator){
        return networkCalculator.getContent().get(HASH);
    }

    public static String getDeviceTime(NetworkCalculator networkCalculator){
        return networkCalculator.getContent().get(DEVICE_TIME);
    }

    public static String getContentMd5(NetworkCalculator networkCalculator){
        return networkCalculator.getHeader().get(CONTENT_MD5);
    }

    public static String getDate(NetworkCalculator networkCalculator){
        return networkCalculator.getHeader().get(DATE);
    }

    public static String getAuthorization(NetworkCalculator networkCalculator){
        return networkCalculator.getHeader().get(AUTHORIZATION);
    }

    public static String getxMethod(NetworkCalculator networkCalculator){
        return networkCalculator.getHeader().get(X_METHOD);
    }
}
