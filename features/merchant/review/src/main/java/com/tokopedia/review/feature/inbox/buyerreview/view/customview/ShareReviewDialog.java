package com.tokopedia.review.feature.inbox.buyerreview.view.customview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.review.R;
import com.tokopedia.review.common.util.ClipboardHandler;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ShareAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ShareModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.widgets.ShareItem;

/**
 * Created by stevenfredian on 2/20/17.
 */

public class ShareReviewDialog {

    public static final String FACEBOOK_ICON_URL = "https://images.tokopedia.net/img/android/review/review_ic_facebook_share.png";
    public static final String LINK_ICON_URL = "https://images.tokopedia.net/img/android/review/review_ic_copy_share.png";

    private Fragment fragment;
    private final Context context;
    private final BottomSheetDialog dialog;
    private final GridView appGrid;
    private final View cancelButton;

    private ArrayAdapter<CharSequence> adapterRead;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private ShareAdapter adapter;
    private ShareModel model;

    public ShareReviewDialog(Context context,
                             CallbackManager callbackManager,
                             Fragment fragment) {
        this.callbackManager = callbackManager;
        this.context = context;
        this.dialog = new BottomSheetDialog(context);
        this.fragment = fragment;
        this.dialog.setContentView(R.layout.reputation_share_review_dialog);
        appGrid = (GridView) this.dialog.findViewById(R.id.grid);
        cancelButton = this.dialog.findViewById(R.id.cancel_but);
        initAdapter();
        setAdapter();
        setListener();
    }

    public void initAdapter() {
        adapterRead = ArrayAdapter.createFromResource(context, R.array.talk_read, R.layout.reputation_dialog_item);
        adapterRead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setAdapter() {
        adapter = new ShareAdapter(context);
        adapter.addItem(new ShareItem(FACEBOOK_ICON_URL, "Facebook", shareFb()));
        adapter.addItem(new ShareItem(LINK_ICON_URL, "Copy Link", shareCopyLink()));
        appGrid.setAdapter(adapter);
    }

    public void setListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    private View.OnClickListener shareCopyLink() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                ClipboardHandler.CopyToClipboard((Activity) context, model.getLink());
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener shareFb() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                shareDialog = new ShareDialog(fragment);
                shareDialog.registerCallback(callbackManager, new
                        FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                SnackbarManager.make(fragment.getActivity(), context.getString(R.string.success_share_review)
                                        , Snackbar.LENGTH_LONG).show();
                                dismissDialog();
                            }

                            @Override
                            public void onCancel() {
                                Log.i("facebook", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.i("facebook", "onError: " + error);
                                SnackbarManager.make(fragment.getActivity(), context.getString(R.string.error_share_review)
                                        , Snackbar.LENGTH_LONG).show();
                                dismissDialog();
                            }
                        });

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
                    if (model.getTitle() != null && !model.getTitle().equals(""))
                        builder.setContentTitle(model.getTitle());

                    if (model.getContent() != null && !model.getContent().equals(""))
                        builder.setQuote(model.getContent());

                    if (model.getImage() != null && !model.getImage().equals(""))
                        builder.setImageUrl(Uri.parse(model.getImage()));

                    if (model.getLink() != null && !model.getLink().equals(""))
                        builder.setContentUrl(Uri.parse(model.getLink()));

                    ShareLinkContent linkContent = builder.build();
                    shareDialog.show(linkContent);
                }
            }
        };
    }

    public void setModel(ShareModel model) {
        this.model = model;
    }

}
