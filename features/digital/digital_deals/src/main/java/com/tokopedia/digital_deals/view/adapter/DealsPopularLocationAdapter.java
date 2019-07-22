package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.model.Location;

import java.util.List;

public class DealsPopularLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private List<Location> locations;
    private boolean isFooterAdded;

    public DealsPopularLocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locations = locationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.popular_location_item, parent, false);
                holder = new ItemViewHolder(v);
                break;
            case VIEW_TYPE_LOADING:
                v = inflater.inflate(R.layout.item_loading, parent, false);
                holder = new LoadingViewHolder(v);
                break;
            default:
                break;
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                ((ItemViewHolder) holder).setIndex(position);
                ((ItemViewHolder) holder).bindData(locations.get(position));
                break;
            case VIEW_TYPE_LOADING:
                ((LoadingViewHolder) holder).showProgressBar(true);
                break;
            default:
                break;
        }
    }

    public void addFooter() {
        isFooterAdded = true;
    }

    public void removeFooter() {
        isFooterAdded = false;
    }

    @Override
    public int getItemCount() {
        return locations == null ? 0 : locations.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemType;
        if (isFooterAdded) {
            itemType = VIEW_TYPE_LOADING;
        } else {
            itemType = VIEW_TYPE_ITEM;
        }
        return itemType;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private int index;
        private TextView locationName, locAddress, locType;
        private ImageView locImage;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationName = itemView.findViewById(R.id.location_name);
            locAddress = itemView.findViewById(R.id.location_address);
            locType = itemView.findViewById(R.id.location_type);
            locImage = itemView.findViewById(R.id.popular_loc_img);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        void bindData(Location location) {
            locationName.setText(location.getName());
            locAddress.setText(location.getAddress());
            locType.setText(location.getLocType());
            ImageHandler.loadImage(context, locImage, location.getImageApp(), R.color.grey_1100, R.color.grey_1100);
        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        void showProgressBar(boolean showProgressBar) {
            if (showProgressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

}
