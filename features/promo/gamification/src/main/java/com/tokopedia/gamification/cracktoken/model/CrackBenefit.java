package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;

import com.tokopedia.gamification.R;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackBenefit {

    private String text;
    private String color;
    private String size;

    public CrackBenefit() {
    }

    public CrackBenefit(String text, String color, String size) {
        this.text = text;
        this.color = color;
        this.size = size;
    }

    public boolean isGeneralErrorType (){
        return this instanceof GeneralErrorCrackBenefit;
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
}
