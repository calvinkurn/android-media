package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.SerializedName;

public class DialogContent {
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;
    @SerializedName("positive_label")
    private String positiveLabel;
    @SerializedName("negative_label")
    private String negativeLabel;
    @SerializedName("negative_label_link")
    private String dialogNegativeLink;

    public String getDialogNegativeLink() {
        return dialogNegativeLink;
    }

    public void setDialogNegativeLink(String dialogNegativeLink) {
        this.dialogNegativeLink = dialogNegativeLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPositiveLabel() {
        return positiveLabel;
    }

    public void setPositiveLabel(String positiveLabel) {
        this.positiveLabel = positiveLabel;
    }

    public String getNegativeLabel() {
        return negativeLabel;
    }

    public void setNegativeLabel(String negativeLabel) {
        this.negativeLabel = negativeLabel;
    }

    @Override
    public String toString() {
        return "DialogContent{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", positiveLabel='" + positiveLabel + '\'' +
                ", negativeLabel='" + negativeLabel + '\'' +
                ", dialogNegativeLink='" + dialogNegativeLink + '\'' +
                '}';
    }
}
