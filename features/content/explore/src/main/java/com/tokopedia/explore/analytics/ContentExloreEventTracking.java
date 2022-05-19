package com.tokopedia.explore.analytics;

/**
 * @author by milhamj on 03/08/18.
 */

public interface ContentExloreEventTracking {
    interface Screen {
        String SCREEN_CONTENT_STREAM = "explore page - inspiration";
        String SCREEN_NAME_EXPLORE = "/feed - explore tab";
        String SCREEN_NAME_UPDATE = "/feed - update tab";
        String SCREEN_NAME_VIDEO = "/feed - video tab";
    }

    interface Event {
        String EXPLORE = "clickExplore";
        String OPEN_SCREEN = "openScreen";
        String SCREEN_NAME = "screenName";
        String IS_LOGGED_IN = "isLoggedInStatus";
        String USER_ID = "userId";
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
        String CLICK_GRID_CONTENT_LABEL = "%s - %s - %s";
    }
}
