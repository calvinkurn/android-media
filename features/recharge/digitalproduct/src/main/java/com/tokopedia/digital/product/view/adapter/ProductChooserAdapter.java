package com.tokopedia.digital.product.view.adapter;

import android.graphics.Paint;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/9/17.
 */
public class ProductChooserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM =
            R.layout.view_holder_digital_item_product_desc_and_price;
    private static final int TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC =
            R.layout.view_holder_digital_price_plus_admin_and_desc;
    private static final int TYPE_HOLDER_PRODUCT_PROMO =
            R.layout.view_holder_digital_product_promo;

    private Fragment hostFragment;
    private List<Product> productList;
    private ActionListener actionListener;

    public interface ActionListener {
        void onProductItemSelected(Product product);
    }

    public ProductChooserAdapter(Fragment hostFragment,
                                 List<Product> productList,
                                 ActionListener actionListener) {
        this.hostFragment = hostFragment;
        this.productList = productList != null ? productList : new ArrayList<Product>();
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                hostFragment.getActivity()).inflate(viewType, parent, false);
        if (viewType == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            return new ItemDescAndPriceHolder(view);
        } else if (viewType == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            return new ItemPriceAdmin(view);
        } else if (viewType == TYPE_HOLDER_PRODUCT_PROMO) {
            return new ItemHolderPromoProduct(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        final Product product = productList.get(position);
        if (type == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            ((ItemDescAndPriceHolder) holder).bind(product);
        } else if (type == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            ((ItemPriceAdmin) holder).bind(product);
        } else if (type == TYPE_HOLDER_PRODUCT_PROMO) {
            ((ItemHolderPromoProduct) holder).bind(product);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (productList.get(position).getPromo() != null)
            return TYPE_HOLDER_PRODUCT_PROMO;
        else if (!TextUtils.isEmpty(productList.get(position).getDetailCompact()))
            return TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC;
        else
            return TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ItemDescAndPriceHolder extends RecyclerView.ViewHolder {
        TextView tvTitlePrice;
        TextView tvPrice;
        TextView emptyStockNotification;

        private Product product;

        ItemDescAndPriceHolder(View itemView) {
            super(itemView);

            tvTitlePrice = itemView.findViewById(R.id.title_price);
            tvPrice = itemView.findViewById(R.id.tv_price);
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification);

            itemView.setOnClickListener(v -> {
                if (product.getStatus() != Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                    actionListener.onProductItemSelected(product);
                }
            });
        }

        public void bind(Product product) {
            this.product = product;
            setViewPriceDescription(product);
            setProductAvailability(product);
        }

        private void setViewPriceDescription(Product product) {
            if (TextUtils.isEmpty(product.getDesc())) {
                tvTitlePrice.setVisibility(View.GONE);
            } else {
                tvTitlePrice.setVisibility(View.VISIBLE);
                tvTitlePrice.setText(product.getDesc());
            }
            if (TextUtils.isEmpty(product.getPrice())) {
                tvPrice.setVisibility(View.GONE);
            } else {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(product.getPrice());
            }
        }

        private void setProductAvailability(final Product product) {
            if (product.getStatus() == Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvTitlePrice.setTextColor(hostFragment.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvPrice.setTextColor(hostFragment.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }
    }

    class ItemPriceAdmin extends RecyclerView.ViewHolder {
        TextView tvProductPrice;
        TextView tvProductDescription;
        TextView tvProductTotalPrice;
        TextView emptyStockNotification;

        private Product product;

        ItemPriceAdmin(View itemView) {
            super(itemView);

            tvProductPrice = itemView.findViewById(R.id.product_price_no_addition);
            tvProductDescription = itemView.findViewById(R.id.product_plain_description);
            tvProductTotalPrice = itemView.findViewById(R.id.product_total_price);
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.getStatus() != Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                        actionListener.onProductItemSelected(product);
                    }
                }
            });
        }

        public void bind(Product product) {
            this.product = product;
            setViewPriceAdditionalFee(product);
            setProductAvailability(product);
        }

        private void setViewPriceAdditionalFee(Product product) {
            if (TextUtils.isEmpty(product.getDesc())) {
                tvProductPrice.setVisibility(View.GONE);
            } else {
                tvProductPrice.setVisibility(View.VISIBLE);
                tvProductPrice.setText(product.getDesc());
            }

            if (TextUtils.isEmpty(product.getPrice())) {
                tvProductTotalPrice.setVisibility(View.GONE);
            } else {
                tvProductTotalPrice.setVisibility(View.VISIBLE);
                tvProductTotalPrice.setText(product.getPrice());
            }

            if (TextUtils.isEmpty(product.getDetailCompact())) {
                tvProductDescription.setVisibility(View.GONE);
            } else {
                tvProductDescription.setVisibility(View.VISIBLE);
                tvProductDescription.setText(MethodChecker.fromHtml(product.getDetailCompact()));
            }
        }

        private void setProductAvailability(final Product product) {
            if (product.getStatus() == Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvProductPrice.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvProductDescription.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200));
            tvProductTotalPrice.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }
    }

    class ItemHolderPromoProduct extends RecyclerView.ViewHolder {
        private TextView tvProductPromoTitle;
        private TextView tvProductPromoTag;
        private TextView tvProductPromoDescription;
        private TextView tvPromoProductPrice;
        private TextView tvProductPromoOldPrice;
        private TextView emptyStockNotification;

        private Product product;

        ItemHolderPromoProduct(View itemView) {
            super(itemView);

            tvProductPromoTitle = itemView.findViewById(R.id.product_promo_title);
            tvProductPromoTag = itemView.findViewById(R.id.product_promo_tag);
            tvProductPromoDescription = itemView.findViewById(R.id.product_promo_description);
            tvPromoProductPrice = itemView.findViewById(R.id.product_promo_price);
            tvProductPromoOldPrice = itemView.findViewById(R.id.product_promo_old_price);
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification);

            itemView.setOnClickListener(v -> {
                if (product.getStatus() != Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                    actionListener.onProductItemSelected(product);
                }
            });
        }

        void bind(Product product) {
            this.product = product;
            setViewPromo(product);
            setProductAvailability(product);
        }

        private void setViewPromo(Product product) {
            tvProductPromoTitle.setText(product.getDesc());
            if (TextUtils.isEmpty(product.getDetailCompact())) {
                tvProductPromoDescription.setVisibility(View.GONE);
            } else {
                tvProductPromoDescription.setVisibility(View.VISIBLE);
                tvProductPromoDescription.setText(MethodChecker.fromHtml(product.getDetailCompact()));
            }
            if (TextUtils.isEmpty(product.getPromo().getTag())) {
                tvProductPromoTag.setVisibility(View.GONE);
            } else {
                tvProductPromoTag.setVisibility(View.VISIBLE);
                tvProductPromoTag.setText(product.getPromo().getTag());
            }
            tvPromoProductPrice.setText(product.getPromo().getNewPrice());
            tvProductPromoOldPrice.setText(product.getPrice());
            tvProductPromoOldPrice
                    .setPaintFlags(tvProductPromoOldPrice.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        private void setProductAvailability(final Product product) {
            if (product.getStatus() == Product.Companion.getSTATUS_OUT_OF_STOCK()) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvProductPromoTitle.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvProductPromoTag.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500));
            tvProductPromoDescription.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200));
            tvProductPromoOldPrice.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvPromoProductPrice.setTextColor(hostFragment.getResources()
                    .getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500));
        }
    }

    private void disableView(View itemView) {
        for (int i = 0; i < ((ViewGroup) itemView).getChildCount(); i++) {
            View adapterElement = ((ViewGroup) itemView).getChildAt(i);
            adapterElement.setEnabled(false);
            if (adapterElement instanceof TextView) {
                disableTextView((TextView) adapterElement);
            } else if (adapterElement instanceof ViewGroup) {
                for (int j = 0; j < ((ViewGroup) adapterElement).getChildCount(); j++) {
                    if (((ViewGroup) adapterElement).getChildAt(j) instanceof TextView) {
                        disableTextView(((TextView) ((ViewGroup) adapterElement)
                                .getChildAt(j)));
                    }
                }
            }
        }
    }

    private void disableTextView(TextView textViewToDisable) {
        textViewToDisable.setTextColor(hostFragment.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N150));
    }

}
