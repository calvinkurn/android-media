package com.tokopedia.core.util.getproducturlutil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.core.util.getproducturlutil.GetProductUrlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 6/2/16.
 */
public class ProductAdapter extends BaseLinearRecyclerViewAdapter {


    private static final int VIEW_PRODUCT = 100;



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image)
        ImageView image;

        @BindView(R2.id.name)
        TextView name;

        @BindView(R2.id.main)
        View main;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<Product> list;
    private final Context context;
    GetProductUrlUtil.OnGetUrlInterface listener;

    public ProductAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_PRODUCT:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_product_url, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_PRODUCT:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindProduct(ViewHolder holder, final int position) {
        if (position < list.size()) {
            holder.name.setText(list.get(position).getProductName());
            ImageHandler.LoadImage(holder.image, list.get(position).getProductImage());
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGetUrl(list.get(position).getProductUrl());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_PRODUCT;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }


    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount() ;
    }

    public static ProductAdapter createInstance(Context context) {
        return new ProductAdapter(context);
    }

    public ArrayList<Product> getList() {
        return list;
    }

    public void addList(List<Product> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnGetUrlListener(GetProductUrlUtil.OnGetUrlInterface listener) {
        this.listener= listener;
    }
}
