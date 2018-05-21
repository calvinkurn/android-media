package com.tokopedia.digital.product.view.model;

/**
 * Created by Rizky on 16/05/18.
 */
public class CardInfo {

    private String cardNumber;
    private int lastBalance;
    private String issuerImage;

    public CardInfo(
            String cardNumber,
            int lastBalance,
            String issuerImage) {
        this.cardNumber = cardNumber;
        this.lastBalance = lastBalance;
        this.issuerImage = issuerImage;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getLastBalance() {
        return lastBalance;
    }

    public String getIssuerImage() {
        return issuerImage;
    }

}
