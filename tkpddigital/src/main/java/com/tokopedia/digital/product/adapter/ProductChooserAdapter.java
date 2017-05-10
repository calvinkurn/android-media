package com.tokopedia.digital.product.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if (viewType == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            return new ItemDescAndPriceHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else if(viewType == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            return new ItemPriceAdmin(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else if(viewType == TYPE_HOLDER_PRODUCT_PROMO) {
            return new ItemHolderPromoProduct(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        final Product product = productList.get(position);
        if (type == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            ItemDescAndPriceHolder itemDescAndPriceHolder = (ItemDescAndPriceHolder) holder;
            itemDescAndPriceHolder.tvDesc.setText(product.getDesc());
            itemDescAndPriceHolder.tvPrice.setText(product.getPrice());
        } else if (type == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            ItemPriceAdmin itemPriceAdmin = (ItemPriceAdmin) holder;
            itemPriceAdmin.tvProductPrice.setText(product.getPricePlain());
            itemPriceAdmin.tvProductDescription.setText(product.getDesc());
            itemPriceAdmin.tvProductTotalPrice.setText(product.getPrice());
        } else if (type == TYPE_HOLDER_PRODUCT_PROMO) {
            ItemHolderPromoProduct itemHolderPromoProduct = (ItemHolderPromoProduct) holder;
            itemHolderPromoProduct.tvProductPromoTitle.setText(product.getDesc());
            itemHolderPromoProduct.tvProductPromoDescription.setText(product.getPromo()
                    .getBonusText());
            itemHolderPromoProduct.tvPromoProductPrice.setText(product.getPrice());
            itemHolderPromoProduct.tvProductPromoOldPrice.setText(product.getPromo().getNewPrice());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onProductItemSelected(product);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ItemDescAndPriceHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_desc)
        TextView tvDesc;
        @BindView(R2.id.tv_price)
        TextView tvPrice;

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

        ItemHolderPromoProduct(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onProductItemSelected(Product product);
    }
}
