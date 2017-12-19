package com.tokopedia.digital.product.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.fragment.DigitalChooserOperatorFragment;
import com.tokopedia.digital.widget.model.operator.Operator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            ImageHandler.LoadImage(itemNameAndImageHolder.operatorImage, operator.getAttributes().getImage());
            itemNameAndImageHolder.operatorName.setText(operator.getAttributes().getName());
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
        @BindView(R2.id.operator_image)
        ImageView operatorImage;
        @BindView(R2.id.operator_name)
        TextView operatorName;

        ItemNameAndImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSearchResultData(List<Operator> operatorQuery) {
        this.operatorList = operatorQuery;
    }
}

