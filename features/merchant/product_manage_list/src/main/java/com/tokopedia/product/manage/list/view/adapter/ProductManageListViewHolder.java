package com.tokopedia.product.manage.list.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.base.list.seller.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.common.util.FreeReturnTypeDef;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.constant.ProductManagePreOrderDef;
import com.tokopedia.product.manage.list.constant.ProductManageStockDef;
import com.tokopedia.product.manage.list.constant.ProductManageWholesaleDef;
import com.tokopedia.product.manage.list.constant.StatusProductOption;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.common.utils.CurrencyUtils;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListViewHolder extends BaseMultipleCheckViewHolder<ProductManageViewModel> {

    public interface ClickOptionCallbackHolder {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ImageView productImageView;
    private TextView titleTextView;
    private TextView stockTextView;
    private TextView priceTextView;
    private ImageView featuredImageView;
    private TextView cashbackTextView;
    private TextView wholesaleTextView;
    private TextView preOrderTextView;
    private ImageView freeReturnImageView;
    private View optionImageButton;
    private CheckBox checkBoxProduct;
    private TextView tagEmptyStock;
    private View viewSuperVision;
    private TextView textViewVariant;
    private ClickOptionCallbackHolder clickOptionCallbackHolder;

    public ProductManageListViewHolder(View layoutView) {
        super(layoutView);
        productImageView = (ImageView) layoutView.findViewById(R.id.image_view_product);
        titleTextView = (TextView) layoutView.findViewById(R.id.text_view_title);
        stockTextView = (TextView) layoutView.findViewById(R.id.text_view_stock);
        priceTextView = (TextView) layoutView.findViewById(R.id.text_view_price);

        featuredImageView = (ImageView) layoutView.findViewById(R.id.image_view_featured);
        cashbackTextView = (TextView) layoutView.findViewById(R.id.text_view_cashback);
        wholesaleTextView = (TextView) layoutView.findViewById(R.id.text_view_wholesale);
        preOrderTextView = (TextView) layoutView.findViewById(R.id.text_view_pre_order);
        freeReturnImageView = (ImageView) layoutView.findViewById(R.id.image_view_free_return);
        checkBoxProduct = (CheckBox) layoutView.findViewById(R.id.check_box_product);
        tagEmptyStock = (TextView) layoutView.findViewById(R.id.tag_empty_product);
        optionImageButton = layoutView.findViewById(R.id.image_button_option);
        viewSuperVision = layoutView.findViewById(R.id.view_product_on_supervision);
        textViewVariant = layoutView.findViewById(R.id.text_view_variant);
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel, boolean checked) {
        bindObject(productManageViewModel);
        setChecked(checked);
        checkBoxProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productManageViewModel, checkBoxProduct.isChecked());
                }
                setChecked(checkBoxProduct.isChecked());
            }
        });
    }

    @Override
    public boolean isChecked() {
        return checkBoxProduct.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBoxProduct.setChecked(checked);
        setBackground(checked);
    }

    public void setBackground(boolean isChecked) {
        if (isChecked) {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
        }
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel) {
        ImageHandler.loadImageRounded2(
                productImageView.getContext(),
                productImageView,
                productManageViewModel.getImageUrl()
        );
        boolean statusUnderSupervision = productManageViewModel.getProductStatus().equals(StatusProductOption.UNDER_SUPERVISION);
        boolean statusStockEmpty = productManageViewModel.getProductStatus().equals(StatusProductOption.EMPTY);
        titleTextView.setText(MethodChecker.fromHtml(productManageViewModel.getProductName()));
        priceTextView.setText(priceTextView.getContext().getString(
                R.string.pml_price_format_text, productManageViewModel.getProductCurrencySymbol(),
                CurrencyUtils.getPriceFormatted(productManageViewModel.getProductCurrencyId(), productManageViewModel.getProductPricePlain())));
        if (productManageViewModel.getProductCashback() > 0) {
            cashbackTextView.setText(cashbackTextView.getContext().getString(
                    R.string.product_manage_item_cashback, productManageViewModel.getProductCashback()));
            cashbackTextView.setVisibility(View.VISIBLE);
        } else {
            cashbackTextView.setVisibility(View.GONE);
        }
        tagEmptyStock.setVisibility(statusStockEmpty ? View.VISIBLE : View.GONE);
        viewSuperVision.setVisibility(statusUnderSupervision ? View.VISIBLE : View.GONE);
        preOrderTextView.setVisibility(productManageViewModel.getProductPreorder() == ProductManagePreOrderDef.PRE_ORDER ? View.VISIBLE : View.GONE);
        freeReturnImageView.setVisibility(productManageViewModel.getProductReturnable() == FreeReturnTypeDef.TYPE_ACTIVE ? View.VISIBLE : View.GONE);
        optionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickOptionCallbackHolder != null) {
                    clickOptionCallbackHolder.onClickOptionItem(productManageViewModel);
                }
            }
        });
        wholesaleTextView.setVisibility(productManageViewModel.getProductWholesale() == ProductManageWholesaleDef.WHOLESALE ? View.VISIBLE : View.GONE);
        if (!statusStockEmpty && productManageViewModel.getProductUsingStock() == ProductManageStockDef.USING_STOCK) {
            stockTextView.setVisibility(View.VISIBLE);
            if (productManageViewModel.isProductVariant()) {
                stockTextView.setText(itemView.getContext().getString(R.string.pml_product_variant_stock_limited));
            } else {
                stockTextView.setText(itemView.getContext().getString(R.string.product_manage_label_stock_counter, productManageViewModel.getProductStock()));
            }
        } else {
            stockTextView.setVisibility(View.GONE);
        }
        boolean isProductVariant = productManageViewModel.isProductVariant();
        if (isProductVariant) {
            textViewVariant.setVisibility(View.VISIBLE);
        } else {
            textViewVariant.setVisibility(View.GONE);
        }
        if (statusUnderSupervision) {
            checkBoxProduct.setEnabled(false);
        } else {
            checkBoxProduct.setEnabled(true);
        }
    }

    public void bindFeaturedProduct(boolean isFeaturedProduct) {
        if (isFeaturedProduct) {
            featuredImageView.setVisibility(View.VISIBLE);
        } else {
            featuredImageView.setVisibility(View.GONE);
        }
    }

    public void bindActionMode(boolean isActionMode) {
        if (isActionMode) {
            checkBoxProduct.setVisibility(View.VISIBLE);
            optionImageButton.setVisibility(View.GONE);
        } else {
            checkBoxProduct.setVisibility(View.GONE);
            optionImageButton.setVisibility(View.VISIBLE);
        }
    }

    public void setClickOptionCallbackHolder(ClickOptionCallbackHolder clickOptionCallbackHolder) {
        this.clickOptionCallbackHolder = clickOptionCallbackHolder;
    }
}
