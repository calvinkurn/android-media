package com.tokopedia.core.geolocation.model.autocomplete.viewmodel;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class PredictionResult {

    private String mainText;

    private String secondaryText;

    private String mainTextFormatted;

    private String secondaryTextFormatted;

    private String placeId;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getMainTextFormatted() {
        return mainTextFormatted;
    }

    public void setMainTextFormatted(String mainTextFormatted) {
        this.mainTextFormatted = mainTextFormatted;
    }

    public String getSecondaryTextFormatted() {
        return secondaryTextFormatted;
    }

    public void setSecondaryTextFormatted(String secondaryTextFormatted) {
        this.secondaryTextFormatted = secondaryTextFormatted;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
