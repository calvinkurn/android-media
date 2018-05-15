package com.tokopedia.feedplus.view.viewmodel.kol;

/**
 * @author by milhamj on 15/05/18.
 */

public class PollOptionViewModel {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    private String optionId, option, weblink, applink, percentage;
    private int selected;

    public PollOptionViewModel(String optionId, String option, String weblink, String applink,
                               String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
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
}
