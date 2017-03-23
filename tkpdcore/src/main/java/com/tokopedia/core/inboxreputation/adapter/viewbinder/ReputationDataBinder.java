package com.tokopedia.core.inboxreputation.adapter.viewbinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.core.inboxreputation.adapter.InboxReputationDetailAdapter;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractor;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenter;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/26/16.
 */
public class ReputationDataBinder extends DataBinder<ReputationDataBinder.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_avatar)
        ImageView productAvatar;

        @BindView(R2.id.product_title)
        TextView productName;

        @BindView(R2.id.product_review_date)
        TextView productReviewDate;

        @BindView(R2.id.product_review)
        TextView productReview;

        @BindView(R2.id.star_quality)
        ImageView quality;

        @BindView(R2.id.star_accuracy)
        ImageView accuracy;

        @BindView(R2.id.btn_overflow)
        ImageView overflow;

        @BindView(R2.id.give_reply)
        View viewGiveReply;

        @BindView(R2.id.btn_give_reply)
        TextView btnGiveReply;

        @BindView(R2.id.view_review)
        View viewReview;

        @BindView(R2.id.view_seller_reply)
        View viewSellerReply;

        @BindView(R2.id.seller_avatar)
        ImageView sellerAvatar;

        @BindView(R2.id.seller_name)
        TextView sellerName;

        @BindView(R2.id.reputation_holder)
        LinearLayout reputationLabel;

        @BindView(R2.id.seller_reply)
        TextView sellerReply;

        @BindView(R2.id.seller_reply_date)
        TextView sellerReplyDate;

        @BindView(R2.id.btn_overflow_seller)
        ImageView overflowSeller;

        @BindView(R2.id.image_holder)
        RecyclerView imageHolder;

        @BindView(R2.id.share_but)
        TextView shareButton;

        LabelUtils label;
        ImageUploadAdapter adapter;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ReputationDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        inboxReputationDetail = new InboxReputationDetail();
        inboxReputationDetail.setInboxReputationDetailItem(new ArrayList<InboxReputationDetailItem>());
    }

    private static final int ACTION_EDIT = 1;
    private static final int ACTION_REPORT = 2;

    private InboxReputationDetail inboxReputationDetail;
    private InboxReputationItem inboxReputation;
    private Context context;
    private InboxReputationDetailFragmentPresenter presenter;
    private CacheInboxReputationInteractor cacheInboxReputationInteractor;

    @Override
    public ViewHolder newViewHolder(ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_inbox_detail_reputation_2, viewGroup, false));

        holder.adapter = ImageUploadAdapter.createAdapter(context);
        holder.adapter.setListener(onProductImageActionListener(holder.adapter.getList()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.imageHolder.setLayoutManager(layoutManager);
        holder.imageHolder.setAdapter(holder.adapter);
        cacheInboxReputationInteractor = new CacheInboxReputationInteractorImpl();
        return holder;
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener(
            final ArrayList<ImageUpload> list) {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position,
                                                       final ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onPreviewImageClicked(position, list);
                    }
                };
            }
        };
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        setHeaderProduct(holder, position);
        holder.productReview.setText(inboxReputationDetail.getInboxReputationDetailItemList()
                .get(position).getReviewMessage());
        holder.productReview.setMovementMethod(new SelectableSpannedMovementMethod());
        holder.shareButton.setVisibility(View.VISIBLE);
        setImage(holder, position);
        setRating(holder, position);
        setResponse(holder, position);
        setListener(holder, position);
        setReadData(position);
    }

    private void setReadData(int position) {
        inboxReputationDetail.getInboxReputationDetailItemList().get(position).setIsRead(1);
        inboxReputationDetail.getInboxReputationDetailItemList().get(position)
                .getReviewResponse().setIsResponseRead(1);
        cacheInboxReputationInteractor.getInboxReputationDetailCache(inboxReputation.getReputationId(),
                new CacheInboxReputationInteractor.GetInboxReputationDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxReputationDetail inboxReputationDetail) {
                        inboxReputationDetail.setInboxReputationDetailItem(inboxReputationDetail
                                .getInboxReputationDetailItemList());
                        cacheInboxReputationInteractor.setInboxReputationDetailCache(inboxReputation.getReputationId(),
                                inboxReputationDetail);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private boolean isReviewRead(int position) {
        return inboxReputationDetail.getInboxReputationDetailItemList().get(position).getIsRead();
    }

    private void setResponse(ViewHolder holder, int position) {
        if (isHasResponse(position)) {
            holder.viewGiveReply.setVisibility(View.GONE);
            holder.viewSellerReply.setVisibility(View.VISIBLE);
            setSellerResponse(holder, position);
        } else if (!isHasResponse(position) && !roleIsBuyer()) {
            holder.viewGiveReply.setVisibility(View.VISIBLE);
            holder.viewSellerReply.setVisibility(View.GONE);
        } else {
            holder.viewGiveReply.setVisibility(View.GONE);
            holder.viewSellerReply.setVisibility(View.GONE);
        }
    }

    private boolean roleIsBuyer() {
        return inboxReputation.getRole() == InboxReputationDetailAdapter.BUYER;
    }

    private boolean isHasResponse(int position) {
        return !inboxReputationDetail.getInboxReputationDetailItemList().get(position)
                .getReviewResponse().getResponseMessage().toString().equals("0");
    }

    private void setRating(ViewHolder holder, int position) {
        holder.accuracy.setImageResource(generateRating(inboxReputationDetail
                .getInboxReputationDetailItemList().get(position)
                .getProductAccuracyRating()));
        holder.quality.setImageResource(generateRating(inboxReputationDetail
                .getInboxReputationDetailItemList().get(position)
                .getProductQualityRating()));
    }

    @Override
    public int getItemCount() {
        return inboxReputationDetail.getInboxReputationDetailItemList().size();
    }

    private void setImage(ViewHolder holder, int position) {
        if (isHasUploadedImage(position)) {
            holder.imageHolder.setVisibility(View.VISIBLE);
            holder.adapter.addList(inboxReputationDetail.getInboxReputationDetailItemList()
                    .get(position).getImages());
        } else {
            holder.imageHolder.setVisibility(View.GONE);
        }
    }

    private boolean isHasUploadedImage(int position) {
        return inboxReputationDetail.getInboxReputationDetailItemList().get(position)
                .getReviewImageList().size() != 0;
    }

    private void setHeaderProduct(ViewHolder holder, int position) {
        holder.productName.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductName());
        holder.productReviewDate.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewPostTime());
        ImageHandler.LoadImage(holder.productAvatar, inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductImageUrl());
        setOverFlow(holder, position);
    }

    private void setSellerResponse(ViewHolder holder, int position) {

        holder.sellerName.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductOwner().getShopName());
        holder.label = LabelUtils.getInstance(context, holder.sellerName);
        holder.label.giveSquareLabel(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductOwner().getUserLabel());
        ReputationLevelUtils.setReputationMedals(context, holder.reputationLabel,
                inboxReputationDetail.getInboxReputationDetailItemList().get(position).getShopBadgeLevel().getSet(),
                inboxReputationDetail.getInboxReputationDetailItemList().get(position).getShopBadgeLevel().getLevel(),
                inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductOwner().getShopReputationScore());
        ImageHandler.loadImageCircle2(context, holder.sellerAvatar,
                inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductOwner().getShopImg());
        holder.sellerReply.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewResponse().getResponseMessage());
        holder.sellerReply.setMovementMethod(new SelectableSpannedMovementMethod());
        holder.sellerReplyDate.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewResponse().getResponseTime());

        if (roleIsBuyer()) {
            holder.overflowSeller.setVisibility(View.GONE);
        } else {
            holder.overflowSeller.setVisibility(View.VISIBLE);
        }

    }

    private boolean isResponseRead(int position) {
        return inboxReputationDetail.getInboxReputationDetailItemList().get(position).getIsRead();
    }

    private int generateRating(int paramStar) {
        int RatingID = 0;
        switch (paramStar) {
            case 0:
                RatingID = R.drawable.ic_star_none;
                break;
            case 1:
                RatingID = R.drawable.ic_star_one;
                break;
            case 2:
                RatingID = R.drawable.ic_star_two;
                break;
            case 3:
                RatingID = R.drawable.ic_star_three;
                break;
            case 4:
                RatingID = R.drawable.ic_star_four;
                break;
            case 5:
                RatingID = R.drawable.ic_star_five;
                break;
        }
        return RatingID;
    }

    private void setOverFlow(ViewHolder holder, int position) {
        switch (getStatusOverFlow(position)) {
            case ACTION_EDIT:
                holder.overflow.setVisibility(View.VISIBLE);
                break;
            case ACTION_REPORT:
                holder.overflow.setVisibility(View.VISIBLE);
                break;
            default:
                holder.overflow.setVisibility(View.GONE);
                break;
        }
    }

    private int getStatusOverFlow(int position) {
        if (isEditaAble(position)) {
            return ACTION_EDIT;
        } else if (isReportAble(position)) {
            return ACTION_REPORT;
        } else {
            return 0;
        }
    }

    public boolean isProductHasReview(int position) {
        return (!inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewMessage().equals(""));
    }

    private boolean isEditaAble(int position) {
        return (isProductHasReview(position) && inboxReputationDetail.getInboxReputationDetailItemList().get(position).getIsEditable());
    }

    private boolean isReportAble(int position) {
        return (isProductOwner(position) && isProductHasReview(position));
    }

    private boolean isProductOwner(int position) {
        return inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductOwner().getUserId()
                .equals(SessionHandler.getLoginID(context));
    }

    private void setListener(ViewHolder holder, int position) {
        holder.overflow.setOnClickListener(onOverflowClickListener(position));
        holder.overflowSeller.setOnClickListener(onSellerOverflowClicked(position));
        holder.btnGiveReply.setOnClickListener(onGiveReplyListener(position));
        holder.productAvatar.setOnClickListener(onGoToProduct(position));
        holder.sellerAvatar.setOnClickListener(onGoToShop(position));
        holder.sellerName.setOnClickListener(onGoToShop(position));
        holder.shareButton.setOnClickListener(showShareProvider(position));
    }

    private View.OnClickListener showShareProvider(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showShareProvider(inboxReputationDetail.getInboxReputationDetailItemList().get(position));
            }
        };
    }

    private View.OnClickListener onGoToShop(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopInfoActivity.class);
                Bundle bundle = ShopInfoActivity.createBundle(inboxReputation.getShopId(), "");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    private View.OnClickListener onGoToProduct(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductInfoActivity.createInstance(context, inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductId());
                context.startActivity(intent);
            }
        };
    }

    private View.OnClickListener onSellerOverflowClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position, R.menu.delete_menu);
            }
        };
    }

    private View.OnClickListener onGiveReplyListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGiveReply(inboxReputation, inboxReputationDetail, position);
            }
        };
    }

    private View.OnClickListener onOverflowClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position, getMenuID(position));
            }
        };
    }

    private int getMenuID(int position) {
        int menuID = 0;

        switch (getStatusOverFlow(position)) {
            case ACTION_EDIT:
                menuID = R.menu.edit_review_menu;
                break;
            case ACTION_REPORT:
                menuID = R.menu.report_menu;
                break;
        }
        return menuID;
    }

    private void showPopup(final View v, final int position, int menuID) {

        PopupMenu popup = new PopupMenu(context, v);
        System.out.println("pos clicked: " + position);
        MenuInflater inflater = popup.getMenuInflater();
        if (menuID != 0)
            inflater.inflate(menuID, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    presenter.onEditReview(inboxReputation, inboxReputationDetail, position);
                    return true;
                } else if (item.getItemId() == R.id.action_report) {
                    showReport(position);
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    presenter.deleteResponse(getParamDelete(position), position);
                    return true;
                } else {
                    return false;
                }
            }

        });
        popup.show();

    }

    private ActReviewPass getParamDelete(int position) {
        ActReviewPass paramDelete = new ActReviewPass();
        paramDelete.setReputationId(inboxReputation.getReputationId());
        paramDelete.setReviewId(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewId());
        paramDelete.setShopId(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getShopId());
        return paramDelete;
    }

    private void showReport(final int position) {

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
                            presenter.postReport(presenter.getReportParam(inboxReputationDetail.getInboxReputationDetailItemList().get(position), message));
                            alertDialog.dismiss();
                        } else
                            userInput.setError(context.getString(R.string.error_field_required));
                    }
                });
            }
        });
        alertDialog.show();

    }

    public void setInboxReputation(InboxReputationItem inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPresenter(InboxReputationDetailFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void addList(List<InboxReputationDetailItem> inboxReputationDetailItem) {
        this.inboxReputationDetail.getInboxReputationDetailItemList().clear();
        this.inboxReputationDetail.getInboxReputationDetailItemList().addAll(inboxReputationDetailItem);
        notifyDataSetChanged();
    }

    public List<InboxReputationDetailItem> getList() {
        return inboxReputationDetail.getInboxReputationDetailItemList();
    }

    public void setToken(String token) {
        this.inboxReputationDetail.setToken(token);
    }

}
