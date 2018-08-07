//package com.tokopedia.challenges.view.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.design.widget.Snackbar;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tokopedia.abstraction.common.utils.image.ImageHandler;
//import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
//import com.tokopedia.digital_deals.DealsModuleRouter;
//import com.tokopedia.digital_deals.R;
//import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
//import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
//import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
//import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
//import com.tokopedia.digital_deals.view.model.ProductItem;
//import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
//import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
//import com.tokopedia.digital_deals.view.utils.Utils;
//import com.tokopedia.usecase.RequestParams;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SubmissionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DealCategoryAdapterContract.View {
//
//    private List<ProductItem> categoryItems;
//    private Context context;
//    private final int ITEM = 1;
////    @Inject
////    DealCategoryAdapterPresenter mPresenter;
//    INavigateToActivityRequest navigateToActivityRequest;
//    private boolean isFooterAdded;
//    public final static int REQUEST_CODE_LOGIN = 104;
//
//
//    public SubmissionItemAdapter(List<ProductItem> categoryItems, INavigateToActivityRequest navigateToActivityRequest) {
//        if (categoryItems == null)
//            this.categoryItems = new ArrayList<>();
//        else
//            this.categoryItems = categoryItems;
//        this.navigateToActivityRequest = navigateToActivityRequest;
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return (categoryItems == null) ? 0 : categoryItems.size();
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        this.context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(
//                parent.getContext());
//        RecyclerView.ViewHolder holder = null;
//        View v;
//        switch (viewType) {
//            case ITEM:
//                v = inflater.inflate(R.layout.submissions_card_item, parent, false);
//                holder = new ItemViewHolder(v);
//                break;
//            default:
//                break;
//        }
//
////        DealsComponentInstance.getDealsComponent(getActivity().getApplication()).inject(this);
////        mPresenter.attachView(this);
////        mPresenter.initialize();
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        switch (getItemViewType(position)) {
//            case ITEM:
//                ((ItemViewHolder) holder).setIndex(position);
//                ((ItemViewHolder) holder).bindData(categoryItems.get(position));
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return ITEM;
//    }
//
//    private boolean isLastPosition(int position) {
//        return (position == categoryItems.size() - 1);
//    }
//
//    public void addFooter() {
//        if (!isFooterAdded) {
//            isFooterAdded = true;
//            add(new ProductItem(), true);
//        }
//    }
//
//
//    public void add(ProductItem item, boolean refreshItem) {
//        categoryItems.add(item);
//        if (refreshItem)
//            notifyItemInserted(categoryItems.size() - 1);
//    }
//
//    public void clearList() {
//        isFooterAdded = false;
//        if (categoryItems != null)
//            categoryItems.clear();
//    }
//
//    public void addAll(List<ProductItem> items, Boolean... refreshItems) {
//        boolean refreshItem = true;
//        if (refreshItems.length > 0)
//            refreshItem = refreshItems[0];
//        if (items != null) {
//            for (ProductItem item : items) {
//                add(item, refreshItem);
//            }
//
//        }
//    }
//
//    public void removeFooter() {
//        if (isFooterAdded) {
//            isFooterAdded = false;
//
//            int position = categoryItems.size() - 1;
//            ProductItem item = categoryItems.get(position);
//
//            if (item != null) {
//                categoryItems.remove(position);
//                notifyItemRemoved(position);
//            }
//        }
//    }
//
//    @Override
//    public Activity getActivity() {
//        return (Activity) context;
//    }
//
//    @Override
//    public RequestParams getParams() {
//        return null;
//    }
//
//    @Override
//    public void notifyDataSetChanged(int position) {
//
//    }
//
//
//    @Override
//    public void showLoginSnackbar(String message, int position) {
//        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
//                getActivity().getResources().getString(R.string.title_activity_login), (View.OnClickListener) v -> {
//                    Intent intent = ((DealsModuleRouter) getActivity().getApplication()).
//                            getLoginIntent(getActivity());
//                    navigateToActivityRequest.onNavigateToActivityRequest(intent, REQUEST_CODE_LOGIN, position);
//                }
//        ).show();
//    }
//
//    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private View itemView;
//        private ImageView submissionImage;
//        private TextView tvBuzzPoints;
//        private TextView submissionTitle;
//        private ImageView ivFavourite;
//        private ImageView ivShareVia;
//        private int index;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            this.itemView = itemView;
//            submissionImage = itemView.findViewById(R.id.iv_challenge);
//            tvBuzzPoints = itemView.findViewById(R.id.tv_buzz_points);
//            submissionTitle = itemView.findViewById(R.id.tv_submission_title);
//            ivFavourite = itemView.findViewById(R.id.iv_like);
//            ivShareVia = itemView.findViewById(R.id.iv_share);
//        }
//
//        public void bindData(final ProductItem productItem) {
//            submissionTitle.setText(productItem.getDisplayName());
//            ImageHandler.loadImage(context, submissionImage, productItem.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
//
//
////            Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_location);
////            tvBuzzPoints.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
////            tvBuzzPoints.setCompoundDrawablePadding(getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8));
//
//
//            setLikes(productItem.getLikes(), productItem.isLiked());
//
//
//            itemView.setOnClickListener(this);
//            ivShareVia.setOnClickListener(this);
//            ivFavourite.setOnClickListener(this);
//        }
//
//        void setLikes(int likes, boolean isLiked) {
//            categoryItems.get(getIndex()).setLikes(likes);
//            categoryItems.get(getIndex()).setLiked(isLiked);
//            if (likes > 0) {
//                tvBuzzPoints.setVisibility(View.VISIBLE);
//                tvBuzzPoints.setText(String.valueOf(likes));
//            } else {
//                tvBuzzPoints.setVisibility(View.GONE);
//            }
//            if (isLiked) {
//                ivFavourite.setImageResource(R.drawable.ic_wishlist_filled);
//            } else {
//                ivFavourite.setImageResource(R.drawable.ic_wishlist_unfilled);
//            }
//        }
//
//        public void setIndex(int position) {
//            this.index = position;
//        }
//
//        public int getIndex() {
//            return this.index;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.iv_share) {
//                Utils.getSingletonInstance().shareDeal(categoryItems.get(getIndex()).getSeoUrl(),
//                        context, categoryItems.get(getIndex()).getDisplayName(),
//                        categoryItems.get(getIndex()).getImageWeb());
//            } else if (v.getId() == R.id.iv_wish_list) {
//
//                boolean isLoggedIn=false;
////                = mPresenter.setDealLike(categoryItems.get(getIndex()), getIndex());
//                if (isLoggedIn) {
//                    if (categoryItems.get(getIndex()).isLiked()) {
//                        setLikes(categoryItems.get(getIndex()).getLikes() - 1, !categoryItems.get(getIndex()).isLiked());
//                    } else {
//                        setLikes(categoryItems.get(getIndex()).getLikes() + 1, !categoryItems.get(getIndex()).isLiked());
//                    }
//                }
//            } else if (v.getId() == R.id.cv_brand) {
//                Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
//                detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, categoryItems.get(getIndex()).getBrand());
//                context.startActivity(detailsIntent);
//            } else {
//                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
//                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
//                navigateToActivityRequest.onNavigateToActivityRequest(detailsIntent, DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY, getIndex());
//            }
//        }
//    }
//
//    public void setLike(int position) {
//        if (position < categoryItems.size()) {
//            if (categoryItems.get(position).isLiked()) {
//                categoryItems.get(position).setLikes(categoryItems.get(position).getLikes() - 1);
//                categoryItems.get(position).setLiked(!categoryItems.get(position).isLiked());
//            } else {
//                categoryItems.get(position).setLikes(categoryItems.get(position).getLikes() + 1);
//                categoryItems.get(position).setLiked(!categoryItems.get(position).isLiked());
//            }
//            notifyItemChanged(position);
//        }
//    }
//
//
//    public class FooterViewHolder extends RecyclerView.ViewHolder {
//
//        View loadingLayout;
//
//        private FooterViewHolder(View itemView) {
//            super(itemView);
//            loadingLayout = itemView.findViewById(R.id.loading_fl);
//        }
//    }
//
//
////    public void unsubscribeUseCase() {
////        mPresenter.onDestroy();
////    }
//
//    public interface INavigateToActivityRequest {
//        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);
//    }
//}