package com.tokopedia.core.customadapter;

import android.app.Fragment;
import android.content.Context;
import android.text.Html;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.selling.presenter.ShippingImpl;

import java.util.List;

/**
 * Created by Tkpd_Eka on 2/5/2015.
 */
@Deprecated
public class ListViewShopShippingV2 extends BaseAdapter{

    public void setData(List<ShippingImpl.Model> data) {
        this.modelList = data;
    }

    public interface LVBarcodeInterface{
        void requestBarcodeScanner(int pos);
        void requestRefNumDialog(int pos);
    }

    public interface LVShippingInterface{
        void onOpenDetail(int pos);
        void onConfirm(int pos);
        void onCancel(int pos);
    }

    public static ListViewShopShippingV2 createInstance(Fragment frag, List<ShippingImpl.Model> modelList){
        ListViewShopShippingV2 adapter = new ListViewShopShippingV2();
        adapter.modelList = modelList;
        adapter.context = frag.getActivity();
        adapter.listenerBarcode = (LVBarcodeInterface)frag;
        adapter.listenerLV = (LVShippingInterface)frag;
        return adapter;
    }

    public static ListViewShopShippingV2 createInstance(android.support.v4.app.Fragment frag, List<ShippingImpl.Model> modelList){
        ListViewShopShippingV2 adapter = new ListViewShopShippingV2();
        adapter.modelList = modelList;
        adapter.context = frag.getActivity();
        adapter.listenerBarcode = (LVBarcodeInterface)frag;
        adapter.listenerLV = (LVShippingInterface)frag;
        return adapter;
    }

    private List<ShippingImpl.Model> modelList;
    private ViewHolder holder;
    private Context context;
    private LVBarcodeInterface listenerBarcode;
    private LVShippingInterface listenerLV;
    private TkpdProgressDialog progressDialog;

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

    private class ViewHolder{
        TextView vRefNumber;
        TextView vError;
        TextView vReceiver;
        TextView vInsertReceiver;
        TextView vDest;
        TextView vInsertDest;
        TextView vShipping;
        TextView vInsertShipping;
        TextView vInsertSenderDS;
        TextView vSenderDS;
        View Dropshipper;
        View InsertDropshipper;
        View InfoView;
        View InsertView;
        TextView UserName;
        TextView InsertUserName;
        ImageView UserAvatar;
        ImageView InsertUserAvatar;
        View BtnOverflow;
        TextView Deadline;
        View DeadlineView;
        TextView Invoice;
//        TextView DetailButton; 1 WTF here
        TextView Bounty;
        TextView ConfirmButton;
        TextView CancelButton;
        TextView vShippingPrice;
        ImageView CancelBut;
        ImageView CameraBut;
        View MainView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listview_shop_shipping_confirmation, null);
            initView(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        setModelToView(position);
        setListener(position);
        return convertView;
    }

    private void initView(View convertView){
        holder = new ViewHolder();
        holder.BtnOverflow = convertView.findViewById(R.id.but_overflow);
        holder.InfoView = convertView.findViewById(R.id.info_view);
        holder.InsertView = convertView.findViewById(R.id.insert_view);
        holder.UserName = (TextView) convertView.findViewById(R.id.user_name);
        holder.ConfirmButton = (TextView) convertView.findViewById(R.id.confirm_button);
        holder.CancelButton = (TextView) convertView.findViewById(R.id.cancel_button);
        holder.UserAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
        holder.InsertUserAvatar = (ImageView) convertView.findViewById(R.id.insert_user_avatar);
        holder.CancelBut = (ImageView) convertView.findViewById(R.id.cancel_but);
        holder.CameraBut = (ImageView) convertView.findViewById(R.id.camera_but);
        holder.Deadline = (TextView) convertView.findViewById(R.id.deadline);
        holder.DeadlineView =  convertView.findViewById(R.id.deadline_view);
        holder.Invoice = (TextView) convertView.findViewById(R.id.invoice_text);
        holder.Bounty = (TextView) convertView.findViewById(R.id.bounty);
        holder.vShippingPrice = (TextView) convertView.findViewById(R.id.shipping_price);
        holder.vRefNumber = (TextView) convertView.findViewById(R.id.ref_number);
        holder.vError = (TextView) convertView.findViewById(R.id.error_msg);
        holder.vReceiver = (TextView) convertView.findViewById(R.id.receiver_name);
        holder.vDest = (TextView) convertView.findViewById(R.id.dest);
        holder.vShipping = (TextView) convertView.findViewById(R.id.shipping);
        holder.vInsertReceiver = (TextView) convertView.findViewById(R.id.insert_receiver_name);
        holder.vInsertDest = (TextView) convertView.findViewById(R.id.insert_dest);
        holder.vInsertShipping = (TextView) convertView.findViewById(R.id.insert_shipping);
        holder.InsertUserName = (TextView) convertView.findViewById(R.id.insert_user_name);
        holder.vSenderDS = (TextView) convertView.findViewById(R.id.sender_detail);
        holder.vInsertSenderDS = (TextView) convertView.findViewById(R.id.insert_sender_detail);
        holder.Dropshipper = convertView.findViewById(R.id.dropshipper);
        holder.InsertDropshipper = convertView.findViewById(R.id.insert_dropshipper);
        holder.MainView =  convertView.findViewById(R.id.main_view);
    }

    private void setModelToView(int position){
        ShippingImpl.Model model = modelList.get(position);
        setChecked(model);
        checkPermission(model.Permission);
        setSenderDetail(model);
        setViewData(model);
        checkError(model);
        CommonUtils.getProcessDay(context, model.Deadline, holder.Deadline, holder.DeadlineView);
    }

    private void checkPermission(String permission){
        if(!permission.equals("0")){
            setHasPermission();
        }
        else{
            setNoPermission();
        }
    }

    private void checkError(ShippingImpl.Model model){
        if(model.ErrorRow){
            holder.vError.setVisibility(View.VISIBLE);
            holder.vError.setText(model.ErrorMsg);
        }
        else{
            try {
                holder.InsertView.setBackground(context.getResources().getDrawable(R.drawable.cards_ui_selected));
            } catch (NoSuchMethodError e) {
                holder.InsertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cards_ui_selected));
            }
            holder.vError.setVisibility(View.GONE);
        }
    }

    private void setSenderDetail(ShippingImpl.Model model){
        if (!CommonUtils.checkNullForZeroJson(model.SenderDetail)) {
            setSender(model);
        } else {
           setNoSender();
        }
    }
    
    private void setSender(ShippingImpl.Model model){
        holder.Dropshipper.setVisibility(View.VISIBLE);
        holder.InsertDropshipper.setVisibility(View.VISIBLE);
        holder.vSenderDS.setText(model.SenderDetail);
        holder.vInsertSenderDS.setText(model.SenderDetail);
    }
    
    private void setNoSender(){
        holder.Dropshipper.setVisibility(View.GONE);
        holder.InsertDropshipper.setVisibility(View.GONE);
    }
    
    private void setHasPermission(){
        holder.BtnOverflow.setVisibility(View.VISIBLE);
    }

    private void setNoPermission(){
        holder.BtnOverflow.setVisibility(View.INVISIBLE);
    }

    private void setChecked(ShippingImpl.Model model){
        if (model.Checked) {
            holder.InfoView.setVisibility(View.GONE);
            holder.InsertView.setVisibility(View.VISIBLE);
        } else {
            holder.InfoView.setVisibility(View.VISIBLE);
            holder.InsertView.setVisibility(View.GONE);
        }
    }
    
    private void setViewData(ShippingImpl.Model model){
        holder.vReceiver.setText(Html.fromHtml(model.ReceiverName));
        holder.vInsertReceiver.setText(Html.fromHtml(model.ReceiverName));
        holder.vDest.setText(model.Dest);
        holder.vInsertDest.setText(model.Dest);
        holder.vShipping.setText(model.Shipping);
        holder.vInsertShipping.setText(model.Shipping);
        holder.UserName.setText(model.UserName);
        holder.InsertUserName.setText(model.UserName);
        holder.vShippingPrice.setText(model.ShippingPrice);
        if(model.RefNum.equals(""))
            holder.vRefNumber.setText(context.getString(R.string.hint_fill_ref));
        else
            holder.vRefNumber.setText(model.RefNum);
        holder.Invoice.setText(model.Invoice);
    }

    private void setListener(int pos){
        holder.vRefNumber.setOnClickListener(onGetRefNumDialog(pos));
        holder.CameraBut.setOnClickListener(onGetBarcodeListener(pos));
        holder.BtnOverflow.setOnClickListener(onOverflowClicked(pos));
    }

    private View.OnClickListener onGetRefNumDialog(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerBarcode.requestRefNumDialog(pos);
            }
        };
    }

    private View.OnClickListener onGetBarcodeListener(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerBarcode.requestBarcodeScanner(pos);
            }
        };
    }

    private View.OnClickListener onOverflowClicked(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpMenu(v, position);
            }
        };
    }

    private void createPopUpMenu(View v, final int position){
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.shipping_confirmation_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(onMenuItem(position));
        popup.show();
    }

    private PopupMenu.OnMenuItemClickListener onMenuItem(final int position){
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((item.getItemId())){
                    case R2.id.action_confirm:
                        listenerLV.onConfirm(position);
                        return true;

                    case R2.id.action_cancel:
                        listenerLV.onCancel(position);
                        return true;

                    case R2.id.action_detail_ship:
                        listenerLV.onOpenDetail(position);
                        return true;

                    default:
                        return false;
                }
            }
        };
    }

}
