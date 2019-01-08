package com.tokopedia.affiliate.feature.explore.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 07/01/19.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {

    public interface OnFilterClickedListener {
        void onItemClicked(FilterViewModel filter);
        void loadDataWithoutFilter();
    }

    private List<FilterViewModel> filterList = new ArrayList<>();
    private Context context;
    private OnFilterClickedListener filterClickedListener;

    public FilterAdapter(Context context, List<FilterViewModel> filterList, OnFilterClickedListener onFilterClickedListener) {
        this.filterList = filterList;
        this.context = context;
        this.filterClickedListener = onFilterClickedListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FilterViewModel filter = filterList.get(position);
        initView(holder, filter);
        initViewListener(holder, filter);
    }

    private void initView(Holder holder, FilterViewModel filter) {
        holder.text.setText(filter.getName());
        ImageHandler.loadImageRounded2(context, holder.imageView, filter.getImage());
        holder.layer.setBackgroundColor(getLayerBackground(filter.isSelected()));
    }

    private void initViewListener(Holder holder, FilterViewModel filter) {
        holder.cardView.setOnClickListener(v -> {
            if (enableCurrentItem(filter)) {
                filterClickedListener.onItemClicked(filter);
            } else {
                filterClickedListener.loadDataWithoutFilter();
            }
        });
    }

    private int getLayerBackground(boolean isSelected) {
        return context.getResources().getColor(isSelected ?
                R.color.filter_background_active :
                R.color.filter_background_inactive);
    }
    private boolean enableCurrentItem(FilterViewModel filter) {
        for (FilterViewModel item: filterList) {
            if (item.getName().equals(filter.getName()) && item.isSelected()) {
                item.setSelected(false);
                return false;
            }
            else item.setSelected(item.getName().equals(filter.getName()));
        }
        notifyDataSetChanged();
        return true;
    }

    public List<FilterViewModel> getFilterList() {
        return filterList;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView text;
        private View layer;
        private CardView cardView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.category);
            layer = itemView.findViewById(R.id.backgroundView);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
