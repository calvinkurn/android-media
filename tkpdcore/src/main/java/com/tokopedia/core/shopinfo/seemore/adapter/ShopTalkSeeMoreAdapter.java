package com.tokopedia.core.shopinfo.seemore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;
import com.tokopedia.core.shopinfo.seemore.model.ShopTalkSeeMore;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ShopTalkSeeMoreAdapter extends com.tokopedia.core.shopinfo.adapter.ShopTalkAdapter {

    public static ShopTalkSeeMoreAdapter createInstance(Context context, com.tokopedia.core.shopinfo.adapter.ShopTalkAdapter.ActionShopTalkListener listener) {
        return new ShopTalkSeeMoreAdapter(context, listener);
    }

    public ShopTalkSeeMoreAdapter(Context context, ActionShopTalkListener listener) {
        super(context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ShopTalkSeeMore.TYPE:
                return new ViewHolder2(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_shop_talk_see_more, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ShopTalkSeeMore.TYPE:
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
            if(position >= 0 && position < list.size() && list.get(position) instanceof ShopTalkSeeMore){
                return ShopTalkSeeMore.TYPE;
            }else{
                return VIEW_TALK;
            }
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        private final View shopTalkButtonSeeMore;

        public ViewHolder2(View itemView) {
            super(itemView);

            shopTalkButtonSeeMore = itemView.findViewById(R.id.shop_talk_button_see_more);
        }
    }
}
