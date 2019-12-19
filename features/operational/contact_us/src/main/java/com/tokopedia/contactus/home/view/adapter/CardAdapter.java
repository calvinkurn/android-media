package com.tokopedia.contactus.home.view.adapter;

import androidx.cardview.widget.CardView;


public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}