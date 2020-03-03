package com.tokopedia.events.view.utils;

import androidx.cardview.widget.CardView;

/**
 * Created by pranaymohapatra on 02/04/18.
 */


public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}