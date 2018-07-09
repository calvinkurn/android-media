package com.tokopedia.digital.product.additionalfeature.etoll.view.model;

import com.tokopedia.digital.product.view.model.CardInfo;

/**
 * Created by Rizky on 18/05/18.
 */
public class InquiryBalanceModel {

    private int id;
    private int status;
    private CardInfo cardInfo;
    private String buttonText;
    private String command;
    private String errorMessage;

    public InquiryBalanceModel(int id, int status, CardInfo cardInfo, String buttonText, String command,
                               String errorMessage) {
        this.id = id;
        this.status = status;
        this.cardInfo = cardInfo;
        this.buttonText = buttonText;
        this.command = command;
        this.errorMessage = errorMessage;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getCommand() {
        return command;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
