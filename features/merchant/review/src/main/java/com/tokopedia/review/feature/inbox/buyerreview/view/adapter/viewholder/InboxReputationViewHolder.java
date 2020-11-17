package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.review.R;
import com.tokopedia.review.common.util.TimeConverter;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShopReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.UserReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationViewHolder extends AbstractViewHolder<InboxReputationItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_item;
    private final InboxReputation.View viewListener;

    private View mainView;
    private TextView textDeadline;
    private TextView deadline;
    private TextView invoice;
    private ImageView avatar;
    private TextView name;
    private UserReputationView userReputationView;
    private ShopReputationView shopReputationView;
    private TextView date;
    private TextView action;
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
    public void bind(final InboxReputationItemViewModel element) {

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
                        element.getReputationDataViewModel(),
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
                        element.getReputationDataViewModel(),
                        getTextDeadline(element),
                        getAdapterPosition(),
                        element.getRole());
            }
        });
    }

    private void setUnreadNotification(InboxReputationItemViewModel element) {
        if (element.getReputationDataViewModel().isShowBookmark()) {
            unreadNotification.setVisibility(View.VISIBLE);
        } else {
            unreadNotification.setVisibility(View.GONE);
        }
    }

    private String getDate(String createTime) {
       return TimeConverter.generateTimeYearly(createTime);
    }


    private void setAction(InboxReputationItemViewModel inboxReputationItemViewModel) {
        action.setText(inboxReputationItemViewModel.getReputationDataViewModel().getActionMessage());
    }

    private void setReputation(InboxReputationItemViewModel element) {
        if (element.getRole() == InboxReputationItemViewModel.ROLE_BUYER) {
            userReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setVisibility(View.GONE);
            userReputationView.setValue(
                    element.getRevieweeBadgeCustomerViewModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerViewModel().getNoReputation() == 1,
                    element.getRevieweeBadgeCustomerViewModel().getPositive(),
                    element.getRevieweeBadgeCustomerViewModel().getNeutral(),
                    element.getRevieweeBadgeCustomerViewModel().getNegative()
            );
        } else {
            userReputationView.setVisibility(View.GONE);
            shopReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setValue(
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerViewModel().getScore()));

        }
    }

    private void setDeadline(InboxReputationItemViewModel element) {
        if (element.getReputationDataViewModel().isShowLockingDeadline()) {
            deadline.setVisibility(View.VISIBLE);
            textDeadline.setVisibility(View.VISIBLE);
            setIconDeadline(deadline, element.getReputationDaysLeft());
        } else {
            deadline.setVisibility(View.INVISIBLE);
            textDeadline.setVisibility(View.INVISIBLE);
        }
    }

    private String getTextDeadline(InboxReputationItemViewModel element) {
        return context.getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                context.getString(R.string.deadline_suffix);
    }

    private void setIconDeadline(TextView deadline, String reputationDaysLeft) {
        deadline.setText(reputationDaysLeft + " " + context.getString(R.string.deadline_suffix));

        Drawable background = MethodChecker.getDrawable(context, R.drawable.custom_label);

        switch (reputationDaysLeft) {
            case "1":
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.abstraction.R
                        .color.red_500),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
            case "2":
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.design.R
                        .color.orange_300),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
            default:
                background.setColorFilter(new
                        PorterDuffColorFilter(MethodChecker.getColor(context, com.tokopedia.design.R
                        .color.light_blue_300),
                        PorterDuff.Mode
                                .MULTIPLY));
                break;
        }

        MethodChecker.setBackground(deadline, background);
    }

}
