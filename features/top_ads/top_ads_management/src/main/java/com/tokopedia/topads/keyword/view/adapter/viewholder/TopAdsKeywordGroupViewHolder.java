package com.tokopedia.topads.keyword.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupViewHolder extends BaseViewHolder<GroupAd> {

    private final TextView groupNameText;

    public TopAdsKeywordGroupViewHolder(View itemView) {
        super(itemView);
        groupNameText = (TextView) itemView.findViewById(R.id.text_group_name);
    }

    @Override
    public void bindObject(final GroupAd groupAd) {
        groupNameText.setText(groupAd.getName());
    }
}