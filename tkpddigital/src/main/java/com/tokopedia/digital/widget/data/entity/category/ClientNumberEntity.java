package com.tokopedia.digital.widget.data.entity.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/14/17.
 */

public class ClientNumberEntity {

    @SerializedName("help")
    @Expose
    private String help;
    @SerializedName("is_shown")
    @Expose
    private boolean isShown;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("operator_style")
    @Expose
    private String operatorStyle;

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public void setOperatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
    }
}
