package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackBenefitEntity {

    @SerializedName("color")
    private String color;

    @SerializedName("size")
    private String size;

    @SerializedName("templateText")
    private String templateText;

    @SerializedName("benefitType")
    private String benefitType;

    @SerializedName("valueAfter")
    private int valueAfter;

    @SerializedName("multiplier")
    private String multiplier;

    @SerializedName("text")
    private String text;

    @SerializedName("valueBefore")
    private int valueBefore;

    @SerializedName("tierInformation")
    private String tierInformation;

    @SerializedName("animationType")
    private String animationType;

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return color;
    }

    public void setSize(String size){
        this.size = size;
    }

    public String getSize(){
        return size;
    }

    public void setTemplateText(String templateText){
        this.templateText = templateText;
    }

    public String getTemplateText(){
        return templateText;
    }

    public void setBenefitType(String benefitType){
        this.benefitType = benefitType;
    }

    public String getBenefitType(){
        return benefitType;
    }

    public void setValueAfter(int valueAfter){
        this.valueAfter = valueAfter;
    }

    public int getValueAfter(){
        return valueAfter;
    }

    public void setMultiplier(String multiplier){
        this.multiplier = multiplier;
    }

    public String getMultiplier(){
        return multiplier;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
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

    @Override
    public String toString(){
        return
                "CrackBenefitEntity{" +
                        "color = '" + color + '\'' +
                        ",size = '" + size + '\'' +
                        ",templateText = '" + templateText + '\'' +
                        ",benefitType = '" + benefitType + '\'' +
                        ",valueAfter = '" + valueAfter + '\'' +
                        ",multiplier = '" + multiplier + '\'' +
                        ",text = '" + text + '\'' +
                        ",valueBefore = '" + valueBefore + '\'' +
                        ",tierInformation = '" + tierInformation + '\'' +
                        ",animationType = '" + animationType + '\'' +
                        "}";
    }
}
