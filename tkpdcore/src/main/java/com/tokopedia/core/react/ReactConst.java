package com.tokopedia.core.react;

/**
 * @author ricoharisin .
 */

public interface ReactConst {

    String GET = "GET";
    String POST = "POST";
    String DELETE = "DELETE";
    String MAIN_MODULE = "MAIN";
    String KEY_SCREEN = "Screen";
    String CODE_PUSH_DEPLOYMENT_KEY = "KVr25gFyi1fzn-AA9KP88Ly2dRoeb85766e2-9e25-4371-8aa3-080b8952449b";

    interface Screen {
        String HOTLIST = "HotList";
        String OFFICIAL_STORE = "official-store";
        String POS_O2O = "pos";
    }

    interface EventEmitter{
        String WISHLIST_ADD = "WishlistAdd";
        String WISHLIST_REMOVE = "WishlistRemove";
        String FAVORITE_ADD = "FavoriteAdd";
        String FAVORITE_REMOVE = "FavoriteRemove";
        String LOGIN = "Login";
        String PAGE_DESTROYED = "PageDestroyed";
    }

}
