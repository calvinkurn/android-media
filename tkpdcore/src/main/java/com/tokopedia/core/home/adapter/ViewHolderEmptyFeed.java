package com.tokopedia.core.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;

/**
 * @author Kulomady on 2/27/17.
 */

public class ViewHolderEmptyFeed extends RecyclerView.ViewHolder {

    public TextView checkFavoriteShopButton;
    public ViewHolderEmptyFeed(View itemView) {
        super(itemView);
        checkFavoriteShopButton = (TextView) itemView.findViewById(R.id.find_favorite_shop);
    }

}
