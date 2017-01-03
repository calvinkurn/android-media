package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.tkpd.R;

/**
 * Created by Alifa on 12/30/2016.
 */

public class TopPicksItemAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Toppick toppick;
    private TopPicksItemAdapter.OnItemClickedListener itemClickedListener;
    private TopPicksItemAdapter.OnTitleClickedListener titleClickedListener;
    private final int homeMenuWidth;

    TopPicksItemAdapter(Toppick topPick, int homeMenuWidth) {

        this.toppick = topPick;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case 0:
                @SuppressLint("InflateParams") View v = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_top_picks_title, null
                );
                v.setMinimumWidth(homeMenuWidth);
                return new TopPicksTitleRowHolder(v);
            default:
                @SuppressLint("InflateParams") View v2 = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_top_picks, null
                );
                v2.setMinimumWidth(homeMenuWidth);
                return new TopPicksItemRowHolder(v2);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        switch (getItemViewType(i)) {
            case 0:
                TopPicksTitleRowHolder topPicksTitleRowHolder = (TopPicksTitleRowHolder) holder;
                topPicksTitleRowHolder.linWrapper.getLayoutParams().width = homeMenuWidth;
                topPicksTitleRowHolder.sparator.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(topPicksTitleRowHolder.itemImage,toppick.getImageUrl());
                topPicksTitleRowHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleClickedListener.onTitleClicked(toppick);
                    }
                });
                break;
            default:
                TopPicksItemRowHolder topPicksItemRowHolder = (TopPicksItemRowHolder) holder;
                if(i % 2 != 0 ){
                    topPicksItemRowHolder.sparator.setVisibility(View.GONE);
                } else {
                    topPicksItemRowHolder.sparator.setVisibility(View.VISIBLE);
                }
                final Item singleItem = toppick.getItem().get(i-1);
                ImageHandler.LoadImage(topPicksItemRowHolder.itemImage,singleItem.getImageUrl());
                topPicksItemRowHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickedListener.onItemClicked(singleItem, holder.getAdapterPosition());
                    }
                });
                Log.d("alifa", "onBindViewHolder: "+singleItem.getUrl());
        }
    }


    public OnItemClickedListener getItemClickedListener() {
        return itemClickedListener;
    }

    public void setItemClickedListener(OnItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    public OnTitleClickedListener getTitleClickedListener() {
        return titleClickedListener;
    }

    public void setTitleClickedListener(OnTitleClickedListener titleClickedListener) {
        this.titleClickedListener = titleClickedListener;
    }

    @Override
    public int getItemCount() {
        return (null != toppick ? (toppick.getItem().size()+1) : 0);
    }


    class TopPicksItemRowHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        LinearLayout linWrapper;
        View sparator;
        protected View view;

        TopPicksItemRowHolder(View view) {
            super(view);
            this.view = view;
            this.sparator = view.findViewById(R.id.sparator);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.linWrapper = (LinearLayout) view.findViewById(R.id.linWrapper);

        }

    }

    class TopPicksTitleRowHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        LinearLayout linWrapper;
        View sparator;
        protected View view;

        TopPicksTitleRowHolder(View view) {
            super(view);
            this.view = view;
            this.sparator = view.findViewById(R.id.sparator);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.linWrapper = (LinearLayout) view.findViewById(R.id.linWrapper);

        }

    }

    public interface OnItemClickedListener {
        void onItemClicked(Item topPickItem, int position);
    }

    public interface OnTitleClickedListener {
        void onTitleClicked(Toppick toppick);
    }

}
