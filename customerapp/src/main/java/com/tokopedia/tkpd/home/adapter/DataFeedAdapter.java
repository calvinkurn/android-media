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
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.adapter.HistoryProductRecyclerViewAdapter;
import com.tokopedia.core.home.adapter.ViewHolderHistoryProduct;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.feed.view.viewModel.EmptyFeedModel;
import com.tokopedia.tkpd.home.feed.view.viewModel.RetryFeedModel;

import org.parceler.Parcels;

import java.util.List;

import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.FAVORITE_TAB;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.HOTLIST_TAB;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.createEmtpyFeed;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.createRetryFeed;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.createViewHistoryProduct;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.createViewProductFeed;
import static com.tokopedia.core.home.model.HistoryProductListItem.HISTORY_PRODUCT_LIST_ITEM;
import static com.tokopedia.core.var.ProductItem.PRODUCT_ITEM_TYPE;

/**
 * Created by normansyahputa on 9/21/16.
 */
public class DataFeedAdapter extends BaseRecyclerViewAdapter {

    public static final String TAG = DataFeedAdapter.class.getSimpleName();
    public static final String MODEL_FLAG = "MODEL_FLAG";
    private ParentIndexHome.ChangeTabListener hotListListener;
    private HistoryProductRecyclerViewAdapter historyAdapter;
    private OnRetryListener retryListener;
    private RetryFeedModel retryModel;

    public DataFeedAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        retryModel = new RetryFeedModel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PRODUCT_ITEM_TYPE:
                return createViewProductFeed(parent);
            case HISTORY_PRODUCT_LIST_ITEM:
                return createViewHistoryProduct(parent, (HistoryProductListItem) data.get(0), historyAdapter);
            case EmptyFeedModel.EMPTY_FEED:
                return createEmtpyFeed(parent);
            case RetryFeedModel.RETRY_FEED:
                return createRetryFeed(parent);
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
            case EmptyFeedModel.EMPTY_FEED:
                bindEmptyFeedModel((ViewHolderEmptyFeed) holder, position);
                break;
            case RetryFeedModel.RETRY_FEED:
                bindRetryFeedModel((ViewHolderRetryFeed) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (checkDataSize(position)) {
            RecyclerViewItem recyclerViewItem = data.get(position);
            return isInType(recyclerViewItem);
        } else {
            return super.getItemViewType(position);
        }
    }

    protected boolean checkIfOffset() {
        return data != null && data.size() > 1 && data.get(0).getType() == HISTORY_PRODUCT_LIST_ITEM;
    }

    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            case PRODUCT_ITEM_TYPE:
            case HISTORY_PRODUCT_LIST_ITEM:
            case EmptyFeedModel.EMPTY_FEED:
            case RetryFeedModel.RETRY_FEED:
                return recyclerViewItem.getType();
            default:
                return -1;
        }
    }

    private boolean checkDataSize(int position) {
        return data != null && data.size() > 0 && position > -1 && position < data.size();
    }

    private void bindEmptyFeedModel(ViewHolderEmptyFeed holder, int position) {
        holder.checkFavoriteShopButton.setOnClickListener(onFindFavoriteClicked());
        holder.officialStoreLinkContainer.setOnClickListener(onOfficialStoreLinkClicked());
        holder.generateTopAds();
    }

    private void bindRetryFeedModel(ViewHolderRetryFeed holder, int position) {
        holder.mainRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(retryListener!=null){
                    retryListener.onRetryCliked();
                }
            }
        });
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
            holder.officialStoreLinkContainer.setOnClickListener(onOfficialStoreLinkClicked());
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

    private View.OnClickListener onOfficialStoreLinkClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventBannerEmptyFeedOS();
                context.startActivity(BrandsWebViewActivity.newInstance(context,
                        TkpdBaseURL.OfficialStore.URL_WEBVIEW));
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
        addAll(newData);
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

    public boolean isHistory(int position) {
        boolean isInRange = position >= 0 && position < data.size();
        return isInRange && data.get(position) != null && data.get(position) instanceof HistoryProductListItem;
    }

    public boolean isEmptyFeed(int position) {
        boolean isInRange = position >= 0 && position < data.size();

        return isInRange && data.get(position)
                != null && data.get(position) instanceof EmptyFeedModel;
    }

    public boolean isRetry(int position) {
        boolean isInRange = position >= 0 && position < data.size();
        return isInRange && data.get(position)
                != null && data.get(position) instanceof RetryFeedModel;
    }

    private ProductPass productPass(ProductItem productItem) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(productItem.getPrice())
                .setProductId(productItem.getId())
                .setProductName(productItem.getName())
                .setProductImage(productItem.getImgUri())
                .build();
    }

    public void setData(List<RecyclerViewItem> datas) {
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    public void addAll(List<RecyclerViewItem> datas) {
        int positionStart = getItemCount();
        data.addAll(datas);
        notifyItemRangeInserted(positionStart, datas.size());
    }

    public void setEmptyFeed() {
        data.add(new EmptyFeedModel());
        notifyItemInserted(1);
    }

    public void setRetryFeed(){
        int posStart = getItemCount();
        data.add(retryModel);
        notifyItemRangeInserted(posStart, 1);
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.retryListener = onRetryListener;
    }

    public void removeRetry() {
        int index = this.data.indexOf(retryModel);
        this.data.remove(retryModel);
        notifyItemRemoved(index);
    }
}
