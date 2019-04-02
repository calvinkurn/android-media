package com.tokopedia.shipping_recommendation.domain.shipping;

/**
 * Created by fajarnuha on 26/12/18.
 */
public class CodModel {

    private boolean isCod;
    private int counterCod;
    private String messageInfo;
    private String messageLink;
    private String messageLogo;

    public CodModel() {
    }

    public CodModel(boolean isCod, int counterCod, String messageInfo, String messageLink, String messageLogo) {
        this.isCod = isCod;
        this.counterCod = counterCod;
        this.messageInfo = messageInfo;
        this.messageLink = messageLink;
        this.messageLogo = messageLogo;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    public int getCounterCod() {
        return counterCod;
    }

    public void setCounterCod(int counterCod) {
        this.counterCod = counterCod;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessageLink() {
        return messageLink;
    }

    public void setMessageLink(String messageLink) {
        this.messageLink = messageLink;
    }

    public String getMessageLogo() {
        return messageLogo;
    }

    public void setMessageLogo(String messageLogo) {
        this.messageLogo = messageLogo;
    }
}
