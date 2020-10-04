package com.tokopedia.home.explore.view.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.media.loader.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListViewHolder extends AbstractViewHolder<CategoryGridListViewModel> {

    private static final String MARKETPLACE = "Marketplace";
    private static final String DIGITAL = "Digital";

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_grid;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private Context context;
    private ItemAdapter adapter;
    private int spanCount = 2;
    private CategoryAdapterListener listener;
    private List<LayoutRows> rowModelList = new ArrayList<>();

    public CategoryGridListViewHolder(View itemView, CategoryAdapterListener listener) {
        super(itemView);
        titleTxt = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.list);
        this.listener = listener;
        this.context = itemView.getContext();
        adapter = new ItemAdapter(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(CategoryGridListViewModel element) {
        titleTxt.setText(element.getTitle());
        rowModelList.addAll(element.getItemList());
        adapter.setData(rowModelList);
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<LayoutRows> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<LayoutRows> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_grid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            final LayoutRows rowModel = data.get(position);
            if (rowModel != null) {
                if (rowModel.getName() != null) {
                    holder.title.setText(rowModel.getName());
                }
                if (rowModel.getImageUrl() != null) {
                    Loader.loadImage(holder.icon, rowModel.getImageUrl(), properties -> {
                        properties.setCacheStrategy(DiskCacheStrategy.RESOURCE);
                        return null;
                    });
                }
                if (rowModel.getType() != null) {
                    holder.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.trackingItemGridClick(rowModel);
                            if (rowModel.getType() != null && rowModel.getType().equalsIgnoreCase(MARKETPLACE)) {
                                listener.onMarketPlaceItemClicked(rowModel);
                            } else if (rowModel.getType() != null && rowModel.getType().equalsIgnoreCase(DIGITAL)) {
                                listener.onDigitalItemClicked(rowModel);
                            } else if (!TextUtils.isEmpty(rowModel.getApplinks())) {
                                listener.onApplinkClicked(rowModel);
                            } else {
                                listener.onGimickItemClicked(rowModel);
                            }
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView title;
            LinearLayout container;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                title = itemView.findViewById(R.id.title);
                container = itemView.findViewById(R.id.container);
            }
        }
    }
}
