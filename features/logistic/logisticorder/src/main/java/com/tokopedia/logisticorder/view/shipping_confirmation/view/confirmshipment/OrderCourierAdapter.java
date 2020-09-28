package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.logisticorder.R;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierUiModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.ListCourierUiModel;

import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierAdapter extends RecyclerView.Adapter<OrderCourierAdapter.OrderCourierViewHolder> {

    private List<CourierUiModel> modelList;

    private OrderCourierAdapterListener listener;

    public OrderCourierAdapter(ListCourierUiModel courierUiModel,
                               OrderCourierAdapterListener listener) {
        this.modelList = courierUiModel.getCourierUiModelList();
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
        final CourierUiModel currentUiModel = modelList.get(position);
        if (currentUiModel.getCourierImageUrl() == null
                || currentUiModel.getCourierImageUrl().isEmpty())
            holder.courierLogo.setVisibility(View.GONE);
        else {
            holder.courierLogo.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.courierLogo, currentUiModel.getCourierImageUrl());
        }
        holder.courierName.setText(currentUiModel.getCourierName());
        holder.courierCheckBox.setChecked(currentUiModel.isSelected());
        holder.courierPlaceHolder.setOnClickListener(onCourierSelectedListener(currentUiModel));
        holder.courierCheckBox.setOnClickListener(onCourierSelectedListener(currentUiModel));
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

    private View.OnClickListener onCourierSelectedListener(final CourierUiModel courierUiModel) {
        return view -> {
            clearSelectedList();
            courierUiModel.setSelected(true);
            notifyDataSetChanged();
            listener.onCourierSelected(courierUiModel);
        };
    }

    private void clearSelectedList() {
        for (int i = 0; i < modelList.size(); i++) {
            modelList.get(i).setSelected(false);
        }
    }

    public interface OrderCourierAdapterListener {

        void onCourierSelected(CourierUiModel courierUiModel);

    }
}
