package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.logisticinputreceiptshipment.R;
import com.tokopedia.transaction.common.data.order.CourierViewModel;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;

import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierAdapter extends RecyclerView.Adapter<OrderCourierAdapter.OrderCourierViewHolder> {

    private List<CourierViewModel> modelList;

    private OrderCourierAdapterListener listener;

    public OrderCourierAdapter(ListCourierViewModel courierViewModel,
                               OrderCourierAdapterListener listener) {
        this.modelList = courierViewModel.getCourierViewModelList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderCourierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_order_courier_adapter, parent, false);
        return new OrderCourierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCourierViewHolder holder, int position) {
        final CourierViewModel currentViewModel = modelList.get(position);
        if (currentViewModel.getCourierImageUrl() == null
                || currentViewModel.getCourierImageUrl().isEmpty())
            holder.courierLogo.setVisibility(View.GONE);
        else {
            holder.courierLogo.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.courierLogo, currentViewModel.getCourierImageUrl());
        }
        holder.courierName.setText(currentViewModel.getCourierName());
        holder.courierCheckBox.setChecked(currentViewModel.isSelected());
        holder.courierPlaceHolder.setOnClickListener(onCourierSelectedListener(currentViewModel));
        holder.courierCheckBox.setOnClickListener(onCourierSelectedListener(currentViewModel));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class OrderCourierViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup courierPlaceHolder;

        private ImageView courierLogo;

        private TextView courierName;

        private RadioButton courierCheckBox;

        OrderCourierViewHolder(View itemView) {
            super(itemView);

            courierPlaceHolder = itemView.findViewById(R.id.item_place_holder);

            courierLogo = itemView.findViewById(R.id.courier_logo);

            courierName = itemView.findViewById(R.id.courier_name);

            courierCheckBox = itemView.findViewById(R.id.courier_radio_button);

        }
    }

    private View.OnClickListener onCourierSelectedListener(final CourierViewModel courierViewModel) {
        return view -> {
            clearSelectedList();
            courierViewModel.setSelected(true);
            notifyDataSetChanged();
            listener.onCourierSelected(courierViewModel);
        };
    }

    private void clearSelectedList() {
        for (int i = 0; i < modelList.size(); i++) {
            modelList.get(i).setSelected(false);
        }
    }

    public interface OrderCourierAdapterListener {

        void onCourierSelected(CourierViewModel courierViewModel);

    }
}
