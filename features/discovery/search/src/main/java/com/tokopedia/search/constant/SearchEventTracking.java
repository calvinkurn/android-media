package com.tokopedia.search.constant;

public interface SearchEventTracking {

    interface Event {
        String IMAGE_SEARCH_CLICK = "imageSearchClick";
    }

    interface Category {
        String IMAGE_SEARCH = "image search";
    }

    interface Action {
        String EXTERNAL_IMAGE_SEARCH = "click image search external";
    }
}
