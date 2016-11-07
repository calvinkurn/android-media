package com.tokopedia.tkpd.manage.people.address.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.addtocart.model.responseatcform.Destination;
import com.tokopedia.tkpd.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.tkpd.manage.people.address.ManageAddressConstant;
import com.tokopedia.tkpd.manage.people.address.activity.AddAddressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int VIEW_ADDRESS = 100;

    private ArrayList<Destination> list;
    private final Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R2.id.title_address)
        TextView titleAddress;

        @Bind(R2.id.address_detail)
        TextView addressDetail;

        @Bind(R2.id.edit_address)
        View btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            Intent intent = ((Activity) context).getIntent();
            intent.putExtra(ManageAddressConstant.EXTRA_ADDRESS, list.get(getAdapterPosition()));
            ((Activity) context).setResult(RESULT_OK, intent);
            ((Activity) context).finish();
        }
    }

    public ChooseAddressAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_ADDRESS:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_choose_address, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_ADDRESS:
                bindAddress((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindAddress(final ViewHolder holder, final int position) {
        holder.titleAddress.setText(Html.fromHtml(list.get(position).getAddressName()));
        holder.addressDetail.setText(Html.fromHtml(list.get(position).getAddressDetail()));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ManageAddressConstant.EDIT_PARAM, list.get(position));
                bundle.putBoolean(ManageAddressConstant.IS_EDIT, true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_ADDRESS;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount() ;
    }

    public static ChooseAddressAdapter createInstance(Context context) {
        return new ChooseAddressAdapter(context);
    }

    public ArrayList<Destination> getList() {
        return list;
    }

    public void addList(List<Destination> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
