package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter.OnAdapterActionListener;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSimpleCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SIMPLE_CODE = R.layout.item_view_promo_simple_code_layout;

    private OnAdapterActionListener adapterActionListener;

    private TextView tvPromoCodeLabel;
    private ImageView ivTooltipInfo;
    private RelativeLayout rlPromoCodeLayout;
    private TextView tvSingleCode;
    private RelativeLayout rlSingleCodeCopyLayout;
    private TextView tvPromoCodeCopy;

    public PromoDetailSimpleCodeViewHolder(View itemView, OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.adapterActionListener = adapterActionListener;

        this.tvPromoCodeLabel = itemView.findViewById(R.id.tv_promo_code_label);
        this.ivTooltipInfo = itemView.findViewById(R.id.iv_tooltip_info);
        this.rlPromoCodeLayout = itemView.findViewById(R.id.rl_promo_code_layout);
        this.tvSingleCode = itemView.findViewById(R.id.tv_single_code);
        this.rlSingleCodeCopyLayout = itemView.findViewById(R.id.rl_single_code_copy_layout);
        this.tvPromoCodeCopy = itemView.findViewById(R.id.tv_promo_code_copy);
    }

    public void bind(SingleCodeViewModel viewModel) {
        boolean withoutPromoCode = TextUtils.isEmpty(viewModel.getSingleCode());

        if (withoutPromoCode) {
            this.tvPromoCodeLabel.setText(R.string.without_promo_code);
        } else {
            this.tvPromoCodeLabel.setText(R.string.with_promo_code);
        }

        this.ivTooltipInfo.setVisibility(withoutPromoCode ? View.GONE : View.VISIBLE);
        this.ivTooltipInfo.setOnClickListener(tooltipInfoListener());
        this.rlPromoCodeLayout.setVisibility(withoutPromoCode ? View.GONE : View.VISIBLE);
        this.tvSingleCode.setText(viewModel.getSingleCode());
        this.rlSingleCodeCopyLayout.setOnClickListener(copyToClipboardListener(
                viewModel.getPromoName(), viewModel.getSingleCode()));
        this.tvPromoCodeCopy.setText(R.string.copy_code);
    }

    private View.OnClickListener tooltipInfoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeTooltipClicked();
            }
        };
    }

    private View.OnClickListener copyToClipboardListener(final String promoName,
                                                         final String singleCode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeCopyClipboardClicked(promoName, singleCode);
                tvPromoCodeCopy.setText(R.string.copied);
                rlSingleCodeCopyLayout.setBackgroundResource(R.drawable.round_button_right_grey);
            }
        };
    }
}
