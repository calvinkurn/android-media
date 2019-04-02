package com.tokopedia.digital.product.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.fragment.DigitalChooserOperatorFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 5/9/17. Tokopedia
 */

public class OperatorChooserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_OPERATOR_NAME_AND_IMAGE =
            R.layout.view_holder_item_operator_name_image;

    private Fragment hostFragment;
    private List<Operator> operatorList;
    private DigitalChooserOperatorFragment.ActionListener actionListener;

    public OperatorChooserAdapter(Fragment hostFragment,
                                  List<Operator> operatorList,
                                  DigitalChooserOperatorFragment.ActionListener operatorActionListener) {
        this.hostFragment = hostFragment;
        this.operatorList = operatorList != null ? operatorList : new ArrayList<Operator>();
        this.actionListener = operatorActionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemNameAndImageHolder(LayoutInflater.from(
                hostFragment.getActivity()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        final Operator operator = operatorList.get(position);
        if (type == TYPE_HOLDER_OPERATOR_NAME_AND_IMAGE) {
            ItemNameAndImageHolder itemNameAndImageHolder = (ItemNameAndImageHolder) holder;
            ImageHandler.LoadImage(itemNameAndImageHolder.operatorImage, operator.getImage());
            itemNameAndImageHolder.operatorName.setText(operator.getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onOperatorItemSelected(operator);
            }
        });
    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_HOLDER_OPERATOR_NAME_AND_IMAGE;
    }

    static class ItemNameAndImageHolder extends RecyclerView.ViewHolder {
        ImageView operatorImage;
        TextView operatorName;

        ItemNameAndImageHolder(View itemView) {
            super(itemView);
            operatorImage = itemView.findViewById(R.id.operator_image);
            operatorName = itemView.findViewById(R.id.operator_name);
        }
    }

    public void setSearchResultData(List<Operator> operatorQuery) {
        this.operatorList = operatorQuery;
    }

}

