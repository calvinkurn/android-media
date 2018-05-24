package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.SerializedName;

public class DetailText {
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String content;
    @SerializedName("tooltip_content")
    private String tooltipContent;
    @SerializedName("description_content")
    private String description;
    @SerializedName("dialog")
    private DialogContent dialog;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DialogContent getDialog() {
        return dialog;
    }

    public void setDialog(DialogContent dialog) {
        this.dialog = dialog;
    }

    public String getTooltipContent() {
        return tooltipContent;
    }

    public void setTooltipContent(String tooltipContent) {
        this.tooltipContent = tooltipContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DetailText{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tooltipContent='" + tooltipContent + '\'' +
                '}';
    }
}
