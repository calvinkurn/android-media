package com.tokopedia.core.customadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingList;

import java.util.List;

/**
 * Created by Tkpd_Eka on 2/2/2015.
 */
@Deprecated
public class ListViewShopNewOrderV2 extends BaseAdapter{

    private List<OrderShippingList> modelList;
    private ViewHolder holder;
    private Context context;

    public static ListViewShopNewOrderV2 createInstance(Context context, List<OrderShippingList> modelList){
        ListViewShopNewOrderV2 adapter = new ListViewShopNewOrderV2();
        adapter.context = context;
        adapter.modelList = modelList;
        return adapter;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(List<OrderShippingList> data) {
        this.modelList = data;
    }

    private class ViewHolder{
        TextView UserName;
        ImageView UserAvatar;
        TextView Deadline;
        View DeadlineView;
        TextView Invoice;
        TextView TotalTransaksi;
        View MainView;
        TextView vOrderDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_shop_new_order, null);
            initView(convertView);
            convertView.setTag(holder);
        }
            else{
            holder = (ViewHolder)convertView.getTag();
        }

        setValue(position);

        return convertView;
    }

    private void initView(View convertView){
        holder.UserName = (TextView) convertView.findViewById(R.id.user_name);
        holder.UserAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
        holder.Deadline = (TextView) convertView.findViewById(R.id.deadline);
        holder.DeadlineView = convertView.findViewById(R.id.deadline_view);
        holder.Invoice = (TextView) convertView.findViewById(R.id.invoice);
        holder.TotalTransaksi = (TextView) convertView.findViewById(R.id.bounty);
        holder.vOrderDate = (TextView) convertView.findViewById(R.id.order_date);
        holder.MainView = convertView.findViewById(R.id.main_view);
    }

    private void setValue(int pos){
        OrderShippingList model = modelList.get(pos);
        setValueToView(model);
    }

    private void setValueToView(OrderShippingList model){
        holder.UserName.setText(model.getOrderCustomer().getCustomerName());
        CommonUtils.getProcessDay(context, model.getOrderPayment().getPaymentProcessDayLeft()+"", holder.Deadline, holder.DeadlineView);
        holder.Invoice.setText(model.getOrderDetail().getDetailInvoice());
        holder.vOrderDate.setText(model.getOrderDetail().getDetailOrderDate());
        holder.TotalTransaksi.setText(model.getOrderPayment().getPaymentKomisi());
//        ImageHandler.LoadImageCircle(holder.UserAvatar, model.AvatarUrl);
        ImageHandler.loadImageCircle2(context, holder.UserAvatar, model.getOrderCustomer().getCustomerImage());
    }
}
