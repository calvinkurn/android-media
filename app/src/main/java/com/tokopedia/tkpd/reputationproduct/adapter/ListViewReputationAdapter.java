package com.tokopedia.tkpd.reputationproduct.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.product.activity.ProductInfoActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.reputationproduct.facade.FacadeReputation;
import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;
import com.tokopedia.tkpd.session.Login;
import com.tokopedia.tkpd.session.presenter.Session;
import com.tokopedia.tkpd.util.LabelUtils;
import com.tokopedia.tkpd.util.ReportTalkReview;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.StarGenerator;
import com.tokopedia.tkpd.util.ToolTipUtils;
import com.tokopedia.tkpd.var.TkpdState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 7/3/15.
 */
public class ListViewReputationAdapter extends BaseAdapter {

    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_DEFAULT = 0;
    public static final int SMILEY_NETRAL = 1;
    public static final int SMILEY_SMILE = 2;

    private Context context;
    private List<Model> list;

    private ViewHolder holder;
    private LayoutInflater inflater;
    private String shopId;
    private String productName;
    private String productId;

    public static class Model implements Serializable {
        public String reviewId;
        public Boolean Editable;
        public String username;
        public String userId;
        public String avatarUrl;
        public String date;
        public String comment;
        public String userLabel;
        public int starQuality;
        public int starAccuracy;
        public int smiley;
        public String counterSmiley;
        public int counterLike;
        public int counterDislike;
        public int counterResponse;
        public String shopName;
        public String shopId;
        public String shopAvatarUrl;
        public String responseMessage;
        public String responseDate;
        public String userNameResponder;
        public String avatarUrlResponder;
        public String labelIdResponder;
        public String userLabelResponder;
        public String userIdResponder;
        public String productId;
        public String productName;
        public String shopReputation;
        public int typeMedal;
        public int levelMedal;
        public boolean isGetLikeDislike;
        public int statusLikeDislike;
        public int positive;
        public int negative;
        public int netral;
        public int noReputationUserScore;
        public String productAvatar;
        public String reputationId;
        public List<ImageUpload> imageUploads;
    }

    public ListViewReputationAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
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
    }

    public static class ParamListViewReputationAdapter {
        public Context context;
        public String productId;
        public String shopId;
        public String productName;
        public List<Model> list = new ArrayList<>();
    }


    public static ListViewReputationAdapter createAdapter(ParamListViewReputationAdapter param) {
        ListViewReputationAdapter adapter = new ListViewReputationAdapter(param.context);
        adapter.list = param.list;
        adapter.productId = param.productId;
        adapter.productName = param.productName;
        adapter.shopId = param.shopId;
        return adapter;
    }

    public static ListViewReputationAdapter createAdapterShop(ParamListViewReputationAdapter param) {
        ListViewReputationAdapter adapter = new ListViewReputationAdapter(param.context);
        adapter.list = param.list;
        adapter.productId = "";
        adapter.productName = param.productName;
        adapter.shopId = param.shopId;
        return adapter;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_reputation, null);
            initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setModelToView(position);
        setVisibility(position);
        setListener(position);
        return convertView;
    }

    private void initView(View view) {
        holder.avatar = (ImageView) view.findViewById(R.id.user_avatar);
        holder.username = (TextView) view.findViewById(R.id.username);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.smiley = (ImageView) view.findViewById(R.id.smiley);
        holder.counterSmiley = (LinearLayout) view.findViewById(R.id.counter_smiley);
        holder.starQuality = (LinearLayout) view.findViewById(R.id.star_quality);
        holder.starAccuracy = (LinearLayout) view.findViewById(R.id.star_accuracy);
        holder.comment = (TextView) view.findViewById(R.id.comment);
        holder.counterComment = (TextView) view.findViewById(R.id.counter_comment);
        holder.counterLike = (TextView) view.findViewById(R.id.counter_like);
        holder.counterDislike = (TextView) view.findViewById(R.id.counter_dislike);
        holder.overFlow = (ImageView) view.findViewById(R.id.btn_overflow);
        holder.viewLikeDislike = view.findViewById(R.id.view_like_dislike);
        holder.loading = (ProgressBar) view.findViewById(R.id.loading);
        holder.iconDislike = (ImageView) view.findViewById(R.id.icon_dislike);
        holder.iconLike = (ImageView) view.findViewById(R.id.icon_like);
        holder.textPercentage = (TextView) view.findViewById(R.id.rep_rating);
        holder.iconPercentage = (ImageView) view.findViewById(R.id.rep_icon);
        holder.viewReputation = view.findViewById(R.id.counter_smiley);
        holder.label = LabelUtils.getInstance(context, holder.username);
        holder.prodName = (TextView) view.findViewById(R.id.prod_name);
        holder.viewProduct = view.findViewById(R.id.product_info);
    }

    private void setModelToView(int position) {
        ImageHandler.loadImageCircle2(context, holder.avatar, list.get(position).avatarUrl);
//        ImageHandler.LoadImageCircle(holder.avatar, list.get(position).avatarUrl);
        holder.username.setText(Html.fromHtml(list.get(position).username).toString());
        holder.date.setText(list.get(position).date);
        holder.comment.setText(Html.fromHtml(list.get(position).comment).toString());
        holder.label.giveSquareLabel(list.get(position).userLabel);
        holder.textPercentage.setText(list.get(position).counterSmiley);
        setProductInfo(list.get(position).productName);
        setIconPercentage(position);
        setCounter(position);
        // setSmiley(position);
        setStar(position);
    }

    private void setProductInfo(String productName) {
        if (productName.length() > 0 && !productName.equals("0")) {
            productName = Html.fromHtml(productName).toString();
            holder.prodName.setText(productName);
            holder.viewProduct.setVisibility(View.VISIBLE);
        } else {
            holder.viewProduct.setVisibility(View.GONE);
        }
    }

    private void setIconPercentage(int position) {
        if (allowActiveSmiley(position)) {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.textPercentage.setVisibility(View.VISIBLE);
        } else {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.textPercentage.setVisibility(View.GONE);
        }
    }

    private String getProductID(int pos) {
        if (!productId.equals("")) {
            return productId;
        } else {
            return list.get(pos).productId;
        }
    }

    private boolean allowActiveSmiley(int position) {
        return list.get(position).noReputationUserScore == 0;
    }

    private void setVisibility(int position) {
        if(SessionHandler.isV4Login(context) && isProductOwner(position)) {
            holder.overFlow.setVisibility(View.VISIBLE);
        } else {
            holder.overFlow.setVisibility(View.GONE);
        }

        if (!list.get(position).isGetLikeDislike) {
            holder.viewLikeDislike.setVisibility(View.GONE);
            holder.loading.setVisibility(View.VISIBLE);
        } else {
            holder.viewLikeDislike.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.GONE);
        }
    }

    private boolean isProductOwner(int position) {
        SessionHandler session = new SessionHandler(context);
        return list.get(position).userIdResponder.equals(session.getLoginID());
    }

    private void setListener(final int position) {
        holder.counterLike.setOnClickListener(OnLikeClickListener(position));
        holder.iconLike.setOnClickListener(OnLikeClickListener(position));
        holder.counterDislike.setOnClickListener(OnDislikeClickListener(position));
        holder.iconDislike.setOnClickListener(OnDislikeClickListener(position));
        holder.overFlow.setOnClickListener(OnOverFlowClickListener(position));
        holder.viewReputation.setOnClickListener(OnReputationViewClickListener(position));
        holder.viewProduct.setOnClickListener(OnProductClick(list.get(position).productId));
    }

    private View.OnClickListener OnReputationViewClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(position), v);
            }
        };
    }

    private View setViewToolTip(final int pos) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText("" + list.get(pos).positive);
                netral.setText("" + list.get(pos).netral);
                bad.setText("" + list.get(pos).negative);
            }

            @Override
            public void setListener() {

            }
        });
    }

    private View.OnClickListener OnOverFlowClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.report_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R2.id.action_report:
                                ReportTalkReview report = new ReportTalkReview(
                                        (Activity) context, 1, list.get(position).reviewId, "report_comment_review", shopId);
                                report.ShowDialogReport();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        };
    }

    private View.OnClickListener OnLikeClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SessionHandler.isV4Login(context)) {
                    OnLikeConnection(position);
                } else {
                    showLoginOption(position);
                }
            }
        };
    }

    private View.OnClickListener OnProductClick(final String prodId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProductInfoActivity.createInstance(context, prodId);
                context.startActivity(intent);
            }
        };
    }

    private void OnLikeConnection(int position) {
        Model UnUpdatedModel = list.get(position);
        switch (list.get(position).statusLikeDislike) {
            case 3:
                setTemporaryActivatedLike(position);
                break;
            case 1:
                setTemporaryDeactivatedLike(position);
                break;
            case 2:
                setTemporaryDeactivatedDislike(position);
                setTemporaryActivatedLike(position);
                break;
        }
        UpdateFacade(UnUpdatedModel, position);

    }

    private void UpdateFacade(Model model, int position) {
        FacadeReputation facade = FacadeReputation.createInstance(context);
        facade.updateLikeDislike(getParam(position), OnUpdateFacadeConnection(model, position));
    }

    private FacadeReputation.UpdateLikeDislikeParam getParam(int position) {
        FacadeReputation.UpdateLikeDislikeParam param = new FacadeReputation.UpdateLikeDislikeParam();
        param.reviewID = list.get(position).reviewId;
        param.productID = getProductID(position);
        param.shopID = shopId;
        param.statusLikeDislike = list.get(position).statusLikeDislike;
        return param;
    }

    private FacadeReputation.OnUpdateLikeDislikeListener OnUpdateFacadeConnection(final Model model, final int position) {
        return new FacadeReputation.OnUpdateLikeDislikeListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String Message) {
                list.set(position, model);
                notifyDataSetChanged();
                CommonUtils.UniversalToast(context, Message);
            }
        };
    }

    private void setTemporaryActivatedLike(int position) {
        Model model = list.get(position);
        model.statusLikeDislike = 1;
        model.counterLike = model.counterLike + 1;
        list.set(position, model);
        notifyDataSetChanged();
    }

    private void setTemporaryDeactivatedLike(int position) {
        Model model = list.get(position);
        model.statusLikeDislike = 3;
        model.counterLike = model.counterLike - 1;
        list.set(position, model);
        notifyDataSetChanged();
    }

    private View.OnClickListener OnDislikeClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SessionHandler.isV4Login(context)){
                    OnDislikeConnection(position);
                } else {
                    showLoginOption(position);
                }
            }
        };
    }

    private void showLoginOption(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.error_not_logged));
        builder.setPositiveButton(context.getString(R.string.title_activity_login), OnLoginClickListener(pos));
        builder.setNegativeButton(context.getString(R.string.title_cancel), OnCancelClickListener());
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private DialogInterface.OnClickListener OnLoginClickListener(final int pos) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, Login.class);
                intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra("product_id", getProductID(pos));
                context.startActivity(intent);
            }
        };
    }

    private DialogInterface.OnClickListener OnCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    private void OnDislikeConnection(int position) {
        Model UnUpdatedModel = list.get(position);
        switch (list.get(position).statusLikeDislike) {
            case 3:
                setTemporaryActivatedDislike(position);
                break;
            case 1:
                setTemporaryDeactivatedLike(position);
                setTemporaryActivatedDislike(position);
                break;
            case 2:
                setTemporaryDeactivatedDislike(position);
                break;
        }
        UpdateFacade(UnUpdatedModel, position);
    }

    private void setTemporaryActivatedDislike(int position) {
        Model model = list.get(position);
        model.statusLikeDislike = 2;
        model.counterDislike = model.counterDislike + 1;
        list.set(position, model);
        notifyDataSetChanged();
    }

    private void setTemporaryDeactivatedDislike(int position) {
        Model model = list.get(position);
        model.statusLikeDislike = 3;
        model.counterDislike = model.counterDislike - 1;
        list.set(position, model);
        notifyDataSetChanged();
    }


    private void setCounter(int position) {
        setIconLike(position);
        setIconDislike(position);

        holder.counterLike.setText("" + list.get(position).counterLike);
        holder.counterDislike.setText("" + list.get(position).counterDislike);
        holder.counterComment.setText("" + list.get(position).counterResponse);
    }

    private void setIconLike(int position) {
        if (list.get(position).statusLikeDislike == 1) {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like_active);
        } else {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like);
        }
    }

    private void setIconDislike(int position) {
        if (list.get(position).statusLikeDislike == 2) {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike_active);
        } else {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike);
        }
    }

    private void setSmiley(int position) {
        holder.smiley.setImageResource(generateSmiley(list.get(position).smiley));
    }

    private int generateSmiley(int args) {
        int smiley = R.drawable.ic_icon_repsis_smile;
        switch (args) {
            case SMILEY_BAD:
                smiley = R.drawable.ic_icon_repsis_sad_active;
                break;
            case SMILEY_NETRAL:
                smiley = R.drawable.ic_icon_repsis_neutral_active;
                break;
            case SMILEY_SMILE:
                smiley = R.drawable.ic_icon_repsis_smile_active;
                break;
        }
        return smiley;
    }

    private void setStar(int position) {
        StarGenerator.setReputationStars(context, holder.starAccuracy, list.get(position).starAccuracy);
        StarGenerator.setReputationStars(context, holder.starQuality, list.get(position).starQuality);
    }
}
