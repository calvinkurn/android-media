package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
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

public class TopPicksItemAdapter  extends RecyclerView.Adapter<TopPicksItemAdapter.TopPicksItemRowHolder> {

    private Toppick toppick;
    private TopPicksItemAdapter.OnItemClickedListener itemClickedListener;
    private TopPicksItemAdapter.OnTitleClickedListener titleClickedListener;
    private final int homeMenuWidth;

    TopPicksItemAdapter(Toppick topPick, int homeMenuWidth) {

        this.toppick = topPick;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public TopPicksItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_top_picks_title, null
        );
        v.setMinimumWidth(homeMenuWidth);
        return new TopPicksItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final TopPicksItemRowHolder holder, final int i) {
        holder.linWrapper.getLayoutParams().width = homeMenuWidth;
        if(i % 2 != 0 ){
            holder.sparator.setVisibility(View.GONE);
        } else {
            holder.sparator.setVisibility(View.VISIBLE);
        }

        if (i==0) {
          //  holder.tvTitle.setText(toppick.getName());
            ImageHandler.LoadImage(holder.itemImage,toppick.getImageUrl());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //itemClickedListener.onItemClicked(singleItem, holder.getAdapterPosition());
                }
            });
        } else {
            final Item singleItem = toppick.getItem().get(i-1);
           // holder.tvTitle.setText(singleItem.getName());
            ImageHandler.LoadImage(holder.itemImage,singleItem.getImageUrl());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //itemClickedListener.onItemClicked(singleItem, holder.getAdapterPosition());
                }
            });
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

       // TextView tvTitle;

        ImageView itemImage;
        LinearLayout linWrapper;
        View sparator;
        protected View view;

        TopPicksItemRowHolder(View view) {
            super(view);
            this.view = view;
            this.sparator = view.findViewById(R.id.sparator);
           // this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
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
