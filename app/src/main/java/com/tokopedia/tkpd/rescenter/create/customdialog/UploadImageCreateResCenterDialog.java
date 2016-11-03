package com.tokopedia.tkpd.rescenter.create.customdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;

import com.tokopedia.tkpd.ImageGallery;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.database.model.AttachmentResCenterDB;
import com.tokopedia.tkpd.rescenter.utils.LocalCacheManager;

/**
 * Created on 4/14/16.
 */
public class UploadImageCreateResCenterDialog extends BaseUploadImageDialog {

    private final String orderID;
    private final LocalCacheManager.AttachmentCreateResCenter cache;

    public UploadImageCreateResCenterDialog(Fragment fragment, String orderID) {
        this.fragment = fragment;
        this.orderID = orderID;
        this.context = fragment.getActivity();
        this.cache = LocalCacheManager.AttachmentCreateResCenter.Builder(orderID);
    }

    public UploadImageCreateResCenterDialog(Activity activity, String orderID) {
        this.activity = activity;
        this.orderID = orderID;
        this.context = activity;
        this.cache = LocalCacheManager.AttachmentCreateResCenter.Builder(orderID);
    }

    @Override
    protected void processImageDataFromCamera(String cameraFileLoc, BaseUploadImageDialog.UploadImageDialogListener listener) {
        cache.setImageLocalPath(cameraFileLoc).save();
        listener.onSuccess(LocalCacheManager.AttachmentCreateResCenter.Builder(orderID).getCache());
    }

    @Override
    protected void processImageDataFromGallery(Intent intent, BaseUploadImageDialog.UploadImageDialogListener listener) {
        boolean valid = true;
        for (AttachmentResCenterDB data : cache.getCache()) {
            if (data.imagePath.equals(intent.getStringExtra(ImageGallery.EXTRA_URL))) {
                valid = false;
            }
        }

        if (valid) {
            cache.setImageLocalPath(intent.getStringExtra(ImageGallery.EXTRA_URL)).save();
            listener.onSuccess(LocalCacheManager.AttachmentCreateResCenter.Builder(orderID).getCache());
        }
    }

    public interface onRemoveAttachmentListener {
        void onRemoveClickListener();
        void onCancelClickListener();
    }

    public void showRemoveDialog(final onRemoveAttachmentListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_remove_attachment));
        builder.setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onRemoveClickListener();
            }
        }).setNegativeButton(context.getString(R.string.title_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onCancelClickListener();
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
