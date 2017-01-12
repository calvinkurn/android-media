package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.favorite.presenter.Favorite;
import com.tokopedia.tkpd.home.model.HorizontalShopList;

import java.util.List;

/**
 * Created by Nisie on 11/06/15.
 */
public class FavoriteRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    Favorite favorite;

    private static final int HOTLIST_TAB = 3;

    public static class ViewHolderWishList extends RecyclerView.ViewHolder {

        TextView seeAll;
        RelativeLayout emptyWishlist;
        LinearLayout mainContent;
        RecyclerView recyclerView;
        TextView findNow;
        TextView title;

        public ViewHolderWishList(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.title);
            seeAll = (TextView) itemLayoutView.findViewById(R.id.textview_see_all);
            emptyWishlist = (RelativeLayout) itemLayoutView.findViewById(R.id.empty_wishlist);
            recyclerView = (RecyclerView) itemLayoutView.findViewById(R.id.wishlist_recycler_view);
            mainContent = (LinearLayout) itemLayoutView.findViewById(R.id.main_content);
            findNow = (TextView) itemLayoutView.findViewById(R.id.find_now);
        }

    }

    public static class ViewHolderRecShop extends RecyclerView.ViewHolder {

        LinearLayout mainContent;
        RecyclerView recyclerView;
        LinearLayout recContent;


        public ViewHolderRecShop(View itemLayoutView) {
            super(itemLayoutView);
            recyclerView = (RecyclerView) itemLayoutView.findViewById(R.id.rec_shop_recycler_view);
            mainContent = (LinearLayout) itemLayoutView.findViewById(R.id.main_content);
            recContent = (LinearLayout) itemLayoutView.findViewById(R.id.rec_shop_content);
        }

    }

    public static class ViewHolderFavShop extends RecyclerView.ViewHolder {

        RelativeLayout shopLayout;
        ImageView avatar;
        TextView name;
        TextView location;
        ImageView favorite;

        public ViewHolderFavShop(View itemLayoutView) {
            super(itemLayoutView);
            shopLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.shop_layout);
            avatar = (ImageView) itemLayoutView.findViewById(R.id.shop_avatar);
            name = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            location = (TextView) itemLayoutView.findViewById(R.id.location);
            favorite = (ImageView) itemLayoutView.findViewById(R.id.fav_button);
        }

        public Context getContext(){
            return itemView.getContext();
        }

    }

    private List<RecyclerViewItem> data;
    private Context context;
    private RecommendShopRecyclerViewAdapter recAdapter;
    private RecommendShopRecyclerViewAdapter.RecShopListener recClicklistener;
    private WishlistRecyclerViewAdapter wishAdapter;
    private ParentIndexHome.ChangeTabListener hotListListener;

    public FavoriteRecyclerViewAdapter(Context context,
                                       List<RecyclerViewItem> data,
                                       RecommendShopRecyclerViewAdapter.RecShopListener recClicklistener) {
        super(context, data);
        this.data = data;
        this.context = context;
        this.recClicklistener = recClicklistener;

    }

    public FavoriteRecyclerViewAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TkpdState.RecyclerView.VIEW_WISHLIST:
                return createViewHolderWishlist(parent);
            case TkpdState.RecyclerView.VIEW_REC_SHOP:
                return createViewHolderRecShop(parent);
            case TkpdState.RecyclerView.VIEW_FAV_SHOP:
                return createViewHolderFavShop(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder createViewHolderFavShop(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_manage_favorited_shop, null);
        return new ViewHolderFavShop(view);
    }

    private RecyclerView.ViewHolder createViewHolderRecShop(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_favorite_rec_shop, null);
        ViewHolderRecShop viewHolder = new ViewHolderRecShop(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        viewHolder.recyclerView.setHasFixedSize(true);
        HorizontalShopList dataShopReccomend = (HorizontalShopList) data.get(Favorite.TOP_ADS_START);
        recAdapter = new RecommendShopRecyclerViewAdapter(context, dataShopReccomend.getShopItemList(), getFavorite());
        viewHolder.recyclerView.setAdapter(recAdapter);
        return viewHolder;
    }

    private RecyclerView.ViewHolder createViewHolderWishlist(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_favorite_wishlist, null);
        ViewHolderWishList viewHolder = new ViewHolderWishList(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        viewHolder.recyclerView.setHasFixedSize(true);
        HorizontalProductList dataWishlist = (HorizontalProductList) data.get(Favorite.WISHLIST_START);
        wishAdapter = new WishlistRecyclerViewAdapter(context, dataWishlist.getListProduct());
        viewHolder.recyclerView.setAdapter(wishAdapter);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_WISHLIST:
                bindWishListViewHolder((ViewHolderWishList) holder);
                break;
            case TkpdState.RecyclerView.VIEW_REC_SHOP:
                bindRecShopViewHolder((ViewHolderRecShop) holder);
                break;
            case TkpdState.RecyclerView.VIEW_FAV_SHOP:
                bindFavShopViewHolder((ViewHolderFavShop) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;

        }

    }

    private void bindFavShopViewHolder(ViewHolderFavShop holder, int position) {

        ShopItem shop = (ShopItem) data.get(position);
        //[BUGFIX] AN-1118 [Home & Login V2] Appear HTML tag on Favorit Tab
        holder.name.setText(MethodChecker.fromHtml(shop.name));
        //[BUGFIX] AN-1118 [Home & Login V2] Appear HTML tag on Favorit Tab
        holder.location.setText(shop.location);
        setFavorite(holder, position);
//        ImageHandler.LoadImageFit(holder.avatar, shop.iconUri);
        ImageHandler.loadImageFit2(holder.getContext(), holder.avatar, shop.iconUri);

        holder.shopLayout.setOnClickListener(onShopClicked(shop));

    }

    private View.OnClickListener onShopClicked(final ShopItem shop) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventFavoriteShop(shop.name);
                favorite.moveToOtherActivity(new RecyclerViewItem(), ShopInfoActivity.class, new Object[]{"shop_id", shop.id});
            }
        };
    }

    private void setFavorite(ViewHolderFavShop holder, int position) {
        ShopItem shop = (ShopItem) data.get(position);
        //sometimes there is a case isFav is null, so we have to prevenet it by hardcoded the value -rico-
        if (shop.isFav == null) {
            shop.isFav = "0";
        }
        if (shop.isFav.equals("1")) {
            holder.favorite.setImageResource(R.drawable.ic_faved);

        } else {
            holder.favorite.setImageResource(R.drawable.ic_fav);
        }

    }

    private void bindRecShopViewHolder(ViewHolderRecShop holder) {

        recAdapter.notifyDataSetChanged();

    }

    private void bindWishListViewHolder(ViewHolderWishList holder) {

        HorizontalProductList dataWishlist = (HorizontalProductList) data.get(0);
        if (!dataWishlist.getListProduct().isEmpty()) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.seeAll.setVisibility(View.VISIBLE);
            holder.emptyWishlist.setVisibility(View.GONE);
            wishAdapter.notifyDataSetChanged();

        } else {
            holder.recyclerView.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
            holder.seeAll.setVisibility(View.GONE);
            holder.emptyWishlist.setVisibility(View.VISIBLE);
        }

        holder.findNow.setOnClickListener(onFindNow());
        holder.seeAll.setOnClickListener(onSeeAllClicked());

    }

    private View.OnClickListener onFindNow() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotListListener = ((ParentIndexHome) context).GetHotListListener();
                hotListListener.onChangeTab(HOTLIST_TAB);
            }
        };
    }

    private View.OnClickListener onSeeAllClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavorite().moveToOtherActivity((HorizontalProductList) data.get(0), SimpleHomeActivity.class, new Object[]{});
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position)) return super.getItemViewType(position);
        else {
            switch (position) {
                case 0:
                    return TkpdState.RecyclerView.VIEW_WISHLIST;
                case 1:
                    return TkpdState.RecyclerView.VIEW_REC_SHOP;
                default:
                    return TkpdState.RecyclerView.VIEW_FAV_SHOP;
            }
        }
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }
}