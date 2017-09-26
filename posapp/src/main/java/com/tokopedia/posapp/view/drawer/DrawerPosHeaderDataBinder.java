package com.tokopedia.posapp.view.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerData;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
//        this.data = createDataFromCache(drawerCache);
        this.listener = listener;

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
        }
    }

    @Override
    public DrawerPosHeaderDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        return new DrawerPosHeaderDataBinder.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_header_pos, parent, false));
    }

    @Override
    public void bindViewHolder(DrawerPosHeaderDataBinder.ViewHolder holder, int position) {
        holder.tvName.setText(PosSessionHandler.getShopName(context));
        holder.tvOnline.setVisibility(View.VISIBLE);
        holder.ivOnline.setVisibility(View.VISIBLE);
        holder.tvOnline.setText("Online");
        holder.tvOutlet.setText(PosSessionHandler.getOutletName(context));
    }

    @Override
    public int getItemCount() {
        return 1;
    }


}
