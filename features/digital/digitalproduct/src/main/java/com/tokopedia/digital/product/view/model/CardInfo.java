package com.tokopedia.digital.product.view.model;

/**
 * Created by Rizky on 16/05/18.
 */
public class CardInfo {

    private String cardNumber;
    private String formattedCardNumber;
    private int lastBalance;
    private String issuerImage;

    public CardInfo(
            String cardNumber,
            String formattedCardNumber,
            int lastBalance,
            String issuerImage) {
        this.cardNumber = cardNumber;
        this.formattedCardNumber = formattedCardNumber;
        this.lastBalance = lastBalance;
        this.issuerImage = issuerImage;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFormattedCardNumber() {
        return formattedCardNumber;
    }

    public int getLastBalance() {
        return lastBalance;
    }

    public String getIssuerImage() {
        return issuerImage;
    }

}
