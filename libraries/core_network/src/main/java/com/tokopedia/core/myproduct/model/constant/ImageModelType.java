package com.tokopedia.core.myproduct.model.constant;

/**
 * Created by noiz354 on 4/8/16.
 */
public enum ImageModelType {
    /**
     * such as when not current fragment at foreground viewpager
     */
    INACTIVE(Constants.INACTIVE)

    /**
     * such as when current fragment is at foreground viewpager
     */
    , ACTIVE(Constants.ACTIVE)

    /**
     * maybe there are activity such as already done or something
     */
    , SELECTED(Constants.SELECTED)

    /**
     * maybe there are activity such as already done or something
     */
    , UNSELECTED(Constants.UNSELECTED);

    int type;
    ImageModelType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    private static class Constants {
        public static final int INACTIVE = -99;
        public static final int ACTIVE = -101;
        public static final int SELECTED = -102;
        public static final int UNSELECTED = -103;
    }
}
