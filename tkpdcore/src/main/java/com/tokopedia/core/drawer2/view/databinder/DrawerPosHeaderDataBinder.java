package com.tokopedia.core.drawer2.view.databinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.router.posapp.PosAppDataGetter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

/**
 * Created by Herdi_WORK on 07.09.17.
 */

public class DrawerPosHeaderDataBinder extends DataBinder<DrawerPosHeaderDataBinder.ViewHolder> {

    public interface DrawerHeaderListener {

    }

    private Context context;
    private DrawerData data;
    private DrawerHeaderListener listener;

    public DrawerPosHeaderDataBinder(DataBindAdapter dataBindAdapter,
                                        Context context,
                                        DrawerHeaderListener listener,
                                        LocalCacheHandler drawerCache) {
        super(dataBindAdapter);
        this.context = context;
        this.data = new DrawerData();
        this.listener = listener;
    }

    public DrawerData getData() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView tvOnline;
        private final TextView tvOutlet;
        private final View ivOnline;


        ImageView ivAvatar;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvOnline = (TextView) itemView.findViewById(R.id.tv_online);
            tvOutlet = (TextView) itemView.findViewById(R.id.tv_outlet);
            ivOnline = itemView.findViewById(R.id.iv_online);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    @Override
    public DrawerPosHeaderDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        return new DrawerPosHeaderDataBinder.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_header_pos, parent, false));
    }

    @Override
    public void bindViewHolder(DrawerPosHeaderDataBinder.ViewHolder holder, int position) {
        holder.tvOnline.setVisibility(View.VISIBLE);
        holder.ivOnline.setVisibility(View.VISIBLE);
        holder.tvOutlet.setVisibility(View.VISIBLE);
        holder.tvName.setText(data.getDrawerProfile().getShopName());
        holder.tvOnline.setText("Online");
        holder.tvOutlet.setText(((PosAppDataGetter)context.getApplicationContext()).getOutletName());
        if (data.getDrawerProfile().getUserAvatar() != null
                && !data.getDrawerProfile().getUserAvatar().equals("")) {
            ImageHandler.LoadImage(holder.ivAvatar, data.getDrawerProfile().getUserAvatar());
        } else {
            holder.ivAvatar.setImageResource(R.drawable.qc_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


}
