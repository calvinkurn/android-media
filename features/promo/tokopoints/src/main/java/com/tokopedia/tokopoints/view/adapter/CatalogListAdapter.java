package com.tokopedia.tokopoints.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class CatalogListAdapter extends RecyclerView.Adapter<CatalogListAdapter.ViewHolder> {

    private List<CatalogsValueEntity> mItems;
    private CatalogPurchaseRedemptionPresenter mPresenter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, label, value, btnContinue, pointValue;
        ImageView imgBanner, imgLabel;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.text_description);
            label = view.findViewById(R.id.text_label);
            label = view.findViewById(R.id.text_label);
            value = view.findViewById(R.id.text_value);
            pointValue = view.findViewById(R.id.text_point_value);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_label);
        }
    }
    
    public CatalogListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CatalogsValueEntity> items) {
        this.mPresenter = presenter;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatalogsValueEntity item = mItems.get(position);
        holder.title.setText(item.getTitle());
        holder.pointValue.setVisibility(View.VISIBLE);
        holder.pointValue.setText(item.getPointsStr());
        holder.btnContinue.setText(R.string.tp_label_exchange);
        ImageHandler.loadImageFit2(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());
        holder.imgLabel.setImageResource(R.drawable.ic_tp_point_stack);

        holder.btnContinue.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(item);
        });

        holder.imgBanner.setOnClickListener(v -> mPresenter.navigateToWebView(CommonConstant.WebLink.COUPON_DETAIL + mItems.get(position).getSlug()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<CatalogsValueEntity> items) {
        this.mItems = items;
    }
}
