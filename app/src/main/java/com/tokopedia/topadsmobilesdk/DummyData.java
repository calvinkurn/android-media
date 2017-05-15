package com.tokopedia.topadsmobilesdk;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class DummyData extends RecyclerViewItem {
    String title;

    public DummyData(String title, int type) {
        this.title = title;
        setType(type);
    }

    public String getTitle() {
        return title;
    }

}
