package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationAdapter
        extends RecyclerView.Adapter<ProductCommunicationAdapter.ViewHolder> {

    private List<ProductCommunicationItemViewModel> itemViewModels;
    private FeedPlus.View viewListener;

    public ProductCommunicationAdapter(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
        itemViewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_communication_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageHandler.loadImage2(holder.image,
                itemViewModels.get(position).getImageUrl(),
                R.drawable.ic_loading_image);

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                viewListener.onContentProductLinkClicked(
                        itemViewModels.get(adapterPosition).getRedirectUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemViewModels.size();
    }

    public void setData(List<ProductCommunicationItemViewModel> itemViewModels) {
        this.itemViewModels = itemViewModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View parentView;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            image = itemView.findViewById(R.id.banner);
        }
    }
}
