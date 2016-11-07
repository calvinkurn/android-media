package com.tokopedia.tkpd.review.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.PreviewProductImage;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.tkpd.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.tkpd.reputationproduct.ReputationProductView;
import com.tokopedia.tkpd.review.model.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.review.model.product_review.ReviewProductModel;
import com.tokopedia.tkpd.review.presenter.ProductReviewPresenter;
import com.tokopedia.tkpd.review.var.Const;
import com.tokopedia.tkpd.util.ReportTalkReview;
import com.tokopedia.tkpd.util.SelectableSpannedMovementMethod;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.StarGenerator;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Steven on 12/01/2016.
 */
public class ProductReviewAdapter extends BaseRecyclerViewAdapter {

    public LayoutInflater inflater;
    private int lastPosition;
    private Context context;
    private ProductReviewPresenter presenter;
    private String productId;

    public static ProductReviewAdapter createAdapter(Context context, List<RecyclerViewItem> reviews) {
        return new ProductReviewAdapter(context, reviews);
    }

    public ProductReviewAdapter(Context context, List<RecyclerViewItem> reviews) {
        super(context, reviews);
        lastPosition = -1;
        this.context = context;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.container)
        LinearLayout container;
        @Bind(R2.id.user_avatar)
        ImageView avatar;
        @Bind(R2.id.star_quality)
        LinearLayout starQuality;
        @Bind(R2.id.star_accuracy)
        LinearLayout starAccuracy;
        @Bind(R2.id.username)
        TextView username;
        @Bind(R2.id.comment)
        TextView comment;
        @Bind(R2.id.date)
        TextView date;
        @Bind(R2.id.btn_overflow)
        ImageView overFlow;
        @Bind(R2.id.image_holder)
        RecyclerView imageHolder;

        ImageUploadAdapter adapter;

        ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class MostHelpfulViewHolder extends RecyclerView.ViewHolder {
        @Bind({R2.id.container1, R2.id.container2, R2.id.container3})
        LinearLayout[] container;
        @Bind({R2.id.user_avatar1, R2.id.user_avatar2, R2.id.user_avatar3})
        ImageView[] avatar;
        @Bind({R2.id.star_quality1, R2.id.star_quality2, R2.id.star_quality3})
        LinearLayout[] starQuality;
        @Bind({R2.id.star_accuracy1, R2.id.star_accuracy2, R2.id.star_accuracy3})
        LinearLayout[] starAccuracy;
        @Bind({R2.id.username1, R2.id.username2, R2.id.username3})
        TextView[] username;
        @Bind({R2.id.comment1, R2.id.comment2, R2.id.comment3})
        TextView[] comment;
        @Bind({R2.id.date1, R2.id.date2, R2.id.date3})
        TextView[] date;
        @Bind({R2.id.btn_overflow1, R2.id.btn_overflow2, R2.id.btn_overflow3})
        ImageView[] overFlow;
        @Bind(R2.id.expand)
        TextView expand;
        @Bind(R2.id.collapse)
        TextView collapse;
        @Bind(R2.id.image_holder)
        RecyclerView imageHolder;

        ImageUploadAdapter adapter;

        MostHelpfulViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0) {
            return super.getItemViewType(position);
        } else if (isLastItemPosition(position)) {
            return super.getItemViewType(position);
        } else {
            if (data.get(position) instanceof HelpfulReviewList) {
                return Const.MOST_HELPFUL_REVIEW;
            } else {
                return Const.REVIEW;
            }
        }
    }

    public boolean isLastItemPosition(int position) {
        if (position == data.size()) return true;
        else return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);

        switch (viewType) {
            case Const.MOST_HELPFUL_REVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_most_helpful, parent, false);
                MostHelpfulViewHolder holder = new MostHelpfulViewHolder(view);
                holder.adapter = ImageUploadAdapter.createAdapter(context);
                holder.adapter.setListener(onProductImageActionListener(holder.adapter.getList()));
                holder.imageHolder.setLayoutManager(layoutManager);
                holder.imageHolder.setAdapter(holder.adapter);
                return holder;

            case Const.REVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_reputation, parent, false);
                ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
                reviewViewHolder.adapter = ImageUploadAdapter.createAdapter(context);
                reviewViewHolder.adapter.setListener(onProductImageActionListener(reviewViewHolder.adapter.getList()));
                reviewViewHolder.imageHolder.setLayoutManager(layoutManager);
                reviewViewHolder.imageHolder.setAdapter(reviewViewHolder.adapter);
                return reviewViewHolder;

            default:
                return super.onCreateViewHolder(parent, viewType);

        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        switch (getItemViewType(position)) {
            case Const.MOST_HELPFUL_REVIEW:
                bindViewMostHelpfulReview(viewHolder, position);
                break;
            case Const.REVIEW:
                bindViewReview(viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    public RecyclerView.ViewHolder bindViewReview(final RecyclerView.ViewHolder viewHolder, final int position) {
        final Context context = viewHolder.itemView.getContext();
        final ReviewProductModel review = (ReviewProductModel) data.get(position);
        final ReviewViewHolder holder = (ReviewViewHolder) viewHolder;
        if(review.getImages().size() > 0){
            holder.imageHolder.setVisibility(View.VISIBLE);
        }else{
            holder.imageHolder.setVisibility(View.GONE);
        }
        holder.adapter.addList(review.getImages());
        ImageHandler.loadImageCircle2(context, holder.avatar, review.getReviewUserImage());
        holder.username.setText(Html.fromHtml(review.getReviewUserName()).toString());
        holder.date.setText(review.getReviewCreateTime());
        holder.comment.setText(review.getReviewMessage());
        holder.comment.setMovementMethod(new SelectableSpannedMovementMethod());

        setVisibility(context, holder, position);
        StarGenerator.setReputationStars(context, holder.starAccuracy, review.getReviewRateAccuracy());
        StarGenerator.setReputationStars(context, holder.starQuality, review.getReviewRateProduct());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReputationProductView.class);
                intent.putExtra("data_model", (Parcelable) review);
                intent.putExtra("product_id", productId);
                intent.putExtra("shop_id", review.getReviewShopId());
                context.startActivity(intent);
            }
        });
        holder.overFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popUp = new PopupMenu(context, v);
                MenuInflater inflater = popUp.getMenuInflater();
                inflater.inflate(R.menu.report_menu, popUp.getMenu());
                popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_report:
                                showDialogReport(review);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popUp.show();
            }
        });
        holder.username.setOnClickListener(OnUserNameClickListener(review.getReviewUserId()));
        holder.avatar.setOnClickListener(OnUserNameClickListener(review.getReviewUserId()));

        return holder;
    }

    private void showDialogReport(final ReviewProductModel review) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_dialog_report, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.reason);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.action_report),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                            }
                        })
                .setNegativeButton(context.getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(userInput.length()>0){
                            String message = userInput.getText().toString();
                            ActReviewPass pass = new ActReviewPass();
                            pass.setReviewId(String.valueOf(review.getReviewId()));
                            pass.setShopId(review.getReviewShopId());
                            pass.setReportMessage(message);
                            presenter.reportReview(pass);
                            alertDialog.dismiss();
                        }
                        else
                            userInput.setError(context.getString(R.string.error_field_required));
                    }
                });
            }
        });
        // show it
        alertDialog.show();
    }

    public RecyclerView.ViewHolder bindViewMostHelpfulReview(RecyclerView.ViewHolder viewHolder,
                                                             final int position) {
        final Context context = viewHolder.itemView.getContext();
        final HelpfulReviewList helpfulReviewList = ((HelpfulReviewList) data.get(position));
        final MostHelpfulViewHolder
                holder = (MostHelpfulViewHolder) viewHolder;
        final int size = helpfulReviewList.getList().size();
        if (size > 1) {
            holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expand.setVisibility(View.GONE);
                    holder.collapse.setVisibility(View.VISIBLE);
                    for (int i = size - 1; i > 0; i--) {
                        holder.container[i].setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.collapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expand.setVisibility(View.VISIBLE);
                    holder.collapse.setVisibility(View.GONE);
                    for (int i = size - 1; i > 0; i--) {
                        holder.container[i].setVisibility(View.GONE);
                    }
                }
            });
        } else {
            holder.expand.setVisibility(View.GONE);
        }
        for (int i = size - 1; i >= 0; i--) {
            final com.tokopedia.tkpd.review.model.helpful_review.HelpfulReview review = helpfulReviewList.getList().get(i);
            if(review.getImages().size() > 0){
                holder.imageHolder.setVisibility(View.VISIBLE);
            }else{
                holder.imageHolder.setVisibility(View.GONE);
            }
            holder.adapter.addList(review.getImages());
            ImageHandler.loadImageCircle2(context, holder.avatar[i], review.getReviewUserImage());
            holder.username[i].setText(Html.fromHtml(review.getReviewUserName()).toString());
            holder.date[i].setText(review.getReviewCreateTime());
            holder.comment[i].setText(Html.fromHtml(review.getReviewMessage()));
            holder.comment[i].setMovementMethod(new SelectableSpannedMovementMethod());
            StarGenerator.setReputationStars(context, holder.starAccuracy[i], review.getReviewRateAccuracy());
            StarGenerator.setReputationStars(context, holder.starQuality[i], review.getReviewRateProduct());
//            -------------------------
//            holder.container[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ReputationProductView.class);
//                    intent.putExtra("data_model", entity);
//                    intent.putExtra("product_id", entity.getProductId());
//                    intent.putExtra("shop_id", entity.getShopId());
//                    Activity activity = (Activity) context;
//                    activity.startActivityForResult(intent, TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW);
//                }
//            });
            holder.username[i].setOnClickListener(OnUserNameClickListener(review.getReviewUserId()));
            holder.avatar[i].setOnClickListener(OnUserNameClickListener(review.getReviewUserId()));
        }
        setAnimation(holder.itemView, position);

        return holder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setVisibility(Context context, ReviewViewHolder holder, int position) {
        if (SessionHandler.isV4Login(context) && isProductOwner(context, position)) {
            holder.overFlow.setVisibility(View.VISIBLE);
        } else {
            holder.overFlow.setVisibility(View.GONE);
        }
    }

    private boolean isProductOwner(Context context, int position) {
        SessionHandler session = new SessionHandler(context);
        try {
            return String.valueOf(
                    ((ReviewProductModel) data.get(position)).getReviewProductOwner().getUserId())
                    .equals(session.getLoginID());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void resetLastPosition() {
        lastPosition = -1;
    }

    private View.OnClickListener OnUserNameClickListener(final String reviewUserId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(context, reviewUserId)
                );
            }
        };
    }

    public void setPresenter(ProductReviewPresenter presenter) {
        this.presenter = presenter;
    }
}
