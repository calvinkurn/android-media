//package com.tokopedia.core.payment.adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tkpd.library.utils.ImageHandler;
//import com.tokopedia.core.R;
//import com.tokopedia.core.R2;
//import com.tokopedia.core.cart.model.GatewayList;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * @author by Angga.Prasetiyo on 23/05/2016.
// */
//public class PaymentGatewayAdapter extends ArrayAdapter<GatewayList> {
//    private final Context context;
//    private final LayoutInflater inflater;
//
//    public PaymentGatewayAdapter(Context context, List<GatewayList> datas) {
//        super(context, R.layout.simple_payment_listview, datas);
//        this.context = context;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @SuppressLint("InflateParams")
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.simple_payment_listview, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        final GatewayList item = getItem(position);
//        holder.tvName.setText(item != null ? item.getGatewayName() : "");
//        ImageHandler.LoadImage(holder.ivLogo, item != null ? item.getGatewayImage() : "");
//        holder.tvFee.setText(item != null ? item.getFeeInformation(context) : "");
//        return convertView;
//    }
//
//    static class ViewHolder {
//
//        @BindView(R2.id.img)
//        ImageView ivLogo;
//        @BindView(R2.id.name)
//        TextView tvName;
//        @BindView(R2.id.payment_fee)
//        TextView tvFee;
//
//        public ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
