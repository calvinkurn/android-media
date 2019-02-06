package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * Created by Rizky on 06/09/18.
 */
public class DigitalOperatorChooserNoStyleAdapter extends RecyclerView.Adapter<DigitalOperatorChooserNoStyleAdapter.ViewHolder> {

    private List<Operator> operatorList;

    private ActionListener actionListener;

    public interface ActionListener {

        void onOperatorItemSelected(Operator operator);

    }

    public DigitalOperatorChooserNoStyleAdapter(ActionListener actionListener, List<Operator> operatorList) {
        this.actionListener = actionListener;
        this.operatorList = operatorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitra_digital_operator,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(operatorList.get(position));
    }

    public void setSearchResultData(List<Operator> operatorQuery) {
        this.operatorList = operatorQuery;
    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageOperator;
        private TextView textOperatorName;

        private Operator operator;

        ViewHolder(View itemView) {
            super(itemView);

            imageOperator = itemView.findViewById(R.id.operator_image);
            textOperatorName = itemView.findViewById(R.id.operator_name);

            itemView.setOnClickListener(v -> actionListener.onOperatorItemSelected(operator));
        }

        void bind(Operator operator) {
            this.operator = operator;
            textOperatorName.setText(operator.getName());
            ImageHandler.LoadImage(imageOperator, operator.getImage());
        }

    }

}
