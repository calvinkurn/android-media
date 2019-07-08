package com.tokopedia.core.manage.people.address.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.presenter.ChooseAddressFragmentPresenter;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int VIEW_ADDRESS = 100;
    private final ChooseAddressFragmentPresenter presenter;

    private ArrayList<Destination> list;
    private final Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleAddress;
        TextView addressDetail;
        View btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            titleAddress = (TextView) itemView.findViewById(R.id.title_address);
            addressDetail = (TextView) itemView.findViewById(R.id.address_detail);
            btnEdit = (View) itemView.findViewById(R.id.edit_address);
        }
        @Override
        public void onClick(View view) {
            Destination destination = list.get(getAdapterPosition());
            if (list.size() > 0 && list.size() > getAdapterPosition()) presenter.setOnChooseAddressClick(context, destination);
        }
    }

    public ChooseAddressAdapter(Context context, ChooseAddressFragmentPresenter presenter) {
        this.context = context;
        this.list = new ArrayList<>();
        this.presenter = presenter;
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
        holder.titleAddress.setText(MethodChecker.fromHtml(list.get(position).getAddressName()));
        holder.addressDetail.setText(MethodChecker.fromHtml(list.get(position).getAddressDetail()));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                presenter.setOnEditAddressClick(context, list.get(position));
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
        return list.size() + super.getItemCount();
    }

    public static ChooseAddressAdapter createInstance(Context context, ChooseAddressFragmentPresenter presenter) {
        return new ChooseAddressAdapter(context, presenter);
    }

    public ArrayList<Destination> getList() {
        return list;
    }

    public void addList(List<Destination> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
