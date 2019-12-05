package com.tokopedia.flight.booking.view.adapter;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceBenefitViewModel;

import java.util.List;

public class FlightInsuranceBenefitAdapter extends RecyclerView.Adapter<FlightInsuranceBenefitAdapter.ViewHolder> {
    private List<FlightInsuranceBenefitViewModel> benefitViewModels;

    public FlightInsuranceBenefitAdapter(List<FlightInsuranceBenefitViewModel> benefitViewModels) {
        this.benefitViewModels = benefitViewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_benefit_insurance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(benefitViewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return benefitViewModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView logoImageView;
        private AppCompatTextView titleTextView;
        private AppCompatTextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(com.tokopedia.flight.R.id.iv_logo);
            titleTextView = itemView.findViewById(com.tokopedia.design.R.id.tv_title);
            descriptionTextView = itemView.findViewById(com.tokopedia.design.R.id.tv_description);
        }

        public void bind(FlightInsuranceBenefitViewModel benefitViewModel) {
            ImageHandler.loadImageWithoutPlaceholder(logoImageView, benefitViewModel.getIcon(),
                    ContextCompat.getDrawable(itemView.getContext(), com.tokopedia.flight.R.drawable.flight_ic_airline_default)
            );
            titleTextView.setText(benefitViewModel.getTitle());
            descriptionTextView.setText(benefitViewModel.getDescription());
        }
    }
}
