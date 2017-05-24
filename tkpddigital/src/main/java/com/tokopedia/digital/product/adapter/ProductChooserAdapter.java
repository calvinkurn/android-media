package com.tokopedia.digital.product.adapter;

import android.app.Fragment;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.Product;

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
    private String productStyleView;
    private ActionListener actionListener;
    private View mainAdapterView;

    public ProductChooserAdapter(Fragment hostFragment,
                                 List<Product> productList,
                                 String productStyleView,
                                 ActionListener actionListener) {
        this.hostFragment = hostFragment;
        this.productList = productList;
        this.productStyleView = productStyleView;
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mainAdapterView = LayoutInflater.from(
                hostFragment.getActivity()).inflate(viewType, parent, false);
        if (viewType == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            return new ItemDescAndPriceHolder(mainAdapterView);
        } else if (viewType == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            return new ItemPriceAdmin(mainAdapterView);
        } else if (viewType == TYPE_HOLDER_PRODUCT_PROMO) {
            return new ItemHolderPromoProduct(mainAdapterView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        final Product product = productList.get(position);
        TextView emptyStockLabel = null;
        if (type == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            setViewPriceDescription((ItemDescAndPriceHolder) holder, product);
            emptyStockLabel = ((ItemDescAndPriceHolder) holder).emptyStockNotification;
        } else if (type == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            setViewPriceAdditionalFee((ItemPriceAdmin) holder, product);
            emptyStockLabel = ((ItemPriceAdmin) holder).emptyStockNotification;
        } else if (type == TYPE_HOLDER_PRODUCT_PROMO) {
            setViewPromo((ItemHolderPromoProduct) holder, product);
            emptyStockLabel = ((ItemHolderPromoProduct) holder).emptyStockNotification;
        }
        setProductAvailability(holder, product, emptyStockLabel);
    }

    private void setViewPriceDescription(ItemDescAndPriceHolder holder, Product product) {
        holder.tvTitlePrice.setText(product.getDesc());
        holder.tvPrice.setText(product.getPrice());
    }

    private void setViewPriceAdditionalFee(ItemPriceAdmin holder, Product product) {
        holder.tvProductPrice.setText(product.getDesc());

        CharSequence sequence = MethodChecker.fromHtml(product.getDetail());
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            int start = strBuilder.getSpanStart(span);
            int end = strBuilder.getSpanEnd(span);
            int flags = strBuilder.getSpanFlags(span);
            ClickableSpan clickable = new ClickableSpan() {
                public void onClick(View view) {
                    actionListener.onProductLinkClicked(span.getURL());
                }
            };
            strBuilder.setSpan(clickable, start, end, flags);
            strBuilder.removeSpan(span);
        }
        holder.tvProductDescription.setText(strBuilder);
        holder.tvProductDescription.setMovementMethod(LinkMovementMethod.getInstance());

        holder.tvProductTotalPrice.setText(product.getPrice());
    }

    private void setViewPromo(ItemHolderPromoProduct holder, Product product) {
        holder.tvProductPromoTitle.setText(product.getDesc());
        if (product.getPromo().getBonusText().isEmpty())
            holder.tvProductPromoDescription.setVisibility(View.GONE);
        holder.tvProductPromoDescription.setText(product.getPromo()
                .getBonusText());
        holder.tvPromoProductPrice.setText(product.getPromo().getNewPrice());
        holder.tvProductPromoOldPrice.setText(product.getPrice());
        holder.tvProductPromoOldPrice
                .setPaintFlags(holder.tvProductPromoOldPrice.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemViewType(int position) {
        if (productList.get(position).getPromo() != null)
            return TYPE_HOLDER_PRODUCT_PROMO;
        else if (!productList.get(position).getDetail().isEmpty())
            return TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC;
        else return TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ItemDescAndPriceHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.title_price)
        TextView tvTitlePrice;
        @BindView(R2.id.tv_price)
        TextView tvPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        ItemDescAndPriceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemPriceAdmin extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_price_no_addition)
        TextView tvProductPrice;
        @BindView(R2.id.product_plain_description)
        TextView tvProductDescription;
        @BindView(R2.id.product_total_price)
        TextView tvProductTotalPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        ItemPriceAdmin(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemHolderPromoProduct extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_promo_title)
        TextView tvProductPromoTitle;
        @BindView(R2.id.product_promo_description)
        TextView tvProductPromoDescription;
        @BindView(R2.id.product_promo_price)
        TextView tvPromoProductPrice;
        @BindView(R2.id.product_promo_old_price)
        TextView tvProductPromoOldPrice;
        @BindView(R2.id.empty_stock_notification)
        TextView emptyStockNotification;

        ItemHolderPromoProduct(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onProductItemSelected(Product product);

        void onProductLinkClicked(String url);
    }

    private void setProductAvailability(final RecyclerView.ViewHolder holder,
                                        final Product product,
                                        final TextView emptyStockLabel) {
        if (product.getStatus() == 3) {
            for (int i = 0; i < ((ViewGroup) mainAdapterView).getChildCount(); i++) {
                View adapterElement = ((ViewGroup) mainAdapterView).getChildAt(i);
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
            emptyStockLabel.setVisibility(View.VISIBLE);
            emptyStockLabel.setTextColor(hostFragment
                    .getResources().getColor(R.color.white));
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onProductItemSelected(product);
                }
            });
            emptyStockLabel.setVisibility(View.GONE);
        }
    }

    private void disableTextView(TextView textViewToDisable) {
        textViewToDisable.setTextColor(hostFragment.getResources().getColor(R.color.grey));
    }
}
