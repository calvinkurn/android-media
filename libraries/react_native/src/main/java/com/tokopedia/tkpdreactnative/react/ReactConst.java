package com.tokopedia.tkpdreactnative.react;

/**
 * @author ricoharisin .
 */

public interface ReactConst {

    String GET = "GET";
    String POST = "POST";
    String DELETE = "DELETE";
    String PUT = "PUT";
    String HEAD = "HEAD";
    String MAIN_MODULE = "MAIN";
    String KEY_SCREEN = "Screen";
    String SUB_PAGE = "SubPage";
    String CODE_PUSH_DEPLOYMENT_KEY_STAGING = "KVr25gFyi1fzn-AA9KP88Ly2dRoeb85766e2-9e25-4371-8aa3-080b8952449b";
    String CODE_PUSH_DEPLOYMENT_KEY = "mZj7iHeXLW9NvlYFf7vANLKYJ7jub85766e2-9e25-4371-8aa3-080b8952449b";
    String YOUTUBE_API_KEY = "AIzaSyCRkgwGBe8ZxjcK07Cnl3Auf72BpgA6lLo";
    int REACT_LOGIN_REQUEST_CODE = 1005;
    int REACT_ADD_CREDIT_CARD_REQUEST_CODE = 101;

    interface Screen {
        String HOTLIST = "HotList";
        String OFFICIAL_STORE = "official-store";
        String PROMO = "promo-page";
        String PROMO_TERMS = "promo-terms";
        String SUB_PROMO = "sub-promo";
        String THANK_YOU_PAGE = "thankyou-page";
        String DISCOVERY_PAGE = "discovery-page";
        String EXPLORE_PAGE = "explore-page";
        String CONTENT_DETAIL = "content-detail";
        String BRANDLIST_PAGE = "brandlist-page";
        String HOW_TO_PAY = "how-to-pay";
        String DEV_OPTIONS = "dev-options-rn";
        String OFFICIAL_STORE_HOME = "OFFICIAL_STORE_HOME";
        String OFFICIAL_STORE_CATEGORY = "OFFICIAL_STORE_CATEGORY";
    }

    interface EventEmitter{
        String WISHLIST_ADD = "WishlistAdd";
        String WISHLIST_REMOVE = "WishlistRemove";
        String FAVORITE_ADD = "FavoriteAdd";
        String FAVORITE_REMOVE = "FavoriteRemove";
        String LOGIN = "Login";
        String PAGE_DESTROYED = "PageDestroyed";
    }

    interface Networking{
        String WSAUTH = "wsauth";
        String BEARER = "bearer";
        String DIGITAL = "digital";

        String URL = "url";
        String PARAMS = "params";
        String METHOD = "method";
        String ENCODING = "encoding";
        String HEADERS = "headers";
        String AUTHORIZATIONMODE = "authorizationMode";
    }
}
