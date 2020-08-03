package com.tokopedia.recentview.view.viewmodel;


/**
 * @author by nisie on 7/31/17.
 */

public class LabelsViewModel {

    private final
    String title;

    private final
    String color;

    public LabelsViewModel(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }
}
