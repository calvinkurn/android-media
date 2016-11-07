package com.tokopedia.tkpd.customadapter;

import android.app.Fragment;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.selling.model.SellingStatusTxModel;
import com.tokopedia.tkpd.var.TkpdState;

import java.util.List;

/**
 * Created by Tkpd_Eka on 2/4/2015.
 */
@Deprecated
public class ListViewShopTxStatusV2 extends BaseAdapter{

    public interface LVShopStatusInterface{
        void onEditRef(int pos);
        void onTrack(int pos);
    }

    private Context context;
    private List<SellingStatusTxModel> modelList;
    private ViewHolder holder;
    private LVShopStatusInterface listener;

    public static ListViewShopTxStatusV2 createInstance(Fragment frag, List<SellingStatusTxModel> modelList){
        ListViewShopTxStatusV2 adapter = new ListViewShopTxStatusV2();
        adapter.context = frag.getActivity();
        adapter.modelList = modelList;
        adapter.listener = (LVShopStatusInterface)frag;
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

    private class ViewHolder{
        TextView UserName;
        ImageView UserAvatar;
        View BtnOverflow;
        TextView Deadline;
        TextView Invoice;
        ImageView DetailButton;
        TextView Bounty;
        TextView LastStatus;
        TextView vDeadlineFin;
        LinearLayout ManageView;
        View Lines;
        TextView EditRef;
        TextView Track;
        View DeadlineView;
        View DetailShipping;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_shop_shipping_status, null);
            initCreateView(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        setValue(position);
        setListener(position);
        return convertView;
    }

    private void initCreateView(View convertView){
        holder.UserName = (TextView) convertView.findViewById(R.id.user_name);
        holder.UserAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
        holder.Deadline = (TextView) convertView.findViewById(R.id.deadline);
        holder.Invoice = (TextView) convertView.findViewById(R.id.invoice_text);
        holder.LastStatus = (TextView) convertView.findViewById(R.id.last_status);
        holder.Bounty = (TextView) convertView.findViewById(R.id.bounty);
        holder.ManageView = (LinearLayout) convertView.findViewById(R.id.manage_layout);
        holder.Track = (TextView) convertView.findViewById(R.id.track_button);
        holder.EditRef = (TextView) convertView.findViewById(R.id.edit_ref);
        holder.DetailShipping = convertView.findViewById(R.id.detail_shipping);
        holder.BtnOverflow = convertView.findViewById(R.id.but_overflow);
        holder.vDeadlineFin = (TextView) convertView.findViewById(R.id.deadline_finish);
        holder.DeadlineView = convertView.findViewById(R.id.deadline_view);
    }

    private void setValue(int pos){
        SellingStatusTxModel model = modelList.get(pos);
        ImageHandler.loadImageCircle2(context, holder.UserAvatar, model.AvatarUrl);
//        ImageHandler.LoadImageCircle(holder.UserAvatar, model.AvatarUrl);
        holder.UserName.setText(model.UserName);
        holder.Deadline.setText(model.Deadline);
        holder.LastStatus.setText(model.LastStatus);
        holder.Invoice.setText(model.Invoice);
        holder.Bounty.setText(model.Komisi);
        if(!CommonUtils.checkNullForZeroJson(model.DeadlineFinish)){
            holder.DeadlineView.setVisibility(View.GONE);
        }else if(holder.LastStatus.getText().toString().trim().equals("Transaksi selesai"))
        {
            holder.DeadlineView.setVisibility(View.GONE);
        }else
            {
                holder.DeadlineView.setVisibility(View.VISIBLE);
                holder.vDeadlineFin.setText(model.DeadlineFinish);
            }

        setOverflow(model);
    }

    private void setListener(int pos){
        holder.BtnOverflow.setOnClickListener(onButtonOverflow(pos));
    }

    private View.OnClickListener onButtonOverflow(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOverflowMenu(v, pos);
            }
        };
    }

    private void createOverflowMenu(View v,int pos){
        PopupMenu menu = new PopupMenu(context, v);
        if (modelList.get(pos).isPickUp == 1) {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu_track_only, menu.getMenu());
        } else {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu, menu.getMenu());
        }
        menu.setOnMenuItemClickListener(onMenuItemClick(pos));
        menu.show();
    }

    private PopupMenu.OnMenuItemClickListener onMenuItemClick(final int pos){
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R2.id.action_edit:
                        listener.onEditRef(pos);
                        return true;

                    case R2.id.action_track:
                        listener.onTrack(pos);
                        return true;

                    default:
                        return false;
                }
            };
        };
    }

    private void setOverflow(SellingStatusTxModel model){
        if(!model.Permission.equals("0") && (model.OrderStatus.equals("500") || model.OrderStatus.equals("501") || model.OrderStatus.equals("520") || model.OrderStatus.equals("530"))){
            holder.BtnOverflow.setVisibility(View.VISIBLE);
        }
        else{
            holder.BtnOverflow.setVisibility(View.GONE);
        }
    }
}
