package com.tokopedia.feedplus.view.viewmodel.kol;

/**
 * @author by milhamj on 15/05/18.
 */

public class PollOptionViewModel {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    private String optionId, option, imageUrl, redirectLink, percentage;
    private int selected;

    public PollOptionViewModel(String optionId, String option, String imageUrl, String redirectLink,
                               String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
        this.imageUrl = imageUrl;
        this.redirectLink = redirectLink;
        this.percentage = percentage;
        this.selected = selected;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
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
