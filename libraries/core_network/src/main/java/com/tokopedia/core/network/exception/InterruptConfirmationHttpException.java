package com.tokopedia.core.network.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.network.exception.model.InterruptConfirmationExceptionEntity;
import com.tokopedia.core.network.exception.model.InterruptEntity;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by alvarisi on 3/29/17.
 */

@Deprecated
public class InterruptConfirmationHttpException extends IOException {
    private InterruptEntity entity;

    public static final String TOS_TOKOPEDIA_INTERRUPT = "tos_tokopedia";
    public static final String TOS_CONFIRMATION_INTERRUPT = "tos_accept_confirmation";
    public static final String SURGE_CONFIRMATION_INTERRUPT = "surge_confirmation";
    public static final String WALLET_ACTIVATION_INTERRUPT = "wallet_activation";
    public static final String WALLET_TOPUP_INTERRUPT = "wallet_topup";
    public static final String PENDING_FARE = "pending_fare";
    public static final String GENERAL_INTERRUPT = "interrupt";

    private static final String TOS_TOKOPEDIA_KEY = "tos_tokopedia_id";
    private static final String TOS_CONFIRMATION_KEY = "tos_confirmation_id";
    private static final String SURGE_KEY = "surge_confirmation_id";

    private static final String DATA_RESPONSE = "data";

    public InterruptConfirmationHttpException() {
        super("Request data is invalid, please check message");
    }

    public InterruptConfirmationHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptConfirmationHttpException(String errorMessage) {
        super(errorMessage);
        this.entity = new InterruptEntity();
        String newResponseString = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(errorMessage);
            if (jsonObject.has(DATA_RESPONSE)) {
                newResponseString = jsonObject.getString(DATA_RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            newResponseString = null;
        }
        Gson gson = new GsonBuilder().create();
        try {
            InterruptConfirmationExceptionEntity entity = gson.fromJson(newResponseString, InterruptConfirmationExceptionEntity.class);
            if (entity.getCode() != null && entity.getCode().length() > 0) {

                this.entity.setMessage(entity.getMessage());
                this.entity.setCode(entity.getCode());
                this.entity.setKey("");

                switch (entity.getCode()) {
                    case TOS_TOKOPEDIA_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getTosTokopediaConfirmationExceptionEntity().getHref());
                        this.entity.setId(entity.getMeta().getTosTokopediaConfirmationExceptionEntity().getTosId());
                        this.entity.setKey(TOS_TOKOPEDIA_KEY);
                        break;
                    case TOS_CONFIRMATION_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getTosAcceptConfirmationEntity().getHref());
                        this.entity.setId(entity.getMeta().getTosAcceptConfirmationEntity().getTosId());
                        this.entity.setKey(TOS_CONFIRMATION_KEY);
                        break;
                    case SURGE_CONFIRMATION_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getSurgeConfirmationEntity().getHref());
                        this.entity.setId(entity.getMeta().getSurgeConfirmationEntity().getSurgeConfirmationId());
                        this.entity.setKey(SURGE_KEY);
                        break;
                    case WALLET_ACTIVATION_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getWalletActivationEntity().getHref());
                        this.entity.setId("");
                        break;
                    case WALLET_TOPUP_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getWalletTopupEntity().getHref());
                        this.entity.setId("");
                        break;
                    case PENDING_FARE:
                        this.entity.setHref(entity.getMeta().getPendingFareEntity().getHref());
                        this.entity.setId("");
                        break;
                    case GENERAL_INTERRUPT:
                        this.entity.setHref(entity.getMeta().getInterruptEntity().getHref());
                        this.entity.setId("");
                        break;
                }
            } else {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        } catch (JsonSyntaxException exception) {
            initCause(exception);
        } catch (RuntimeException e) {
            initCause(e);
        }
    }

    public String getHref() {
        return entity.getHref();
    }

    @Override
    public String getMessage() {
        return entity.getMessage();
    }

    public String getType() {
        return entity.getCode();
    }

    public String getId() {
        return entity.getId();
    }

    public String getKey() {
        return entity.getKey();
    }
}