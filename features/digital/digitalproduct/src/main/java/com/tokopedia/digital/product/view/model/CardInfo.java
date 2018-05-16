package com.tokopedia.digital.product.view.model;

/**
 * Created by Rizky on 16/05/18.
 */
public class CardInfo {

    private String selectEMoney;
    private String cardUID;
    private String cardAttribute;
    private String cardInfo;
    private String lastBalance;

    public CardInfo(String selectEMoney, String cardUID, String cardAttribute, String cardInfo, String lastBalance) {
        this.selectEMoney = selectEMoney;
        this.cardUID = cardUID;
        this.cardAttribute = cardAttribute;
        this.cardInfo = cardInfo;
        this.lastBalance = lastBalance;
    }

    public String getSelectEMoney() {
        return selectEMoney;
    }

    public String getCardUID() {
        return cardUID;
    }

    public String getCardAttribute() {
        return cardAttribute;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public String getLastBalance() {
        return lastBalance;
    }

    public boolean isValid() {
        return (selectEMoney.substring(selectEMoney.length()-4, selectEMoney.length()).equals("9000") &
                (cardAttribute.substring(cardAttribute.length()-4, cardAttribute.length()).equals("9000")) &
                (cardInfo.substring(cardInfo.length()-4, cardInfo.length()).equals("9000")) &
                (lastBalance.substring(lastBalance.length()-4, lastBalance.length()).equals("9000")));
    }
}
