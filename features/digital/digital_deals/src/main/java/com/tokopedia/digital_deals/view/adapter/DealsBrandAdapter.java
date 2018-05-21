package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.presenter.DealsBrandPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

public class DealsBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BrandViewModel> brandItems;
    private Context context;
    private int MAX_BRANDS = 8;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private static final int ITEM2 = 3;
    private boolean isFooterAdded = false;
    private boolean isShortLayout;


    public DealsBrandAdapter(Context context, List<BrandViewModel> brandItems, boolean isShortLayout) {
        this.context = context;
        this.brandItems = new ArrayList<>();
        this.isShortLayout = isShortLayout;
        if (isShortLayout) {
            MAX_BRANDS = brandItems.size() < MAX_BRANDS ? brandItems.size() : MAX_BRANDS;
            for (int i = 0; i < MAX_BRANDS; i++) {
                this.brandItems.add(brandItems.get(i));
            }
        }
    }

    public void updateAdapter(List<BrandViewModel> brands) {
        this.brandItems = brands;
        notifyDataSetChanged();
    }


    public class BrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView imageViewBrandItem;
        private int index;

        public BrandViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewBrandItem = itemView.findViewById(R.id.imageViewBrandItem);
        }

        public void bindData(final BrandViewModel brandViewModel, int position) {
            ImageHandler.loadImage(context, imageViewBrandItem, brandViewModel.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);

            Log.d("Date Title 1234 ", " " + brandViewModel.getTitle());

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
            Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
            detailsIntent.putExtra(DealsBrandPresenter.BRAND_DATA, brandItems.get(getIndex()));
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

    @Override
    public int getItemCount() {
        if (brandItems != null) {
            return brandItems.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        return isShortLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM
                : (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM2;
    }

    private boolean isLastPosition(int position) {
        return (position == brandItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new BrandViewModel());
        }
    }

    public void add(BrandViewModel item) {
        brandItems.add(item);
        notifyItemInserted(brandItems.size() - 1);
    }

    public void addAll(List<BrandViewModel> items) {
        for (BrandViewModel item : items) {
            add(item);
        }
    }


    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = brandItems.size() - 1;
            BrandViewModel item = brandItems.get(position);

            if (item != null) {
                brandItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.item_category_brand, parent, false);
                holder = new DealsBrandAdapter.BrandViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new DealsBrandAdapter.FooterViewHolder(v);
                break;
            case ITEM2:
                v = inflater.inflate(R.layout.item_brand_big, parent, false);
                holder = new DealsBrandAdapter.BrandViewHolder(v);
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((DealsBrandAdapter.BrandViewHolder) holder).bindData(brandItems.get(position), position);
                ((DealsBrandAdapter.BrandViewHolder) holder).setIndex(position);
                break;
            case FOOTER:
                break;
            case ITEM2:
                ((DealsBrandAdapter.BrandViewHolder) holder).bindData(brandItems.get(position), position);
                ((DealsBrandAdapter.BrandViewHolder) holder).setIndex(position);
                break;
            default:
                break;
        }

    }
}
