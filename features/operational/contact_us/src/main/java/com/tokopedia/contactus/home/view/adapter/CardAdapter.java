package com.tokopedia.contactus.home.view.adapter;

import android.support.v7.widget.CardView;


public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}