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
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.ViewHolder> {

    private List<CouponValueEntity> mItems;
    private CatalogPurchaseRedemptionPresenter mPresenter;
    private boolean mIsLimitEnable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, label, value, btnContinue;
        ImageView imgBanner, imgLabel;

        public ViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.text_description);
            label = view.findViewById(R.id.text_label);
            value = view.findViewById(R.id.text_time_value);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_time);
        }
    }

    public CouponListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CouponValueEntity> items) {
        this.mItems = items;
        this.mPresenter = presenter;
    }

    public CouponListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CouponValueEntity> items, boolean isLimitEnable) {
        this.mItems = items;
        this.mPresenter = presenter;
        this.mIsLimitEnable = isLimitEnable;
    }

    @Override
    public CouponListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon, parent, false);

        return new CouponListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CouponListAdapter.ViewHolder holder, int position) {
        final CouponValueEntity item = mItems.get(position);
        holder.description.setText(item.getTitle());
        ImageHandler.loadImageFit2(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());

        if (item.getUsage() != null) {
            holder.imgLabel.setImageResource(R.drawable.ic_tp_time);
            holder.label.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.imgLabel.setVisibility(View.VISIBLE);
            holder.value.setText(item.getUsage().getUsageStr());
            if (item.getUsage().getBtnUsage() != null) {
                holder.btnContinue.setText(item.getUsage().getBtnUsage().getText());
            }
        }

        holder.btnContinue.setOnClickListener(v -> mPresenter.showRedeemCouponDialog(item.getCta(), item.getCode(), item.getTitle()));
        holder.imgBanner.setOnClickListener(v -> mPresenter.navigateToWebView(CommonConstant.WebLink.SEE_COUPON + mItems.get(position).getCode()));
    }

    @Override
    public int getItemCount() {
        if (mIsLimitEnable) {
            return mItems.size() > CommonConstant.HOMEPAGE_PAGE_SIZE ? CommonConstant.HOMEPAGE_PAGE_SIZE : mItems.size();
        } else {
            return mItems.size();
        }
    }
}
