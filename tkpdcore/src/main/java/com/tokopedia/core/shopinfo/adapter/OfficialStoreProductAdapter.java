package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by nakama on 26/04/17.
 */

public class OfficialStoreProductAdapter extends RecyclerView.Adapter {

    private static final int SPAN_COUNT = 2;

    private ProductModel productModel;
    private ProductLargeDelegate grid;
    private ProductListAdapterListener listener;

    public interface ProductListAdapterListener {

        void onProductClick(int pos);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return grid.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        grid.onBindViewHolder(productModel.list.get(position), holder);
        grid.onItemClickListener(onProductItemClick(position), holder);
    }

    @Override
    public int getItemCount() {
        return productModel.list.size();
    }

    public OfficialStoreProductAdapter(ProductModel productModel) {
        grid = new ProductLargeDelegate();
        this.productModel = productModel;
    }

    public void setListener(ProductListAdapterListener listener) {
        this.listener = listener;
    }

    private View.OnClickListener onProductItemClick(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductClick(i);
            }
        };
    }

    public GridLayoutManager getLayoutManager(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, SPAN_COUNT);
        return gridLayoutManager;
    }
}
