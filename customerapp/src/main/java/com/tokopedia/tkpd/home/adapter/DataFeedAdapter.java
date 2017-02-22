package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.adapter.HistoryProductRecyclerViewAdapter;
import com.tokopedia.core.home.adapter.ProductFeedAdapter;
import com.tokopedia.core.home.adapter.ViewHolderHistoryProduct;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.SimpleHomeActivity;

import org.parceler.Parcels;

import java.util.List;

import static com.tokopedia.core.home.adapter.ProductFeedAdapter.FAVORITE_TAB;
import static com.tokopedia.core.home.adapter.ProductFeedAdapter.HOTLIST_TAB;
import static com.tokopedia.core.home.adapter.ProductFeedAdapter.createViewHistoryProduct;
import static com.tokopedia.core.home.adapter.ProductFeedAdapter.createViewProductFeed;
import static com.tokopedia.core.home.model.HistoryProductListItem.HISTORY_PRODUCT_LIST_ITEM;
import static com.tokopedia.core.var.ProductItem.PRODUCT_ITEM_TYPE;

/**
 * Created by normansyahputa on 9/21/16.
 */
public class DataFeedAdapter extends ProductAdapter {

    public static final String TAG = DataFeedAdapter.class.getSimpleName();
    public static final String MODEL_FLAG = "MODEL_FLAG";
    private ParentIndexHome.ChangeTabListener hotListListener;
    private HistoryProductRecyclerViewAdapter historyAdapter;

    public DataFeedAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PRODUCT_ITEM_TYPE:
                return createViewProductFeed(parent);
            case HISTORY_PRODUCT_LIST_ITEM:
                return createViewHistoryProduct(parent, (HistoryProductListItem) data.get(0), historyAdapter);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case PRODUCT_ITEM_TYPE:
                bindProductFeedViewHolder((ProductFeedAdapter.ViewHolderProductFeed) holder, (ProductItem) data.get(position));
                break;
            case HISTORY_PRODUCT_LIST_ITEM:
                bindHistoryProductViewHolder((ViewHolderHistoryProduct) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    protected boolean checkIfOffset() {
        return data != null && data.size() > 1 && data.get(0).getType() == HISTORY_PRODUCT_LIST_ITEM;
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case PRODUCT_ITEM_TYPE:
            case HISTORY_PRODUCT_LIST_ITEM:
                return recyclerViewItem.getType();
        }

        return super.isInType(recyclerViewItem);
    }

    private void bindProductFeedViewHolder(ProductFeedAdapter.ViewHolderProductFeed holder, ProductItem data) {
        if (data.getSpannedName() != null)
            holder.productName.setText(data.getSpannedName());
        else
            holder.productName.setText(MethodChecker.fromHtml(data.name));
        holder.productPrice.setText(data.price);
        if (data.getSpannedShop() != null)
            holder.shopName.setText(data.getSpannedShop());
        else
            holder.shopName.setText(MethodChecker.fromHtml(data.shop));
        holder.shopLocation.setText(data.shop_location);
        ImageHandler.loadImageFit2(holder.itemView.getContext(), holder.productImage, data.imgUri);
        setLabels(holder, data);
        setBadges(holder, data);
        holder.mainContent.setOnClickListener(onClickProductItem(data));
    }

    private void setBadges(ProductFeedAdapter.ViewHolderProductFeed holder, ProductItem data) {
        holder.badgesContainer.removeAllViews();
        holder.badgesContainer.setVisibility(View.GONE);
        if (data.getBadges() != null)
            for (Badge badges : data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.getImageUrl(), holder.badgesContainer);
            }
        holder.badgesContainer.setVisibility(View.VISIBLE);
    }

    private void setLabels(ProductFeedAdapter.ViewHolderProductFeed holder, ProductItem data) {
        holder.labelsContainer.removeAllViews();
        if (data.getLabels() != null)
            for (Label label : data.getLabels()) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals(context.getString(R.string.white_hex_color))) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                holder.labelsContainer.addView(view);
            }
    }

    private View.OnClickListener onClickProductItem(final ProductItem data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UnifyTracking.eventFeedView(data.getName());
                context.startActivity(
                        ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                context, productPass(data)
                        )
                );
            }
        };
    }

    private void bindHistoryProductViewHolder(ViewHolderHistoryProduct holder, int position) {
        if (data != null && data.size() > 0) {
            HistoryProductListItem dataHistory = (HistoryProductListItem) data.get(0);
            if (!dataHistory.getProductItems().isEmpty()) {
                renderHistoryProduct(holder, dataHistory);
            } else {
                renderEmptyHistoryProduct(holder);
            }
        } else {
            renderEmptyHistoryProduct(holder);
        }
        holder.seeAll.setOnClickListener(onSeeAllHistory());
        holder.findNow.setOnClickListener(onFindNowClicked());

        if (data.size() == 1) {
            holder.emptyLayout.setVisibility(View.VISIBLE);
            holder.findFavoriteShop.setOnClickListener(onFindFavoriteClicked());
        } else {
            holder.emptyLayout.setVisibility(View.GONE);
        }

    }

    private void renderHistoryProduct(ViewHolderHistoryProduct holder, HistoryProductListItem dataHistory) {
        holder.recyclerView.setVisibility(View.VISIBLE);
        holder.title.setVisibility(View.VISIBLE);
        holder.seeAll.setVisibility(View.VISIBLE);
        holder.emptyHistory.setVisibility(View.GONE);
        holder.historyAdapter.notifyDataSetChanged();
    }

    private void renderEmptyHistoryProduct(ViewHolderHistoryProduct holder) {
        holder.recyclerView.setVisibility(View.GONE);
        holder.title.setVisibility(View.GONE);
        holder.seeAll.setVisibility(View.GONE);
        holder.emptyHistory.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onSeeAllHistory() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventFeedViewAll();
                Intent intent = new Intent(context, SimpleHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.PRODUCT_HISTORY_FRAGMENT);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    private View.OnClickListener onFindNowClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotListListener = ((ParentIndexHome) context).GetHotListListener();
                hotListListener.onChangeTab(HOTLIST_TAB);
            }
        };
    }

    private View.OnClickListener onFindFavoriteClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentIndexHome.ChangeTabListener listener = ((ParentIndexHome) context).GetFavoriteListener();
                listener.onChangeTab(FAVORITE_TAB);
            }
        };
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MODEL_FLAG, Parcels.wrap(data));
    }

    public HistoryProductRecyclerViewAdapter getHistoryAdapter() {
        return historyAdapter;
    }

    public void addNextPage(List<RecyclerViewItem> newData) {
        data.addAll(newData);
    }

    public void updateHistoryAdapter(RecyclerViewItem recyclerViewItem) {
        if (recyclerViewItem != null && recyclerViewItem instanceof HistoryProductListItem) {
            HistoryProductListItem item = (HistoryProductListItem) recyclerViewItem;
            if (historyAdapter != null) {
                historyAdapter.setData(item.getProductItems());
            } else {
                historyAdapter = new HistoryProductRecyclerViewAdapter(context, item.getProductItems());
            }
        }
    }

    @Override
    public int addTopAds(List<ProductItem> listProduct, int page) {
        return super.addTopAds(listProduct, page);
    }

    public boolean isHistory(int position) {
        boolean isInRange = position >= 0 && position < data.size();
        return isInRange && data.get(position) != null && data.get(position) instanceof HistoryProductListItem;
    }

    private ProductPass productPass(ProductItem productItem) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(productItem.getPrice())
                .setProductId(productItem.getId())
                .setProductName(productItem.getName())
                .setProductImage(productItem.getImgUri())
                .build();
    }
}
