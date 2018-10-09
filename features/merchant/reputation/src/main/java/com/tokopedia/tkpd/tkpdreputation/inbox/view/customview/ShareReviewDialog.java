package com.tokopedia.tkpd.tkpdreputation.inbox.view.customview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.widgets.ShareItem;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ShareAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ShareModel;

/**
 * Created by stevenfredian on 2/20/17.
 */

public class ShareReviewDialog {

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
        this.dialog.setContentView(R.layout.share_review_dialog);
        appGrid = (GridView) this.dialog.findViewById(R.id.grid);
        cancelButton = this.dialog.findViewById(R.id.cancel_but);
        initAdapter();
        setAdapter();
        setListener();
    }

    public void initAdapter() {
        adapterRead = ArrayAdapter.createFromResource(context, R.array.talk_read, R.layout.dialog_item);
        adapterRead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setAdapter() {
        adapter = new ShareAdapter(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adapter.addItem(new ShareItem(context.getDrawable(R.drawable.ic_facebook_share), "Facebook", shareFb()));
            adapter.addItem(new ShareItem(context.getDrawable(R.drawable.ic_copy_share), "Copy Link", shareCopyLink()));
        } else {
            adapter.addItem(new ShareItem(context.getResources().getDrawable(R.drawable.ic_facebook_share), "Facebook", shareFb()));
            adapter.addItem(new ShareItem(context.getResources().getDrawable(R.drawable.ic_copy_share), "Copy Link", shareCopyLink()));
        }
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

    public void setView() {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
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
