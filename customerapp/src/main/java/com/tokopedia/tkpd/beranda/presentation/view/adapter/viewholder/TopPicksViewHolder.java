package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksViewHolder extends AbstractViewHolder<TopPicksViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_toppicks;
    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.see_more)
    TextView seeMoreTxt;

    private ItemAdapter adapter;
    private int spanCount = 3;
    private HomeCategoryListener listener;

    public TopPicksViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(itemView);
        this.listener = listener;
        adapter = new ItemAdapter(itemView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                itemView.getResources().getDimensionPixelSize(R.dimen.home_card_page_margin), true));
    }

    @Override
    public void bind(final TopPicksViewModel element) {
        titleTxt.setText(element.getTitle());
        adapter.setData(element.getTopPicksItems());
        seeMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTopPicksMoreClicked(element.getTopPickUrl(), getAdapterPosition());
            }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<TopPicksItemModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<TopPicksItemModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_toppicks_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            Glide.with(context).load(data.get(position).getImageUrl()).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTopPicksItemClicked(data.get(position), getAdapterPosition(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.image)
            ImageView image;

            public ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


}
