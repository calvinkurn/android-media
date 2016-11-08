package com.tokopedia.tkpd.network.v4;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.gcm.GCMHandler;
import com.tokopedia.tkpd.util.SessionHandler;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tkpd.library.kirisame.network.entity.VolleyNetwork;

/**
 * Created by m.normansyah on 03/11/2015.
 * builder pattern needed for network handler
 *
 * Features :
 * 1. validating over url
 * 2. mandatory
 */
@Deprecated
public class NetworkHandlerBuilder {
    public String messageTAG = NetworkHandlerBuilder.class.getSimpleName();

    private NetworkHandler networkHandler;

    // instance for some data processing
    private final static Pattern p = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);

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

    /**
     * currently support action {@link NetworkConfig }.GET & {@link NetworkConfig}.POST
     * @param NetworkType {@link NetworkConfig }.GET & {@link NetworkConfig }.POST
     * @param context
     * @param url
     */
    public NetworkHandlerBuilder(int NetworkType, @NonNull Context context, String url){
        Matcher matcher = p.matcher(url);
        if(!matcher.find()){
            throw new RuntimeException(messageTAG+" need to supply valid url!!!");
        }
        if(context==null){
            throw new RuntimeException(messageTAG+" need to supply valid context!!!");
        }
        if(NetworkType==NetworkConfig.GET)
            networkHandler = NetworkHandlerFactory.createBasicGetNetworkHandler(context, url);
        else
            networkHandler = NetworkHandlerFactory.createBasicPostNetworkHandler(context, url);

        this.NetworkType = NetworkType;
        this.url = url;
        userId = SessionHandler.getLoginID(context);
        deviceId = GCMHandler.getRegistrationId(context);
    }

    /**
     * @param isNeedlogin true means NetworkConfig.LOGIN_NEEDED, false means NetworkConfig.LOGIN_BYPASS
     * @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setNeedLogin(boolean isNeedlogin){
        this.isNeedLogin = isNeedlogin ? NetworkConfig.LOGIN_NEEDED : NetworkConfig.LOGIN_BYPASS;
        return this;
    }

    /**
     * when Method specified as POST, then all param becomes body
     * when Method specified as GET, then all param becomes url-params
     * @param key non-null String key
     * @param value non-null String key
     * @return @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder addParam(String key, String value){
        if(key==null||value==null){
            throw new RuntimeException(messageTAG+" need to supply non null key and value !!!");
        }
        networkHandler.addParam(key, value);
        return this;
    }

    /**
     * after {@link NetworkHandlerBuilder}.setAllParamSupply() called, then
     * you could set the listener
     * @param listener non-null listener
     * @return @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setNetworkResponse(OnNetworkResponseListener listener){
        if(Content_MD5==null){
            throw new RuntimeException(messageTAG+" before set OnNetworkResponseListener please input param and header !!!");
        }
        isListenerSet = true;
        networkHandler.setOnNetworkResponseListener(listener);
        return this;
    }

    /**
     * after {@link NetworkHandlerBuilder}.setAllParamSupply() called, then
     * (optional) timeout
     * @param listener non null listener
     * @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setNetworkTimeout(OnNetworkTimeout listener){
        if(Content_MD5==null){
            throw new RuntimeException(messageTAG+" before set OnNetworkResponseListener please input param and header !!!");
        }
        networkHandler.setTimeoutListener(listener);
        return this;
    }

    /**
     * after {@link NetworkHandlerBuilder}.setAllParamSupply() called, then
     * (optional) retry policy
     * @param timeout time in milliseconds
     * @param maxcount number of retry
     * @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setRetryPolicy(int timeout, int maxcount){
        networkHandler.setRetryPolicy(timeout, maxcount);
        return this;
    }

    /**
     * before setListener then all param need to be compiled.
     * @param isAllParamSupply true means compiled every param, false means do nothing
     *  @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setAllParamSupply(boolean isAllParamSupply){
        this.isAllParamSupply = isAllParamSupply ? NetworkConfig.PARAM_ALL_PASSED : NetworkConfig.PARAM_ALL_NOT_PASSED;
        if(this.isAllParamSupply==NetworkConfig.PARAM_ALL_PASSED){
            Content_MD5 = NetworkConfigUtil.md5(networkHandler.getContent().toString());

            Date currentDate = new Date();
            String act = "";
            String contentType = "";
            String tkpdUrl = NetworkConfigUtil.generateTkpdPath(url);
            if(NetworkType==NetworkConfig.GET) {
                act += "GET";
                contentType = "";// to indicate that it just blank
            }else {
                act += "POST";
                contentType += "application/x-www-form-urlencoded";
            }

            String signToString = NetworkConfigUtil.generateTkpdAuthString(act, Content_MD5, contentType, dateFormat.format(currentDate), tkpdUrl);
            String signature = NetworkConfigUtil.generateHMACSignature(signToString);

            networkHandler.addHeader("Content-MD5",Content_MD5);
            networkHandler.addHeader("Date", dateFormat.format(currentDate));
            networkHandler.addHeader("Authorization", "TKPD Tokopedia:"+ signature);
            networkHandler.addHeader("X-Method", act);
        }
        return this;
    }

    /**
     * call this after set {@link NetworkHandlerBuilder}.isNeedLogin() method
     * @return {@link NetworkHandlerBuilder} object
     */
    public NetworkHandlerBuilder setIdentity(){
        if(isNeedLogin==NetworkConfig.LOGIN_UNSPECIFIED){
            throw new RuntimeException(messageTAG+" please specify if network handler need login or not !!!");
        }
        if(isNeedLogin==NetworkConfig.LOGIN_NEEDED) {

            networkHandler.addParam("user_id", userId);
            networkHandler.addParam("device_id", deviceId);
            networkHandler.addParam("hash", NetworkConfigUtil.md5(userId + "~" + deviceId));
            networkHandler.addParam("device_time",(System.currentTimeMillis() / 1000L) + "");
        }else{
            networkHandler.addParam("user_id", "");
            networkHandler.addParam("device_id", "");
            networkHandler.addParam("hash", NetworkConfigUtil.md5("~"));
            networkHandler.addParam("device_time",(System.currentTimeMillis() / 1000L) + "");
        }
        return this;
    }

    /**
     * finished construct all that necessary
     * @return {@link NetworkHandler} object
     */
    public NetworkHandler finish(){
        if(isAllParamSupply==NetworkConfig.PARAM_ALL_NOT_PASSED||isAllParamSupply==NetworkConfig.PARAM_UNKNOWN_CONDITION)
            throw new RuntimeException(messageTAG+" please specify if network handler already supply all param or not !!!");
        if(isNeedLogin==NetworkConfig.LOGIN_UNSPECIFIED)
            throw new RuntimeException(messageTAG+" please specify if network handler need login or not !!!");
        if(!isListenerSet)
            throw new RuntimeException(messageTAG+" please set listener for returned value from server before commit !!!!");
        return networkHandler;
    }
}
