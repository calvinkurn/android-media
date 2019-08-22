package com.tokopedia.train.homepage.presentation;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 23/07/18.
 */
public class TrainPromoAdapter extends RecyclerView.Adapter {

    private List<TrainPromoViewModel> trainPromoViewModelList;
    private ListenerTrainPromoAdapter listener;
    private Context context;

    public TrainPromoAdapter() {
        this.trainPromoViewModelList = new ArrayList<>();
    }

    public void setListener(ListenerTrainPromoAdapter listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_train_promo, parent, false);
        return new TrainPromoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrainPromoItemViewHolder itemHolder = (TrainPromoItemViewHolder) holder;
        TrainPromoViewModel trainPromoViewModel = trainPromoViewModelList.get(position);
        itemHolder.descPromo.setText(trainPromoViewModel.getAttributes().getDescription());
        itemHolder.promoCode.setText(trainPromoViewModel.getAttributes().getPromoCode());

        if (trainPromoViewModel.isPromoCodeCopied()) {
            itemHolder.layoutItemPromo.setBackground(ContextCompat.getDrawable(context, R.drawable.train_bg_banner_selected));
            itemHolder.promoCode.setBackground(ContextCompat.getDrawable(context, R.color.tkpd_main_green));
            itemHolder.promoCode.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            itemHolder.layoutItemPromo.setBackground(ContextCompat.getDrawable(context, R.drawable.train_bg_banner_item));
            itemHolder.promoCode.setBackground(ContextCompat.getDrawable(context, R.color.white));
            itemHolder.promoCode.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green));
        }

        itemHolder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < trainPromoViewModelList.size(); i++) {
                    if (trainPromoViewModelList.get(i).isPromoCodeCopied()) {
                        trainPromoViewModelList.get(i).setPromoCodeCopied(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
                trainPromoViewModel.setPromoCodeCopied(true);
                notifyItemChanged(position);
                listener.onClickCopyPromoCode(trainPromoViewModel.getAttributes().getPromoCode());
            }
        });

        itemHolder.layoutItemPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickItemPromo(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainPromoViewModelList.size();
    }

    public static class TrainPromoItemViewHolder extends RecyclerView.ViewHolder {

        private TextView descPromo;
        private TextView promoCode;
        private TextView copyButton;
        private RelativeLayout layoutItemPromo;

        public TrainPromoItemViewHolder(View itemView) {
            super(itemView);
            descPromo = itemView.findViewById(R.id.description_promo);
            promoCode = itemView.findViewById(R.id.promo_code);
            copyButton = itemView.findViewById(R.id.copy_button);
            layoutItemPromo = itemView.findViewById(R.id.layout_item_promo);
        }
    }

    public void addItems(List<TrainPromoViewModel> trainPromoViewModelList) {
        this.trainPromoViewModelList.clear();
        this.trainPromoViewModelList = trainPromoViewModelList;
        notifyDataSetChanged();
    }

    public interface ListenerTrainPromoAdapter {
        void onClickCopyPromoCode(String promoCode);

        void onClickItemPromo(int position);
    }
}
