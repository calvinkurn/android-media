package com.tokopedia.shop.score.view.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shop.score.R;
import com.tokopedia.shop.score.view.model.ShopScoreDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SHOP_SCORE_DETAIL = 500;
    private List<ShopScoreDetailItemViewModel> data = new ArrayList<>();

    @Override
    public int getItemCount() {
        return data.size();     //+ super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOP_SCORE_DETAIL:
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shop_score_detail, parent, false);
                return new ShopScoreDetailViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case SHOP_SCORE_DETAIL:
                bindView((ShopScoreDetailViewHolder) viewHolder, data.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty()) {
            return super.getItemViewType(position);
        } else {
            return SHOP_SCORE_DETAIL;
        }
    }

    private void bindView(ShopScoreDetailViewHolder viewHolder, ShopScoreDetailItemViewModel data) {
        viewHolder.setTitle(data.getTitle());
        viewHolder.setShopScoreDescription(data.getDescription());
        viewHolder.setProgressBarColor(data.getProgressBarColor());
        viewHolder.setShopScoreValue(data.getValue());
    }

    public void updateData(List<ShopScoreDetailItemViewModel> viewModel) {
        data = viewModel;
        notifyDataSetChanged();
    }
}
