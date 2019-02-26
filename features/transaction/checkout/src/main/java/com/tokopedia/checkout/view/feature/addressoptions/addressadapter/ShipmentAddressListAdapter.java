package com.tokopedia.checkout.view.feature.addressoptions.addressadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CornerAddressModel mSampaiModel;
    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
        mSampaiModel = null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == R.layout.item_sampai) return new SampaiViewHolder(view);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == R.layout.item_sampai) {
            SampaiViewHolder sampaiViewHolder = (SampaiViewHolder) holder;
            sampaiViewHolder.bind(mSampaiModel, mActionListener, position);
        } else {
            int addressPosition = position - getExtraCount();
            RecipientAddressViewHolder addressHolder = (RecipientAddressViewHolder) holder;
            RecipientAddressModel address = mAddressModelList.get(addressPosition);
            addressHolder.bind(address, mActionListener, position);
            if (position == getItemCount() - 1) addressHolder.setState(RecipientAddressViewHolder.VIEW_TYPE.BUTTON_ON);
            if (position == getExtraCount()) addressHolder.setState(RecipientAddressViewHolder.VIEW_TYPE.HEADER_ON);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size() + getExtraCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mSampaiModel != null) return R.layout.item_sampai;
        else return RecipientAddressViewHolder.TYPE;
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.clear();
        mAddressModelList.addAll(addressModelList);
    }

    public void updateAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
        notifyDataSetChanged();
    }

    public void showSampaiWithoutSelected() {
        mSampaiModel = new CornerAddressModel();
        notifyDataSetChanged();
    }

    public void setSampai(CornerAddressModel cornerAddressModel) {
        mSampaiModel = cornerAddressModel;
        notifyDataSetChanged();
    }

    public void updateSelected(int position) {
        if (getItemViewType(position) == R.layout.item_sampai) {
            mSampaiModel.setSelected(true);
            for (RecipientAddressModel addressModel : mAddressModelList) {
                addressModel.setSelected(false);
            }
        } else {
            for (int i = 0; i < mAddressModelList.size(); i++) {
                if (position - getExtraCount() == i) {
                    mAddressModelList.get(i).setSelected(true);
                } else {
                    mAddressModelList.get(i).setSelected(false);
                }
            }
            if (mSampaiModel != null) mSampaiModel.setSelected(false);
        }
        notifyDataSetChanged();
    }

    private int getExtraCount() {
        return mSampaiModel != null ? 1 : 0;
    }

    public interface ActionListener {

        void onAddressContainerClicked(RecipientAddressModel model, int position);

        void onCornerAddressClicked(CornerAddressModel cornerAddressModel, int position);

        void onEditClick(RecipientAddressModel model);

        void onAddAddressButtonClicked();

        void onCornerButtonClicked();
    }

    class SampaiViewHolder extends RecyclerView.ViewHolder {

        Button mButton;
        View mCornerView;
        TextView mCornerName, mBranchName;
        RadioButton mRadio;

        public SampaiViewHolder(View itemView) {
            super(itemView);
            mCornerView = itemView.findViewById(R.id.view_tkpd_corner);
            mButton = itemView.findViewById(R.id.button_tkpd_corner);
            mCornerName = itemView.findViewById(R.id.text_view_corner);
            mBranchName = itemView.findViewById(R.id.text_view_branch);
            mRadio = itemView.findViewById(R.id.radio_button_corner);
        }

        public void bind(CornerAddressModel model, ActionListener listener, int position) {
            if (!TextUtils.isEmpty(model.getCornerName())) {
                mCornerView.setVisibility(View.VISIBLE);
                mCornerView.setOnClickListener(view -> listener.onCornerAddressClicked(model, position));
                mCornerName.setText(model.getCornerName());
                mBranchName.setText(model.getCornerBranchName());
                mRadio.setChecked(model.isSelected());
                mButton.setText("Ubah Lokasi Tokopedia Corner");
            } else {
                mCornerView.setVisibility(View.GONE);
                mButton.setText("Pilih Lokasi Tokopedia Corner");
            }
            mButton.setOnClickListener(view -> listener.onCornerButtonClicked());
        }
    }

}
