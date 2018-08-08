package com.tokopedia.explore.analytics;

/**
 * @author by milhamj on 03/08/18.
 */

public interface ContentExloreEventTracking {
    interface Screen {
        String SCREEN_CONTENT_STREAM = "explore page - inspiration";
    }

    interface Event {
        String EXPLORE = "eventExplore";
    }

    interface Category {
        String EXPLORE_INSPIRATION = "explore page - inspiration";
    }

    interface Action {
        String LOAD_MORE = "load more";
        String CLICK_GRID_CONTENT = "click grid content";
        String FILTER_CATEGORY = "filter kol by category";
        String DESELECT_CATEGORY = "deselect filter category";
        String SEARCH = "search";
    }

    interface EventLabel {
        String CLICK_GRID_CONTENT_LABEL = "%s - %s";
    }
}
