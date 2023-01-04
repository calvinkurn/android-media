package com.tokopedia.dilayanitokopedia.home.presentation.listener;

public interface HomeTabFeedListener {

    void onFeedContentScrolled(int dy, int totalScrollY);

    void onFeedContentScrollStateChanged(int newState);
}
