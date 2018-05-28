package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsBrandPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

public class DealsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private static final int ITEM2 = 3;
    private boolean isFooterAdded = false;
    private boolean isShortLayout;


    public DealsCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems, boolean isShortLayout) {
        this.context = context;
        this.categoryItems = categoryItems;
        this.isShortLayout = isShortLayout;
    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.deals_item_card, parent, false);
                holder = new ItemViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            case ITEM2:
                v = inflater.inflate(R.layout.deal_item_card_short, parent, false);
                holder = new ItemViewHolder2(v);
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((ItemViewHolder) holder).bindData(categoryItems.get(position), position);
                ((ItemViewHolder) holder).setIndex(position);
                break;
            case FOOTER:
                break;
            case ITEM2:
                ((ItemViewHolder2) holder).bindData(categoryItems.get(position), position);
                ((ItemViewHolder2) holder).setIndex(position);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        return isShortLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM2
                : (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new CategoryItemsViewModel());
        }
    }

    public void add(CategoryItemsViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<CategoryItemsViewModel> items) {
        for (CategoryItemsViewModel item : items) {
            add(item);
        }
    }


    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            CategoryItemsViewModel item = categoryItems.get(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private ImageView brandImage;
        private TextView discount;
        private TextView likes;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView dealavailableLocations;
        private TextView dealListPrice;
        private ImageView ivFavourite;
        private ImageView ivShareVia;
        private TextView dealSellingPrice;
        private TextView hotDeal;
        private int index;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.imageView);
            likes = itemView.findViewById(R.id.tv_wish_list);
            discount = itemView.findViewById(R.id.tv_off);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            ivFavourite = itemView.findViewById(R.id.iv_wish_list);
            dealavailableLocations = itemView.findViewById(R.id.tv_available_locations);
            dealListPrice = itemView.findViewById(R.id.tv_Mrp);
            brandImage = itemView.findViewById(R.id.iv_brand);
            ivShareVia = itemView.findViewById(R.id.iv_share);
            dealSellingPrice = itemView.findViewById(R.id.tv_salesPrice);
            hotDeal = itemView.findViewById(R.id.tv_hot_deal);
        }

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel, int position) {
            dealsDetails.setText(categoryItemsViewModel.getDisplayName());
            ImageHandler.loadImage(context, dealImage, categoryItemsViewModel.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            ImageHandler.loadImage(context, brandImage, categoryItemsViewModel.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            likes.setText(String.valueOf(categoryItemsViewModel.getLikes()));
            dealListPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getMrp()));
            dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(categoryItemsViewModel.getSavingPercentage());
            dealSellingPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getSalesPrice()));
            if (categoryItemsViewModel.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            brandName.setText(categoryItemsViewModel.getBrand().getTitle());
            itemView.setOnClickListener(this);
            ivShareVia.setOnClickListener(this);
            ivFavourite.setOnClickListener(this);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_share) {
                Toast.makeText(context, "iv_share", Toast.LENGTH_SHORT).show();
            } else if (v.getId() == R.id.iv_wish_list) {
                Toast.makeText(context, "iv_favourite", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "iv_card", Toast.LENGTH_SHORT).show();
                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
//            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()));
                context.startActivity(detailsIntent);
            }
        }
    }

    public class ItemViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private ImageView brandImage;
        private TextView discount;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView dealListPrice;
        private TextView dealSellingPrice;
        private TextView hotDeal;
        private int index;

        public ItemViewHolder2(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.imageView);
            discount = itemView.findViewById(R.id.tv_off);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            dealListPrice = itemView.findViewById(R.id.tv_Mrp);
            brandImage = itemView.findViewById(R.id.iv_brand);
            dealSellingPrice = itemView.findViewById(R.id.tv_salesPrice);
            hotDeal = itemView.findViewById(R.id.tv_hot_deal);
        }

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel, int position) {
            dealsDetails.setText(categoryItemsViewModel.getDisplayName());
            ImageHandler.loadImage(context, dealImage, categoryItemsViewModel.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            ImageHandler.loadImage(context, brandImage, categoryItemsViewModel.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            brandName.setText(categoryItemsViewModel.getBrand().getTitle());
            if (categoryItemsViewModel.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            dealListPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getMrp()));
            dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(categoryItemsViewModel.getSavingPercentage());
            dealSellingPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getSalesPrice()));
            itemView.setOnClickListener(this);

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "iv_card", Toast.LENGTH_SHORT).show();
            Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
            detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()));
            context.startActivity(detailsIntent);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

}
