package com.tokopedia.home.beranda.listener;

public interface HomeTabFeedListener {

    void onFeedContentScrolled(int dy, int totalScrollY);

    void onFeedContentScrollStateChanged(int newState);
}
