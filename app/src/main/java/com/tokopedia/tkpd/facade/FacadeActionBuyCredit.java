package com.tokopedia.tkpd.facade;

import android.content.Context;

import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.util.StringVariable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 3/16/2015.
 */
public class FacadeActionBuyCredit extends BaseFacade{

    private static String URL = StringVariable.BELI_PULSA;

    public interface BuyCreditInterface{
        void onSuccess();
    }

    public static FacadeActionBuyCredit createInstance(Context context){
        FacadeActionBuyCredit facade = new FacadeActionBuyCredit(context);
        return facade;
    }

    public FacadeActionBuyCredit(Context context) {
        super(context);
    }


    private NetworkHandler.NetworkHandlerListener onBuyCredit(final BuyCreditInterface listener){
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                listener.onSuccess();
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        };
    }
}
