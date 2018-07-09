package com.tokopedia.tokocash.historytokocash.presentation.model;

/**
 * Created by nabillasabbaha on 10/16/17.
 */

public class HelpHistoryTokoCash {

    private String id;
    private String translation;

    public HelpHistoryTokoCash() {
    }

    public HelpHistoryTokoCash(String translation) {
        this.translation = translation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return translation;
    }
}
