package com.tokopedia.tkpd.splash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DynamicSplash {

    @SerializedName("main_logo")
    private String mainLogo;

    @SerializedName("background")
    private List<DynamicBackground> background;

    public String getMainLogo() {
        return mainLogo;
    }

    public List<DynamicBackground> getBackground() {
        return background;
    }

    public void setMainLogo(String mainLogo) {
        this.mainLogo = mainLogo;
    }

    public void setBackground(List<DynamicBackground> background) {
        this.background = background;
    }
}
