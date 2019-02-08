package com.tokopedia.linker;

import android.content.Context;

import com.tokopedia.linker.interfaces.WrapperInterface;
import com.tokopedia.linker.requests.LinkerDeeplinkRequest;
import com.tokopedia.linker.requests.LinkerGenericRequest;
import com.tokopedia.linker.requests.LinkerShareRequest;

public class LinkerManager {

    private static LinkerManager linkerManager;
    private WrapperInterface wrapperObj;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private LinkerManager(Context context){
        this.context = context;
    }

    public static void initLinkerManager(Context context){
        if(linkerManager == null){
            synchronized (LinkerManager.class){
                if(linkerManager == null) {
                    linkerManager = new LinkerManager(context);
                    linkerManager.initWrapper();
                }
            }
        }
    }

    public static LinkerManager getInstance(){
        if(linkerManager == null){
            try {
                throw new Exception(LinkerConstants.MSG_UNINITIALIZED_LINKER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return linkerManager;
    }

    private void initWrapper(){
        if(wrapperObj == null) {
            wrapperObj = new BranchWrapper();
            wrapperObj.init(linkerManager.context);
        }
    }

    public void executeShareRequest(LinkerShareRequest linkerShareRequest){
        if(wrapperObj != null) {
            wrapperObj.createShareUrl(linkerShareRequest, context);
        }
    }

    public void sendEvent(LinkerGenericRequest linkerGenericRequest){
        if(wrapperObj != null) {
            wrapperObj.sendEvent(linkerGenericRequest, context);
        }
    }

    public void handleDefferedDeeplink(LinkerDeeplinkRequest linkerDeeplinkRequest){
        if(wrapperObj != null) {
            wrapperObj.handleDefferedDeeplink(linkerDeeplinkRequest, context);
        }
    }

    public String getDefferedDeeplinkForSession(){
        String dd4Session = "";
        if(wrapperObj != null) {
            dd4Session = wrapperObj.getDefferedDeeplinkForSession();
        }
        return dd4Session;
    }
}
