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
import com.tokopedia.digital_deals.view.activity.BrandOutletDetailsActivity;
import com.tokopedia.digital_deals.view.presenter.DealsBrandPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;

import java.util.ArrayList;
import java.util.List;

public class DealsBrandAdapter extends RecyclerView.Adapter<DealsBrandAdapter.ViewHolder> {

    private List<BrandViewModel> brandItems;
    private Context context;

    public DealsBrandAdapter(Context context, List<BrandViewModel> brandItems) {
        this.context = context;
        this.brandItems = new ArrayList<>();
        this.brandItems=brandItems;
        Log.d("List Size 1234 ", " "+brandItems.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView imageViewBrandItem;
        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewBrandItem = itemView.findViewById(R.id.imageViewBrandItem);
        }

        public void bindData(final BrandViewModel brandViewModel, int position) {
            ImageHandler.loadImageCover2(imageViewBrandItem, brandViewModel.getFeaturedThumbnailImage());
            Log.d("Date Title 1234 ", " "+brandViewModel.getTitle());

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

    @Override
    public int getItemCount() {
        if (brandItems != null) {
            return brandItems.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_brand, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(brandItems.get(position), position);
    }

}
