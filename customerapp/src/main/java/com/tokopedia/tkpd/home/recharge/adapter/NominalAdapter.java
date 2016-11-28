package com.tokopedia.tkpd.home.recharge.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.database.recharge.product.Attributes;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.Promo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Kulomady 05 on 7/14/2016.
 */
public class NominalAdapter extends ArrayAdapter<Product> {
    private final static int OUT_OF_STOCK = 3;
    private final LayoutInflater inflater;
    private List<Product> productList;
    private Boolean isShowPrice = true;

    public NominalAdapter(Context context, int resource, List<Product> productList, Boolean isShowPrice) {
        super(context, resource, productList);
        this.productList = productList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isShowPrice = isShowPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        Product product = productList.get(position);
        NormalViewHolder normalViewHolder;
        View rowNormal = convertView;
        if (rowNormal == null) {
            rowNormal = inflater.inflate(R.layout.recharge_spinner_item_view, parent, false);
            normalViewHolder = new NormalViewHolder(rowNormal);
            rowNormal.setTag(normalViewHolder);
        } else {
            normalViewHolder = (NormalViewHolder) rowNormal.getTag();
        }
        renderProduct(normalViewHolder, product);
        return rowNormal;
    }

    private void renderProduct(NormalViewHolder holder, Product product) {
        Attributes productAttributes = product.getAttributes();
        holder.nominalDescriptionTextview.setText(productAttributes.getDesc());
        renderNominalDetail(holder, productAttributes);
        if (product.getAttributes().getPromo() != null) {
            renderProductPromo(holder, product);
        } else {
            renderProductWithoutPromo(holder, productAttributes);
        }
        renderEmptyStock(holder, productAttributes);
    }

    private void renderNominalDetail(NormalViewHolder holder, Attributes productAttributes) {
        String nominalDetail = productAttributes.getDetail();
        if (nominalDetail != null && nominalDetail.length() > 0) {
            holder.nominalDetailTextView.setText(Html.fromHtml(nominalDetail));
            holder.nominalDetailTextView.setVisibility(View.VISIBLE);
        } else {
            holder.nominalDetailTextView.setVisibility(View.GONE);
        }
    }

    private void renderProductWithoutPromo(NormalViewHolder holder, Attributes productAttributes) {
        //holder.linPromoView.setVisibility(View.GONE);
        holder.nominalPriceTextView.setText(productAttributes.getPrice());
        holder.nominalDiskonTextView.setVisibility(View.GONE);
        holder.nominalTagTextview.setVisibility(View.GONE);
        //holder.linPromoView.setVisibility(View.VISIBLE);
        holder.nominalPriceTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_600));
        if (!isShowPrice)
            holder.nominalPriceTextView.setVisibility(View.GONE);
    }

    private void renderProductPromo(NormalViewHolder holder, Product product) {
        //holder.linNormalView.setVisibility(View.VISIBLE);
        Promo promo = product.getAttributes().getPromo();
        //holder.linPromoView.setVisibility(View.VISIBLE);
        holder.nominalPriceTextView.setText(promo.getNewPrice());
        holder.nominalTagTextview.setText(promo.getTag());
        holder.nominalDiskonTextView.setText(product.getAttributes().getPrice());
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

    private void renderEmptyStock(NormalViewHolder holder, Attributes product) {
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
        int status = productList.get(position).getAttributes().getStatus();
        switch (status) {
            case OUT_OF_STOCK:
                return false;
            default:
                return true;
        }
    }

    static class NormalViewHolder {
        @Bind(R2.id.nominal_description_textview)
        TextView nominalDescriptionTextview;
        @Bind(R2.id.nominal_tag_textview)
        TextView nominalTagTextview;
        @Bind(R2.id.nominal_detail_textview)
        TextView nominalDetailTextView;
        @Bind(R2.id.real_price_textview)
        TextView nominalPriceTextView;
        @Bind(R2.id.nominal_diskon_textview)
        TextView nominalDiskonTextView;
        @Bind(R2.id.lin_normal_view)
        RelativeLayout linNormalView;
        @Bind(R2.id.lin_promo)
        LinearLayout linPromoView;
        @Bind(R2.id.tv_promo_terms)
        TextView promoTermTextView;
        @Bind(R2.id.tv_promo_value_text)
        TextView promoValueTextView;
        @Bind(R2.id.tv_promo_tag)
        TextView promoTagTextView;
        @Bind(R2.id.tv_promo_bonus_text)
        TextView promoBonusTextView;
        @Bind(R2.id.tv_promo_new_price)
        TextView promoNewPriceTextView;
        @Bind(R2.id.empty_stock)
        TextView emptyStockTextView;

        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}