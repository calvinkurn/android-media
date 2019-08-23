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
import com.tokopedia.core2.R;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.helper.IndicatorViewHelper;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductItem;

import java.util.ArrayList;
import java.util.List;

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
        return new ViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
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
        ImageView productImage;
        TextView title;
        TextView price;
        FlowLayout labelContainer;
        LinearLayout badgesContainer;
        View container;

        private final OnItemClickListener onItemClickListener;
        private Context context;

        public ViewHolder(Context context, View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            this.context = context;

            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
            badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
            container = (View) itemView.findViewById(R.id.container);
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
