package com.tokopedia.flight.searchV2.presentation.model;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightFareViewModel {

    private String adult;
    private String adultCombo;
    private String child;
    private String childCombo;
    private String infant;
    private String infantCombo;
    private int adultNumeric;
    private int adultNumericCombo;
    private int childNumeric;
    private int childNumericCombo;
    private int infantNumeric;
    private int infantNumericCombo;

    public FlightFareViewModel(String adult, String adultCombo, String child, String childCombo,
                               String infant, String infantCombo, int adultNumeric, int adultNumericCombo,
                               int childNumeric, int childNumericCombo, int infantNumeric, int infantNumericCombo) {
        this.adult = adult;
        this.adultCombo = adultCombo;
        this.child = child;
        this.childCombo = childCombo;
        this.infant = infant;
        this.infantCombo = infantCombo;
        this.adultNumeric = adultNumeric;
        this.adultNumericCombo = adultNumericCombo;
        this.childNumeric = childNumeric;
        this.childNumericCombo = childNumericCombo;
        this.infantNumeric = infantNumeric;
        this.infantNumericCombo = infantNumericCombo;
    }

    public String getAdult() {
        return adult;
    }

    public String getAdultCombo() {
        return adultCombo;
    }

    public String getChild() {
        return child;
    }

    public String getChildCombo() {
        return childCombo;
    }

    public String getInfant() {
        return infant;
    }

    public String getInfantCombo() {
        return infantCombo;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public int getAdultNumericCombo() {
        return adultNumericCombo;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public int getChildNumericCombo() {
        return childNumericCombo;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    public int getInfantNumericCombo() {
        return infantNumericCombo;
    }

}
