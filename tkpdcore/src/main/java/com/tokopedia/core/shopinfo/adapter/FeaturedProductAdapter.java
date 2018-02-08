package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.helper.IndicatorViewHelper;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HenryPri on 15/06/17.
 */

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;

    public FeaturedProductAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<FeaturedProductItem> dataList = new ArrayList<>();

    public void setDataList(List<FeaturedProductItem> featuredProductItemList) {
        dataList.clear();
        dataList.addAll(featuredProductItemList);
        notifyDataSetChanged();
    }

    public FeaturedProductItem getItem(int pos) {
        if (isDataAvailableAtPosition(pos)) {
            return dataList.get(pos);
        } else {
            return null;
        }
    }

    private boolean isDataAvailableAtPosition(int pos) {
        return !dataList.isEmpty() && pos < dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_product_item, parent, false),
                onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_image)
        ImageView productImage;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.price)
        TextView price;
        @BindView(R2.id.label_container)
        FlowLayout labelContainer;
        @BindView(R2.id.badges_container)
        LinearLayout badgesContainer;
        @BindView(R2.id.container)
        View container;

        private final OnItemClickListener onItemClickListener;
        private Context context;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            this.context = MainApplication.getAppContext();
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final FeaturedProductItem data, final int position) {
            title.setText(data.getName());
            ImageHandler.loadImageThumbs(context, productImage, data.getImageUri());
            price.setText(data.getPrice());

            container.setOnClickListener(getContainerClickListener(position));

            IndicatorViewHelper.renderBadgesView(context, badgesContainer, data.getBadges());
            IndicatorViewHelper.renderLabelsView(context, labelContainer, data.getLabels());
        }

        private View.OnClickListener getContainerClickListener(final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            };
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
