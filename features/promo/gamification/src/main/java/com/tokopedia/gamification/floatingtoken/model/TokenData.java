package com.tokopedia.gamification.floatingtoken.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenData {

    private Boolean offFlag;
    private Integer sumToken;
    private String sumTokenStr;
    private String tokenUnit;
    private TokenFloating floating;
    private TokenHome home;

    public Boolean getOffFlag() {
        return offFlag;
    }

    public Integer getSumToken() {
        return sumToken;
    }

    public String getSumTokenStr() {
        return sumTokenStr;
    }

    public String getTokenUnit() {
        return tokenUnit;
    }

    public TokenFloating getFloating() {
        return floating;
    }

    public TokenHome getHome() {
        return home;
    }

    public void setOffFlag(Boolean offFlag) {
        this.offFlag = offFlag;
    }

    public void setSumToken(Integer sumToken) {
        this.sumToken = sumToken;
    }

    public void setSumTokenStr(String sumTokenStr) {
        this.sumTokenStr = sumTokenStr;
    }

    public void setTokenUnit(String tokenUnit) {
        this.tokenUnit = tokenUnit;
    }

    public void setFloating(TokenFloating floating) {
        this.floating = floating;
    }

    public void setHome(TokenHome home) {
        this.home = home;
    }
}
