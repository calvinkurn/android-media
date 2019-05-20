package com.tokopedia.kol.analytics;

/**
 * @author by milhamj on 18/04/18.
 */

public interface KolEventTracking {
    interface Screen {
        String SCREEN_KOL_COMMENTS = "Kol Comments Page";
        String SCREEN_KOL_POST_DETAIL = "content detail page";
        String SCREEN_SHOP_PAGE_FEED = "ShopPageActivity";
    }

    interface Event {
        String USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";
        String EVENT_CLICK_TOP_PROFILE = "clickTopProfile";
        String EVENT_CLICK_FEED = "clickFeed";
        String EVENT_SHOP_PAGE = "clickShopPage";
    }

    interface Category {
        String FEED_CONTENT_COMMENT_DETAIL = "content - comment detail";
        String HOMEPAGE = "Homepage";
        String KOL_TOP_PROFILE = "kol top profile";
        String CONTENT_FEED = "content feed";
        String CONTENT_FEED_TIMELINE = "content feed timeline";
        String CONTENT_FEED_TIMELINE_DETAIL = "content feed timeline - product detail";
        String SHOP_PAGE_FEED = "shop page feed";
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
        String SHOP_ITEM_IMPRESSION_DYNAMIC = "impression-{count}-merchant-all-{type}";
        String SHOP_ITEM_CLICK_DYNAMIC = "click-{count}-merchant-all-{type}-tag";
        String CLICK_PROMPT = "click prompt";
        String CLICK_YOUTUBE_VIDEO = "click youtube video";
    }

    interface EventLabel {
        String FEED_CONTENT_COMMENT_DETAIL_LOAD_MORE = "load more";
        String FEED_CONTENT_COMMENT_DETAIL_BACK = "back";
        String FEED_CONTENT_COMMENT_DETAIL_COMMENT = "comment";
        String FEED_CONTENT_TYPE_RECOMMENDED = "recommended content";
        String FEED_CONTENT_TYPE_FOLLOWED = "followed content";
        String FEED_CAMPAIGN_TYPE_SUFFIX = " endorsement";
        String GO_TO_EXPLORE_FORMAT = "go to explore - %s";
    }
}
