package com.tokopedia.imagepicker.gallery.type;

/**
 * Created by hangnadi on 5/19/17.
 */

public class GalleryType {

    private static final int ALL = 0x0001;
    private static final int IMAGE = 0x0002;
    private static final int VIDEO = 0x0003;

    public static int ofAll() {
        return GalleryType.ALL;
    }

    public static int ofImageOnly() {
        return GalleryType.IMAGE;
    }

    public static int ofVideoOnly() {
        return GalleryType.VIDEO;
    }
}
