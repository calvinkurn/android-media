package com.tokopedia.feedplus.view.viewmodel.kol;

/**
 * @author by milhamj on 15/05/18.
 */

public class PollOptionViewModel {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    private String optionId, option, imageUrl, weblink, applink, percentage;
    private int selected;

    public PollOptionViewModel(String optionId, String option, String imageUrl, String weblink,
                               String applink, String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
        this.imageUrl = imageUrl;
        this.weblink = weblink;
        this.applink = applink;
        this.percentage = percentage;
        this.selected = selected;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getOption() {
        return option;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getApplink() {
        return applink;
    }

    public String getPercentage() {
        return percentage;
    }

    public int getSelected() {
        return selected;
    }

    public int getPercentageInteger() {
        try {
            return (int) Double.parseDouble(percentage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
