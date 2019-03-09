package com.tokopedia.checkout.view.feature.addressoptions.cornerbtmsheet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;

import java.util.List;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerAdapter extends RecyclerView.Adapter<CornerAdapter.CornerViewHolder> {

    private List<CornerAddressModel> mData;
    private OnItemCliciListener mListener;

    public CornerAdapter(List<CornerAddressModel> mData, OnItemCliciListener listener) {
        this.mData = mData;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CornerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_corner_branch, parent, false);
        return new CornerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CornerViewHolder holder, int position) {
        holder.bind(mData.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CornerViewHolder extends RecyclerView.ViewHolder {

        TextView mCornerName, mCornerDesc;
        View mView;

        CornerViewHolder(View itemView) {
            super(itemView);
            mCornerName = itemView.findViewById(R.id.text_view_branch_name);
            mCornerDesc = itemView.findViewById(R.id.text_view_branch_desc);
            mView = itemView;
        }

        public void bind(CornerAddressModel model, OnItemCliciListener listener) {
            mCornerName.setText(model.getCornerBranchName());
            mCornerDesc.setText(model.getCornerBranchDesc());
            mView.setOnClickListener(view -> listener.onItemClick(model));
        }
    }

    public interface OnItemCliciListener {
        void onItemClick(CornerAddressModel corner);
    }
}
