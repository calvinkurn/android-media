package com.tokopedia.tkpd.facade;

import android.content.Context;

import com.tokopedia.tkpd.fragment.FragmentCreditCard;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.util.CreditCardUtils;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 5/22/2015.
 */
public class FacadeCreditCard {

    public interface OnActionProcessListener{
        void onVeritrans(String ccBank);
        void onSprintAsia(String email, String paymentId, String ccType);
        void onFailure();
    }

    public interface OnVeritransStep2Listener{
        void onSuccess(String result);
        void onFailure();
    }

    public interface OnSprintAsiaFinish{
        void onTransactionSuccess();
        void onTransactionFailed();
    }

    public static FacadeCreditCard createInstance(Context context){
        FacadeCreditCard facade = new FacadeCreditCard();
        facade.context = context;
        return facade;
    }

    private Context context;

    ////////////////////////////////////////////////////////////// GET CREDIT CARD DATA ///////////////////////////////////////////

    public static CreditCardUtils.Model getCreditCardModel(JSONObject ccData)throws JSONException{
        CreditCardUtils.Model model = new CreditCardUtils.Model();
        setCCDResultToModel(model, ccData);
        return model;
    }

    private static void setCCDResultToModel(CreditCardUtils.Model model, JSONObject ccData)throws JSONException{
        model.address = ccData.getString("address");
        model.province = ccData.getString("state");
        model.city = ccData.getString("city");
        model.forename = ccData.getString("first_name");
        model.surname = ccData.getString("last_name");
        model.postCode = ccData.getString("postal_code");
        model.phone = ccData.getString("phone");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////// ACTION VERITRANS STEP 2 ////////////////////////////////////////////////

    public void actionVeritransStep2(String token, FragmentCreditCard.Model model, OnVeritransStep2Listener listener){
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.TRANSACTION);
        network.AddParam("step", "2");
        network.AddParam("method", "POST");
        network.AddParam("token_cart", model.tokenCart);
        network.AddParam("credit_card_token", token);
        network.AddParam("first_name", model.ccModel.forename);
        network.AddParam("last_name", model.ccModel.surname);
        network.AddParam("city", model.ccModel.city);
        network.AddParam("postal_code", model.ccModel.postCode);
        network.AddParam("address_street", model.ccModel.address);
        network.AddParam("phone", model.ccModel.phone);
        network.AddParam("state", model.ccModel.province);
        network.AddParam("card_owner", model.ccModel.ccName);
        network.AddParam("card_number", model.ccModel.ccNumber);
        network.AddParam("lp_flag", 1);
        if(model.ccModel.installment){
            network.AddParam("installment", model.ccModel.installment);
            network.AddParam("installment_term", model.ccModel.term);
            network.AddParam("bank", model.ccModel.bankName);
            network.AddParam("gateway", "12");
        }else{
            network.AddParam("gateway", "8");
        }
        network.Commit(onVeritransStep2Listener(listener));
    }

    private NetworkHandler.NetworkHandlerListener onVeritransStep2Listener(final OnVeritransStep2Listener listener){
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                listener.onSuccess(Result.toString());
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        };
    }
}
