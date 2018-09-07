package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

/**
 * @author by nisie on 9/26/17.
 */

public class ReviewTips {
    String title;
    String tips;

    public ReviewTips(String title, String tips) {
        this.title = title;
        this.tips = tips;
    }

    public String getTitle() {
        return title;
    }

    public String getTips() {
        return tips;
    }
}
