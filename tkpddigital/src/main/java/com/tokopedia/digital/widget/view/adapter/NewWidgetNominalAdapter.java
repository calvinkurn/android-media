package com.tokopedia.digital.widget.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.model.Promo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rizky on 16/01/18.
 */

public class NewWidgetNominalAdapter extends ArrayAdapter<Product> {

    private final static int OUT_OF_STOCK = 3;
    private final LayoutInflater inflater;
    private List<Product> productList;
    private Boolean isShowPrice = true;

    public NewWidgetNominalAdapter(Context context, int resource, List<Product> productList, Boolean isShowPrice) {
        super(context, resource, productList);
        this.productList = productList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isShowPrice = isShowPrice;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Product product = productList.get(position);
        NewWidgetNominalAdapter.NormalViewHolder normalViewHolder;
        View rowNormal = convertView;
        if (rowNormal == null) {
            rowNormal = inflater.inflate(R.layout.view_widget_product_spinner_item, parent, false);
            normalViewHolder = new NewWidgetNominalAdapter.NormalViewHolder(rowNormal);
            rowNormal.setTag(normalViewHolder);
        } else {
            normalViewHolder = (NewWidgetNominalAdapter.NormalViewHolder) rowNormal.getTag();
        }
        renderProduct(normalViewHolder, product);
        return rowNormal;
    }

    private void renderProduct(NewWidgetNominalAdapter.NormalViewHolder holder, Product product) {
        holder.nominalDescriptionTextview.setText(product.getDesc());
        renderNominalDetail(holder, product);
        if (product.getPromo() != null) {
            renderProductPromo(holder, product);
        } else {
            renderProductWithoutPromo(holder, product);
        }
        renderEmptyStock(holder, product);
    }

    private void renderNominalDetail(NewWidgetNominalAdapter.NormalViewHolder holder, Product product) {
        String nominalDetail = product.getDetail();
        if (nominalDetail != null && nominalDetail.length() > 0) {
            holder.nominalDetailTextView.setText(MethodChecker.fromHtml(nominalDetail));
            holder.nominalDetailTextView.setVisibility(View.VISIBLE);
        } else {
            holder.nominalDetailTextView.setVisibility(View.GONE);
        }
    }

    private void renderProductWithoutPromo(NewWidgetNominalAdapter.NormalViewHolder holder, Product product) {
        holder.nominalPriceTextView.setText(product.getPrice());
        holder.nominalDiskonTextView.setVisibility(View.GONE);
        holder.nominalTagTextview.setVisibility(View.GONE);
        holder.nominalPriceTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_600));
        if (!isShowPrice)
            holder.nominalPriceTextView.setVisibility(View.GONE);
    }

    private void renderProductPromo(NewWidgetNominalAdapter.NormalViewHolder holder, Product product) {
        Promo promo = product.getPromo();
        holder.nominalPriceTextView.setText(promo.getNewPrice());
        holder.nominalTagTextview.setText(promo.getTag());
        holder.nominalDiskonTextView.setText(product.getPrice());
        holder.nominalDiskonTextView.setPaintFlags(holder.nominalDiskonTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
        holder.nominalTagTextview.setTextColor(ContextCompat.getColor(getContext(),R.color.red_300));
        holder.nominalPriceTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.red_300));
        if (!isShowPrice) {
            holder.nominalPriceTextView.setVisibility(View.GONE);
            holder.nominalTagTextview.setVisibility(View.GONE);
            holder.nominalDiskonTextView.setVisibility(View.GONE);
        }
    }

    private void renderEmptyStock(NewWidgetNominalAdapter.NormalViewHolder holder, Product product) {
        if (product.getStatus() == OUT_OF_STOCK) {
            holder.nominalDescriptionTextview.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_400));
            holder.nominalDiskonTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_400));
            holder.nominalDetailTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_400));
            holder.nominalPriceTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_400));
            holder.nominalTagTextview.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_400));
            holder.emptyStockTextView.setVisibility(View.VISIBLE);
        } else {
            holder.nominalDescriptionTextview.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_600));
            holder.nominalDiskonTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_600));
            holder.nominalDetailTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_600));
            holder.emptyStockTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        int status = productList.get(position).getStatus();
        switch (status) {
            case OUT_OF_STOCK:
                return false;
            default:
                return true;
        }
    }

    static class NormalViewHolder {
        @BindView(R2.id.nominal_description_textview)
        TextView nominalDescriptionTextview;
        @BindView(R2.id.nominal_tag_textview)
        TextView nominalTagTextview;
        @BindView(R2.id.nominal_detail_textview)
        TextView nominalDetailTextView;
        @BindView(R2.id.real_price_textview)
        TextView nominalPriceTextView;
        @BindView(R2.id.nominal_diskon_textview)
        TextView nominalDiskonTextView;
        @BindView(R2.id.empty_stock)
        TextView emptyStockTextView;

        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
