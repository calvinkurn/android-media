package com.tokopedia.core.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.home.fragment.FragmentIndexMainHeader;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.home.model.ViewHolderProductTopAds;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

/**
 * Created by Nisie on 5/06/15.
 * modified by m.normansyah 21/9/2016 change adapter to just helper class.
 */
public class ProductFeedAdapter {

    private static final String TAG = ProductFeedAdapter.class.getSimpleName();
    public static final int HOTLIST_TAB = 3;
    public static final int FAVORITE_TAB = 2;
    private static final int HISTORY_PRODUCT = 1;
    private static final int TOTAL_PRODUCT = 12;
    private static final int TOP_ADS = 1;

    public static class ViewHolderProductFeed extends RecyclerView.ViewHolder {

        public RelativeLayout mainContent;
        public LinearLayout badgesContainer;
        public FlowLayout labelsContainer;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public TextView shopLocation;
        public ImageView productImage;
        public View grosir;
        public View preorder;

        public ViewHolderProductFeed(View itemLayoutView) {
            super(itemLayoutView);
            mainContent = (RelativeLayout) itemLayoutView.findViewById(R.id.container);
            badgesContainer = (LinearLayout) itemLayoutView.findViewById(R.id.badges_container);
            labelsContainer = (FlowLayout) itemLayoutView.findViewById(R.id.label_container);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            shopLocation = (TextView) itemLayoutView.findViewById(R.id.location);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            grosir = itemLayoutView.findViewById(R.id.grosir);
            preorder = itemLayoutView.findViewById(R.id.preorder);
        }
    }

    public static class ViewHolderEmpty extends RecyclerView.ViewHolder {

        LinearLayout emptyIndexMain;
        TextView hotListButton;

        public ViewHolderEmpty(View itemView) {
            super(itemView);
            emptyIndexMain = (LinearLayout) itemView.findViewById(R.id.empty_index_main);
            hotListButton = (TextView) itemView.findViewById(R.id.hot_list_button);
        }
    }

    private RecyclerView.ViewHolder createViewEmpty(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_index_main, null);
        return new ViewHolderEmpty(itemLayoutView);
    }

    public static ViewHolderProductTopAds createViewTopAds(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_main_top_ads, null);
        return new ViewHolderProductTopAds(itemLayoutView);
    }

    public static ViewHolderProductFeed createViewProductFeed(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_product_item_grid, parent, false);
        return new ViewHolderProductFeed(itemLayoutView);
    }

    public static ViewHolderHistoryProduct
                createViewHistoryProduct(ViewGroup parent,
                                         HistoryProductListItem historyProductListItem) {
        return new ViewHolderHistoryProduct(new FragmentIndexMainHeader()
                .addParentView(LayoutInflater.from(parent.getContext()), parent)
                .initView(R.id.history_product_recycler_view)
                .addData(historyProductListItem.getProductItems())
                .createAdapter());
    }

    public static ViewHolderHistoryProduct
    createViewHistoryProduct(ViewGroup parent,
                             HistoryProductListItem historyProductListItem,
                             HistoryProductRecyclerViewAdapter historyProductRecyclerViewAdapter) {
        return new ViewHolderHistoryProduct(new FragmentIndexMainHeader()
                .addParentView(LayoutInflater.from(parent.getContext()), parent)
                .initView(R.id.history_product_recycler_view)
                .addData(historyProductListItem.getProductItems())
                .createAdapter(historyProductRecyclerViewAdapter));
    }

    private void bindEmpty(ViewHolderEmpty holder, int position) {
//        holder.hotListButton.setOnClickListener(onHotListClicked());
    }

    public static boolean isTopAds(List<RecyclerViewItem> data, int position) {
        try {
            return position > 0
                    && data.get(position).getType() == TkpdState.RecyclerViewItem.TYPE_LIST
                    && ((HorizontalProductList) data.get(position)).getListProduct().get(0).getIsTopAds();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTopAds(RecyclerViewItem data) {
        return data.getType() == TkpdState.RecyclerView.VIEW_TOP_ADS;
    }

}