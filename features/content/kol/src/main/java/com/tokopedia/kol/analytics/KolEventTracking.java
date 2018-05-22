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
        String HOMEPAGE = "Homepage";
    }

    interface Action {
        String FEED_LOAD_MORE_COMMENTS = "load more comments";
        String FEED_COMMENT_CLICK_BACK = "click back";
        String FEED_SUBMIT_COMMENT = "submit comment";
        String FEED_CLICK_CONTENT_WRITER_NAME = "feed - click content writer name";
        String FEED_FOLLOW_CONTENT = "feed - follow content";
        String FEED_UNFOLLOW_CONTENT = "feed - unfollow content";
        String FEED_EXPAND_CONTENT = "feed - expand content";
        String FEED_LIKE_CONTENT = "feed - like content";
        String FEED_UNLIKE_CONTENT = "feed - unlike content";
        String FEED_CLICK_CONTENT_COMMENT = "feed - click content comment";
        String FEED_CLICK_CONTENT_CTA = "feed - click content cta";
    }

    interface EventLabel {
        String FEED_CONTENT_COMMENT_DETAIL_LOAD_MORE = "load more";
        String FEED_CONTENT_COMMENT_DETAIL_BACK = "back";
        String FEED_CONTENT_COMMENT_DETAIL_COMMENT = "comment";
        String FEED_CONTENT_TYPE_RECOMMENDED = "recommended content";
        String FEED_CONTENT_TYPE_FOLLOWED = "followed content";
        String FEED_CAMPAIGN_TYPE_SUFFIX = " endorsement";
    }
}
