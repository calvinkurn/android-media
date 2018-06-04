package com.tokopedia.kol.analytics;

/**
 * @author by milhamj on 18/04/18.
 */

public interface KolEventTracking {
    interface Screen {
        String SCREEN_KOL_COMMENTS = "Kol Comments Page";
    }

    interface Event {
        String USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";
    }

    interface Category {
        String FEED_CONTENT_COMMENT_DETAIL = "content - comment detail";
    }

    interface Action {
        String FEED_LOAD_MORE_COMMENTS = "load more comments";
        String FEED_COMMENT_CLICK_BACK = "click back";
        String FEED_SUBMIT_COMMENT = "submit comment";
    }

    interface EventLabel {
        String FEED_CONTENT_COMMENT_DETAIL_LOAD_MORE = "load more";
        String FEED_CONTENT_COMMENT_DETAIL_BACK = "back";
        String FEED_CONTENT_COMMENT_DETAIL_COMMENT = "comment";
    }
}
