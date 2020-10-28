package com.tokopedia.tkpd.tkpdreputation.constant;

public interface Constant {

    interface ImageUpload {
        String FILELOC = "fileloc";
        int REQUEST_CODE = 111;
        int REQUEST_CODE_GALLERY = 1243;
        int CODE_UPLOAD_IMAGE = 789;
        int QUALITY_COMPRESS = 80;
    }

    interface Notification {
        String EXTRA_FROM_PUSH = "from_notif";
        String GCM_NOTIFICATION = "GCM_NOTIFICATION";
    }

    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";
    public static final String IS_DIRECTLY_GO_TO_RATING = "is_directly_go_to_rating";

    public static final int TAB_WAITING_REVIEW = 1;
    public static final int TAB_MY_REVIEW = 2;
    public static final int TAB_BUYER_REVIEW = 3;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;

}
