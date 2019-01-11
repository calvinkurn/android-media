package com.tokopedia.discovery.catalog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.discovery.catalog.presenter.ICatalogDetailListPresenter;

import java.util.ArrayList;
import java.util.List;

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

        holder.productName.setText(MethodChecker.fromHtml(catalogDetailItemProduct.getName()));
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
        private TextView productName;
        private TextView productPrice;
        private TextView productCondition;
        private TextView productId;
        private RelativeLayout productContainer;

        ProductItemHolder(View view) {
            super(view);
            initView(view);
        }

        private void initView(View view) {
            productName = view.findViewById(R.id.product_name);
            productPrice = view.findViewById(R.id.prod_price);
            productCondition = view.findViewById(R.id.prod_condition);
            productId = view.findViewById(R.id.product_id);
            productContainer = view.findViewById(R.id.main_view);
        }
    }
}
