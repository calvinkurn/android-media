package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.review.R;
import com.tokopedia.review.common.util.TimeConverter;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShopReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.UserReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationViewHolder extends AbstractViewHolder<InboxReputationItemUiModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_item;
    private final InboxReputation.View viewListener;

    private View mainView;
    private Typography textDeadline;
    private Typography deadline;
    private Typography invoice;
    private ImageView avatar;
    private Typography name;
    private UserReputationView userReputationView;
    private ShopReputationView shopReputationView;
    private Typography date;
    private Typography action;
    private ImageView unreadNotification;
    private Context context;

    public InboxReputationViewHolder(Context context, View itemView, InboxReputation.View viewListener) {
        super(itemView);
        mainView = itemView.findViewById(R.id.main_view);
        textDeadline = itemView.findViewById(R.id.deadline_text);
        deadline = itemView.findViewById(R.id.label_deadline);
        invoice = itemView.findViewById(R.id.invoice);
        avatar = itemView.findViewById(R.id.avatar);
        name = itemView.findViewById(R.id.name);
        userReputationView =  itemView.findViewById(R.id.user_reputation);
        shopReputationView = itemView.findViewById(R.id.shop_reputation);
        date = itemView.findViewById(R.id.date);
        action = itemView.findViewById(R.id.action);
        unreadNotification = itemView.findViewById(R.id.unread_notif);
        this.viewListener = viewListener;
        this.context = context;


    }

    @Override
    public void bind(final InboxReputationItemUiModel element) {

        name.setText(MethodChecker.fromHtml(element.getRevieweeName()));
        date.setText(getDate(element.getCreateTime()));
        invoice.setText(element.getInvoice());
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getRevieweePicture());
        setDeadline(element);
        setReputation(element);
        setAction(element);
        setUnreadNotification(element);

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToDetail(
                        element.getReputationId(),
                        element.getInvoice(),
                        element.getCreateTime(),
                        element.getRevieweeName(),
                        element.getRevieweePicture(),
                        element.getReputationDataUiModel(),
                        getTextDeadline(element),
                        getAdapterPosition(),
                        element.getRole());
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToDetail(
                        element.getReputationId(),
                        element.getInvoice(),
                        element.getCreateTime(),
                        element.getRevieweeName(),
                        element.getRevieweePicture(),
                        element.getReputationDataUiModel(),
                        getTextDeadline(element),
                        getAdapterPosition(),
                        element.getRole());
            }
        });
    }

    private void setUnreadNotification(InboxReputationItemUiModel element) {
        if (element.getReputationDataUiModel().isShowBookmark()) {
            unreadNotification.setVisibility(View.VISIBLE);
        } else {
            unreadNotification.setVisibility(View.GONE);
        }
    }

    private String getDate(String createTime) {
       return TimeConverter.generateTimeYearly(createTime);
    }


    private void setAction(InboxReputationItemUiModel inboxReputationItemUiModel) {
        action.setText(inboxReputationItemUiModel.getReputationDataUiModel().getActionMessage());
    }

    private void setReputation(InboxReputationItemUiModel element) {
        if (element.getRole() == InboxReputationItemUiModel.ROLE_BUYER) {
            userReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setVisibility(View.GONE);
            userReputationView.setValue(
                    element.getRevieweeBadgeCustomerUiModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerUiModel().getNoReputation() == 1,
                    element.getRevieweeBadgeCustomerUiModel().getPositive(),
                    element.getRevieweeBadgeCustomerUiModel().getNeutral(),
                    element.getRevieweeBadgeCustomerUiModel().getNegative()
            );
        } else {
            userReputationView.setVisibility(View.GONE);
            shopReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setValue(
                    element.getRevieweeBadgeSellerUiModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerUiModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerUiModel().getScore()));

        }
    }

    private void setDeadline(InboxReputationItemUiModel element) {
        if (element.getReputationDataUiModel().isShowLockingDeadline()) {
            deadline.setVisibility(View.VISIBLE);
            textDeadline.setVisibility(View.VISIBLE);
            setIconDeadline(deadline, element.getReputationDaysLeft());
        } else {
            deadline.setVisibility(View.INVISIBLE);
            textDeadline.setVisibility(View.INVISIBLE);
        }
    }

    private String getTextDeadline(InboxReputationItemUiModel element) {
        return context.getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                context.getString(R.string.deadline_suffix);
    }

    private void setIconDeadline(Typography deadline, String reputationDaysLeft) {
        deadline.setText(reputationDaysLeft + " " + context.getString(R.string.deadline_suffix));

        Drawable background = MethodChecker.getDrawable(context, R.drawable.custom_label);

        switch (reputationDaysLeft) {
            case "1":
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
            case "2":
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
            default:
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_B400),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
        }

        MethodChecker.setBackground(deadline, background);
    }

}
