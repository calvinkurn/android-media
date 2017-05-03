package com.tokopedia.digital.product.model;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Promo {


    private String bonusText;
    private String id;
    private String newPrice;
    private int newPricePlain;
    private String tag;
    private String terms;
    private String valueText;

    public String getBonusText() {
        return bonusText;
    }

    public void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public int getNewPricePlain() {
        return newPricePlain;
    }

    public void setNewPricePlain(int newPricePlain) {
        this.newPricePlain = newPricePlain;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }
}
