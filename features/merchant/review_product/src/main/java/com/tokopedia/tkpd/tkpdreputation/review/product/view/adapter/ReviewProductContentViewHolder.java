package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ImageAttachmentUiModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.util.TimeUtil;
import com.tokopedia.unifycomponents.HtmlLinkHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductContentViewHolder extends AbstractViewHolder<ReviewProductModelContent> {
    public static final int LAYOUT = R.layout.item_product_review;
    private static final int MAX_CHAR = 50;
    public static final int UNLIKE_STATUS = 3;
    public static final int LIKE_STATUS_ACTIVE = 1;
    public static final String WIB = "WIB";
    public static final String TARGET = "WIB";

    private static final int MENU_REPORT = 102;
    private static final int MENU_DELETE = 103;

    boolean isReplyOpened = false;
    private ListenerReviewHolder viewListener;

    private TextView reviewerName;
    private TextView reviewTime;
    private RecyclerView reviewAttachment;
    private ImageView reviewOverflow;
    private TextView review;
    private RatingBar reviewStar;
    private ImageUploadAdapter adapter;

    private View replyReviewLayout;
    private TextView seeReplyText;
    private ImageView replyArrow;

    private TextView sellerName;
    private TextView sellerReplyTime;
    private TextView sellerReply;
    private ImageView replyOverflow;
    private ImageView iconLike;
    private TextView counterLike;
    private View containerReplyView;
    private View containerLike;
    private Context context;

    public ReviewProductContentViewHolder(View itemView, ListenerReviewHolder viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        this.context = itemView.getContext();
        reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
        reviewTime = (TextView) itemView.findViewById(R.id.review_time);
        reviewAttachment = (RecyclerView) itemView.findViewById(R.id.product_review_image);
        reviewOverflow = (ImageView) itemView.findViewById(R.id.review_overflow);
        review = (TextView) itemView.findViewById(R.id.review);
        reviewStar = (RatingBar) itemView.findViewById(R.id.product_rating);
        adapter = ImageUploadAdapter.createAdapter(itemView.getContext());
        adapter.setReviewImage(true);
        adapter.setCanUpload(false);
        reviewAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        reviewAttachment.setAdapter(adapter);
        seeReplyText = (TextView) itemView.findViewById(R.id.see_reply_button);
        replyArrow = (ImageView) itemView.findViewById(R.id.reply_chevron);

        replyReviewLayout = itemView.findViewById(R.id.reply_review_layout);
        sellerName = (TextView) itemView.findViewById(R.id.seller_reply_name);
        sellerReplyTime = (TextView) itemView.findViewById(R.id.seller_reply_time);
        sellerReply = (TextView) itemView.findViewById(R.id.seller_reply);
        replyOverflow = (ImageView) itemView.findViewById(R.id.reply_overflow);
        iconLike = itemView.findViewById(R.id.icon_like);
        counterLike = itemView.findViewById(R.id.text_counter_like);
        containerReplyView = itemView.findViewById(R.id.container_reply_view);
        containerLike = itemView.findViewById(R.id.container_like);
    }

    @Override
    public void bind(final ReviewProductModelContent element) {
        adapter.setListener(onImageClicked(element));

        reviewerName.setText(getReviewerNameText(element));
        reviewerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!element.isReviewIsAnonymous() || element.isSellerRepliedOwner()) {
                    viewListener.onGoToProfile(element.getReviewerId(), getAdapterPosition());
                }
            }
        });
        containerReplyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onSeeReplied(getAdapterPosition());
                toggleReply(element);
            }
        });
        reviewTime.setText(TimeUtil.generateTimeYearly(element.getReviewTime().replace(WIB, "")));

        reviewStar.setRating(element.getReviewStar());
        review.setText(getReview(element.getReviewMessage()));
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review.getText().toString().endsWith(context.getString(R.string.more_to_complete))) {
                    review.setText(MethodChecker.fromHtml(element.getReviewMessage()));
                }
            }
        });

        if (element.isReviewCanReported()) {
            reviewOverflow.setVisibility(View.VISIBLE);
            reviewOverflow.setOnClickListener(onReviewOverflowClicked(element));
        }else{
            reviewOverflow.setVisibility(View.GONE);
        }

        initReplyViewState(element);

        if (element.isReviewHasReplied()) {
            setSellerReply(element);
        } else {
            replyReviewLayout.setVisibility(View.GONE);
            seeReplyText.setVisibility(View.GONE);
            replyArrow.setVisibility(View.GONE);
        }

        containerLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onLikeDislikePressed(element.getReviewId(), element.isLikeStatus() ? UNLIKE_STATUS : LIKE_STATUS_ACTIVE, element.getProductId(),
                        element.isLikeStatus(), getAdapterPosition());
                element.setLikeStatus(!element.isLikeStatus());
                element.setTotalLike(element.isLikeStatus() ? element.getTotalLike() + 1 : element.getTotalLike() - 1);
                setLikeStatus(element);
            }
        });
        setLikeStatus(element);

        if(element.getReviewAttachment() != null && !element.getReviewAttachment().isEmpty()) {
            reviewAttachment.setVisibility(View.VISIBLE);
        }else{
            reviewAttachment.setVisibility(View.GONE);
        }
        adapter.addList(convertToAdapterViewModel(element.getReviewAttachment()));
        adapter.notifyDataSetChanged();
    }

    void setLikeStatus(ReviewProductModelContent element) {
        if (element.isLikeStatus()) {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_pressed));
        } else {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_normal));
        }
        if (element.isLogin()) {
            if (element.isLikeStatus() && element.getTotalLike() > 1) {
                counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_1_formatted, element.getTotalLike() - 1));
            } else if (element.isLikeStatus() && element.getTotalLike() == 1) {
                counterLike.setText(R.string.product_review_label_counter_like_2_formatted);
            } else if (!element.isLikeStatus() && element.getTotalLike() < 1) {
                counterLike.setText(R.string.product_review_label_counter_like_3_formatted);
            } else {
                counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_4_formatted, element.getTotalLike()));
            }
        } else {
            counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_4_formatted, element.getTotalLike()));
        }
    }

    private ArrayList<ImageUpload> convertToAdapterViewModel(List<ImageAttachmentUiModel> reviewAttachment) {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ImageAttachmentUiModel vm : reviewAttachment) {
            list.add(new ImageUpload(
                    vm.getUriThumbnail(),
                    vm.getUriLarge(),
                    vm.getDescription(),
                    String.valueOf(vm.getAttachmentId())));
        }
        return list;
    }

    private ImageUploadAdapter.ProductImageListener onImageClicked(ReviewProductModelContent element) {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.goToPreviewImage(position, adapter.getList(), element);
                    }
                };
            }
        };
    }

    private void setSellerReply(final ReviewProductModelContent element) {
        seeReplyText.setVisibility(View.VISIBLE);
        replyArrow.setVisibility(View.VISIBLE);

        sellerName.setText(element.getSellerName());
        sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopInfo(element.getShopId());
            }
        });
        sellerReplyTime.setText(TimeUtil.generateTimeYearly(element.getResponseCreateTime().replace(WIB, "")));
        sellerReply.setText(MethodChecker.fromHtml(element.getResponseMessage()));
        if (element.isSellerRepliedOwner()) {
            replyOverflow.setVisibility(View.VISIBLE);
            replyOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                    popup.getMenu().add(1, MENU_DELETE, 1,
                            context
                                    .getString(R.string.menu_delete));

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == MENU_DELETE) {
                                viewListener.onDeleteReviewResponse(element, getAdapterPosition());
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    popup.show();

                }
            });
        } else {
            replyOverflow.setVisibility(View.GONE);
        }
    }

    private void toggleReply(ReviewProductModelContent element) {
        element.setReplyOpened(!element.isReplyOpened());
        initReplyViewState(element);
    }

    private void initReplyViewState(ReviewProductModelContent element) {
        if (element.isReplyOpened()) {
            seeReplyText.setText(context.getText(R.string.close_reply));
            replyArrow.setRotation(180);
            replyReviewLayout.setVisibility(View.VISIBLE);
            viewListener.onSmoothScrollToReplyView(getAdapterPosition());
        } else {
            seeReplyText.setText(context.getText(R.string.see_reply));
            replyArrow.setRotation(0);
            replyReviewLayout.setVisibility(View.GONE);
        }
    }

    private CharSequence getReview(String review) {
        if (MethodChecker.fromHtml(review).length() > MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR);
            return new HtmlLinkHelper(context, subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                    + context.getString(R.string.review_expand)).getSpannedString();
        } else {
            return MethodChecker.fromHtml(review);
        }
    }

    private String getReviewerNameText(ReviewProductModelContent element) {
        if (element.isReviewIsAnonymous() && !element.isSellerRepliedOwner()) {
            return getAnonymousName(element.getReviewerName());
        } else {
            return element.getReviewerName();
        }
    }

    private String getAnonymousName(String name) {
        String first = name.substring(0, 1);
        String last = name.substring(name.length() - 1);
        return first + "***" + last;
    }

    private View.OnClickListener onReviewOverflowClicked(final ReviewProductModelContent element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onMenuClicked(getAdapterPosition());
                PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                popup.getMenu().add(1, MENU_REPORT, 2, v.getContext().getString(R.string.menu_report));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == MENU_REPORT) {
                            viewListener.onGoToReportReview(
                                    element.getShopId(),
                                    element.getReviewId(),
                                    getAdapterPosition()
                            );
                            return true;
                        }
                        return false;
                    }

                });
                popup.show();

            }
        };
    }

    public interface ListenerReviewHolder {
        void onGoToProfile(String reviewerId, int adapterPosition);

        void goToPreviewImage(int position, ArrayList<ImageUpload> list, ReviewProductModelContent element);

        void onGoToShopInfo(String shopId);

        void onDeleteReviewResponse(ReviewProductModelContent element, int adapterPosition);

        void onSmoothScrollToReplyView(int adapterPosition);

        void onGoToReportReview(String shopId, String reviewId, int adapterPosition);

        void onLikeDislikePressed(String reviewId, int likeStatus, String productId, boolean status, int adapterPosition);

        void onMenuClicked(int adapterPosition);

        void onSeeReplied(int adapterPosition);
    }
}
