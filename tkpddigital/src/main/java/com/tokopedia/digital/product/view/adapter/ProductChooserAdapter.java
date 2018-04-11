package com.tokopedia.digital.product.view.adapter;

import android.app.Fragment;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/9/17.
 */
public class ProductChooserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM =
            R.layout.view_holder_item_product_desc_and_price_digital_module;
    private static final int TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC =
            R.layout.view_holder_price_plus_admin_and_desc;
    private static final int TYPE_HOLDER_PRODUCT_PROMO =
            R.layout.view_holder_product_promo;

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
        else if (!TextUtils.isEmpty(productList.get(position).getDetail()))
            return TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC;
        else return TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ItemDescAndPriceHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.title_price)
        TextView tvTitlePrice;
        @BindView(R2.id.tv_price)
        TextView tvPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        private Product product;

        ItemDescAndPriceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.getStatus() != Product.STATUS_OUT_OF_STOCK) {
                        actionListener.onProductItemSelected(product);
                    }
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
            if (product.getStatus() == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(R.color.white));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvTitlePrice.setTextColor(hostFragment.getResources().getColor(R.color.digital_item_title));
            tvPrice.setTextColor(hostFragment.getResources().getColor(R.color.digital_item_title));
        }
    }

    class ItemPriceAdmin extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_price_no_addition)
        TextView tvProductPrice;
        @BindView(R2.id.product_plain_description)
        TextView tvProductDescription;
        @BindView(R2.id.product_total_price)
        TextView tvProductTotalPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        private Product product;

        ItemPriceAdmin(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.getStatus() != Product.STATUS_OUT_OF_STOCK) {
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

            if (TextUtils.isEmpty(product.getDetail())) {
                tvProductDescription.setVisibility(View.GONE);
            } else {
                tvProductDescription.setVisibility(View.VISIBLE);
                tvProductDescription.setText(MethodChecker.fromHtml(product.getDetail()));
            }
        }

        private void setProductAvailability(final Product product) {
            if (product.getStatus() == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(R.color.white));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvProductPrice.setTextColor(hostFragment.getResources()
                    .getColor(R.color.digital_item_title));
            tvProductDescription.setTextColor(hostFragment.getResources()
                    .getColor(R.color.grey_500));
            tvProductTotalPrice.setTextColor(hostFragment.getResources()
                    .getColor(R.color.digital_item_title));
        }
    }

    class ItemHolderPromoProduct extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_promo_title)
        TextView tvProductPromoTitle;
        @BindView(R2.id.product_promo_tag)
        TextView tvProductPromoTag;
        @BindView(R2.id.product_promo_description)
        TextView tvProductPromoDescription;
        @BindView(R2.id.product_promo_price)
        TextView tvPromoProductPrice;
        @BindView(R2.id.product_promo_old_price)
        TextView tvProductPromoOldPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        private Product product;

        ItemHolderPromoProduct(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.getStatus() != Product.STATUS_OUT_OF_STOCK) {
                        actionListener.onProductItemSelected(product);
                    }
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
            if (TextUtils.isEmpty(product.getDetail())) {
                tvProductPromoDescription.setVisibility(View.GONE);
            } else {
                tvProductPromoDescription.setVisibility(View.VISIBLE);
                tvProductPromoDescription.setText(MethodChecker.fromHtml(product.getDetail()));
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
            if (product.getStatus() == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView);
                emptyStockNotification.setVisibility(View.VISIBLE);
                emptyStockNotification.setTextColor(hostFragment
                        .getResources().getColor(R.color.white));
            } else {
                enableView();
                emptyStockNotification.setVisibility(View.GONE);
            }
        }

        private void enableView() {
            tvProductPromoTitle.setTextColor(hostFragment.getResources()
                    .getColor(R.color.digital_item_title));
            tvProductPromoTag.setTextColor(hostFragment.getResources()
                    .getColor(R.color.deep_orange_500));
            tvProductPromoDescription.setTextColor(hostFragment.getResources()
                    .getColor(R.color.grey_500));
            tvProductPromoOldPrice.setTextColor(hostFragment.getResources()
                    .getColor(R.color.digital_item_title));
            tvPromoProductPrice.setTextColor(hostFragment.getResources()
                    .getColor(R.color.deep_orange_500));
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
        textViewToDisable.setTextColor(hostFragment.getResources().getColor(R.color.grey_400));
    }

}
