package com.tokopedia.common_digital.product.presentation.model;

/**
 * Created by Rizky on 13/09/18.
 */
public class AdditionalButton {

    private String type;
    private String buttonText;

    public AdditionalButton(String type, String buttonText) {
        this.type = type;
        this.buttonText = buttonText;
    }

    public String getType() {
        return type;
    }

    public String getButtonText() {
        return buttonText;
    }

}
