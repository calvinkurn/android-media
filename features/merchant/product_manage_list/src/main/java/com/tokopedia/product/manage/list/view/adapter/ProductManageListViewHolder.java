package com.tokopedia.product.manage.list.view.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
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

public class ProductManageListViewHolder extends BaseCheckableViewHolder<ProductManageViewModel> implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {

    @LayoutRes
    public static int LAYOUT = R.layout.item_manage_product_list;

    public interface ProductManageViewHolderListener {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);

        void onProductClicked(ProductManageViewModel productManageViewModel);
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
    private ProductManageViewHolderListener viewListener;

    public ProductManageListViewHolder(View layoutView, BaseCheckableViewHolder.CheckableInteractionListener listener, ProductManageViewHolderListener productManageViewHolderListener) {
        super(layoutView, listener);
        viewListener = productManageViewHolderListener;
        productImageView = layoutView.findViewById(R.id.image_view_product);
        titleTextView = layoutView.findViewById(R.id.text_view_title);
        stockTextView = layoutView.findViewById(R.id.text_view_stock);
        priceTextView = layoutView.findViewById(R.id.text_view_price);

        featuredImageView = layoutView.findViewById(R.id.image_view_featured);
        cashbackTextView = layoutView.findViewById(R.id.text_view_cashback);
        wholesaleTextView = layoutView.findViewById(R.id.text_view_wholesale);
        preOrderTextView = layoutView.findViewById(R.id.text_view_pre_order);
        freeReturnImageView = layoutView.findViewById(R.id.image_view_free_return);
        checkBoxProduct = layoutView.findViewById(R.id.check_box_product);
        tagEmptyStock = layoutView.findViewById(R.id.tag_empty_product);
        optionImageButton = layoutView.findViewById(R.id.image_button_option);
        viewSuperVision = layoutView.findViewById(R.id.view_product_on_supervision);
        textViewVariant = layoutView.findViewById(R.id.text_view_variant);
    }

    @Override
    public CompoundButton getCheckable() {
        return checkBoxProduct;
    }

    @Override
    public void bind(ProductManageViewModel productManageViewModel) {
        super.bind(productManageViewModel);
        ImageHandler.loadImageRounded2(
                productImageView.getContext(),
                productImageView,
                productManageViewModel.getImageUrl()
        );
        boolean statusUnderSupervision = productManageViewModel.getProductStatus().equals(StatusProductOption.UNDER_SUPERVISION);
        boolean statusStockEmpty = productManageViewModel.getProductStatus().equals(StatusProductOption.EMPTY);

        optionImageButton.setOnClickListener(v -> {
            if (viewListener != null) {
                viewListener.onClickOptionItem(productManageViewModel);
            }
        });

        itemView.setOnClickListener(v -> {
            if (viewListener != null) {
                viewListener.onProductClicked(productManageViewModel);
            }
        });

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

        if (productManageViewModel.getIsFeatureProduct()) {
            featuredImageView.setVisibility(View.VISIBLE);
        } else {
            featuredImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        toggle();
    }
}
