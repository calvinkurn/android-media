package com.tokopedia.tkpd.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.product.model.passdata.ProductPass;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderProduct;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TxProductListAdapter
 * Created by Angga.Prasetiyo on 28/04/2016.
 */
public class TxProductListAdapter extends ArrayAdapter<OrderProduct> {
    private final LayoutInflater inflater;
    private final ActionListener listener;
    private final Context context;

    public interface ActionListener {
        void actionToProductInfo(ProductPass productPass);
    }

    public TxProductListAdapter(Context context, ActionListener listener) {
        super(context, R.layout.listview_shop_order_detail_product, new ArrayList<OrderProduct>());
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_shop_order_detail_product, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderProduct item = getItem(position);
        holder.tvProductName.setText(Html.fromHtml(item.getProductName()));
        holder.tvProductPrice.setText(item.getProductPrice());
        holder.tvNotes.setText(Html.fromHtml(item.getProductNotes().length()==0 ? "-" : item.getProductNotes()));
        holder.tvDeliverQty.setText(MessageFormat.format(" x {0} {1}",
                item.getOrderDeliverQuantity(), context.getString(R.string.title_item)));
        holder.tvTotalPrice.setText(item.getOrderSubtotalPriceIdr());
        ImageHandler.loadImageRounded2(context, holder.ivProductPic, item.getProductPicture());

        holder.tvProductName.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                listener.actionToProductInfo(ProductPass.Builder.aProductPass()
                        .setProductId(getItem(position).getProductId())
                        .setProductName(getItem(position).getProductName())
                        .setProductImage(getItem(position).getProductPicture())
                        .setProductPrice(getItem(position).getProductPrice())
                        .build());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.product_image)
        ImageView ivProductPic;
        @Bind(R.id.product_name)
        TextView tvProductName;
        @Bind(R.id.product_price)
        TextView tvProductPrice;
        @Bind(R.id.total_order)
        TextView tvDeliverQty;
        @Bind(R.id.total_price)
        TextView tvTotalPrice;
        @Bind(R.id.message)
        TextView tvNotes;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
