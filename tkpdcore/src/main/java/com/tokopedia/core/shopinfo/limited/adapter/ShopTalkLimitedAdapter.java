package com.tokopedia.core.shopinfo.limited.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.shopinfo.adapter.ShopTalkAdapter;
import com.tokopedia.core.shopinfo.limited.model.ShopTalkLimited;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ShopTalkLimitedAdapter extends ShopTalkAdapter {

    private View.OnClickListener showMoreListener;

    public ShopTalkLimitedAdapter(Context context, ActionShopTalkListener listener, View.OnClickListener showMoreListener) {
        super(context, listener);
        this.showMoreListener = showMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ShopTalkLimited.TYPE:
                return new ShowMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_shop_talk_see_more, viewGroup, false), showMoreListener);

            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ShopTalkLimited.TYPE:
                // do nothing
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            if (position >= 0 && position < list.size() && list.get(position) instanceof ShopTalkLimited) {
                return ShopTalkLimited.TYPE;
            } else {
                return VIEW_TALK;
            }
        }
    }

    public class ShowMoreViewHolder extends RecyclerView.ViewHolder {

        public ShowMoreViewHolder(View itemView, View.OnClickListener showMoreListener) {
            super(itemView);
            Button showCompleteDiscussionButton = itemView.findViewById(R.id.button_show_complete_discussion);
            showCompleteDiscussionButton.setOnClickListener(showMoreListener);
        }
    }
}
