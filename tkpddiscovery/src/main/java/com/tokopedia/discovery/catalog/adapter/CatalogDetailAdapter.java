package com.tokopedia.discovery.catalog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.catalog.model.CatalogDetailItem;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemShop;
import com.tokopedia.discovery.catalog.presenter.ICatalogDetailListPresenter;

import java.util.ArrayList;
import java.util.List;

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
        holder.shopName.setText(MethodChecker.fromHtml(shop.getName()));
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

        private ImageView shopImage;
        private TextView shopName;
        private TextView shopLocation;
        private ImageView shopRating;
        private RecyclerView listProduct;
        private RelativeLayout shopContainer;

        ShopItemHolder(View view) {
            super(view);
            initView(view);
        }

        private void initView(View view) {
            shopImage = view.findViewById(R.id.seller_img);
            shopName = view.findViewById(R.id.seller_name);
            shopLocation = view.findViewById(R.id.seller_loc);
            shopRating = view.findViewById(R.id.seller_rating);
            listProduct = view.findViewById(R.id.list_product);
            shopContainer = view.findViewById(R.id.header_seller);
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
