package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;

import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.topads.R;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

public class TopAdsEmptyProductListDataBinder extends BaseEmptyDataBinder {
    public TopAdsEmptyProductListDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_product_list);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        super.bindViewHolder(holder, position);
        EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        if (TextUtils.isEmpty(emptyTitleText)) {
            emptyViewHolder.emptyTitleTextView.setVisibility(View.GONE);
        }
    }


}
