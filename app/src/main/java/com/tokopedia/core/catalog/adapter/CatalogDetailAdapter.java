package com.tokopedia.core.catalog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.catalog.model.CatalogDetailItem;
import com.tokopedia.core.catalog.model.CatalogDetailItemShop;
import com.tokopedia.core.catalog.presenter.ICatalogDetailListPresenter;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int TYPE_VIEW_CATALOG_LIST = 120;
    private List<CatalogDetailItem> mCatalogDetailItem = new ArrayList<>();
    private Context mContext;
    private ICatalogDetailListPresenter mPresenter;

    private CatalogDetailAdapter(Context context,
                                 List<CatalogDetailItem> catalogDetailItems,
                                 ICatalogDetailListPresenter presenter) {
        this.mCatalogDetailItem = catalogDetailItems;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    public static CatalogDetailAdapter createAdapter(Context context,
                                                     List<CatalogDetailItem> catalogDetailItems,
                                                     ICatalogDetailListPresenter presenter) {
        return new CatalogDetailAdapter(context, catalogDetailItems, presenter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VIEW_CATALOG_LIST:
                @SuppressLint("InflateParams") View view = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.list_catalog_seller, null
                );
                return new ShopItemHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_VIEW_CATALOG_LIST:
                bindShopView((ShopItemHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    private void bindShopView(final ShopItemHolder holder, int position) {
        if (this.mCatalogDetailItem.get(position) == null) return;
        CatalogDetailItemShop shop = this.mCatalogDetailItem.get(position).getCatalogDetailItemShop();
        holder.shopName.setText(Html.fromHtml(shop.getName()));
        if (shop.getCity() != null) {
            holder.shopLocation.setText(shop.getCity());
        }
        if (!TextUtils.isEmpty(shop.getReputationImageUri())){
            ImageHandler.loadImageFitCenter(mContext, holder.shopRating, shop.getReputationImageUri());
        }
        ImageHandler.loadImageCircle2(mContext, holder.shopImage, shop.getImageUri());
        CatalogDetailProductAdapter adapter = CatalogDetailProductAdapter.createAdapter(mContext,
                this.mCatalogDetailItem.get(position).getCatalogDetailItemProductList(), mPresenter);
        holder.listProduct.setHasFixedSize(true);
        holder.listProduct.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        );
        holder.listProduct.setAdapter(adapter);
        holder.shopContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToShopPage(
                        mCatalogDetailItem.get(holder.getAdapterPosition()).getCatalogDetailItemShop()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mCatalogDetailItem.size() + super.getItemCount();
    }

    class ShopItemHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.seller_img)
        ImageView shopImage;
        @Bind(R2.id.seller_name)
        TextView shopName;
        @Bind(R2.id.seller_loc)
        TextView shopLocation;
        @Bind(R2.id.seller_rating)
        ImageView shopRating;
        @Bind(R2.id.list_product)
        RecyclerView listProduct;
        @Bind(R2.id.header_seller)
        RelativeLayout shopContainer;

        ShopItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCatalogDetailItem.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return TYPE_VIEW_CATALOG_LIST;
        }
    }
}
