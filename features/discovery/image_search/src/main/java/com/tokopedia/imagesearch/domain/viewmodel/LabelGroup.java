package com.tokopedia.imagesearch.domain.viewmodel;

public class LabelGroup {

        private String position;
        private String type;
        private String title;

    public LabelGroup(String position, String type, String title) {
        this.position = position;
        this.type = type;
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
