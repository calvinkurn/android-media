package com.tokopedia.flight.detail.view.adapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;

import java.util.List;

public class FlightOrderDetailInsuranceAdapter extends RecyclerView.Adapter<FlightOrderDetailInsuranceAdapter.ViewHolder> {
    private List<FlightInsurance> insurances;

    public FlightOrderDetailInsuranceAdapter(List<FlightInsurance> insurances) {
        this.insurances = insurances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_order_detail_insurance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(insurances.get(position));
    }

    @Override
    public int getItemCount() {
        return insurances.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView InsuranceProtTv;
        private AppCompatTextView titleTextView;
        private AppCompatTextView taglineTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(com.tokopedia.flight.R.id.tv_title);
            taglineTextView = itemView.findViewById(com.tokopedia.flight.R.id.tv_tagline);
            InsuranceProtTv = itemView.findViewById(com.tokopedia.flight.R.id.ic_flight_insurance_protection_tv);

            InsuranceProtTv.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                    (InsuranceProtTv.getContext(), com.tokopedia.flight.R.drawable.ic_flight_insurance_protection), null);
        }

        public void bind(FlightInsurance insurance) {
            titleTextView.setText(insurance.getTitle());
            taglineTextView.setText(insurance.getTagline());
        }
    }
}
