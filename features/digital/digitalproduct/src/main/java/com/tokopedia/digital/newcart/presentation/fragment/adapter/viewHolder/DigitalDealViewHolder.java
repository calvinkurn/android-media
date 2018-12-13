package com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;

public class DigitalDealViewHolder extends AbstractViewHolder<DealProductViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_digital_deal;

    private AppCompatImageView dealImageView;
    private AppCompatTextView brandTextView;
    private AppCompatTextView titleTextView;
    private AppCompatTextView slashedPriceTextView;
    private AppCompatTextView priceTextView;
    private AppCompatImageView closeImageView;
    private AppCompatImageView checkImageView;
    private AppCompatButton buyButton;
    private LinearLayout infoContainer;

    private DigitalDealActionListener actionListener;
    private boolean insideCheckoutPage;
    private DealProductViewModel element;

    public DigitalDealViewHolder(View itemView, DigitalDealActionListener actionListener, boolean insideCheckoutPage) {
        super(itemView);
        this.actionListener = actionListener;
        this.insideCheckoutPage = insideCheckoutPage;
        setupView(itemView);
    }

    private void setupView(View view) {
        infoContainer = (LinearLayout) view.findViewById(R.id.info_container);
        dealImageView = (AppCompatImageView) view.findViewById(R.id.iv_image);
        brandTextView = (AppCompatTextView) view.findViewById(R.id.tv_brand_name);
        titleTextView = (AppCompatTextView) view.findViewById(R.id.tv_title);
        slashedPriceTextView = (AppCompatTextView) view.findViewById(R.id.tv_slashed_price);
        priceTextView = (AppCompatTextView) view.findViewById(R.id.tv_price);
        closeImageView = (AppCompatImageView) view.findViewById(R.id.iv_close);
        checkImageView = (AppCompatImageView) view.findViewById(R.id.iv_check);
        buyButton = (AppCompatButton) view.findViewById(R.id.btn_buy);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.actionBuyButton(element);
                }
            }
        });


        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.actionCloseButon(element);
                }
            }
        });
        dealImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.actionDetail(element);
                }
            }
        });
        infoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.actionDetail(element);
                }
            }
        });
    }

    @Override
    public void bind(DealProductViewModel element) {
        this.element = element;
        ImageHandler.loadImageRounded(itemView.getContext(), dealImageView, element.getImageUrl(), 5.0f);
        brandTextView.setText(element.getBrandName());
        titleTextView.setText(element.getTitle());

        renderPriceLabel(element);

        renderCloseButton();

        if (element.isSelected() || insideCheckoutPage) {
            if (element.isSelected() && !insideCheckoutPage) {
                checkImageView.setVisibility(View.VISIBLE);
            } else {
                checkImageView.setVisibility(View.GONE);
            }
            buyButton.setVisibility(View.GONE);
        } else {
            checkImageView.setVisibility(View.GONE);
            buyButton.setVisibility(View.VISIBLE);
        }
    }

    private void renderPriceLabel(DealProductViewModel element) {
        if (element.getSalesPriceNumeric() > 0) {
            priceTextView.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace((int) element.getSalesPriceNumeric()));
            if (element.getBeforePrice() > 0) {
                long slashedPrice = element.getBeforePrice();
                slashedPriceTextView.setText(
                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(
                                (int) slashedPrice
                        )
                );
                slashedPriceTextView.setVisibility(View.VISIBLE);
            } else {
                slashedPriceTextView.setVisibility(View.GONE);
            }
        } else {
            priceTextView.setText(getString(R.string.digital_cart_deal_free_label));
            slashedPriceTextView.setVisibility(View.GONE);
        }
    }

    private void renderCloseButton() {
        if (insideCheckoutPage) {
            closeImageView.setVisibility(View.VISIBLE);
        } else {
            closeImageView.setVisibility(View.GONE);
        }
    }
}
