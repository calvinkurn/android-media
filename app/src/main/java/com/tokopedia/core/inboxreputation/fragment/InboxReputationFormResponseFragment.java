package com.tokopedia.core.inboxreputation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.inboxreputation.listener.InboxReputationFormResponseFragmentView;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenterImpl;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFormResponseFragmentPresenter;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFormResponseFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.ToolTipUtils;

import butterknife.Bind;

/**
 * Created by Nisie on 2/29/16.
 */
public class InboxReputationFormResponseFragment
        extends BasePresenterFragment<InboxReputationFormResponseFragmentPresenter>
        implements InboxReputationFormResponseFragmentView, InboxReputationConstant {

    private static final String DEFAULT_MSG_ERROR = "Mohon maaf, mohon coba kembali";

    public interface DoActionReputationListener {
        void postResponse(Bundle param);
    }

    @Bind(R2.id.username)
    TextView revieweeName;

    @Bind(R2.id.avatar)
    ImageView revieweeAvatar;

    @Bind(R2.id.rep_icon)
    ImageView iconPercentage;

    @Bind(R2.id.rep_rating)
    TextView textPercentage;

    @Bind(R2.id.product_avatar)
    ImageView productAvatar;

    @Bind(R2.id.product_title)
    TextView productName;

    @Bind(R2.id.product_review_date)
    TextView productReviewDate;

    @Bind(R2.id.product_review)
    TextView productReview;

    @Bind(R2.id.star_quality)
    ImageView quality;

    @Bind(R2.id.star_accuracy)
    ImageView accuracy;

    @Bind(R2.id.btn_overflow)
    ImageView overflow;

    @Bind(R2.id.image_holder)
    RecyclerView imageHolder;

    @Bind(R2.id.send_button)
    ImageButton sendButton;

    @Bind(R2.id.reply_box)
    EditText responseBox;

    private LabelUtils label;
    private ImageUploadAdapter adapter;
    private TkpdProgressDialog progressDialog;


    private InboxReputationFormResponseFragmentPresenter presenter;
    private InboxReputationDetailItem inboxReputationDetail;
    private InboxReputationItem inboxReputation;

    public static InboxReputationFormResponseFragment createInstance(Bundle extras) {
        InboxReputationFormResponseFragment fragment = new InboxReputationFormResponseFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxReputationFormResponseFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        inboxReputation = getArguments().getParcelable("inbox_reputation");
        inboxReputationDetail = getArguments().getParcelable("inbox_reputation_detail");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_reputation_form_response;
    }

    @Override
    protected void initView(View view) {

        setHeader();
        setReview();

    }

    private void setReview() {
        ImageHandler.LoadImage(productAvatar, inboxReputationDetail.getProductImageUrl());
        overflow.setVisibility(View.GONE);
        productName.setText(inboxReputationDetail.getProductName());
        productReview.setText(inboxReputationDetail.getReviewMessage());
        productReviewDate.setText(inboxReputationDetail.getReviewPostTime());
        accuracy.setImageResource(presenter.generateRating(inboxReputationDetail
                .getProductAccuracyRating()));
        quality.setImageResource(presenter.generateRating(inboxReputationDetail
                .getProductQualityRating()));
        setReviewImage();
        productName.setOnClickListener(onGoToProduct(inboxReputationDetail));
        productAvatar.setOnClickListener(onGoToProduct(inboxReputationDetail));

    }

    private View.OnClickListener onGoToProduct(final InboxReputationDetailItem inboxReputationDetail) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductInfoActivity.createInstance(context, inboxReputationDetail.getProductId());
                context.startActivity(intent);
            }
        };
    }

    private void setReviewImage() {
        adapter = ImageUploadAdapter.createAdapter(getActivity().getApplicationContext());
        adapter.setListener(onProductImageActionListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        imageHolder.setLayoutManager(layoutManager);
        imageHolder.setAdapter(adapter);

        if (isHasUploadedImage()) {
            imageHolder.setVisibility(View.VISIBLE);
            adapter.addList(inboxReputationDetail.getImages());
        } else {
            imageHolder.setVisibility(View.GONE);
        }
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onPreviewImageClicked();
                    }
                };
            }

        };
    }

    private boolean isHasUploadedImage() {
        try {
            return inboxReputationDetail.getImages().size() != 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void setHeader() {
        ImageHandler.LoadImage(revieweeAvatar, inboxReputation.getRevieweeImageUrl());
        revieweeName.setText(inboxReputation.getRevieweeName());
        label = LabelUtils.getInstance(context, revieweeName);
        label.giveSquareLabel(inboxReputation.getLabel());
        textPercentage.setText(inboxReputation.getUserReputation().getPositivePercentage());

        if (allowActiveSmiley()) {
            iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            textPercentage.setVisibility(View.VISIBLE);
        } else {
            iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            textPercentage.setVisibility(View.GONE);
        }
        revieweeName.setOnClickListener(onGoToProfile(inboxReputation));
        revieweeAvatar.setOnClickListener(onGoToProfile(inboxReputation));
        textPercentage.setOnClickListener(onReputationClicked(inboxReputation));

    }

    private View.OnClickListener onReputationClicked(final InboxReputationItem inboxReputation) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(inboxReputation), v);
            }
        };
    }

    private View setViewToolTip(final InboxReputationItem pos) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user,
                new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        TextView smile = (TextView) view.findViewById(R.id.text_smile);
                        TextView netral = (TextView) view.findViewById(R.id.text_netral);
                        TextView bad = (TextView) view.findViewById(R.id.text_bad);
                        smile.setText(String.valueOf(inboxReputation.getUserReputation().getPositive()));
                        netral.setText(String.valueOf(inboxReputation.getUserReputation().getNegative()));
                        bad.setText(String.valueOf(inboxReputation.getUserReputation().getNeutral()));
                    }

                    @Override
                    public void setListener() {

                    }
                });
    }

    private View.OnClickListener onGoToProfile(final InboxReputationItem inboxReputation) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), inboxReputation.getBuyerId())
                );
            }
        };
    }

    private boolean allowActiveSmiley() {
        return inboxReputation.getUserReputation().getNoReputation().equals("0");
    }

    @Override
    protected void setViewListener() {
        sendButton.setOnClickListener(onSendClicked());
        responseBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (responseBox.getText().length() > 29) {
                    responseBox.setError(null);
                }
            }
        });
    }

    private View.OnClickListener onSendClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = responseBox.getText().toString();

                if (response.trim().equals("")) {
                    responseBox.setError(getString(R.string.error_review_response_message_empty));
                    responseBox.requestFocus();
                } else if (response.length() < 5) {
                    responseBox.setError(getString(R.string.error_min_5));
                    responseBox.requestFocus();
                } else {
                    presenter.postResponse(response);
                }
            }
        };
    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {

    }

    public void showLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
        progressDialog.showDialog();
    }

    public InboxReputationItem getInboxReputation() {
        return inboxReputation;
    }

    public InboxReputationDetailItem getInboxReputationDetail() {
        return inboxReputationDetail;
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessPostResponse(Bundle resultData) {
        setActionsEnabled(true);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_success", 1);
        bundle.putString("action", InboxReputationDetailFragmentPresenterImpl.ACTION_UPDATE_PRODUCT);
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFailedPostResponse(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if (errorMessage.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                SnackbarManager.make(getActivity(), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        dismissProgressDialog();
        setActionsEnabled(true);
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        responseBox.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        revieweeAvatar.setEnabled(isEnabled);
        revieweeName.setEnabled(isEnabled);
        productAvatar.setEnabled(isEnabled);
        productName.setEnabled(isEnabled);
        productReview.setEnabled(isEnabled);
        imageHolder.setEnabled(isEnabled);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
