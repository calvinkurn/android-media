package com.tokopedia.loyalty.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.List;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.CouponListViewHolder> {

    private List<CouponData> listCouponModel;

    private CouponListAdapterListener listener;

    public CouponListAdapter(List<CouponData> listCouponModel, CouponListAdapterListener listener) {
        this.listCouponModel = listCouponModel;
        this.listener = listener;
    }

    @Override
    public CouponListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon_list_adapter, parent, false);
        return new CouponListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CouponListViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.promoIcon, listCouponModel.get(position).getIcon());
        holder.promoTitle.setText(listCouponModel.get(position).getTitle());
        holder.promoBenefit.setText(listCouponModel.get(position).getSubTitle());
        holder.promoDescription.setText(listCouponModel.get(position).getDescription());
        holder.promoExpiry.setText(listCouponModel.get(position).getExpired());
        if (listCouponModel.get(position).getErrorMessage() != null
                && !listCouponModel.get(position).getErrorMessage().isEmpty()) {
            holder.promoError.setText(listCouponModel.get(position).getErrorMessage());
            holder.promoError.setVisibility(View.VISIBLE);
        } else holder.promoError.setVisibility(View.GONE);
        holder.mainView.setOnClickListener(
                onMainViewClickedListener(listCouponModel.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return listCouponModel.size();
    }

    class CouponListViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup mainView;

        private ImageView promoIcon;

        private TextView promoTitle;

        private TextView promoBenefit;

        private TextView promoDescription;

        private TextView promoExpiry;

        private TextView promoError;

        CouponListViewHolder(View itemView) {
            super(itemView);
            mainView = itemView.findViewById(R.id.item_main_view);
            promoIcon = itemView.findViewById(R.id.promo_icon);
            promoTitle = itemView.findViewById(R.id.promo_title);
            promoBenefit = itemView.findViewById(R.id.promo_benefit);
            promoDescription = itemView.findViewById(R.id.promo_description);
            promoExpiry = itemView.findViewById(R.id.promo_expiry_time);
            promoError = itemView.findViewById(R.id.promo_error_message);

        }
    }

    private View.OnClickListener onMainViewClickedListener(final CouponData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVoucherChosen(data);
            }
        };
    }

    public void clearError() {
        for (int i = 0; i < listCouponModel.size(); i++) {
            listCouponModel.get(i).setErrorMessage("");
        }
    }

    public interface CouponListAdapterListener {

        void onVoucherChosen(CouponData data);

    }

}
