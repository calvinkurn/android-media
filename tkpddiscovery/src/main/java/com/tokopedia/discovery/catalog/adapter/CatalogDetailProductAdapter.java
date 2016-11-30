package com.tokopedia.discovery.catalog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.discovery.catalog.presenter.ICatalogDetailListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by alvarisi on 10/18/16.
 */

class CatalogDetailProductAdapter extends
        RecyclerView.Adapter<CatalogDetailProductAdapter.ProductItemHolder> {

    private List<CatalogDetailItemProduct> catalogDetailItemProductList = new ArrayList<>();
    private Context mContext;
    private ICatalogDetailListPresenter mPresenter;

    private CatalogDetailProductAdapter(Context context,
                                        List<CatalogDetailItemProduct> catalogDetailItemProductList,
                                        ICatalogDetailListPresenter presenter) {
        this.catalogDetailItemProductList = catalogDetailItemProductList;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    public static CatalogDetailProductAdapter createAdapter(Context context,
                                                            List<CatalogDetailItemProduct> products,
                                                            ICatalogDetailListPresenter presenter) {
        return new CatalogDetailProductAdapter(context, products, presenter);
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_catalog_seller_product, null
        );
        return new ProductItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductItemHolder holder, int position) {
        CatalogDetailItemProduct catalogDetailItemProduct = this.catalogDetailItemProductList.get(position);

        holder.productName.setText(Html.fromHtml(catalogDetailItemProduct.getName()));
        if (catalogDetailItemProduct.getCondition() != null
                && catalogDetailItemProduct.getCondition() == 1) {
            holder.productCondition.setText(mContext.getString(R.string.title_new));
        } else {
            holder.productCondition.setText(mContext.getString(R.string.title_used));
        }

        if (!TextUtils.isEmpty(catalogDetailItemProduct.getPrice())) {
            holder.productPrice.setText(catalogDetailItemProduct.getPrice());
        }

        holder.productContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToProductDetailPage(
                        catalogDetailItemProductList.get(holder.getAdapterPosition())
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return catalogDetailItemProductList.size();
    }

    class ProductItemHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_name)
        TextView productName;
        @BindView(R2.id.prod_price)
        TextView productPrice;
        @BindView(R2.id.prod_condition)
        TextView productCondition;
        @BindView(R2.id.product_id)
        TextView productId;
        @BindView(R2.id.main_view)
        RelativeLayout productContainer;

        ProductItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
