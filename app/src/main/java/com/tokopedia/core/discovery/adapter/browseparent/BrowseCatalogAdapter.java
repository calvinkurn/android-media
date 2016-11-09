package com.tokopedia.core.discovery.adapter.browseparent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.discovery.activity.BrowseProductActivity;
import com.tokopedia.core.discovery.adapter.ProductAdapter;
import com.tokopedia.core.discovery.model.BrowseCatalogModel;
import com.tokopedia.core.router.Router;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Toped10 on 7/4/2016.
 */
public class BrowseCatalogAdapter extends ProductAdapter {
    public static final int CATALOG_MODEL_TYPE = 1_234_15;
    private static final String TAG = BrowseCatalogAdapter.class.getSimpleName();

    public BrowseCatalogAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CATALOG_MODEL_TYPE:
                return new CatalogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_catalog_item_list, parent, false));
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return new CatalogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_catalog_item_grid, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CATALOG_MODEL_TYPE:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                CatalogViewHolder itemHolder = (CatalogViewHolder) holder;
                itemHolder.bindData(context, itemHolder, (CatalogModel) getData().get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public void setGridView(BrowseProductActivity.GridType gridType) {
        Log.d(TAG, "GridType " + gridType.name());
        for (RecyclerViewItem item : data) {
            if (item.getType() == CATALOG_MODEL_TYPE || item.getType() == TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1 || item.getType() == TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2) {
                if (gridType.equals(BrowseProductActivity.GridType.GRID_1)) {
                    item.setType(CATALOG_MODEL_TYPE);
                } else if (gridType.equals(BrowseProductActivity.GridType.GRID_2)) {
                    item.setType(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2);
                } else {
                    item.setType(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1);
                }
            }
        }
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.product_image)
        SquareImageView productImage;
        @Bind(R2.id.title)
        TextView title;
        @Bind(R2.id.price)
        TextView price;
        @Bind(R2.id.seller)
        TextView seller;
        @Bind(R2.id.badges_container)
        LinearLayout badgesContainer;

        private Context context;
        private CatalogModel catalogModel;

        public CatalogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
        }

        public void bindData(Context context, CatalogViewHolder holder, CatalogModel data) {
            this.catalogModel = data;
            ImageHandler.loadImageThumbs(context, holder.productImage, catalogModel.catalogImage);
            holder.seller.setText(catalogModel.catalogCountProduct + " " + context.getString(R.string.title_total_prods));
            holder.title.setText(Html.fromHtml(catalogModel.catalogName));
            holder.price.setText(catalogModel.catalogPrice);
        }

        @OnClick(R2.id.container)
        public void onClick() {
//            Intent intent = new Intent(context, Catalog.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("ctg_id", catalogModel.catalogId);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
            context.startActivity(Router.getCatalogDetailActivity(context, catalogModel.catalogId));
        }
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case CATALOG_MODEL_TYPE:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return recyclerViewItem.getType();
        }
        return super.isInType(recyclerViewItem);
    }

    @Parcel
    public static class CatalogModel extends RecyclerViewItem {
        String catalogName;
        String catalogDescription;
        String catalogImage;
        String catalogCountProduct;
        String catalogImage300;
        String catalogPrice;
        String catalogUri;
        String catalogId;

        public CatalogModel() {
            setType(CATALOG_MODEL_TYPE);
        }

        public CatalogModel(BrowseCatalogModel.Catalogs catalogs) {
            this();
            catalogName = catalogs.catalogName;
            catalogDescription = catalogs.catalogDescription;
            catalogImage = catalogs.catalogImage;
            catalogCountProduct = catalogs.catalogCountProduct;
            catalogImage300 = catalogs.catalogImage300;
            catalogPrice = catalogs.catalogPrice;
            catalogUri = catalogs.catalogUri;
            catalogId = catalogs.catalogId;
        }
    }
}
