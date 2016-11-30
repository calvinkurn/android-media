package com.tokopedia.core.inboxreputation.adapter.viewbinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.inboxreputation.activity.InboxReputationDetailActivity;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.ToolTipUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/26/16.
 */
public class HeaderReputationDataBinder extends DataBinder<HeaderReputationDataBinder.ViewHolder> {

    public static final String STATUS_BAD = "smiley_bad";
    public static final String STATUS_NEUTRAL = "smiley_neutral";
    public static final String STATUS_GOOD = "smiley_good";
    public static final String STATUS_HIDDEN = "blue_question_mark";
    public static final String STATUS_UNASSESSED = "smiley_none";

    public static final String SCORE_BAD = "-1";
    public static final String SCORE_NEUTRAL = "1";
    public static final String SCORE_GOOD = "2";


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.username)
        TextView username;

        @BindView(R2.id.invoice)
        TextView headerInvoice;

        @BindView(R2.id.avatar)
        ImageView avatar;

        @BindView(R2.id.smiley_bad)
        ImageView smileyBad;

        @BindView(R2.id.view_smiley_bad)
        View viewBad;

        @BindView(R2.id.smiley_neutral)
        ImageView smileyNeutral;

        @BindView(R2.id.view_smiley_neutral)
        View viewNeutral;

        @BindView(R2.id.smiley_good)
        ImageView smileyGood;

        @BindView(R2.id.smiley_good_title)
        TextView smileyGoodTitle;

        @BindView(R2.id.reviewee_reputation_smiley)
        ImageView smileyReviewee;

        @BindView(R2.id.rep_rating)
        TextView textPercentage;

        @BindView(R2.id.rep_icon)
        ImageView iconPercentage;

        @BindView(R2.id.reputation_holder)
        LinearLayout reputationLabel;

        @BindView(R2.id.reputation_holder_user)
        View viewPercentage;

        @BindView(R2.id.reviewer_reputation_title)
        TextView reviewerTitle;

        @BindView(R2.id.reviewee_reputation_title_edited)
        TextView revieweeTitleEdited;

        @BindView(R2.id.reviewer_reputation_title_edited)
        TextView reviewerTitleEdited;

        @BindView(R2.id.give_reputation_container)
        RelativeLayout viewRevieweeReputation;

        @BindView(R2.id.reviewer_reputation_container)
        RelativeLayout viewReviewerReputation;

        @BindView(R2.id.deadline_view)
        View viewDeadline;

        @BindView(R2.id.deadline)
        TextView deadline;

        LabelUtils label;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HeaderReputationDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    private static final int BUYER = 1;
    private static final int SELLER = 2;

    private InboxReputationItem inboxReputation;
    private Context context;
    private InboxReputationDetailFragmentPresenter presenter;


    @Override
    public HeaderReputationDataBinder.ViewHolder newViewHolder(ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_header_reputation_detail_2, viewGroup, false));

        holder.headerInvoice.setOnClickListener(onInvoiceClickListener(holder));
        holder.username.setOnClickListener(onGoToProfile());
        holder.avatar.setOnClickListener(onGoToProfile());
        holder.viewPercentage.setOnClickListener(onLabelReputationClickListener());
        holder.smileyBad.setOnClickListener(onButtonSmileyClickListener(STATUS_BAD));
        holder.smileyNeutral.setOnClickListener(onButtonSmileyClickListener(STATUS_NEUTRAL));
        holder.smileyGood.setOnClickListener(onButtonSmileyClickListener(STATUS_GOOD));
        holder.smileyReviewee.setOnClickListener(onSmileyFromOpponentClickListener());

        return holder;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.headerInvoice.setText(inboxReputation.getInvoiceRefNum());
        ImageHandler.loadImageCircle2(context, holder.avatar, inboxReputation.getRevieweeImageUrl());
        holder.username.setText(inboxReputation.getRevieweeName());
        holder.label = LabelUtils.getInstance(context, holder.username);
        holder.label.giveSquareLabel(inboxReputation.getLabel());

        switch (inboxReputation.getRevieweeRole()) {
            case SELLER:
                showReputationSeller(holder);
                setReputationTitle(holder, context.getString(R.string.reputation_system_sub_for_seller));
                break;
            case BUYER:
                setReputationTitle(holder, context.getString(R.string.reputation_system_sub_for_buyer));
                showReputationBuyer(holder);

                break;
        }

        setReviewerSmiley(holder);
        setRevieweeSmiley(holder);
        setDeadline(holder);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private View.OnClickListener onGoToProfile() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onUsernameClicked(inboxReputation);
            }
        };
    }

    private View.OnClickListener onInvoiceClickListener(final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.InvoiceDialog((InboxReputationDetailActivity) context, inboxReputation.getInvoiceUri(),
                        inboxReputation.getInvoiceRefNum(),
                        holder.headerInvoice.getText().toString());
            }
        };
    }

    private View.OnClickListener onButtonSmileyClickListener(final String status) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (inboxReputation.getRevieweeScore()) {
                    case STATUS_BAD: {
                        switch (status) {
                            case STATUS_BAD:
                                break;
                            case STATUS_NEUTRAL:
                                showDialogReputationForm(status);
                                break;
                            case STATUS_GOOD:
                                showDialogReputationForm(status);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    case STATUS_NEUTRAL: {
                        switch (status) {
                            case STATUS_BAD:
                                showErrorLowerReputationForm();
                                break;
                            case STATUS_NEUTRAL:
                                showDialogReputationForm(status);
                                break;
                            case STATUS_GOOD:
                                showDialogReputationForm(status);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    case STATUS_GOOD:
                        break;
                    default:
                        if (inboxReputation.getReputationProgress() == 2 || inboxReputation.getReputationProgress() == 3) {
                            showLockedDialog();
                        } else {
                            showDialogReputationForm(status);
                        }
                        break;
                }
            }
        };
    }

    private void showErrorLowerReputationForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(initViewDialog(context.getString(R.string.error_lower_reputation)));
        builder.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int param) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void showDialogReputationForm(final String status) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(initViewDialog(presenter.getMessageForSmileyForOpponent(status)));
        builder.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActReviewPass param = presenter.getSmileyParam(inboxReputation,status);
                        presenter.postReputation(param);
                    }
                });
        builder.setNegativeButton(context.getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int param) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


    }

    private View.OnClickListener onSmileyFromOpponentClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(initViewDialog(presenter.getMessageForSmileyFromOpponent(
                        inboxReputation)));
                builder.setPositiveButton(context.getString(R.string.title_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int param) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        };
    }

    private View initViewDialog(String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialog = inflater.inflate(R.layout.view_dialog_message_reputation, null);
        TextView messageReputation = (TextView) viewDialog.findViewById(R.id.message_reputation);
        messageReputation.setText(message);
        return viewDialog;
    }

    private void showLockedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(initViewDialog(context.getString(R.string.msg_reputation_locked)));
        builder.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int param) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private View.OnClickListener onLabelReputationClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(), v);
            }
        };
    }

    private View setViewToolTip() {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user,
                new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        TextView smile = (TextView) view.findViewById(R.id.text_smile);
                        TextView netral = (TextView) view.findViewById(R.id.text_netral);
                        TextView bad = (TextView) view.findViewById(R.id.text_bad);
                        smile.setText(String.valueOf(inboxReputation.getUserReputation().getPositive()));
                        netral.setText(String.valueOf(inboxReputation.getUserReputation().getNeutral()));
                        bad.setText(String.valueOf(inboxReputation.getUserReputation().getNegative()));
                    }

                    @Override
                    public void setListener() {

                    }
                });
    }

    private void setReviewerSmiley(ViewHolder holder) {
        if (isReviewerReputationRead()) {
            holder.viewReviewerReputation.setBackgroundColor(context.getResources()
                    .getColor(R.color.white));
        } else {
            holder.viewReviewerReputation.setBackgroundColor(context.getResources()
                    .getColor(R.color.yellow_50));

        }
        holder.smileyReviewee.setImageResource(presenter.getSmiley(inboxReputation.getReviewerScore()));
    }

    private void setRevieweeSmiley(ViewHolder holder) {

        switch (inboxReputation.getRevieweeScore())

        {
            case STATUS_BAD:
                holder.smileyBad.setImageResource(R.drawable.ic_icon_repsis_sad_active);
                holder.smileyNeutral.setImageResource(R.drawable.ic_icon_repsis_neutral);
                holder.smileyGood.setImageResource(R.drawable.ic_icon_repsis_smile);
                break;
            case STATUS_NEUTRAL:
                holder.smileyBad.setImageResource(R.drawable.ic_icon_repsis_sad);
                holder.smileyNeutral.setImageResource(R.drawable.ic_icon_repsis_neutral_active);
                holder.smileyGood.setImageResource(R.drawable.ic_icon_repsis_smile);
                break;
            case STATUS_GOOD:
                holder.viewBad.setVisibility(View.GONE);
                holder.viewNeutral.setVisibility(View.GONE);
                holder.smileyGood.setImageResource(R.drawable.ic_icon_repsis_smile_active);
                break;
            default:
                if (inboxReputation.getReputationProgress() == 2 || inboxReputation.getReputationProgress() == 3) {
                    showRevieweeSmileLocked(holder);
                } else {
                    holder.smileyBad.setImageResource(R.drawable.ic_icon_repsis_sad);
                    holder.smileyNeutral.setImageResource(R.drawable.ic_icon_repsis_neutral);
                    holder.smileyGood.setImageResource(R.drawable.ic_icon_repsis_smile);
                }
                break;
        }

    }


    private boolean isRevieweeHasReputation() {
        return !inboxReputation.getRevieweeScore().equals(STATUS_UNASSESSED);
    }

    private boolean isReviewerReputationRead() {
        return inboxReputation.isSmileyReviewerRead();
    }

    private boolean isRevieweeReputationEdited() {
        return inboxReputation.getIsRevieweeScoreEdited();
    }

    private boolean isReviewerReputationEdited() {
        return inboxReputation.getIsReviewerScoreEdited();
    }

    private void showRevieweeSmileLocked(ViewHolder holder) {
        holder.viewBad.setVisibility(View.GONE);
        holder.viewNeutral.setVisibility(View.GONE);
        holder.smileyGood.setImageResource(R.drawable.ic_locked);
        holder.smileyGoodTitle.setText(context.getString(R.string.title_reputation_locked));

    }

    private void setReputationTitle(ViewHolder holder, String title) {
        if (isRevieweeReputationEdited()) {
            holder.revieweeTitleEdited.setVisibility(View.VISIBLE);
        } else {
            holder.revieweeTitleEdited.setVisibility(View.GONE);

        }
        if (isReviewerReputationEdited()) {
            holder.reviewerTitleEdited.setVisibility(View.VISIBLE);
        } else {
            holder.reviewerTitleEdited.setVisibility(View.GONE);

        }
        holder.reviewerTitle.setText(title);

    }

    private void setIconPercentage(ViewHolder holder) {
        if (inboxReputation.getUserReputation().getNoReputation().equals("0")) {
            setHasReputation(holder);
        } else {
            setNoReputation(holder);

        }
    }

    private void setHasReputation(ViewHolder holder) {
        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
        holder.textPercentage.setVisibility(View.VISIBLE);
    }

    private void setNoReputation(ViewHolder holder) {
        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
        holder.textPercentage.setVisibility(View.GONE);
    }

    private void showReputationSeller(ViewHolder holder) {
        holder.viewPercentage.setVisibility(View.GONE);
        holder.reputationLabel.setVisibility(View.VISIBLE);
        ReputationLevelUtils.setReputationMedals(context, holder.reputationLabel,
                inboxReputation.getShopBadgeLevel().getSet(),
                inboxReputation.getShopBadgeLevel().getLevel(),
                inboxReputation.getReputationScore());

    }

    private void showReputationBuyer(ViewHolder holder) {
        holder.viewPercentage.setVisibility(View.VISIBLE);
        holder.reputationLabel.setVisibility(View.GONE);
        holder.textPercentage.setText(inboxReputation.getUserReputation().getPositivePercentage());
        setIconPercentage(holder);
    }


    private void setDeadline(ViewHolder holder) {
        if (inboxReputation.getCanShowReputationDay()) {
            holder.viewDeadline.setVisibility(View.VISIBLE);
            String deadlineText = context.getString(R.string.deadline_prompt) +
                    inboxReputation.getReputationDaysLeft() + " " + context.getString(R.string.more);
            holder.deadline.setText(deadlineText);
        } else {
            holder.viewDeadline.setVisibility(View.GONE);
        }
    }

    public void setData(InboxReputationItem inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public void setPresenter(InboxReputationDetailFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
