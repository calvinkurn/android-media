package com.tokopedia.tkpd.beranda.listener;

/**
 * Created by errysuprayogi on 12/4/17.
 */
public interface OnSectionChangeListener {
    void onChange(int firstPosition);

    void onScrollStateChanged(int newState, int firstPosition);
}
