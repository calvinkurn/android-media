package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * @author Erry on 2/27/17.
 */

public class ViewHolderRetryFeed extends RecyclerView.ViewHolder {

    private Context context;
    public TextView mainRetry;

    public ViewHolderRetryFeed(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mainRetry = (TextView) itemView.findViewById(R.id.main_retry);
    }

}
