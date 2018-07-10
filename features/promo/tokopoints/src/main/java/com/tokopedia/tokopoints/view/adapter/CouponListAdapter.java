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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, label, value, btnContinue;
        ImageView imgBanner, imgLabel;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.text_description);
            label = view.findViewById(R.id.text_label);
            value = view.findViewById(R.id.text_value);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_label);
        }
    }

    public CouponListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CouponValueEntity> items) {
        this.mItems = items;
        this.mPresenter = presenter;
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
        holder.title.setText(item.getTitle());

        if (item.getUsage() != null) {
            holder.imgLabel.setImageResource(R.drawable.ic_tp_time);
            holder.label.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.value.setText(item.getUsage().getUsageStr());
            if (item.getUsage().getBtnUsage() != null) {
                holder.btnContinue.setText(item.getUsage().getBtnUsage().getText());
            }
        }

        ImageHandler.loadImageFit2(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());

        holder.btnContinue.setOnClickListener(v -> mPresenter.showRedeemCouponDialog(item.getCta(), item.getCode(), item.getTitle()));

        holder.imgBanner.setOnClickListener(v -> mPresenter.navigateToWebView(CommonConstant.WebLink.SEE_COUPON + mItems.get(position).getCode()));
    }

    @Override
    public int getItemCount() {
        return mItems.size() > CommonConstant.HOMEPAGE_PAGE_SIZE ? CommonConstant.HOMEPAGE_PAGE_SIZE : mItems.size();
    }
}
