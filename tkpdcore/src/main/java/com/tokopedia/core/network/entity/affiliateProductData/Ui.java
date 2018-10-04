
package com.tokopedia.core.network.entity.affiliateProductData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ui {

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;
    @SerializedName("labels")
    @Expose
    private List<Label> labels = null;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}
