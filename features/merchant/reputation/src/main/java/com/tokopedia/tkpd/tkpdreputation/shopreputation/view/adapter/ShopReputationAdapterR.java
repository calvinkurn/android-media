package com.tokopedia.tkpd.tkpdreputation.shopreputation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.customadapter.AbstractRecyclerAdapter;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.StarGenerator;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ReputationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 11/4/2015.
 */
public class ShopReputationAdapterR extends AbstractRecyclerAdapter{

    public interface ReputationAdapterListener{
        void onOpenReview(int pos);
        void onLikeReview(int pos);
        void onDislikeReview(int pos);
        void onProdClick(int pos);
        void onUserClick(int pos);
        void onReport(int pos);
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        ImageView smiley;
        ImageView overFlow;
        LinearLayout starQuality;
        LinearLayout starAccuracy;
        TextView username;
        TextView counterComment;
        TextView counterLike;
        TextView counterDislike;
        LinearLayout counterSmiley;
        TextView comment;
        TextView date;
        TextView prodName;
        LabelUtils label;
        ImageView iconLike;
        ImageView iconDislike;
        ProgressBar loading;
        View viewLikeDislike;
        View viewReputation;
        View viewProduct;
        TextView textPercentage;
        ImageView iconPercentage;
        View mainView;
        RecyclerView imageHolder;

        ImageUploadAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            username = (TextView) itemView.findViewById(R.id.username);
            date = (TextView) itemView.findViewById(R.id.date);
            smiley = (ImageView) itemView.findViewById(R.id.smiley);
            counterSmiley = (LinearLayout) itemView.findViewById(R.id.counter_smiley);
            starQuality = (LinearLayout) itemView.findViewById(R.id.star_quality);
            starAccuracy = (LinearLayout) itemView.findViewById(R.id.star_accuracy);
            comment = (TextView) itemView.findViewById(R.id.comment);
            counterComment = (TextView) itemView.findViewById(R.id.counter_comment);
            counterLike = (TextView) itemView.findViewById(R.id.counter_like);
            counterDislike = (TextView) itemView.findViewById(R.id.counter_dislike);
            overFlow = (ImageView) itemView.findViewById(R.id.btn_overflow);
            viewLikeDislike = itemView.findViewById(R.id.view_like_dislike);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
            iconDislike = (ImageView) itemView.findViewById(R.id.icon_dislike);
            iconLike = (ImageView) itemView.findViewById(R.id.icon_like);
            textPercentage = (TextView) itemView.findViewById(R.id.rep_rating);
            iconPercentage = (ImageView) itemView.findViewById(R.id.rep_icon);
            viewReputation = itemView.findViewById(R.id.counter_smiley);
            prodName = (TextView)itemView.findViewById(R.id.prod_name);
            viewProduct = itemView.findViewById(R.id.product_info);
            label = LabelUtils.getInstance(context, username);
            mainView = itemView.findViewById(R.id.container);
            imageHolder = (RecyclerView) itemView.findViewById(R.id.image_holder);
        }
    }

    private Context context;
    private List<ReputationModel> modelList;
    private ReputationAdapterListener listener;
    private View.OnClickListener onRetryClickListener;

    public void setOnRetryClickListener(View.OnClickListener listener){
        onRetryClickListener = listener;
    }

    public static ShopReputationAdapterR createAdapter(Context context, List<ReputationModel> modelList){
        ShopReputationAdapterR adapter = new ShopReputationAdapterR();
        adapter.context = context;
        adapter.modelList = modelList;
        return adapter;
    }

    public void setReputationAdapterListener(ReputationAdapterListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateVHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_reputation, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.adapter = ImageUploadAdapter.createAdapter(context);
        holder.adapter.setListener(onProductImageActionListener(holder.adapter.getList()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.imageHolder.setLayoutManager(layoutManager);
        holder.imageHolder.setAdapter(holder.adapter);
        return holder;
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener(final ArrayList<ImageUpload> list) {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> listImage = new ArrayList<>();
                        ArrayList<String> listDesc = new ArrayList<>();
                        for (ImageUpload imageUpload : list) {
                            listImage.add(imageUpload.getPicSrcLarge());
                            listDesc.add(imageUpload.getDescription());
                        }

                        Intent intent = new Intent(context, PreviewProductImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("fileloc", listImage);
                        bundle.putStringArrayList("image_desc", listDesc);
                        bundle.putInt("img_pos", position);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                };
            }
        };
    }

    @Override
    public void onBindVHolder(RecyclerView.ViewHolder holder, int position) {
        setModelToView((ViewHolder)holder, modelList.get(position));
        setListener((ViewHolder)holder, modelList.get(position));
    }

    private void setModelToView(ViewHolder holder, ReputationModel model) {
        ImageHandler.loadImageCircle2(context, holder.avatar, model.avatarUrl);
//        ImageHandler.LoadImageCircle(holder.avatar, model.avatarUrl);
        if(model.getImages().size() > 0){
            holder.imageHolder.setVisibility(View.VISIBLE);
            holder.adapter.addList(model.getImages());
        }else{
            holder.imageHolder.setVisibility(View.GONE);
        }
        holder.username.setText(MethodChecker.fromHtml(model.username).toString());
        holder.date.setText(model.date);
        holder.comment.setText(MethodChecker.fromHtml(model.comment));
        holder.label.giveSquareLabel(model.userLabel);
        holder.textPercentage.setText(model.counterSmiley);
        StarGenerator.setReputationStars(context, holder.starAccuracy, model.starAccuracy);
        StarGenerator.setReputationStars(context, holder.starQuality, model.starQuality);
        setProductInfo(holder, model.productName);
        setIconPercentage(holder, model);
        setCounter(holder, model);
        setVisibility(holder, model);
        // setSmiley(position);
    }

    private void setProductInfo(ViewHolder holder, String productName){
        if(productName.length()>0 && !productName.equals("0")){
            holder.prodName.setText(MethodChecker.fromHtml(productName));
            holder.viewProduct.setVisibility(View.VISIBLE);
        }
        else{
            holder.viewProduct.setVisibility(View.GONE);
        }
    }

    private void setIconPercentage(ViewHolder holder, ReputationModel model) {
        if(allowActiveSmiley(model)) {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.textPercentage.setVisibility(View.VISIBLE);
        } else {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.textPercentage.setVisibility(View.GONE);
        }
    }

//    private String getProductId(int pos) { TODO what is this?
//        if(!productId.equals("")) {
//            return productId;
//        } else {
//            return modelList.get(pos).productId;
//        }
//    }

    private boolean allowActiveSmiley(ReputationModel model) {
        return model.noReputationUserScore == 0;
    }

    private void setCounter(ViewHolder holder, ReputationModel model) {
        setIconLike(holder, model);
        setIconDislike(holder, model);

        holder.counterLike.setText(""+model.counterLike);
        holder.counterDislike.setText(""+model.counterDislike);
        holder.counterComment.setText(""+model.counterResponse);
    }

    private void setIconLike(ViewHolder holder, ReputationModel model) {
        if(model.statusLikeDislike == 1) {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like_active);
        } else {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like);
        }
    }

    private void setIconDislike(ViewHolder holder, ReputationModel model) {
        if(model.statusLikeDislike == 2) {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike_active);
        } else {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike);
        }
    }

    private int generateStar(int args) {
        int star = R.drawable.ic_star_none;
        switch (args) {
            case 1 : star = R.drawable.ic_star_one;
                break;
            case 2 : star = R.drawable.ic_star_two;
                break;
            case 3 : star = R.drawable.ic_star_three;
                break;
            case 4 : star = R.drawable.ic_star_four;
                break;
            case 5 : star = R.drawable.ic_star_five;
                break;
        }
        return star;
    }

    private void setVisibility(ViewHolder holder, ReputationModel model) {
//        if(SessionHandler.isV4Login(context) && isProductOwner(model)) {
            holder.overFlow.setVisibility(View.VISIBLE);
//        } else {
//            holder.overFlow.setVisibility(View.GONE);
//        }

        if(!model.isGetLikeDislike) {
            holder.viewLikeDislike.setVisibility(View.GONE);
            holder.loading.setVisibility(View.VISIBLE);
        } else {
            holder.viewLikeDislike.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.GONE);
        }
    }

    private boolean isProductOwner(ReputationModel model) {
        SessionHandler session = new SessionHandler(context);
        return model.userIdResponder.equals(session.getLoginID());
    }

    private void setListener(ViewHolder holder, ReputationModel reputationModel) {
//        holder.counterLike.setOnClickListener(OnLikeClickListener(position));
        holder.iconLike.setOnClickListener(onLikeClickListener(holder.getAdapterPosition()));
//        holder.counterDislike.setOnClickListener(OnDislikeClickListener(position));
        holder.iconDislike.setOnClickListener(onDislikeClickListener(holder.getAdapterPosition()));
        holder.mainView.setOnClickListener(onItemClickListener(holder.getAdapterPosition()));
        holder.overFlow.setOnClickListener(onOverFlowClickListener(holder.getAdapterPosition()));
//        holder.viewReputation.setOnClickListener(OnReputationViewClickListener(position));
//        holder.viewProduct.setOnClickListener(OnProductClick(list.get(position).productId));
    }

    private View.OnClickListener onOverFlowClickListener(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOverflow(v, pos);
            }
        };
    }

    private void createOverflow(View v, final int position) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(getMenuID(position), popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_report) {
                    listener.onReport(position);
                    return true;
                } else {
                    return false;
                }
            }

        });
        popup.show();
    }

    private int getMenuID(int pos){
        return R.menu.report_menu;
    }

    private View.OnClickListener onDislikeClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDislikeReview(position);
            }
        };
    }

    private View.OnClickListener onLikeClickListener(final int pos) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLikeReview(pos);
            }
        };
    }

    private View.OnClickListener onItemClickListener(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOpenReview(pos);
            }
        };
    }

    @Override
    public int getChildItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemType(int pos) {
        return 0;
    }

    @Override
    public View.OnClickListener getRetryClickListener() {
        return onRetryClickListener;
    }
}
