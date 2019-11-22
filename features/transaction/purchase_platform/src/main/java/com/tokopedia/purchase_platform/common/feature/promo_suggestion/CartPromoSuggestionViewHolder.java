package com.tokopedia.purchase_platform.common.feature.promo_suggestion;

import android.graphics.Color;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartPromoSuggestionViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO_SUGGESTION = R.layout.holder_item_cart_potential_promo;
    private final PromoActionListener actionListener;

    private RelativeLayout mRlPromoSuggestionLayout;
    private ImageView btnClose;
    private TextView tvDesc;
    private TextView tvAction;
    private RecyclerView.LayoutParams layoutParamsVisible;
    private RecyclerView.LayoutParams layoutParamsGone;
    private CartPromoSuggestionHolderData cartPromoSuggestionHolderData;

    public CartPromoSuggestionViewHolder(View itemView, PromoActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        mRlPromoSuggestionLayout = itemView.findViewById(R.id.rl_promo_suggestion_layout);
        this.btnClose = itemView.findViewById(R.id.btn_close);
        this.tvAction = itemView.findViewById(R.id.tv_action);
        this.tvDesc = itemView.findViewById(R.id.tv_desc);

        setupLayoutParams();
    }

    private void setupLayoutParams() {
        layoutParamsVisible = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsGone = new RecyclerView.LayoutParams(0, 0);
    }

    public void bindData(CartPromoSuggestionHolderData data, int position) {
        cartPromoSuggestionHolderData = data;
        if (data.isVisible()) {
            mRlPromoSuggestionLayout.setVisibility(View.VISIBLE);
            mRlPromoSuggestionLayout.setLayoutParams(layoutParamsVisible);

            tvDesc.setText(fromHtml(data.getText()));
            tvAction.setText(data.getCta());
            tvAction.setTextColor(Color.parseColor(data.getCtaColor()));

            tvAction.setOnClickListener(actionClickListener(data, position));
            btnClose.setOnClickListener(closeClickListener(data, position));
        } else {
            mRlPromoSuggestionLayout.setVisibility(View.GONE);
            mRlPromoSuggestionLayout.setLayoutParams(layoutParamsGone);
        }
    }

    public CartPromoSuggestionHolderData getCartPromoSuggestionHolderData() {
        return cartPromoSuggestionHolderData;
    }

    private View.OnClickListener actionClickListener(final CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                                     final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener closeClickListener(final CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                                    final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestionHolderData, position);
            }
        };
    }

    private Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(source);
        }
    }
}
