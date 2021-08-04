package com.tokopedia.review.feature.reputationhistory.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.old.BaseLinearRecyclerViewAdapter;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.review.feature.reputationhistory.util.DateHeaderFormatter;
import com.tokopedia.review.feature.reputationhistory.view.helper.DateUtilHelper;
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationHeaderViewHelper;
import com.tokopedia.review.feature.reputationhistory.view.model.EmptyListModel;
import com.tokopedia.review.feature.reputationhistory.view.model.EmptySeparatorModel;
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationReviewModel;
import com.tokopedia.review.feature.reputationhistory.view.model.SetDateHeaderModel;
import com.tokopedia.review.R;
import com.tokopedia.unifyprinciples.Typography;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vishal.gupta on 20/02/2017.
 * modify by normansyahputra 16-03-2017
 */
public class SellerReputationAdapter extends BaseLinearRecyclerViewAdapter {
    public static final int SET_DATE_MODEL_POSITION = 0;
    public static final String KEY_LIST_DATA = "KEY_LIST_DATA";
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String TAG = "SellerReputationAdapter";
    private final Context context;
    private ArrayList<ItemType> list;
    private Fragment fragment;

    public SellerReputationAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public static SellerReputationAdapter createInstance(Context context) {
        return new SellerReputationAdapter(context);
    }

    public static SellerReputationAdapter createInstance(Activity activity, ArrayList<Parcelable> tempParcelables) {
        SellerReputationAdapter sellerReputationAdapter = new SellerReputationAdapter(activity);
        sellerReputationAdapter.restoreParcelable(tempParcelables);
        return sellerReputationAdapter;
    }

    public void setFragment(@Nullable Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ReputationReviewModel.VIEW_DEPOSIT:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_seller_reputation, viewGroup, false);
                return new ViewHolder(itemLayoutView);
            case SetDateHeaderModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.widget_header_reputation, viewGroup, false);
                SellerDateHeaderViewHolder viewHolder2 = new SellerDateHeaderViewHolder(itemLayoutView);
                viewHolder2.setFragment(fragment);
                return viewHolder2;
            case EmptySeparatorModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.empty_separator_model, viewGroup, false);
                return new EmptySeparatorViewHolder(itemLayoutView);
            case EmptyListModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.deisgn_retry_reputation, viewGroup, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(itemLayoutView);
                emptyListViewHolder.setFragment(fragment);
                return emptyListViewHolder;
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ReputationReviewModel.VIEW_DEPOSIT:
                bindDeposit((ViewHolder) holder, position);
                break;
            case SetDateHeaderModel.TYPE:
                ((SellerDateHeaderViewHolder) holder).bindData((SetDateHeaderModel) list.get(position));
                break;
            case EmptySeparatorModel.TYPE:
                break;
            case EmptyListModel.TYPE:
                ((EmptyListViewHolder) holder).bindData((EmptyListModel) list.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindDeposit(ViewHolder holder, int position) {

        if (position >= 0 && position < getDataSize()) {
            if (list.get(position) != null && list.get(position) instanceof ReputationReviewModel) {
                ReputationReviewModel reputationReviewModel =
                        (ReputationReviewModel) list.get(position);
                ReputationReviewModel.Data data = reputationReviewModel.getData();
                holder.date.setText(data.getDate());
                String information = data.getInformation();
                int indexOfI = 0;
                if (!TextUtils.isEmpty(information)) {
                    indexOfI = information.indexOf("I");
                }
                String descriptionText = "";
                String invoiceText = "";
                if (indexOfI > 0) {
                    descriptionText = information.substring(0, indexOfI);
                    invoiceText = information.substring(indexOfI);
                } else {
                    descriptionText = information;
                }
                holder.description.setText(descriptionText);
                holder.tvInvoice.setText(invoiceText);
                holder.penaltyScore.setText(decimalFormat.format(data.getPenaltyScore()));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return list.get(position).getType();
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    public int getDataSize() {
        return (list != null) ? list.size() : 0;
    }

    public void clear() {
        showEmptyFull(false);
        showEmpty(false);
        showLoadingFull(false);
        showLoading(false);
        showRetry(false);
        showRetryFull(false);
        list.clear();
    }

    public void insertHeader(SetDateHeaderModel headerModel) {
        list.add(0, headerModel);
        notifyItemInserted(0);
    }

    public void addAllWithoutNotify(List<ItemType> datas) {
        list.addAll(datas);
    }

    public void notifyHeaderChange(SetDateHeaderModel headerModel) {
        list.set(0, headerModel);
        notifyItemChanged(0);
    }

    public SetDateHeaderModel getHeaderModel() {
        if (getDataSize() > 0) {
            ItemType itemType = list.get(SET_DATE_MODEL_POSITION);
            if (itemType != null && itemType instanceof SetDateHeaderModel) {
                SetDateHeaderModel setDateHeaderModel = (SetDateHeaderModel) itemType;
                return setDateHeaderModel;
            }
        }
        return null;
    }

    public void saveStates(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(KEY_LIST_DATA, reformatParcelable());
    }

    private ArrayList<Parcelable> reformatParcelable() {
        ArrayList<Parcelable> parcelables = new ArrayList<>();
        for (ItemType itemType : list) {
            switch (itemType.getType()) {
                case ReputationReviewModel.VIEW_DEPOSIT:
                    parcelables.add((ReputationReviewModel) itemType);
                    break;
                case SetDateHeaderModel.TYPE:
                    parcelables.add((SetDateHeaderModel) itemType);
                    break;
                case EmptySeparatorModel.TYPE:
                    parcelables.add((EmptySeparatorModel) itemType);
                    break;
                case EmptyListModel.TYPE:
                    parcelables.add((EmptyListModel) itemType);
                    break;
                default:
                    throw new IllegalArgumentException("error in " + TAG);
            }
        }
        return parcelables;
    }

    public void restoreParcelable(ArrayList<Parcelable> parcelables) {
        for (Parcelable parcelable : parcelables) {
            if (parcelable != null && parcelable instanceof ItemType) {
                list.add((ItemType) parcelable);
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Typography date;

        Typography description;

        Typography penaltyScore;

        Typography tvInvoice;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (Typography) itemView.findViewById(R.id.tv_date);
            description = (Typography) itemView.findViewById(R.id.tv_note);
            penaltyScore = (Typography) itemView.findViewById(R.id.tv_score);
            tvInvoice = (Typography) itemView.findViewById(R.id.tv_invoice);

        }
    }

    public class SellerDateHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ReputationHeaderViewHelper reputationViewHelper;
        private SetDateHeaderModel setDateHeaderModel;
        private Fragment fragment;
        private DateHeaderFormatter dateHeaderFormatter;

        public SellerDateHeaderViewHolder(View itemView) {
            super(itemView);
            dateHeaderFormatter = new DateHeaderFormatter(
                    itemView.getResources().getStringArray(com.tokopedia.datepicker.range.R.array.lib_date_picker_month_entries)
            );
            reputationViewHelper = new ReputationHeaderViewHelper(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickHeader();
                }
            });
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onClickHeader() {
            if (fragment != null) {
                reputationViewHelper.onClick(fragment);
                return;
            }
        }

        public void bindData(SetDateHeaderModel setDateHeaderModel) {
            this.setDateHeaderModel = setDateHeaderModel;

            reputationViewHelper.bindDate(
                    dateHeaderFormatter,
                    setDateHeaderModel.getsDate(),
                    setDateHeaderModel.geteDate(),
                    DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE,
                    DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
            );
        }
    }

    public class EmptySeparatorViewHolder extends RecyclerView.ViewHolder {

        public EmptySeparatorViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class EmptyListViewHolder extends RecyclerView.ViewHolder {

        DateUtilHelper dateUtilHelper;
        Fragment fragment;
        private LinearLayout containerClick;
        private Typography reputationInfo;
        private Typography descReputationInfo;
        private ImageView imageEmptyReputation;

        public EmptyListViewHolder(View itemView) {
            super(itemView);

            dateUtilHelper = new DateUtilHelper(itemView.getContext());
            containerClick = (LinearLayout) itemView.findViewById(R.id.reputation_container_change_date);
            reputationInfo = (Typography) itemView.findViewById(R.id.good_job_reputation_retry);
            descReputationInfo = (Typography) itemView.findViewById(R.id.description_reputation_retry);
            imageEmptyReputation = (ImageView) itemView.findViewById(R.id.img_reputation_retry);
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public void bindData(EmptyListModel emptyListModel) {

            dateUtilHelper.setsDate(emptyListModel.getSetDateHeaderModel().getsDate());
            dateUtilHelper.seteDate(emptyListModel.getSetDateHeaderModel().geteDate());
            if (emptyListModel.isEmptyShop()) {
                imageEmptyReputation.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_penalti_reputasi_zero));
                reputationInfo.setText(context.getString(R.string.reputation_history_label_congrats_no_penalty));
                descReputationInfo.setText(context.getString(R.string.reputation_history_label_improve_selling_get_badge));
            }

            containerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragment() != null) {
                        dateUtilHelper.onClick(fragment);
                    }
                }
            });
        }
    }
}
