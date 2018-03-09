package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.customView.ReputationRecyclerView;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.StarGenerator;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.Const;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.activity.ReputationProductActivity;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ProductReviewPresenter;

import java.util.ArrayList;
import java.util.List;

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
        LinearLayout container;
        ImageView avatar;
        LinearLayout starQuality;
        LinearLayout starAccuracy;
        TextView username;
        TextView comment;
        TextView date;
        ImageView overFlow;
        ReputationRecyclerView imageHolder;

        ImageUploadAdapter adapter;

        ReviewViewHolder(View view) {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.container);
            avatar = (ImageView) view.findViewById(R.id.user_avatar);
            starQuality = (LinearLayout) view.findViewById(R.id.star_quality);
            starAccuracy = (LinearLayout) view.findViewById(R.id.star_accuracy);
            username = (TextView) view.findViewById(R.id.username);
            comment = (TextView) view.findViewById(R.id.comment);
            date = (TextView) view.findViewById(R.id.date);
            overFlow = (ImageView) view.findViewById(R.id.btn_overflow);
            imageHolder = (ReputationRecyclerView) view.findViewById(R.id.image_holder);
        }
    }

    public static class MostHelpfulViewHolder extends RecyclerView.ViewHolder {
        LinearLayout[] container = new LinearLayout[3];
        ImageView[] avatar = new ImageView[3];
        LinearLayout[] starQuality = new LinearLayout[3];
        LinearLayout[] starAccuracy = new LinearLayout[3];
        TextView[] username = new TextView[3];
        TextView[] comment = new TextView[3];
        TextView[] date = new TextView[3];
        ImageView[] overFlow = new ImageView[3];
        TextView expand;
        TextView collapse;
        ReputationRecyclerView imageHolder;

        ImageUploadAdapter adapter;

        MostHelpfulViewHolder(View view) {
            super(view);
            container[0] = (LinearLayout) view.findViewById(R.id.container1);
            container[1] = (LinearLayout) view.findViewById(R.id.container2);
            container[2] = (LinearLayout) view.findViewById(R.id.container3);
            avatar[0] = (ImageView) view.findViewById(R.id.user_avatar1);
            avatar[1] = (ImageView) view.findViewById(R.id.user_avatar2);
            avatar[2] = (ImageView) view.findViewById(R.id.user_avatar3);
            starQuality[0] = (LinearLayout) view.findViewById(R.id.star_quality1);
            starQuality[1] = (LinearLayout) view.findViewById(R.id.star_quality2);
            starQuality[2] = (LinearLayout) view.findViewById(R.id.star_quality3);
            starAccuracy[0] = (LinearLayout) view.findViewById(R.id.star_accuracy1);
            starAccuracy[1] = (LinearLayout) view.findViewById(R.id.star_accuracy2);
            starAccuracy[2] = (LinearLayout) view.findViewById(R.id.star_accuracy3);
            username[0] = (TextView) view.findViewById(R.id.username1);
            username[1] = (TextView) view.findViewById(R.id.username2);
            username[2] = (TextView) view.findViewById(R.id.username3);
            comment[0] = (TextView) view.findViewById(R.id.comment1);
            comment[1] = (TextView) view.findViewById(R.id.comment2);
            comment[2] = (TextView) view.findViewById(R.id.comment3);
            date[0] = (TextView) view.findViewById(R.id.date1);
            date[1] = (TextView) view.findViewById(R.id.date2);
            date[2] = (TextView) view.findViewById(R.id.date3);
            overFlow[0] = (ImageView) view.findViewById(R.id.btn_overflow1);
            overFlow[1] = (ImageView) view.findViewById(R.id.btn_overflow2);
            overFlow[2] = (ImageView) view.findViewById(R.id.btn_overflow3);
            expand = (TextView) view.findViewById(R.id.expand);
            collapse = (TextView) view.findViewById(R.id.collapse);
            imageHolder = (ReputationRecyclerView) view.findViewById(R.id.image_holder);
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
        return position == data.size();
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
        if (review.getImages().size() > 0) {
            holder.imageHolder.setVisibility(View.VISIBLE);
        } else {
            holder.imageHolder.setVisibility(View.GONE);
        }
        holder.adapter.addList(review.getImages());
        ImageHandler.loadImageCircle2(context, holder.avatar, review.getReviewUserImage());
        holder.username.setText(MethodChecker.fromHtml(review.getReviewUserName()).toString());
        holder.date.setText(review.getReviewCreateTime());
        holder.comment.setText(review.getReviewMessage());
        holder.comment.setMovementMethod(new SelectableSpannedMovementMethod());

        setVisibility(context, holder, position);
        StarGenerator.setReputationStars(context, holder.starAccuracy, review.getReviewRateAccuracy());
        StarGenerator.setReputationStars(context, holder.starQuality, review.getReviewRateProduct());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReputationProductActivity.class);
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
                        if (item.getItemId() == R.id.action_report) {
                            showDialogReport(review);
                            return true;
                        } else {
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
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setNegativeButton(context.getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                        if (userInput.length() > 0) {
                            String message = userInput.getText().toString();
                            ActReviewPass pass = new ActReviewPass();
                            pass.setReviewId(String.valueOf(review.getReviewId()));
                            pass.setShopId(review.getReviewShopId());
                            pass.setReportMessage(message);
                            presenter.reportReview(pass);
                            alertDialog.dismiss();
                        } else
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
            final com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review.HelpfulReview review = helpfulReviewList.getList().get(i);
            if (review.getImages().size() > 0) {
                holder.imageHolder.setVisibility(View.VISIBLE);
            } else {
                holder.imageHolder.setVisibility(View.GONE);
            }
            holder.adapter.addList(review.getImages());
            ImageHandler.loadImageCircle2(context, holder.avatar[i], review.getReviewUserImage());
            holder.username[i].setText(MethodChecker.fromHtml(review.getReviewUserName()).toString());
            holder.date[i].setText(review.getReviewCreateTime());
            holder.comment[i].setText(MethodChecker.fromHtml(review.getReviewMessage()));
            holder.comment[i].setMovementMethod(new SelectableSpannedMovementMethod());
            StarGenerator.setReputationStars(context, holder.starAccuracy[i], review.getReviewRateAccuracy());
            StarGenerator.setReputationStars(context, holder.starQuality[i], review.getReviewRateProduct());
//            -------------------------
//            holder.container[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ReputationProductActivity.class);
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
                        ((ReputationRouter) context.getApplicationContext())
                                .getTopProfileIntent(context, reviewUserId));
            }
        };
    }

    public void setPresenter(ProductReviewPresenter presenter) {
        this.presenter = presenter;
    }
}
