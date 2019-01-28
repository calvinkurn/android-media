package com.tokopedia.gamification.cracktoken.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackBenefit {

    private String text;
    private String color;
    private String size;
    private String templateText;
    private String benefitType;
    private int valueAfter;
    private String multiplier;
    private int valueBefore;
    private String tierInformation;
    private String animationType;

    public CrackBenefit() {
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setBenefitType(String benefitType) {
        this.benefitType=benefitType;
    }

    public void setValueBefore(int valueBefore){
        this.valueBefore = valueBefore;
    }

    public int getValueBefore(){
        return valueBefore;
    }

    public void setTierInformation(String tierInformation){
        this.tierInformation = tierInformation;
    }

    public String getTierInformation(){
        return tierInformation;
    }

    public void setAnimationType(String animationType){
        this.animationType = animationType;
    }

    public String getAnimationType(){
        return animationType;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public int getValueAfter() {
        return valueAfter;
    }

    public void setValueAfter(int valueAfter) {
        this.valueAfter = valueAfter;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }
}
